package banned.mirai

import banned.mirai.command.ImageReloadCommand
import banned.mirai.command.ImageRenameCommand
import banned.mirai.command.ImageShowTagCommand
import banned.mirai.config.CommandConfig
import banned.mirai.config.FileConfig
import banned.mirai.data.ImageFileData
import banned.mirai.register.downloadImageRegister
import banned.mirai.register.sendImageRegister
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.permission.PermissionService
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.utils.info

object ImageLibrary : KotlinPlugin(JvmPluginDescription(
        id = "banned.mirai.image-library",
        name = "Image Library",
        version = "0.4.0",
                                                       ) {
    author("banned")
    info("a plugin that can send picture from your storage")
})
{
    override fun onEnable()
    {
        logger.info { "Plugin loaded" }
    
        FileConfig.reload()
        CommandConfig.reload()
        ImageFileData.reload()
    
        ImageReloadCommand.register()
        ImageRenameCommand.register()
        ImageShowTagCommand.register()
        
        sendImageRegister()
        downloadImageRegister()
    
        PERMISSION_EXECUTE_1 // 初始化, 注册权限
    }
    
    var ImageMessageChannel = GlobalEventChannel.filter { it is GroupMessageEvent || it is FriendMessageEvent }
    
    val PERMISSION_EXECUTE_1 by lazy {
        PermissionService.INSTANCE.register(permissionId("reload-image"), "重新读取图片路径")
    }
    
    override fun onDisable()
    {
        super.onDisable()
    
        FileConfig.save()
        CommandConfig.save()
        ImageFileData.save()
    }
    
}

