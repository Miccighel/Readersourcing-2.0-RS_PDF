package it.uniud.readersourcing2.utils

import java.nio.file.Paths

object Constants {
    val PROGRAM_NAME = "Readersourcing 2.0"
    val PATH_SEPARATOR = System.getProperty("file.separator").toString()
    val BASE_PATH = "${Paths.get("").toAbsolutePath().parent}$PATH_SEPARATOR"
    val PATH = "${Paths.get("").toAbsolutePath()}$PATH_SEPARATOR"
    val INPUT_PATH = "${PATH}data$PATH_SEPARATOR"
    val OUTPUT_PATH = "${PATH}res$PATH_SEPARATOR"
    val LOG_PATH = "${PATH}log$PATH_SEPARATOR"
    val LOG_FILE_NAME = "Execution"
    val LOG_FILE_SUFFIX = ".log"
    val RS_SERVER_URL = "http://localhost:3000/"
}