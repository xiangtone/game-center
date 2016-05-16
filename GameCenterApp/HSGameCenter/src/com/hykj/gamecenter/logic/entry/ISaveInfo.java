package com.hykj.gamecenter.logic.entry;

public interface ISaveInfo
{
    /**
     * homepage上面的页面标志
     */
    String HOME_PAGE_INDEX = "home_page_index";

    /**
     * 判断首页数据是否加载(对于activity)
     */
    String HOME_IS_LOAD = "home_is_load";

    /**
     * 排行界面页面（topic fragment）数据标志
     */
    String TOPIC_FRAGMENT = "topic_fragment";

    /**
     * 轮播播放第几张
     */
    String RECOMMED_CIRCLE_INDEX = "recommed_circle_index";

    /**
     * 是否获取全局变量的值
     */
    String HOME_IS_GET_GLOBAL = "home_is_get_global";

    String BROAD_CAST_FLAG = "broadcastflag";
    String RSP_UPDATE = "rsp_update";
    String SHOULD_UPDATE = "shouldUpdate";

    String GAME_CLASSIFY = "game_classfiy";
    String APP_CLASSIFY = "app_classfiy";
    
    String GROUP_ELEM_INFO = "group_elem_info";

    String IS_FOOTER_PULL = "footer_pull_ENABLE";
    
    String CURRENT_PAGE = "current_page";
    
}
