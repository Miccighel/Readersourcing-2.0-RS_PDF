package it.medialab.pdfxreadersourcing.publications

import it.medialab.pdfxreadersourcing.utils.Constants
import org.apache.logging.log4j.LogManager
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.file.Files

class PublicationController {

    private var models = mutableListOf<PublicationModel>()
    private lateinit var inputPath: String
    private lateinit var outputPath: String
    private var numberOfPublications: Int = 0
    private var logger = LogManager.getLogger()

    init {
        logger.info("Controller launched.")
    }

    fun load(publicationInputPath: String, publicationOutputPath: String) {

        this.inputPath = publicationInputPath
        this.outputPath = publicationOutputPath

        logger.info("Data set loading started.")
        logger.info("Path: \"${this.inputPath}\".")

        val outputDirectory = File(this.outputPath)

        if (this.outputPath == Constants.OUTPUT_PATH) {
            logger.info("Checking if ${Constants.PROGRAM_NAME} output dir. exists.")
            if (!outputDirectory.exists()) {
                logger.info("Output dir. not exists.")
                if (outputDirectory.mkdirs()) {
                    logger.info("Output dir. created.")
                    logger.info("Path: \"${this.outputPath}\".")
                }
            } else {
                logger.info("Output dir. already exists.")
                logger.info("Output dir. creation skipped.")
                logger.info("Path: \"${this.outputPath}\".")
            }
        } else {
            logger.info("Custom output dir. presence already checked.")
            logger.info("Path: \"${this.outputPath}\".")
        }
        try {
            File(this.inputPath).walkTopDown().forEach {
                if (!it.isDirectory)
                    if (Files.probeContentType(it.toPath()) == "application/pdf")
                        numberOfPublications++
            }
            logger.info("Number of detected files (PDF format): $numberOfPublications.")
            File(this.inputPath).walkTopDown().forEach {
                if (!it.isDirectory) {
                    if (Files.probeContentType(it.toPath()) == "application/pdf") {
                        val model = PublicationModel()
                        if (this.outputPath == Constants.OUTPUT_PATH)
                            model.loadData(it)
                        else
                            model.loadData(it, this.outputPath)
                        models.plusAssign(model)
                    }
                }
            }
        } catch (exception: FileNotFoundException) {
            logger.warn("Data set not found. Is file/folder inside a \"data\" dir.? If you've specified a custom input path, please check it.")
        } catch (exception: IOException) {
            logger.warn(exception.message as String)
        }

        logger.info("Data set loading for input path \"${this.inputPath}\" completed.")

    }

    fun addUrl(parameters: Parameters) {
        models.forEach { model ->
            model.addUrl(parameters)
        }
    }

}