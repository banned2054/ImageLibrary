package banned.mirai.register

import banned.mirai.ImageLibrary
import banned.mirai.config.CommandConfig
import banned.mirai.config.FileConfig
import banned.mirai.data.ImageFileData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.xdrop.fuzzywuzzy.FuzzySearch
import net.mamoe.mirai.event.events.FriendEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.MessageSource.Key.quote
import net.mamoe.mirai.utils.ExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import java.io.File

fun sendImageRegister()
{
    ImageLibrary.ImageMessageChannel.subscribeAlways<MessageEvent> { event ->
        if (event.message.contentToString().startsWith('/'))
        {
            val command = event.message.contentToString().substring(startIndex = 1)
            if (command.lowercase().startsWith(CommandConfig.sendImageCommandList) != "Wrong")
            {
                val matchAns = FuzzySearch.extractOne(command.lowercase(), ImageFileData.subTags)
                
                val contact = event.sender
                if (FileConfig.haveSub)
                {
                    val tag = matchAns.string
                    if (matchAns.score >= 80)
                    {
                        val filePath = getImagePath(tag)
                        
                        if (filePath == "Wrong")
                        {
                            if (event is FriendEvent) subject.sendMessage("该tag下没有图片，请导入图片")
                            else subject.sendMessage(message.quote() + "该tag下没有图片，请导入图片")
                        }
                        else
                        {
                            val res : ExternalResource = File(filePath).toExternalResource()
                            val image : Image = contact.uploadImage(res)
                            withContext(Dispatchers.IO) {
                                res.close()
                            }
                            if (event is FriendEvent) subject.sendMessage(image)
                            else subject.sendMessage(message.quote() + image)
                        }
                        
                    }
                    else
                    {
                        if (event is FriendEvent) subject.sendMessage("没有该tag，请检查tag是否出错")
                        else subject.sendMessage(message.quote() + "没有该tag，请检查tag是否出错")
                    }
                }
                else
                {
                    val filePath = getImagePath()
                    
                    val res : ExternalResource = File(filePath).toExternalResource()
                    val image : Image = contact.uploadImage(res)
                    withContext(Dispatchers.IO) {
                        res.close()
                    }
                    if (event is FriendEvent) subject.sendMessage(image)
                    else subject.sendMessage(message.quote() + image)
                }
            }
        }
    }
    
}

fun getImagePath() : String
{
    val length = ImageFileData.imagePaths[0].size
    if (length < 1)
    {
        return "Wrong"
    }
    val imageIndex = (1..length).random() - 1
    return ImageFileData.imagePaths[0][imageIndex]
}

fun getImagePath(tag : String) : String
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