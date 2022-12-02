package banned.mirai.command

import banned.mirai.data.FileConfig
import banned.mirai.data.ImageFileData
import banned.mirai.ImageLibrary
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission
import java.io.File

object ImageReloadCommand : SimpleCommand(
        ImageLibrary,
        "image-reload",
        description = "重新读取文件中的图片"
                                         )
{
    @Handler
    suspend fun CommandSender.handle()
    {
        
        if (this.hasPermission(ImageLibrary.PERMISSION_EXECUTE_1))
        {
            reloadFilePath()
            sendMessage("图片重新读取成功！")
        }
        else
        {
            sendMessage(
                    """
                你没有 ${ImageLibrary.PERMISSION_EXECUTE_1.id} 权限.
                可以在控制台使用 /permission 管理权限.
            """.trimIndent()
                       )
        }
    }
    
    @Suppress("NAME_SHADOWING")
    public fun reloadFilePath()
    {
        var homePath = File(FileConfig.filePath)
        if (FileConfig.haveSub)
        {
            ImageFileData.subFiles.clear()
            ImageFileData.subTags.clear()
            ImageFileData.imagePaths.clear()
            val files = homePath.listFiles()
            for (file in files!!)
            {
                if (file.isDirectory)
                {
                    val keyWord = file.name.lowercase()
                    if (FileConfig.fileFilter.contains(keyWord))
                    {
                        continue
                    }
                    ImageFileData.subFiles.add(file.absolutePath)
                    ImageFileData.subTags.add(keyWord)
                }
            }
            for (filePath in ImageFileData.subFiles)
            {
                homePath = File(filePath)
                val files = homePath.listFiles()
                val imageList = listOf<String>("").toMutableList()
                imageList.clear()
                for (file in files!!)
                {
                    if (!file.isDirectory)
                    {
                        imageList.add(file.absolutePath)
                    }
                }
                ImageFileData.imagePaths.add(imageList)
            }
        }
        else
        {
            ImageFileData.imagePaths.clear()
            val images = listOf<String>(" ").toMutableList()
            images.clear()
            val files = homePath.listFiles()
            for (file in files!!)
            {
                if (!file.isDirectory)
                {
                    images.add(file.absolutePath)
                }
            }
            ImageFileData.imagePaths.add(images)
        }
    }
}