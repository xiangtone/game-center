package com.x.business.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.x.R;
import com.x.business.account.AccountManager;
import com.x.business.audio.AudioEffectManager;
import com.x.business.shake.ShakeDetector;
import com.x.business.shake.ShakeDetector.OnShakeListener;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.KeywordsRequest;
import com.x.publics.http.model.KeywordsResponse;
import com.x.publics.http.model.MasUser;
import com.x.publics.http.model.Pager;
import com.x.publics.http.model.SearchRequest;
import com.x.publics.http.model.SearchResponse;
import com.x.publics.http.model.SearchTipsRequest;
import com.x.publics.http.model.SearchRequest.SearchData;
import com.x.publics.http.model.SearchTipsRequest.SearchTipsData;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.RingtonesBean;
import com.x.publics.model.WallpaperBean;
import com.x.publics.utils.Constan;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.SharedPrefsUtil;
import com.x.publics.utils.TextUtils;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;

/**
 * 
 * @ClassName: SearchManager
 * @Description: TODO(这里用一句话描述这个类的作用)
 
 * @date 2014-7-25 下午12:20:54
 * 
 */
public class SearchManager {

	private long lastTime;
	private long tipLastTime;
	private int tipsIntervalTime = 500;
	private int shakeIntervalTime = 1000;
	private ShakeDetector shakeDetector;
	private boolean isLoadingData = false;
	private static SearchManager searchManager;
	private Map<Integer, Boolean> shakeMap = new HashMap<Integer, Boolean>();

	/**
	 * 
	 * @Title: SearchManager
	 * @Description: 私有化的构造方法，保证外部的类不能通过构造器来实例化。
	 * @param @return
	 * @throws
	 */
	private SearchManager() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @Title: getInstance
	 * @Description: 获取单例对象实例，同步方法，实现线程互斥访问，保证线程安全。
	 * @param @return 构建单例模式
	 * @return NoticationManager
	 * @throws
	 */
	public static synchronized SearchManager getInstance() {
		if (searchManager == null) {
			searchManager = new SearchManager();
		}
		return searchManager;
	}

	public boolean getInitState(Context context) {
		return SharedPrefsUtil.getValue(context, SearchConstan.SEARCH_FRAGMENT_INITED, false);
	}

	public void setInitState(Context context, boolean inited) {
		SharedPrefsUtil.putValue(context, SearchConstan.SEARCH_FRAGMENT_INITED, inited);
	}

	public String getQueryKey(Context context) {
		return SharedPrefsUtil.getValue(context, SearchConstan.QUERY_KEY, "");
	}

	public void setQueryKey(Context context, String queryKey) {
		SharedPrefsUtil.putValue(context, SearchConstan.QUERY_KEY, queryKey);
	}

	public void removeQueryKey(Context context) {
		SharedPrefsUtil.removeValue(context, SearchConstan.QUERY_KEY);
	}

	public int getPosition(Context context) {
		return SharedPrefsUtil.getValue(context, SearchConstan.CURRENT_POSITION_ID, 0);
	}

	public void setPosition(Context context, int position) {
		SharedPrefsUtil.putValue(context, SearchConstan.CURRENT_POSITION_ID, position);
	}

	public boolean isShowTipsView(Context context) {
		return SharedPrefsUtil.getValue(context, SearchConstan.SHOW_TIPS_VIEW, false);
	}

	public void setShowTipsView(Context context, boolean flag) {
		SharedPrefsUtil.putValue(context, SearchConstan.SHOW_TIPS_VIEW, flag);
	}

	/**
	 * 
	* @Title: initParams 
	* @Description: TODO 
	* @param @param context    
	* @return void
	 */
	public void initParams(Context context) {
		removeQueryKey(context);
		setPosition(context, 0);
		setInitState(context, false);
		setShowTipsView(context, false);
	}

