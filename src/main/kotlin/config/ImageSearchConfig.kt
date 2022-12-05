package banned.mirai.config

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object ImageSearchConfig : AutoSavePluginConfig("SearchSetting")
{
    @ValueDescription("http代理地址，例如http://127.0.0.1:1080")
    val httpProxyUrl by value("")
    
    
    @ValueDescription("socket代理地址，例如127.0.0.1:1080")
    val socketProxyUrl by value("")
    
    @ValueDescription("基于Saucenao的搜索功能，基本能搜到大部分的pixiv的图片")
    val pixivSearchCommandList : List<String> by value(listOf("搜", "搜图", "search"))
    
    @ValueDescription(
            """
        要注册账号获取api key，才能使用它的搜图功能。
        注册：https://saucenao.com/user.php
        api key：https://saucenao.com/user.php?page=search-api
    """
                     )
    val saucenaoApiKey by value("")
    
    @ValueDescription("基于TraceMoe的搜索功能，可以搜索动漫中出现的画面")
    val animeSearchCommandList : List<String> by value(listOf("搜动漫"))
    
    @ValueDescription("基于Ascii2D的搜索功能，可以搜索推特和pixiv的图片")
    val twitterSearchCommandList : List<String> by value(listOf("搜推特"))
    
}