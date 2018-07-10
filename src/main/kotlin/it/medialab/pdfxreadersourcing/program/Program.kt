package it.medialab.pdfxreadersourcing.program

import it.medialab.pdfxreadersourcing.publications.PublicationController
import it.medialab.pdfxreadersourcing.utils.Constants
import it.medialab.pdfxreadersourcing.utils.Tools.updateLogger
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.FileNotFoundException

object Program {

    @JvmStatic
    fun main(arguments: Array<String>) {

        val publicationController: PublicationController
        val publicationPath: String
        val logger: Logger

        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider")
        System.setProperty("baseLogFileName", "${Constants.LOG_PATH}${Constants.LOG_FILE_NAME}${Constants.LOG_FILE_SUFFIX}")

        logger = updateLogger(LogManager.getLogger(this::class.qualifiedName), Level.INFO)
        try {

            publicationPath = Constants.INPUT_PATH
            publicationController = PublicationController()
            publicationController.load(publicationPath)
            publicationController.addLink("Click here to rate this article", "http://google.it")

        } catch (exception: FileNotFoundException) {

            logger.error(exception.message)
            logger.info("${Constants.PROGRAM_NAME} execution terminated.")

        }

    }

}


