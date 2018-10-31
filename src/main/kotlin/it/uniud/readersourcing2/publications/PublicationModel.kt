package it.uniud.readersourcing2.publications

import it.uniud.readersourcing2.utils.Constants
import org.apache.logging.log4j.LogManager
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.interactive.action.PDActionURI
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink
import org.apache.pdfbox.pdmodel.interactive.annotation.PDBorderStyleDictionary
import java.io.File
import java.nio.file.Files

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

    private var logger = LogManager.getLogger()

    fun loadData(publication: File) {
        updateData(publication)
        this.outputPath = "${Constants.OUTPUT_PATH}$name-Link.pdf"
        publicationDataToString()
    }

    fun loadData(publication: File, outputPath: String) {
        updateData(publication)
        this.outputPath = "$outputPath$name-Link.pdf"
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

        logger.info("Name:")
        logger.info(name)

        logger.info("Mime type:")
        logger.info(mimeType)

        logger.info("Page width:")
        logger.info("$pageWidth pt")

        logger.info("Page height:")
        logger.info("$pageHeight pt")

        logger.info("Number of Pages:")
        logger.info(numberOfPages)

        logger.info("Input Path:")
        logger.info(inputPath)

        logger.info("Output Path:")
        logger.info(outputPath)

        logger.info("---------- PUBLICATION DATA END ----------")
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
        val lineCounter = 5
        val offsetX = 55F
        val offsetY = 40F
        val leading = 15F
        val fontSize = 9F

        contentStream.beginText()
        contentStream.setFont(font, fontSize)
        contentStream.setLeading(leading)
        contentStream.newLineAtOffset(offsetX, upperRightY - offsetY)
        contentStream.showText("---------- READERSOURCING 2.0 ANNOTATION STARTS HERE -----------")
        contentStream.newLine()
        contentStream.newLine()
        contentStream.showText(caption)
        contentStream.newLine()
        contentStream.newLine()
        contentStream.showText("---------- READERSOURCING 2.0 ANNOTATION ENDS HERE ----------")
        contentStream.endText()
        contentStream.close()

        val annotation = PDAnnotationLink()
        val underline = PDBorderStyleDictionary()
        underline.style = PDBorderStyleDictionary.STYLE_UNDERLINE
        annotation.borderStyle = underline

        val textWidth = font.getStringWidth(caption) / 100
        val position = PDRectangle()
        position.lowerLeftX = offsetX
        position.lowerLeftY = upperRightY - (offsetY + (fontSize * (lineCounter - 1)))
        position.upperRightX = (offsetX + textWidth)
        position.upperRightY = upperRightY
        annotation.rectangle = position

        val action = PDActionURI()
        action.uri = url
        annotation.action = action

        newPage.annotations.add(annotation)

        publication.addPage(newPage)
        publication.save(outputPath)
        publication.close()

        logger.info("New page construction completed.")

        logger.info("URL added successfully.")
        logger.info("Updated file path:")
        logger.info(outputPath)
    }

}