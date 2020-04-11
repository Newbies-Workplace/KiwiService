package pl.teamkiwi.application.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
internal class DownloadPathProviderTest {

    private val serverAddress = "serverAddress"
    private val pathProvider = DownloadPathProvider(serverAddress)

    @Nested
    inner class ImagePath {

        @Test
        fun `should return valid image path`() {
            //given
            val imageName = "image.jpg"

            //when
            val path = pathProvider.imagePath(imageName)

            //then
            assertEquals("serverAddress/v1/file/image/image.jpg", path)
        }
    }

    @Nested
    inner class SongPath {

        @Test
        fun `should return valid song path`() {
            //given
            val songName = "song.mp3"

            //when
            val path = pathProvider.songPath(songName)

            //then
            assertEquals("serverAddress/v1/file/song/song.mp3", path)

        }
    }
}