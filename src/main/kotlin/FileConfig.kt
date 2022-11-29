package banned.mirai

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object FileConfig : AutoSavePluginConfig("ImagePath")
{
    
    @ValueDescription("是否有子目录，例如不同图片存不同的子文件里，根据子文件名分类")
    var haveSub by value<Boolean>()
    
    @ValueDescription("图片根文件夹，如果有子文件夹则会按子文件夹的名字来分类")
    val filePath by value<String>()
    
    @ValueDescription("不需要的文件夹，例如Cache之类")
    val fileFilter : List<String> by value()
}