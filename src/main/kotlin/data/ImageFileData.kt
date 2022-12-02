package banned.mirai.data

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object ImageFileData : AutoSavePluginData("ImageFileData")
{
    
    //Sub File name list
    var subFiles : MutableList<String> by value()
    
    var subTags : MutableList<String> by value()
    
    //imagepaths[mapnumb][index]
    var imagePaths : MutableList<MutableList<String>> by value()
}