package banned.mirai.command

import banned.mirai.ImageLibrary
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission

object ImageHelpCommand : SimpleCommand(
        ImageLibrary, "image", "image-help", description = "提供指令帮助"
                                       )
{
    @Handler
    suspend fun CommandSender.handle()
    {
        
        if (this.hasPermission(ImageLibrary.PERMISSION_EXECUTE_1))
        {
            sendMessage(
                    "/发(或者send) + tag                  发图片\n" + "/存(或者add) + tag + [图片]\t上传图\n" + "/image-tags                             展示当前图库中存的图片数\n"
                       )
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
}