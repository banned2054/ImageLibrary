package banned.mirai

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info

import java.io.File

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
        
        
    }
    
    override fun onDisable()
    {
        super.onDisable()
        
        FileConfig.save()
        ImageFileData.save()
    }
    
    @Suppress("NAME_SHADOWING")
    fun reloadFilePath()
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
