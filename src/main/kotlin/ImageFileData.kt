package banned.mirai

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object ImageFileData : AutoSavePluginData("ImageFileData")
{
    
    //Sub File name list
    var subFiles : MutableList<String> by value()
    
    //Map file name to numb
    var subFileNumber : HashMap<String, Int> by value()
    
    //imagepaths[mapnumb][index]
    var imagePaths : MutableList<MutableList<String>> by value()
}