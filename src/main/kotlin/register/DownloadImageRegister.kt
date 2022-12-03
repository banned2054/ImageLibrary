package banned.mirai.register

import banned.mirai.ImageLibrary
import banned.mirai.command.ImageReloadCommand.reloadFilePath
import banned.mirai.command.ImageRenameCommand.getRandomName
import banned.mirai.config.CommandConfig
import banned.mirai.config.FileConfig
import banned.mirai.data.ImageFileData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.events.FriendEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.data.MessageSource.Key.quote
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.time
import net.mamoe.mirai.message.nextMessage
import net.mamoe.mirai.utils.ExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.MiraiInternalApi
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths

@OptIn(MiraiInternalApi::class)
fun downloadImageRegister()
{
    ImageLibrary.ImageMessageChannel.subscribeAlways<MessageEvent> { event ->
        if (message.contentToString().startsWith('/'))
        {
            var command = message.contentToString().substring(startIndex = 1)
            val match = command.lowercase().startsWith(CommandConfig.downloadImageCommandList)
            if (match != "Wrong")
            {
                command = command.substring(match.length)
                if (FileConfig.haveSub)
                {
                    val tag = command.lowercase().indexOf(ImageFileData.subTags)
                    if (tag != "Wrong")
                    {
                        if (message.contains(Image))
                        {
                            val images = message.filterIsInstance<Image>()
                            downloadImage(images, tag)
                            
                            reloadFilePath()
                            if (event is FriendEvent) subject.sendMessage("图片上传成功")
                            else subject.sendMessage(message.quote() + "图片上传成功")
                            
                        }
                        else
                        {
                            if (event is FriendEvent) subject.sendMessage("没有图片，请在60s内发送图片")
                            else subject.sendMessage(message.quote() + "没有图片，请在60s内发送图片")
                            
                            val nextMsg = nextMessage()
                            //判断发送的时间
                            if (nextMsg.time - time < 60)
                            {
                                val images = nextMsg.filterIsInstance<Image>()
                                if (message.contains(Image))
                                {
                                    downloadImage(images, tag)
                                    
                                    reloadFilePath()
                                    if (event is FriendEvent) subject.sendMessage("图片上传成功")
                                    else subject.sendMessage(message.quote() + "图片上传成功")
                                    
                                }
                                else
                                {
                                    if (event is FriendEvent) subject.sendMessage("没有获取图片")
                                    else subject.sendMessage(message.quote() + "没有获取图片")
                                }
                            }
                        }
                    }
                }
                else
                {
                    if (message.contains(Image))
                    {
                        val images = message.filterIsInstance<Image>()
                        downloadImage(images)
                        
                        reloadFilePath()
                        if (event is FriendEvent) subject.sendMessage("图片上传成功")
                        else subject.sendMessage(message.quote() + "图片上传成功")
                    }
                    else
                    {
                        if (event is FriendEvent) subject.sendMessage("没有图片，请在60s内发送图片")
                        else subject.sendMessage(message.quote() + "没有图片，请在60s内发送图片")
                        
                        val nextMsg = nextMessage()
                        //判断发送的时间
                        if (nextMsg.time - time < 60)
                        {
                            val images = nextMsg.filterIsInstance<Image>()
                            if (message.contains(Image))
                            {
                                downloadImage(images)
                                
                                reloadFilePath()
                                if (event is FriendEvent) subject.sendMessage("图片上传成功")
                                else subject.sendMessage(message.quote() + "图片上传成功")
                            }
                            else
                            {
                                if (event is FriendEvent) subject.sendMessage("没有获取图片")
                                else subject.sendMessage(message.quote() + "没有获取图片")
                            }
                        }
                    }
                }
            }
        }
    }
}

fun String.startsWith(command : List<String>) : String
{
    command.forEach {
        if (this.startsWith(it)) return it
    }
    return "Wrong"
}

fun String.indexOf(tags : List<String>) : String
{
    tags.forEach {
        if (this.indexOf(it) >= 0) return it
    }
    return "Wrong"
}

suspend fun downloadFile(url : String, fileName : String)
{
    URL(url).openStream().use { Files.copy(it, Paths.get(fileName)) }
    val master = Bot.instances[0].getFriend(FileConfig.master)!!
    val res : ExternalResource = File(fileName).toExternalResource()
    val image : Image = master.uploadImage(res)
    withContext(Dispatchers.IO) {
        res.close()
    }
    master.sendMessage(PlainText("$fileName upload") + image)
}

@OptIn(MiraiInternalApi::class)
suspend fun downloadImage(images : List<Image>)
{
    val uniqueImages = listOf<Image>().toMutableList()
    
    val md5s = listOf<ByteArray>().toMutableList()
    
    for (image in images)
    {
        if (md5s.contains(image.md5)) continue
        md5s.add(image.md5)
        uniqueImages.add(image)
    }
    md5s.clear()
    val paths = ImageFileData.imagePaths[0]
    val path = FileConfig.filePath + '/'
    
    for (url in uniqueImages)
    {
        val newPath = getRandomName(path, url.imageType.formatName, paths)
        downloadFile(url.queryUrl(), newPath)
        
    }
}

@OptIn(MiraiInternalApi::class)
suspend fun downloadImage(images : List<Image>, tag : String)
{
    val uniqueImages = listOf<Image>().toMutableList()
    
    val md5s = listOf<ByteArray>().toMutableList()
    
    for (image in images)
    {
        if (md5s.contains(image.md5)) continue
        md5s.add(image.md5)
        uniqueImages.add(image)
    }
    md5s.clear()
    val index = ImageFileData.subTags.indexOf(tag)
    val paths = ImageFileData.imagePaths[index]
    val path = ImageFileData.subFiles[index] + '/'
    
    for (url in uniqueImages)
    {
        val newPath = getRandomName(path, url.imageType.formatName, paths)
        downloadFile(url.queryUrl(), newPath)
        
    }
}