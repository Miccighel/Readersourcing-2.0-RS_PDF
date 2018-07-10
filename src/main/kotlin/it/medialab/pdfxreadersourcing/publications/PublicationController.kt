package it.medialab.pdfxreadersourcing.publications

import it.medialab.pdfxreadersourcing.utils.Constants
import org.apache.logging.log4j.LogManager
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class PublicationController {

    private var models = mutableListOf<PublicationModel>()
    var view: PublicationView = PublicationView()
    private lateinit var datasetPath: String
    private var numberOfPublications: Int = 0
    private var logger = LogManager.getLogger()

    init {
        logger.info("Controller launched.")
    }

    fun load(publicationPath: String) {

        this.datasetPath = publicationPath

        logger.info("Data set loading started.")
        logger.info("Path: \"$publicationPath\".")

        val outputDirectory = File(Constants.OUTPUT_PATH)
        logger.info("Checking if ${Constants.PROGRAM_NAME} output dir. exists.")
        if (!outputDirectory.exists()) {
            logger.info("Output dir. not exists.")
            if (outputDirectory.mkdirs()) {
                logger.info("Output dir. created.")
                logger.info("Path: \"${Constants.OUTPUT_PATH}\".")
            }
        } else {
            logger.info("Output dir. already exists.")
            logger.info("Output dir. creation skipped.")
            logger.info("Path:\"${Constants.OUTPUT_PATH}\".")
        }
        try {
            File(Constants.INPUT_PATH).walkTopDown().forEach {
                if (!it.isDirectory)
                    numberOfPublications++
            }
            logger.info("Number of detected files: $numberOfPublications.")
            File(Constants.INPUT_PATH).walkTopDown().forEach {
                if (!it.isDirectory) {
                    logger.info(it)
                    val model = PublicationModel()
                    model.loadData(it)
                    models.plusAssign(model)
                }
            }
        } catch (exception: FileNotFoundException) {
            logger.warn("Data set not found. Is file/folder inside a \"data\" dir.?")
        } catch (exception: IOException) {
            logger.warn(exception.message as String)
        }

        logger.info("Data set loading for input path \"$datasetPath\" completed.")

    }

    fun addLink(text: String, url: String) {
        models.forEach { model ->
            model.addLink(text, url)
        }
    }

}