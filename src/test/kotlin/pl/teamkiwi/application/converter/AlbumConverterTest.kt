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
import pl.teamkiwi.domain.`interface`.SongRepository
import pl.teamkiwi.domain.service.createTestAlbum

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
internal class AlbumConverterTest {

    private val pathProvider = mockk<DownloadPathProvider>()
    private val songRepository = mockk<SongRepository>()
    private val albumConverter = AlbumConverter(pathProvider, songRepository)

    @Nested
    inner class ToAlbumResponse {

        @Test
        fun `should return empty list when album do not have any song`() {
            //given
            val albumId = "albumId"
            val album = createTestAlbum(id = albumId)

            every { songRepository.findIdsByAlbumId(albumId) } returns emptyList()
            every { pathProvider.imagePath(any()) } returns "validPath"

            //when
            val response = with(albumConverter) { album.toAlbumResponse() }

            //then
            assertEquals(emptyList<String>(), response.songs)
        }

        @Test
        fun `should convert date to millis`() {
            //given
            val date = DateTime.now()
            val album = createTestAlbum(uploadDate = date)

            every { songRepository.findIdsByAlbumId(any()) } returns emptyList()
            every { pathProvider.imagePath(any()) } returns "validPath"

            //when
            val response = with(albumConverter) { album.toAlbumResponse() }

            //then
            assertEquals(date.millis, response.uploadDate)
        }

        @Test
        fun `should return null imagePath if imageFile is null`() {
            //given
            val album = createTestAlbum(imageFile = null)

            every { songRepository.findIdsByAlbumId(any()) } returns emptyList()

            //when
            val response = with(albumConverter) { album.toAlbumResponse() }

            //then
            assertNull(response.imagePath)
        }
    }
}