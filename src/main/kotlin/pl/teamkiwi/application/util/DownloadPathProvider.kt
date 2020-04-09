package pl.teamkiwi.application.util

class DownloadPathProvider(
    private val serverAddress: String
) {

    private val getImageV1Path = "/v1/file/image/"
    private val getSongV1Path = "/v1/file/song/"

    fun imagePath(fileName: String) =
        serverAddress + getImageV1Path + fileName

    fun songPath(fileName: String) =
        serverAddress + getSongV1Path + fileName
}