package banned.mirai.command

import banned.mirai.ImageLibrary
import banned.mirai.config.FileConfig
import banned.mirai.data.ImageFileData
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission

object ImageShowTagCommand : SimpleCommand(
        ImageLibrary, "image-tags", description = "展示图片tag和对应tag的图片数量"
                                          )
{
    @Handler
    suspend fun CommandSender.handle()
    {
        
        if (this.hasPermission(ImageLibrary.PERMISSION_EXECUTE_1))
        {
            if (FileConfig.haveSub)
            {
                val message = getTags()
                sendMessage(message)
            }
            else
            {
                sendMessage("没有子文件，也就没有tag")
            }
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
    
    fun getTags() : String
    {
        var ans = ""
        var i = 0
        for (tag in ImageFileData.subTags)
        {
            if (ans != "")
            {
                ans += '\n'
            }
            ans += "${tag}:${ImageFileData.imagePaths[i++].size}张图片"
            
        }
        return ans
    }
}