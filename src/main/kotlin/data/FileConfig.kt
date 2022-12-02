package banned.mirai.data

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object FileConfig : AutoSavePluginConfig("ImagePath")
{
    
    @ValueDescription("是否有子目录，例如不同图片存不同的子文件里，根据子文件名分类")
    var haveSub by value<Boolean>(true)
    
    @ValueDescription("图片根文件夹，如果有子文件夹则会按子文件夹的名字来分类")
    val filePath by value<String>()
    
    @ValueDescription("不需要的文件夹，例如Cache之类,建议全小写英文")
    val fileFilter : List<String> by value()
    
    @ValueDescription("发图时使用的命令，建议全小写英文")
    val sendImageCommandList : List<String> by value(listOf<String>("发", "send", "发图"))
    
    @ValueDescription("文件名长度，如果使用image-rename命令会给所有文件统一命名")
    val imageNameLength by value<Int>(5)
}