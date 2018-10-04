package it.uniud.readersourcing2.program

import it.uniud.readersourcing2.publications.Parameters
import it.uniud.readersourcing2.publications.PublicationController
import it.uniud.readersourcing2.utils.Constants
import it.uniud.readersourcing2.utils.Tools.updateLogger
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
        val caption: String
        val url: String
        var authenticationToken: String? = null
        var publicationIdentifier: String? = null
        val logger: Logger

        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider")
        System.setProperty("baseLogFileName", "${Constants.LOG_PATH}${Constants.LOG_FILE_NAME}${Constants.LOG_FILE_SUFFIX}")

        logger = updateLogger(LogManager.getLogger(this::class.qualifiedName), Level.INFO)

        try {

            parser = DefaultParser()
            commandLine = parser.parse(options, arguments)

            if (commandLine.hasOption("pIn")) {
                if (!commandLine.hasOption("pOut")) throw ParseException("Value for the option <<pOut>> or <<pathOut>> is missing. Check the usage section below.")
                publicationInputPath = commandLine.getOptionValue("pIn")
                if (!File(publicationInputPath).exists()) throw FileNotFoundException("Value for the option <<pIn>> or <<pathIn>> must be a existing path. Check the usage section below.") else {
                    if (commandLine.hasOption("a") && commandLine.hasOption("pId")) {
                        if (!File(publicationInputPath).isFile) throw ParseException("Value for the option <<pIn>> or <<pathIn>> must be a path to a file. Check the usage section below.") else {
                            logger.info("Input path found: CUSTOM")
                            logger.info("Path: $publicationInputPath")
                        }
                    } else {
                        logger.info("Input path found: CUSTOM")
                        logger.info("Path: $publicationInputPath")
                    }
                }
            } else {
                if (commandLine.hasOption("pOut")) throw ParseException("Value for the option <<pOut>> or <<pathOut>> is missing. Check the usage section below.")
                publicationInputPath = Constants.INPUT_PATH
                logger.info("Input path found: DEFAULT")
                logger.info("Path: $publicationInputPath")
            }

            if (commandLine.hasOption("pOut")) {
                if (!commandLine.hasOption("pIn")) throw ParseException("Value for the option <<pIn>> or <<pathIn>> is missing. Check the usage section below.")
                publicationOutputPath = commandLine.getOptionValue("pOut")
                if (!File(publicationOutputPath).exists()) throw FileNotFoundException("Value for the option <<pOut>> or <<pathOut>> must be an existing path. Check the usage section below.") else {
                    if (!File(publicationOutputPath).isDirectory) throw ParseException("Value for the option <<pOut>> or <<pathOut>> must be a path to a directory. Check the usage section below.") else {
                        logger.info("Output path found: CUSTOM")
                        logger.info("Path: $publicationOutputPath")
                    }
                }
            } else {
                if (commandLine.hasOption("pIn")) throw ParseException("Value for the option <<pIn>> or <<pathIn>> is missing. Check the usage section below.")
                publicationOutputPath = Constants.OUTPUT_PATH
                logger.info("Output path found: DEFAULT")
                logger.info("Path: $publicationOutputPath")
            }

            url = commandLine.getOptionValue("u")
            logger.info("URL found: $url")

            caption = commandLine.getOptionValue("c")
            logger.info("Caption found: $caption")

            if (commandLine.hasOption("a")) {
                if (!commandLine.hasOption("pId")) throw ParseException("Value for the option <<pId>> or <<publicationId>> is missing. Check the usage section below.")
                if (!commandLine.hasOption("pIn")) throw ParseException("Value for the option <<pIn>> or <<pathIn>> is missing. Check the usage section below.")
                if (!commandLine.hasOption("pOut")) throw ParseException("Value for the option <<pOut>> or <<pathOut>> is missing. Check the usage section below.")
                authenticationToken = commandLine.getOptionValue("a")
                if (commandLine.hasOption("pId")) {
                    if (!commandLine.hasOption("a")) throw ParseException("Value for the option <<a>> or <<authToken>> is missing. Check the usage section below.")
                    if (!commandLine.hasOption("pIn")) throw ParseException("Value for the option <<pIn>> or <<pathIn>> is missing. Check the usage section below.")
                    if (!commandLine.hasOption("pOut")) throw ParseException("Value for the option <<pOut>> or <<pathOut>> is missing. Check the usage section below.")
                    publicationIdentifier = commandLine.getOptionValue("pId")
                }
            }

            if (commandLine.hasOption("pId")) {
                if (!commandLine.hasOption("a")) throw ParseException("Value for the option <<a>> or <<authToken>> is missing. Check the usage section below.")
                if (!commandLine.hasOption("pIn")) throw ParseException("Value for the option <<pIn>> or <<pathIn>> is missing. Check the usage section below.")
                if (!commandLine.hasOption("pOut")) throw ParseException("Value for the option <<pOut>> or <<pathOut>> is missing. Check the usage section below.")
                publicationIdentifier = commandLine.getOptionValue("pId")
                if (commandLine.hasOption("a")) {
                    if (!commandLine.hasOption("pId")) throw ParseException("Value for the option <<pId>> or <<publicationId>> is missing. Check the usage section below.")
                    if (!commandLine.hasOption("pIn")) throw ParseException("Value for the option <<pIn>> or <<pathIn>> is missing. Check the usage section below.")
                    if (!commandLine.hasOption("pOut")) throw ParseException("Value for the option <<pOut>> or <<pathOut>> is missing. Check the usage section below.")
                    authenticationToken = commandLine.getOptionValue("a")
                }
            }

            if (commandLine.hasOption("a")) {
                logger.info("Authentication token found: $authenticationToken")
            }
            if (commandLine.hasOption("pId")) {
                logger.info("Publication identifier found: $publicationIdentifier")
            }

            publicationController = PublicationController()
            publicationController.load(publicationInputPath, publicationOutputPath)
            publicationController.addUrl(Parameters(caption, url, authenticationToken, publicationIdentifier))

            logger.info("${Constants.PROGRAM_NAME} execution terminated.")

        } catch (exception: FileNotFoundException) {

            logger.error(exception.message)
            logger.info("${Constants.PROGRAM_NAME} execution terminated.")

        } catch (exception: ParseException) {

            logger.error(exception.message)
            val formatter = HelpFormatter()
            formatter.printHelp(Constants.PROGRAM_NAME, options)
            logger.error("End of the usage section.")
            logger.info("${Constants.PROGRAM_NAME} execution terminated.")

        }

    }

    private fun loadCommandLineOptions(): Options {

        val options = Options()
        var source = Option.builder("pIn").longOpt("pathIn").desc("Relative path to the input file/directory. It must be used with <<pOut>> or <<pathOut>> option. [OPTIONAL]").hasArg().argName("Source File/Directory").build()
        options.addOption(source)
        source = Option.builder("pOut").longOpt("pathOut").desc("Relative path to the output directory. It must be used with <<pIn>> or <<pathIn>> option. [OPTIONAL]").hasArg().argName("Result Directory").build()
        options.addOption(source)
        source = Option.builder("c").longOpt("caption").desc("Caption for the URL to add. [REQUIRED]").hasArg().argName("Caption").required().build()
        options.addOption(source)
        source = Option.builder("u").longOpt("url").desc("URL to add. It must be a valid URL. [REQUIRED]").hasArg().argName("Url").required().build()
        options.addOption(source)
        source = Option.builder("a").longOpt("authToken").desc("Authentication token for R@SM-ReadersourcingServerSide. It must be used with options <<pIn>> or <<pathIn>>,  <<pOut>> or <<pathOut>> and  <<pId>> or <<publicationId>> [OPTIONAL]").hasArg().argName("Authentication Token").build()
        options.addOption(source)
        source = Option.builder("pId").longOpt("publicationId").desc("Publication identifier for R@SM-ReadersourcingServerSide. It must be used with options <<pIn>> or <<pathIn>>,  <<pOut>> or <<pathOut>> and  <<a>> or <<authToken>> [OPTIONAL]").hasArg().argName("Publication Identifier").build()
        options.addOption(source)
        return options

    }

}