	/**
	 * @Title: getKwData
	 * @Description: TODO
	 * @param @param albumId
	 * @param @param ps
	 * @param @param pn
	 * @return void
	 */
	public void getKwData(Context context, final Handler handler, int albumId, int pn) {
		KeywordsRequest request = new KeywordsRequest(Constan.Rc.GET_SEARCH_KEYWORDS);
		request.setAlbumId(albumId);
		Pager pager = new Pager(pn);
		pager.setPs(ResourceUtil.getInteger(context, R.integer.keyword_page_size));
		request.setPager(pager);
		DataFetcher.getInstance().getSearchKeywords(request, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				LogUtil.getLogger().d("response==>" + response.toString()); // 日志输出
				KeywordsResponse keywordsResponse = (KeywordsResponse) JsonUtil.jsonToBean(response,
						KeywordsResponse.class);
				if (keywordsResponse != null && keywordsResponse.keywordlist != null) {
					// 处理成功响应数据
					handler.sendMessage(handler.obtainMessage(SearchConstan.State.SEARCH_KEYWORDS_SUCCESS,
							keywordsResponse));
				} else {
					// 木有资源
					handler.sendEmptyMessage(SearchConstan.State.SEARCH_KEYWORDS_EMPTY);
				}
			}
		},

		new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
				// 处理异常响应数据
				handler.sendEmptyMessage(SearchConstan.State.SEARCH_KEYWORDS_FAILURE);
			}
		}, false);
	}

	/**
	 * 
	 * @Title: getQueryKeyData
	 * @Description: 获取列表数据
	 * @param
	 * @return void
	 */
	public void getQueryKeyData(Context context, final Handler handler, final int rc, final String searchKey,
			String action, int pageSize, int pageNum) {

		SearchRequest request = new SearchRequest(rc);
		Pager pager = new Pager(pageNum);
		if (pageSize != 0) {
			pager.setPs(pageSize);
		}
		SearchData data = new SearchData();
		data.setClientId(AccountManager.getInstance().getClientId(context));
		data.setContent(searchKey);

		MasUser masUser = new MasUser();
		masUser.setUserId(AccountManager.getInstance().getUserId(context));
		masUser.setUserName(AccountManager.getInstance().getUserName(context));
		masUser.setUserPwd(AccountManager.getInstance().getPwd(context));

		request.setAction(action);
		request.setPage(pager);
		request.setData(data);
		request.setMasUser(masUser);

		// 标记正在加载数据
		isLoadingData = true;

		switch (rc) {
		case Constan.Rc.GET_APPS_SEARCH:
		case Constan.Rc.KEYWORDS_APPS_LIST:
			DataFetcher.getInstance().getSearchData(request, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
					LogUtil.getLogger().d("response==>" + response.toString());
					SearchResponse searchResponse = (SearchResponse) JsonUtil
							.jsonToBean(response, SearchResponse.class);

					// 获取相应数据
					Message msg = new Message();
					Bundle bundle = new Bundle();
					ArrayList<AppInfoBean> appList = new ArrayList<AppInfoBean>();
					if (searchResponse.appNum == 0) {
						appList = searchResponse.recommendlist;
						bundle.putBoolean("isRecommend", true);
						if (rc == Constan.Rc.GET_APPS_SEARCH) {
							DataEyeManager.getInstance().source(StatisticConstan.SrcName.SEARCH, 0, null, 0L, null,
									DataEyeManager.getSearchKey(searchKey), false);
						}
					} else {
						appList = searchResponse.applist;
						bundle.putBoolean("isRecommend", false);

						if (rc == Constan.Rc.GET_APPS_SEARCH) {
							DataEyeManager.getInstance().source(StatisticConstan.SrcName.SEARCH, 0, null, 0L, null,
									DataEyeManager.getSearchKey(searchKey), true);
						}

					}
					bundle.putParcelableArrayList("appList", appList);
					bundle.putBoolean("isLast", searchResponse.isLast);
					msg.setData(bundle);
					msg.what = SearchConstan.State.RESPONSE_APP_DATA_SUCCESS;
					handler.sendMessage(msg);

				}
			}, new ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					error.printStackTrace();
					handler.sendEmptyMessage(SearchConstan.State.RESPONSE_APP_DATA_FAILURE);
					if (rc == Constan.Rc.GET_APPS_SEARCH) {
						DataEyeManager.getInstance().source(StatisticConstan.SrcName.SEARCH, 0, null, 0L, null,
								DataEyeManager.getSearchKey(searchKey), false);
					}
				}
			}, false);
			break;

		case Constan.Rc.POST_MUSIC_SEARCH:
		case Constan.Rc.KEYWORDS_MUSIC_LIST:
			DataFetcher.getInstance().getSearchData(request, new Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					LogUtil.getLogger().d("response==>" + response.toString());
					SearchResponse searchResponse = (SearchResponse) JsonUtil
							.jsonToBean(response, SearchResponse.class);

					// 获取相应数据
					Message msg = new Message();
					Bundle bundle = new Bundle();
					ArrayList<RingtonesBean> musicList = new ArrayList<RingtonesBean>();
					if (searchResponse.musicNum == 0) {
						musicList = searchResponse.recommendMusiclist;
						bundle.putBoolean("isRecommend", true);
						if (rc == Constan.Rc.POST_MUSIC_SEARCH) {
							DataEyeManager.getInstance().source(StatisticConstan.SrcName.SEARCH, 0, null, 0L, null,
									DataEyeManager.getSearchKey(searchKey), false);
						}
					} else {
						musicList = searchResponse.musiclist;
						bundle.putBoolean("isRecommend", false);
						if (rc == Constan.Rc.POST_MUSIC_SEARCH) {
							DataEyeManager.getInstance().source(StatisticConstan.SrcName.SEARCH, 0, null, 0L, null,
									DataEyeManager.getSearchKey(searchKey), true);
						}
					}
					bundle.putParcelableArrayList("musicList", musicList);
					bundle.putBoolean("isLast", searchResponse.isLast);
					msg.setData(bundle);
					msg.what = SearchConstan.State.RESPONSE_MUSIC_DATA_SUCCESS;
					handler.sendMessage(msg);

				}
			}, new ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					error.printStackTrace();
					handler.sendEmptyMessage(SearchConstan.State.RESPONSE_MUSIC_DATA_FAILURE);
					if (rc == Constan.Rc.POST_MUSIC_SEARCH) {
						DataEyeManager.getInstance().source(StatisticConstan.SrcName.SEARCH, 0, null, 0L, null,
								DataEyeManager.getSearchKey(searchKey), false);
					}
				}
			}, false);
			break;

		case Constan.Rc.POST_WALLPAPER_SEARCH:
		case Constan.Rc.KEYWORDS_WALLPAPER_LIST:
			DataFetcher.getInstance().getSearchData(request, new Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					LogUtil.getLogger().d("response==>" + response.toString());
					SearchResponse searchResponse = (SearchResponse) JsonUtil
							.jsonToBean(response, SearchResponse.class);

					// 获取相应数据
					Message msg = new Message();
					Bundle bundle = new Bundle();
					ArrayList<WallpaperBean> imageList = new ArrayList<WallpaperBean>();
					if (searchResponse.imageNum == 0) {
						imageList = searchResponse.recommendImagelist;
						bundle.putBoolean("isRecommend", true);
						if (rc == Constan.Rc.POST_WALLPAPER_SEARCH) {
							DataEyeManager.getInstance().source(StatisticConstan.SrcName.SEARCH, 0, null, 0L, null,
									DataEyeManager.getSearchKey(searchKey), false);
						}
					} else {
						imageList = searchResponse.imagelist;
						bundle.putBoolean("isRecommend", false);
						if (rc == Constan.Rc.POST_WALLPAPER_SEARCH) {
							DataEyeManager.getInstance().source(StatisticConstan.SrcName.SEARCH, 0, null, 0L, null,
									DataEyeManager.getSearchKey(searchKey), true);
						}
					}
					bundle.putParcelableArrayList("imageList", imageList);
					bundle.putBoolean("isLast", searchResponse.isLast);
					msg.setData(bundle);
					msg.what = SearchConstan.State.RESPONSE_IMAGE_DATA_SUCCESS;
					handler.sendMessage(msg);

				}
			}, new ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					error.printStackTrace();
					handler.sendEmptyMessage(SearchConstan.State.RESPONSE_IMAGE_DATA_FAILURE);
					if (rc == Constan.Rc.POST_WALLPAPER_SEARCH) {
						DataEyeManager.getInstance().source(StatisticConstan.SrcName.SEARCH, 0, null, 0L, null,
								DataEyeManager.getSearchKey(searchKey), false);
					}
				}
			}, false);
			break;
		}
	}

	/**
	 * @Title: getSearchTips
	 * @Description: TODO
	 * @param @param context
	 * @param @param handler
	 * @param @param inputText
	 * @param @param pageNum
	 * @param @param pageSize
	 * @return void
	 */
	public void getSearchTips(Context context, final Handler handler, final int rc, String inputText, int pageNum) {
		inputText = Utils.removeSpace(inputText);
		if (TextUtils.isEmpty(inputText)) {
			return;
		}
		long currentTime = System.currentTimeMillis();
		if (currentTime - tipLastTime > tipsIntervalTime || tipLastTime == 0) {
			isLoadingData = false;
			tipLastTime = currentTime;
			SearchTipsRequest request = new SearchTipsRequest(rc);
			SearchTipsData data = new SearchTipsData();
			data.setContent(inputText);
			Pager pager = new Pager(pageNum);
			request.setData(data);
			request.setPage(pager);
			DataFetcher.getInstance().getSearchTips(request, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
					LogUtil.getLogger().d("response==>" + response.toString());

					// 正在加载数据，取消显示tips
					if (isLoadingData) {
						return;
					}

					SearchResponse searchResponse = (SearchResponse) JsonUtil
							.jsonToBean(response, SearchResponse.class);
					if (searchResponse != null && searchResponse.state.code == 200) {
						switch (rc) {
						case Constan.Rc.SEARCH_APPS_TIPS:
							if (searchResponse.appNum != 0)
								handler.sendMessage(handler.obtainMessage(
										SearchConstan.State.RESPONSE_APPS_TIPS_SUCCESS, searchResponse.applist));
							break;

						case Constan.Rc.SEARCH_MUSIC_TIPS:
							if (searchResponse.musicNum != 0)
								handler.sendMessage(handler.obtainMessage(
										SearchConstan.State.RESPONSE_RINGTONES_TIPS_SUCCESS, searchResponse.musiclist));
							break;

						case Constan.Rc.SEARCH_IMAGE_TIPS:
							if (searchResponse.imageNum != 0)
								handler.sendMessage(handler.obtainMessage(
										SearchConstan.State.RESPONSE_WALLPAPERS_TIPS_SUCCESS, searchResponse.imagelist));
							break;
						}
					}
				}
			}, new ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					// TODO Auto-generated method stub
				}
			});
		}
	}

	/**
	 * 开始摇一摇监听
	 */
	public void startShakeDetector(final Context context, final Handler handler) {
		try {
			stopShakeDetector();
			shakeDetector = new ShakeDetector(context);
			shakeDetector.registerOnShakeListener(new OnShakeListener() {

				@Override
				public void onShake() {
					// TODO Auto-generated method stub
					if (!NetworkUtils.isNetworkAvailable(context)) {
						ToastUtil.show(context, ResourceUtil.getString(context, R.string.network_canot_work),
								Toast.LENGTH_SHORT);
						return;
					}
					long currentTime = System.currentTimeMillis();
					if (currentTime - lastTime > shakeIntervalTime || lastTime == 0) {
						lastTime = currentTime;
						if (shakeMap.isEmpty() || !shakeMap.get(getPosition(context)) || isShowTipsView(context)) {
							return;
						}
						AudioEffectManager.getInstance().playShakeAudioEffect(context);
						handler.sendEmptyMessage(SearchConstan.State.REFRESH_DATA_SUCCESS);
					}
				}
			});
			shakeDetector.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 停止摇一摇监听
	 */
	public void stopShakeDetector() {
		if (shakeDetector != null)
			shakeDetector.stop();
		AudioEffectManager.getInstance().releaseMusicPlayer();
	}

	public void putCurValue(int index, boolean flag) {
		shakeMap.put(index, flag);
	}

	public void showSoftInput(Activity activity) {
		WindowManager.LayoutParams params = activity.getWindow().getAttributes();
		if (params.softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) {
			// 隐藏软键盘
			//activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
			//params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;
		} else {
			// 弹出软键盘
			params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE;
		}
	}

	public void hideSoftInput(Activity activity) {
		try {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
		} catch (Exception e) {
			//ToDo 
		}
	}
}
