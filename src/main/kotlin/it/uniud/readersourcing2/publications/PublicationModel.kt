package it.uniud.readersourcing2.publications

import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.QRCodeWriter
import it.uniud.readersourcing2.utils.Constants
import org.apache.logging.log4j.LogManager
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import org.apache.pdfbox.pdmodel.interactive.action.PDActionURI
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink
import org.apache.pdfbox.pdmodel.interactive.annotation.PDBorderStyleDictionary
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class PublicationModel {

    private lateinit var publication: PDDocument
    private lateinit var name: String
    private lateinit var mimeType: String
    private var pageWidth = 0.0F
    private var pageHeight = 0.0F
    private var numberOfPages = 0

    private lateinit var inputPath: String
    private lateinit var outputPath: String

    private lateinit var caption: String
    private lateinit var url: String
    private var authenticationToken: String? = null
    private var publicationIdentifier: String? = null

    private lateinit var qrCodePath: String
    private val qrCodeHeight = 200F
    private val qrCodeWidth = 200F

    private var logger = LogManager.getLogger()

    fun loadData(publication: File) {
        updateData(publication)
        this.outputPath = "${Constants.OUTPUT_PATH}$name-Link.pdf"
        this.qrCodePath = "${Constants.OUTPUT_PATH}$name-QRCode.png"
        publicationDataToString()
    }

    fun loadData(publication: File, outputPath: String) {
        updateData(publication)
        this.outputPath = "$outputPath$name-Link.pdf"
        this.qrCodePath = "$outputPath$name-QRCode.png"
        publicationDataToString()
    }

    private fun updateData(publication: File) {
        this.publication = PDDocument.load(publication)
        val lastPage = this.publication.documentCatalog.pages.last()
        this.pageWidth = lastPage.mediaBox.width
        this.pageHeight = lastPage.mediaBox.height
        this.name = publication.nameWithoutExtension
        this.mimeType = Files.probeContentType(publication.toPath())
        this.numberOfPages = this.publication.numberOfPages
        this.inputPath = publication.absolutePath
    }

    private fun publicationDataToString() {
        logger.info("---------- PUBLICATION DATA BEGIN ----------")
        logger.info("Name: $name")
        logger.info("Mime type: $mimeType")
        logger.info("Page width: $pageWidth pt")
        logger.info("Page height: $pageHeight pt")
        logger.info("Number of Pages: $numberOfPages")
        logger.info("Input Path: $inputPath")
        logger.info("Output Path: $outputPath")
        logger.info("---------- PUBLICATION DATA END ----------")
    }

    private fun buildQRCode(url: String): File? {
        logger.info("Building QR Code")
        val qrCodeWriter = QRCodeWriter()
        val bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, this.qrCodeWidth.toInt(), this.qrCodeHeight.toInt())
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", Paths.get(this.qrCodePath))
        val qrCodeFile = File(this.qrCodePath)
        return if (qrCodeFile.exists()) {
            logger.info("Path: ${this.qrCodePath}")
            qrCodeFile
        } else null
    }

    fun addUrl(parameters: Parameters) {
        caption = parameters.caption
        url = parameters.url
        authenticationToken = parameters.authenticationToken
        publicationIdentifier = parameters.publicationIdentifier

        logger.info("Adding URL with caption to \"$name.pdf\"")
        logger.info("Base URL: $url")
        logger.info("Caption: $caption")
        publicationIdentifier?.let { logger.info("Publication Identifier: $it") }
        authenticationToken?.let { logger.info("Authentication Token: $it") }

        logger.info("New page construction started.")

        val newPage = PDPage(PDRectangle(pageWidth, pageHeight))
        val contentStream = PDPageContentStream(publication, newPage, PDPageContentStream.AppendMode.APPEND, false)

        val font = PDType1Font.HELVETICA
        val upperRightY = newPage.mediaBox.upperRightY
        val offsetX = 55F
        val offsetY = 40F
        val leading = 15F
        val fontSize = 9F
        val urlLineNumber = 7
        val qrCodeLineNumber = 8

        logger.info("Adding caption.")

        contentStream.beginText()
        contentStream.setFont(font, fontSize)
        contentStream.setLeading(leading)
        contentStream.newLineAtOffset(offsetX, upperRightY - offsetY)
        contentStream.showText("------------------------------ READERSOURCING 2.0 ANNOTATION: RATING URL ------------------------------")
        contentStream.newLine()
        contentStream.showText("Either follow the underlined link or scan the QR Code to rate the publication.")
        contentStream.newLine()
        contentStream.newLine()
        contentStream.showText(caption)
        contentStream.endText()

        val textWidth = (font.getStringWidth(caption) / 100) - 11
        contentStream.moveTo(offsetX, upperRightY - (offsetY + (fontSize * urlLineNumber) - 10))
        contentStream.lineTo(offsetX + textWidth, upperRightY - (offsetY + (fontSize * urlLineNumber) - 10))
        contentStream.stroke()

        buildQRCode(url)?.let {
            logger.info("Adding QR Code.")
            val qrCodePDFImage = PDImageXObject.createFromFile(it.absolutePath, publication)
            contentStream.drawImage(qrCodePDFImage, offsetX, upperRightY - (offsetY + ((fontSize * (qrCodeLineNumber)) + this.qrCodeHeight - 18)))
            it.delete()
        }

        contentStream.close()

        logger.info("Adding Hyperlink.")

        val annotation = PDAnnotationLink()
        val underline = PDBorderStyleDictionary()
        underline.style = PDBorderStyleDictionary.STYLE_UNDERLINE
        annotation.borderStyle = underline

        val position = PDRectangle()
        position.lowerLeftX = offsetX
        position.upperRightX = (offsetX + textWidth)
        position.lowerLeftY = upperRightY - (offsetY + (fontSize * (urlLineNumber - 1)))
        position.upperRightY = upperRightY - (offsetY + (fontSize + 20))
        annotation.rectangle = position

        val action = PDActionURI()
        action.uri = url
        annotation.action = action
        newPage.annotations.add(annotation)

        logger.info("Appending new page.")

        publication.addPage(newPage)

        logger.info("New page construction completed.")
        logger.info("Saving URL to metadata.")

        publication.documentInformation.setCustomMetadataValue("BaseUrl", url)

        logger.info("URL added to metadata successfully.")

        publication.save(outputPath)
        publication.close()

        logger.info("Updated file path:")
        logger.info(outputPath)
    }

}