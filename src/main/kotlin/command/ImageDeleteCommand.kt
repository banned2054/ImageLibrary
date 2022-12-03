package banned.mirai.command

import banned.mirai.ImageLibrary
import banned.mirai.command.ImageReloadCommand.reloadFilePath
import banned.mirai.config.FileConfig
import banned.mirai.data.ImageFileData
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

object ImageDeleteCommand : SimpleCommand(
        ImageLibrary, "image-delete", description = "删除指定的图片"
                                         )
{
    @Handler
    suspend fun CommandSender.handle(message : String)
    {
        
        if (this.hasPermission(ImageLibrary.PERMISSION_EXECUTE_1))
        {
            reloadFilePath()
            
            if (message.indexOf(FileConfig.filePath) >= 0)
            {
                val ans = deleteImage(message)
                if (ans) sendMessage("删除成功")
                else sendMessage("删除失败")
                reloadFilePath()
            }
            else
            {
                if (FileConfig.haveSub)
                {
                    val splited = message.split('/')
                    if (splited.size == 2)
                    {
                        if (ImageFileData.subTags.contains(splited[0].lowercase()))
                        {
                            val ans = deleteImage(FileConfig.filePath + '/' + splited[0] + '/' + splited[1])
                            if (ans) sendMessage("删除成功")
                            else sendMessage("删除失败")
                        }
                    }
                }
                else
                {
                    val ans = deleteImage(FileConfig.filePath + '/' + message)
                    if (ans) sendMessage("删除成功")
                    else sendMessage("删除失败")
                }
            }
            reloadFilePath()
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
    
    private fun deleteImage(originPath : String) : Boolean
    {
        val path = Paths.get(originPath)
        
        return try
        {
            return Files.deleteIfExists(path)
        }
        catch (e : IOException)
        {
            e.printStackTrace()
            false
        }
    }
}