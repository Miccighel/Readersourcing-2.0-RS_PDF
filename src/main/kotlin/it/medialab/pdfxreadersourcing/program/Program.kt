package it.medialab.pdfxreadersourcing.program

import it.medialab.pdfxreadersourcing.publications.PublicationController
import it.medialab.pdfxreadersourcing.utils.Constants
import it.medialab.pdfxreadersourcing.utils.Tools.updateLogger
import org.apache.commons.cli.*
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File
import java.io.FileNotFoundException

object Program {

    @JvmStatic
    fun main(arguments: Array<String>) {

        val commandLine: CommandLine
        val parser: CommandLineParser
        val options = loadCommandLineOptions()
        val publicationController: PublicationController
        val publicationInputPath: String
        val publicationOutputPath: String
        val logger: Logger

        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider")
        System.setProperty("baseLogFileName", "${Constants.LOG_PATH}${Constants.LOG_FILE_NAME}${Constants.LOG_FILE_SUFFIX}")

        logger = updateLogger(LogManager.getLogger(this::class.qualifiedName), Level.INFO)

        try {

            parser = DefaultParser()
            commandLine = parser.parse(options, arguments)

            if (commandLine.hasOption("pIn")) {
                publicationInputPath = commandLine.getOptionValue("pIn")
                val errorMessage = "Input path not found. Please, check it. Path: \"$publicationInputPath\""
                if (!File(publicationInputPath).exists()) throw FileNotFoundException(errorMessage) else {
                    logger.info("Input path detected: CUSTOM")
                }
                println(publicationInputPath)
            } else {
                publicationInputPath = Constants.INPUT_PATH
                logger.info("Input path detected: DEFAULT")
            }

            if (commandLine.hasOption("pOut")) {
                publicationOutputPath = commandLine.getOptionValue("pOut")
                val errorMessage = "Output path not found. Please, check it. Path: \"$publicationOutputPath\""
                if (!File(publicationOutputPath).exists()) throw FileNotFoundException(errorMessage) else {
                    logger.info("Output path detected: CUSTOM")
                    if (!File(publicationOutputPath).isDirectory) throw ParseException("Value for the option <<pOut>> or <<pOut>> must be a path to a directory. Check the usage section below")
                }
            } else {
                publicationOutputPath = Constants.OUTPUT_PATH
                logger.info("Output path detected: DEFAULT")
            }

            publicationController = PublicationController()
            publicationController.load(publicationInputPath, publicationOutputPath)
            publicationController.addLink("Click here to rate this article", "http://google.it")

        } catch (exception: FileNotFoundException) {

            logger.error(exception.message)
            logger.info("${Constants.PROGRAM_NAME} execution terminated.")

        }

    }

    private fun loadCommandLineOptions(): Options {

        val options = Options()
        var source = Option.builder("pIn").longOpt("pathIn").desc("Relative path to the input file/directory.").hasArg().argName("Source File/Directory").build()
        options.addOption(source)
        source = Option.builder("pOut").longOpt("pathOut").desc("Relative path to the output directory.").hasArg().argName("Result Directory").build()
        options.addOption(source)
        return options
    }

}


