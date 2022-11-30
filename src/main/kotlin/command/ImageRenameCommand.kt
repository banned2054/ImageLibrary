package banned.mirai.command

import banned.mirai.FileConfig
import banned.mirai.ImageFileData
import banned.mirai.ImageLibrary
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission
import java.io.File
import java.util.concurrent.ThreadLocalRandom
import kotlin.streams.asSequence

object ImageRenameCommand : SimpleCommand(
        ImageLibrary, "image-rename"
                                         )
{
    private val charPool : List<Char> =('A'..'Z') + ('0'..'9')
    
    @Handler
    suspend fun CommandSender.handle()
    {
        
        if (this.hasPermission(ImageLibrary.PERMISSION_EXECUTE_1))
        {
            renameFile()
            ImageReloadCommand.reloadFilePath()
            sendMessage("图片重命名成功！")
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
    
    private fun renameFile()
    {
        val oldFilePaths = ImageFileData.imagePaths
        for (oldPaths in oldFilePaths)
        {
            for (image in oldPaths)
            {
                var splits = image.split('/')
                val finalName = splits[splits.size - 1]
                splits = finalName.split('.')
                val imageType = splits[splits.size - 1]
                val path = image.dropLast(finalName.length)
                var newFileName =
                        ThreadLocalRandom.current().ints(FileConfig.imageNameLength.toLong(), 0, charPool.size)
                                .asSequence().map(charPool::get).joinToString("")
                var nowPath = "$path$newFileName.$imageType"
                while (oldPaths.contains(nowPath))
                {
                    newFileName =
                            ThreadLocalRandom.current().ints(FileConfig.imageNameLength.toLong(), 0, charPool.size)
                                    .asSequence().map(charPool::get).joinToString("")
                    nowPath = "$path$newFileName.$imageType"
                }
                val oldFile = File(image)
                val newFile = File(nowPath)
                oldFile.renameTo(newFile)
            }
        }
    }
}