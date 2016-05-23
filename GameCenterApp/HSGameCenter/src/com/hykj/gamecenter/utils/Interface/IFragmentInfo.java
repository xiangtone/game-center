package com.hykj.gamecenter.utils.Interface;

public interface IFragmentInfo {

	final class FragmentTabLabel {

		public static final String RECOM_LABEL = "RECOM_LABEL"; // 推荐
		public static final String TOPIC_LABEL = "TOPIC_LABEL"; // 排行
		public static final String CLASSIFY_LABEL = "CLASSIFY_LABEL"; // 分类
		public static final String GAME_LABEL = "GAME_LABEL"; // 游戏
	}

	String getFragmentTabLabel();

}
