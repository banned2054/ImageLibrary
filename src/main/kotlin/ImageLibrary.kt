package banned.mirai

import banned.mirai.command.ImageReloadCommand
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.permission.PermissionService
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.utils.info

object ImageLibrary : KotlinPlugin(JvmPluginDescription(
        id = "banned.mirai.image-library",
        name = "Image Library",
        version = "0.1.0",
                                                       ) {
    author("banned")
    info("a plugin that can send picture from your storage")
})
{
    override fun onEnable()
    {
        logger.info { "Plugin loaded" }
        
        FileConfig.reload()
        ImageFileData.reload()
        
        ImageReloadCommand.register()
        
        ImageMessageChannel.subscribeAlways<MessageEvent> { event ->
            if (event.message.toString().startsWith('/'))
            {
                val command = event.message.toString().substring(startIndex = 1)
                val splits = command.split(' ').toTypedArray()
                if (splits.size == 1)
                {
                
                }
                else if (splits.size > 1)
                {
                
                }
            }
        }
        
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
        ImageFileData.save()
    }
    
    fun getImagePath() : String
    {
        val length = ImageFileData.imagePaths[0].size
        val imageIndex = (1..length).random() - 1
        return ImageFileData.imagePaths[0][imageIndex]
    }
    
    fun getImagePath(tag : String) : String
    {
        if (ImageFileData.subFiles.contains(tag))
        {
            val index = ImageFileData.subFileNumber[tag]
            
            val length = ImageFileData.imagePaths[index!!].size
            val imageIndex = (1..length).random() - 1
            return ImageFileData.imagePaths[index][imageIndex]
        }
        return "Wrong"
    }
}
