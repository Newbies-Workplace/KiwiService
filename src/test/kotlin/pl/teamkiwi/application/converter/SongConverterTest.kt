package pl.teamkiwi.application.converter

import io.mockk.every
import io.mockk.mockk
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import pl.teamkiwi.application.util.DownloadPathProvider
import pl.teamkiwi.domain.service.createTestSong

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
internal class SongConverterTest {

    private val pathProvider = mockk<DownloadPathProvider>()
    private val songConverter = SongConverter(pathProvider)

    @Nested
    inner class ToSongResponse {

        @Test
        fun `should convert date to millis`() {
            //given
            val date = DateTime.now()
            val song = createTestSong(uploadDate = date)

            every { pathProvider.imagePath(any()) } returns "validPath"
            every { pathProvider.songPath(any()) } returns "validPath"

            //when
            val response = with(songConverter) { song.toSongResponse() }

            //then
            assertEquals(date.millis, response.uploadDate)
        }

        @Test
        fun `should return null imagePath if imageFile is null`() {
            //given
            val song = createTestSong(imageFile = null)

            every { pathProvider.songPath(any()) } returns "validPath"

            //when
            val response = with(songConverter) { song.toSongResponse() }

            //then
            assertNull(response.imagePath)
        }
    }
}