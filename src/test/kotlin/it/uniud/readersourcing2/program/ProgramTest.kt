package it.uniud.readersourcing2.program

import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.client.j2se.BufferedImageLuminanceSource
import com.google.zxing.common.HybridBinarizer
import org.apache.pdfbox.Loader
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import org.apache.pdfbox.pdmodel.interactive.action.PDActionURI
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ProgramTest {

    @TempDir
    lateinit var temporaryDirectory: Path

    @Test
    @DisplayName("The CLI preserves the RS_Server PDF contract")
    fun testMain() {

        println("[ProgramTest main] - Test begins.")

        val input = temporaryDirectory.resolve("Reader.pdf")
        Files.copy(Path.of("data", "Reader.pdf"), input)
        val inputChecksum = sha256(input)
        val outputDirectory = temporaryDirectory.resolve("output")
        Files.createDirectory(outputDirectory)
        val outputPath = outputDirectory.toString()
        val caption = "Express your rating"
        val url = "https://example.test/rate/42/token"

        Program.main(
            arrayOf(
                "-pIn", input.toString(),
                "-pOut", outputPath,
                "-c", caption,
                "-u", url,
                "-a", "authentication-token",
                "-pId", "42"
            )
        )

        val output = outputDirectory.resolve("Reader-Link.pdf")
        assertTrue(Files.isRegularFile(output))
        assertEquals(inputChecksum, sha256(input), "The input PDF must remain unchanged")
        assertFalse(Files.exists(outputDirectory.resolve("Reader-QRCode.png")))

        Loader.loadPDF(input.toFile()).use { sourceDocument ->
            Loader.loadPDF(output.toFile()).use { outputDocument ->
                assertEquals(sourceDocument.numberOfPages + 1, outputDocument.numberOfPages)
                assertEquals(sourceDocument.version, outputDocument.version)

                val sourceLastPage = sourceDocument.getPage(sourceDocument.numberOfPages - 1)
                val appendedPage = outputDocument.getPage(outputDocument.numberOfPages - 1)
                assertEquals(sourceLastPage.mediaBox.width, appendedPage.mediaBox.width)
                assertEquals(sourceLastPage.mediaBox.height, appendedPage.mediaBox.height)
                assertEquals(url, outputDocument.documentInformation.getCustomMetadataValue("BaseUrl"))

                val link = appendedPage.annotations.filterIsInstance<PDAnnotationLink>().single()
                assertEquals(url, (link.action as PDActionURI).uri)

                val embeddedImages = appendedPage.resources.xObjectNames
                    .map { appendedPage.resources.getXObject(it) }
                    .filterIsInstance<PDImageXObject>()

                assertTrue(embeddedImages.isNotEmpty(), "The appended page must contain the QR code")
                val qrCode = embeddedImages
                    .mapNotNull { image ->
                        runCatching {
                            MultiFormatReader().decode(
                                BinaryBitmap(HybridBinarizer(BufferedImageLuminanceSource(image.image)))
                            ).text
                        }.getOrNull()
                    }
                    .firstOrNull()
                assertEquals(url, qrCode)
            }
        }

        println("[ProgramTest main] - Test ends.")
    }

    private fun sha256(path: Path): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(Files.readAllBytes(path))
        return digest.joinToString("") { "%02x".format(it) }
    }
}
