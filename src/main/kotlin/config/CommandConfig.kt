package banned.mirai.config

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object CommandConfig : AutoSavePluginConfig("CommandSetting")
{
    @ValueDescription("发图时使用的命令，建议全小写英文")
    val sendImageCommandList : List<String> by value(listOf("发", "send", "发图"))
    
    @ValueDescription("存图时使用的命令，建议全小写英文")
    val downloadImageCommandList : List<String> by value(listOf("add", "存"))
}