package banned.mirai.command

import banned.mirai.FileConfig
import banned.mirai.ImageFileData
import banned.mirai.ImageLibrary
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission
import java.io.File

object ImageReloadCommand : SimpleCommand(
        ImageLibrary,
        "image-reload"
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
    private fun reloadFilePath()
    {
        var homePath = File(FileConfig.filePath)
        if (FileConfig.haveSub)
        {
            ImageFileData.subFiles.clear()
            ImageFileData.subFileNumber.clear()
            ImageFileData.imagePaths.clear()
            val files = homePath.listFiles()
            for (file in files!!)
            {
                if (file.isDirectory)
                {
                    ImageFileData.subFiles.add(file.absolutePath)
                    val keyWord = file.name
                    if (FileConfig.fileFilter.contains(keyWord))
                    {
                        continue
                    }
                    ImageFileData.subFileNumber[keyWord] = ImageFileData.subFiles.size - 1;
                    var images = listOf<String>(" ")
                    images = images.toMutableList()
                    images.clear()
                    ImageFileData.imagePaths.add(images)
                }
            }
            for (filePath in ImageFileData.subFiles)
            {
                homePath = File(filePath)
                val files = homePath.listFiles()
                for (file in files!!)
                {
                    if (!file.isDirectory)
                    {
                        val splits = filePath.split(',').toTypedArray()
                        val key = splits[splits.size - 1]
                        ImageFileData.imagePaths[ImageFileData.subFileNumber[key]!!].add(file.absolutePath)
                    }
                }
            }
        }
        else
        {
            ImageFileData.imagePaths.clear()
            var images = listOf<String>(" ")
            images = images.toMutableList()
            images.clear()
            ImageFileData.imagePaths.add(images)
            val files = homePath.listFiles()
            for (file in files!!)
            {
                if (!file.isDirectory)
                {
                    ImageFileData.imagePaths[0].add(file.absolutePath)
                }
            }
            
        }
    }
}