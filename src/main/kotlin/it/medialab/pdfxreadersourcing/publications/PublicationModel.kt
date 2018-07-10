package it.medialab.pdfxreadersourcing.publications

import it.medialab.pdfxreadersourcing.utils.Constants
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


class PublicationModel {

    private lateinit var publication: PDDocument
    private lateinit var name: String
    private lateinit var path: String
    private var numberOfPages = 0
    private var logger = LogManager.getLogger()

    fun loadData(publication: File) {

        logger.info("---------- PUBLICATION DATA BEGIN ----------")

        this.publication = PDDocument.load(publication)

        logger.info("Name:")
        name = publication.nameWithoutExtension
        logger.info(name)

        logger.info("Path:")
        path = publication.absolutePath
        logger.info(path)

        logger.info("Number of Pages:")
        numberOfPages = this.publication.numberOfPages
        logger.info(numberOfPages)

        logger.info("---------- PUBLICATION DATA END ----------")

    }

    fun addLink(text: String, url: String) {

        logger.info("Adding link to \"$name.pdf\"")

        val newPage = PDPage()
        val contentStream = PDPageContentStream(publication, newPage, PDPageContentStream.AppendMode.APPEND, false)

        val font = PDType1Font.HELVETICA
        val upperRightY = newPage.mediaBox.upperRightY
        val lineCounter = 3
        val offsetX = 55F
        val offsetY = 40F
        val leading = 15F
        val fontSize = 10F

        contentStream.beginText()
        contentStream.setFont(font, fontSize)
        contentStream.setLeading(leading)
        contentStream.newLineAtOffset(offsetX, upperRightY - offsetY)
        contentStream.showText("Before you go...")
        contentStream.newLine()
        contentStream.showText(text)
        contentStream.newLine()
        contentStream.showText("Thank you!")
        contentStream.endText()
        contentStream.close()

        val caption = PDAnnotationLink()
        val underline = PDBorderStyleDictionary()
        underline.style = PDBorderStyleDictionary.STYLE_UNDERLINE
        caption.borderStyle = underline

        val textWidth = font.getStringWidth(text) / 100
        val position = PDRectangle()
        position.lowerLeftX = offsetX
        position.lowerLeftY = upperRightY - (offsetY + (fontSize * (lineCounter - 1)))
        position.upperRightX = offsetX + textWidth
        position.upperRightY = upperRightY
        caption.rectangle = position

        val action = PDActionURI()
        action.uri = url
        caption.action = action

        newPage.annotations.add(caption)

        publication.addPage(newPage)
        publication.save("${Constants.OUTPUT_PATH}$name - Link.pdf")
        publication.close()

        logger.info("Link added successfully.")
        logger.info("Updated file path:")
        logger.info("${Constants.OUTPUT_PATH}$name - Link.pdf")

    }

}