package banned.mirai

import banned.mirai.FileConfig.sendImageCommandList
import banned.mirai.command.ImageReloadCommand
import banned.mirai.command.ImageRenameCommand
import banned.mirai.command.ImageShowTagCommand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.permission.PermissionService
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.MessageSource.Key.quote
import net.mamoe.mirai.utils.ExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.info
import java.io.File

object ImageLibrary : KotlinPlugin(JvmPluginDescription(
        id = "banned.mirai.image-library",
        name = "Image Library",
        version = "0.2.0",
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
        ImageRenameCommand.register()
        ImageShowTagCommand.register()
        
        ImageMessageChannel.subscribeAlways<MessageEvent> { event ->
            if (event.message.contentToString().startsWith('/'))
            {
                val command = event.message.contentToString().substring(startIndex = 1)
                val splits = command.split(' ').toTypedArray()
                if (splits.isNotEmpty())
                {
                    if (sendImageCommandList.contains(splits[0]))
                    {
                        if (splits.size < 2)
                        {
                            if (FileConfig.haveSub)
                            {
                                //获取文件tag
                                val length = ImageFileData.subTags.size
                                val imageIndex = (1..length).random() - 1
                                val tag = ImageFileData.subTags[imageIndex].lowercase()
                                //读取随机图片路径
                                val filePath = getImagePath(tag)
                                //读取图片
                                val contact = event.sender
                                val res : ExternalResource = File(filePath).toExternalResource()
                                val image : Image = contact.uploadImage(res)
                                withContext(Dispatchers.IO) {
                                    res.close()
                                }
                                //分群消息和好友消息
                                if (event is GroupMessageEvent)
                                {
                                    subject.sendMessage(message.quote() + image)
                                }
                                else
                                {
                                    subject.sendMessage(image)
                                }
                            }
                            else
                            {
                                val filePath = getImagePath()
                                
                                val contact = event.sender
                                val res : ExternalResource = File(filePath).toExternalResource()
                                val image : Image = contact.uploadImage(res)
                                withContext(Dispatchers.IO) {
                                    res.close()
                                }
                                subject.sendMessage(image)
                            }
                            
                        }
                        else
                        {
                            if (FileConfig.haveSub)
                            {
                                val tag = splits[1].lowercase()
                                if (ImageFileData.subTags.contains(tag))
                                {
                                    val filePath = getImagePath(tag)
                                    
                                    if (filePath == "Wrong")
                                    {
                                        subject.sendMessage("该tag下没有图片，请导入图片")
                                    }
                                    else
                                    {
                                        val contact = event.sender
                                        val res : ExternalResource = File(filePath).toExternalResource()
                                        val image : Image = contact.uploadImage(res)
                                        withContext(Dispatchers.IO) {
                                            res.close()
                                        }
                                        subject.sendMessage(image)
                                    }
                                    
                                }
                                else
                                {
                                    subject.sendMessage("没有该tag，请检查tag是否出错")
                                }
                            }
                            else
                            {
                                val filePath = getImagePath()
                                
                                val contact = event.sender
                                val res : ExternalResource = File(filePath).toExternalResource()
                                val image : Image = contact.uploadImage(res)
                                withContext(Dispatchers.IO) {
                                    res.close()
                                }
                                subject.sendMessage(image)
                            }
                        }
                    }
                }
            }
        }
        
        PERMISSION_EXECUTE_1 // 初始化, 注册权限
    }
    
    private var ImageMessageChannel = GlobalEventChannel.filter { it is GroupMessageEvent || it is FriendMessageEvent }
    
    val PERMISSION_EXECUTE_1 by lazy {
        PermissionService.INSTANCE.register(permissionId("reload-image"), "重新读取图片路径")
    }
    
    override fun onDisable()
    {
        super.onDisable()
        
        FileConfig.save()
        ImageFileData.save()
    }
    
    private fun getImagePath() : String
    {
        val length = ImageFileData.imagePaths[0].size
        if (length < 1)
        {
            return "Wrong"
        }
        val imageIndex = (1..length).random() - 1
        return ImageFileData.imagePaths[0][imageIndex]
    }
    
    private fun getImagePath(tag : String) : String
    {
        if (ImageFileData.subTags.contains(tag))
        {
            val index = ImageFileData.subTags.indexOf(tag)
            
            val length = ImageFileData.imagePaths[index].size
            if (length < 1)
            {
                return "Wrong"
            }
            
            val imageIndex = (1..length).random() - 1
            return ImageFileData.imagePaths[index][imageIndex]
        }
        return "Wrong"
    }
    
    fun sendUrlImage(url : String)
    {
        val res : ExternalResource = File(url).toExternalResource()
    }
    
}
