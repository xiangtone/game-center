package com.x.ui.activity.floating;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

import com.x.R;
import com.x.publics.utils.Utils;
import com.x.ui.activity.floating.RadialMenuItem.OnSemiCircularRadialMenuPressed;
import com.x.ui.view.floating.FloatPreferebce;

/**
 * 
* @ClassName: RadialMenu
* @Description: 扇型菜单窗口

* @date 2014-5-23 下午3:49:17
*
 */
public class RadialMenu {
	
	private Context context = null;
	private RadialMenuView mMenu;
	//数据存储
	private FloatPreferebce	mfloatPreferebce = null;
	private RadialMenuItem mOne, mTwo, mThree, mFour;
	private RadialMenuItem mFive, mSix, mSeven, mEight;
	public static final int COOLINGTIME = 1200; //点击冷却间隔
	
	public RadialMenu(Context con) {
		context = con;
	}
	
	public RadialMenuView getView(){
		init();
		return mMenu;
	}

	private void init() {
		mMenu = new RadialMenuView(context);
		mfloatPreferebce = FloatPreferebce.getInstance(context);
		//temp
		String one = mfloatPreferebce.getTempSave("one");
		mOne = new RadialMenuItem("one", context.getResources().getDrawable(R.drawable.desktop_add_normal),
				context.getResources().getDrawable(R.drawable.desktop_delete));
		if(one != null){
			Drawable drawable = getDrawable(one);
			if(drawable != null){
				mOne.setIcon(drawable);
				mOne.setPackages(one);
			}else{
				mOne.setIcon(context.getResources().getDrawable(R.drawable.desktop_add_normal));
				mOne.setPackages(null);
				mfloatPreferebce.setTempSave(mOne.getMenuID(), null);
			}
		}
		String two = mfloatPreferebce.getTempSave("two");
		mTwo = new RadialMenuItem("two", context.getResources().getDrawable(R.drawable.desktop_add_normal),
				context.getResources().getDrawable(R.drawable.desktop_delete));
		if(two != null){
			Drawable drawable = getDrawable(two);
			if(drawable != null){
				mTwo.setIcon(drawable);
				mTwo.setPackages(two);
			}else{
				mTwo.setIcon(context.getResources().getDrawable(R.drawable.desktop_add_normal));
				mTwo.setPackages(null);
				mfloatPreferebce.setTempSave(mTwo.getMenuID(), null);
			}

		}
		String three = mfloatPreferebce.getTempSave("three");
		mThree = new RadialMenuItem("three", context.getResources().getDrawable(R.drawable.desktop_add_normal),
				context.getResources().getDrawable(R.drawable.desktop_delete));
		if(three != null){
			Drawable drawable = getDrawable(three);
			if(drawable != null){
				mThree.setIcon(drawable);
				mThree.setPackages(three);
			}else{
				mThree.setIcon(context.getResources().getDrawable(R.drawable.desktop_add_normal));
				mThree.setPackages(null);
				mfloatPreferebce.setTempSave(mThree.getMenuID(), null);
			}
		}
		String four = mfloatPreferebce.getTempSave("four");
		mFour = new RadialMenuItem("four", context.getResources().getDrawable(R.drawable.desktop_add_normal),
				context.getResources().getDrawable(R.drawable.desktop_delete));
		if(four != null){
			Drawable drawable = getDrawable(four);
			if(drawable != null){
				mFour.setIcon(drawable);
				mFour.setPackages(four);
			}else{
				mFour.setIcon(context.getResources().getDrawable(R.drawable.desktop_add_normal));
				mFour.setPackages(null);
				mfloatPreferebce.setTempSave(mFour.getMenuID(), null);
			}
		}
		
		String five = mfloatPreferebce.getTempSave("five");
		mFive = new RadialMenuItem("five", context.getResources().getDrawable(R.drawable.desktop_add_normal),
				context.getResources().getDrawable(R.drawable.desktop_delete));
		if(five != null){
			Drawable drawable = getDrawable(five);
			if(drawable != null){
				mFive.setIcon(drawable);
				mFive.setPackages(five);
			}else{
				mFive.setIcon(context.getResources().getDrawable(R.drawable.desktop_add_normal));
				mFive.setPackages(null);
				mfloatPreferebce.setTempSave(mFive.getMenuID(), null);
			}
		}
		
		String six = mfloatPreferebce.getTempSave("six");
		mSix = new RadialMenuItem("six", context.getResources().getDrawable(R.drawable.desktop_add_normal),
				context.getResources().getDrawable(R.drawable.desktop_delete));
		if(six != null){
			Drawable drawable = getDrawable(six);
			if(drawable != null){
				mSix.setIcon(drawable);
				mSix.setPackages(six);
			}else{
				mSix.setIcon(context.getResources().getDrawable(R.drawable.desktop_add_normal));
				mSix.setPackages(null);
				mfloatPreferebce.setTempSave(mSix.getMenuID(), null);
			}
		}
		
		String seven = mfloatPreferebce.getTempSave("seven");
		mSeven = new RadialMenuItem("seven", context.getResources().getDrawable(R.drawable.desktop_add_normal),
				context.getResources().getDrawable(R.drawable.desktop_delete));
		if(seven != null){
			Drawable drawable = getDrawable(seven);
			if(drawable != null){
				mSeven.setIcon(drawable);
				mSeven.setPackages(seven);
			}else{
				mSeven.setIcon(context.getResources().getDrawable(R.drawable.desktop_add_normal));
				mSeven.setPackages(null);
				mfloatPreferebce.setTempSave(mSeven.getMenuID(), null);
			}
		}
		
		String eight = mfloatPreferebce.getTempSave("eight");
		mEight = new RadialMenuItem("eight", context.getResources().getDrawable(R.drawable.desktop_add_normal),
				context.getResources().getDrawable(R.drawable.desktop_delete));
		if(eight != null){
			Drawable drawable = getDrawable(eight);
			if(drawable != null){
				mEight.setIcon(drawable);
				mEight.setPackages(eight);
			}else{
				mEight.setIcon(context.getResources().getDrawable(R.drawable.desktop_add_normal));
				mEight.setPackages(null);
				mfloatPreferebce.setTempSave(mEight.getMenuID(), null);
			}
		}
		
		mMenu.addMenuItem(mOne.getMenuID(), mOne);
		mMenu.addMenuItem(mTwo.getMenuID(), mTwo);
		mMenu.addMenuItem(mThree.getMenuID(), mThree);
		mMenu.addMenuItem(mFour.getMenuID(), mFour);
		mMenu.addMenuItem(mFive.getMenuID(), mFive);
		mMenu.addMenuItem(mSix.getMenuID(), mSix);
		mMenu.addMenuItem(mSeven.getMenuID(), mSeven);
		mMenu.addMenuItem(mEight.getMenuID(), mEight);
				
		mOne.setOnSemiCircularRadialMenuPressed(new OnSemiCircularRadialMenuPressed() {
			@Override
			public void onMenuItemPressed(boolean isEdit) {
				if(isEdit){
					mOne.setIcon(context.getResources().getDrawable(R.drawable.desktop_add_normal));
					mOne.setPackages(null);
					mfloatPreferebce.setTempSave(mOne.getMenuID(), null);
					mMenu.updateView();
				}else{
					long cooling = mfloatPreferebce.getCoolingTime();
					if(cooling != -1){
						long time = System.currentTimeMillis() - cooling;
						if(time < COOLINGTIME){
							mfloatPreferebce.setCoolingTime(System.currentTimeMillis());
							return;
						}
					}
					if(mOne.getPackages() != null){
						boolean result = Utils.launchApp(context, mOne.getPackages());
						if(!result){
							mOne.setIcon(context.getResources().getDrawable(R.drawable.desktop_add_normal));
							mOne.setPackages(null);
							mfloatPreferebce.setTempSave(mOne.getMenuID(), null);
							mMenu.updateView();
						}
					}else{
						selectApp(mOne.getMenuID());	
					}
					mfloatPreferebce.setCoolingTime(System.currentTimeMillis());
				}
			}
		});
		
		mTwo.setOnSemiCircularRadialMenuPressed(new OnSemiCircularRadialMenuPressed() {
			@Override
			public void onMenuItemPressed(boolean isEdit) {
				if(isEdit){
					mTwo.setIcon(context.getResources().getDrawable(R.drawable.desktop_add_normal));
					mTwo.setPackages(null);
					mfloatPreferebce.setTempSave(mTwo.getMenuID(), null);
					mMenu.updateView();
				}else{
					long cooling = mfloatPreferebce.getCoolingTime();
					if(cooling != -1){
						long time = System.currentTimeMillis() - cooling;
						if(time < COOLINGTIME){
							mfloatPreferebce.setCoolingTime(System.currentTimeMillis());
							return;
						}
					}
					if(mTwo.getPackages() != null){
						boolean result = Utils.launchApp(context, mTwo.getPackages());
						if(!result){
							mTwo.setIcon(context.getResources().getDrawable(R.drawable.desktop_add_normal));
							mTwo.setPackages(null);
							mfloatPreferebce.setTempSave(mTwo.getMenuID(), null);
							mMenu.updateView();
						}
					}else{
						selectApp(mTwo.getMenuID());
					}
					mfloatPreferebce.setCoolingTime(System.currentTimeMillis());
				}
			}
		});
		
		mThree.setOnSemiCircularRadialMenuPressed(new OnSemiCircularRadialMenuPressed() {
			@Override
			public void onMenuItemPressed(boolean isEdit) {
				if(isEdit){
					mThree.setIcon(context.getResources().getDrawable(R.drawable.desktop_add_normal));
					mThree.setPackages(null);
					mfloatPreferebce.setTempSave(mThree.getMenuID(), null);
					mMenu.updateView();
				}else{
					long cooling = mfloatPreferebce.getCoolingTime();
					if(cooling != -1){
						long time = System.currentTimeMillis() - cooling;
						if(time < COOLINGTIME){
							mfloatPreferebce.setCoolingTime(System.currentTimeMillis());
							return;
						}
					}
					if(mThree.getPackages() != null){
						boolean result = Utils.launchApp(context, mThree.getPackages());
						if(!result){
							mThree.setIcon(context.getResources().getDrawable(R.drawable.desktop_add_normal));
							mThree.setPackages(null);
							mfloatPreferebce.setTempSave(mThree.getMenuID(), null);
							mMenu.updateView();
						}
					}else{
						selectApp(mThree.getMenuID());
					}
					mfloatPreferebce.setCoolingTime(System.currentTimeMillis());
				}
			}
		});
		
		mFour.setOnSemiCircularRadialMenuPressed(new OnSemiCircularRadialMenuPressed() {
			@Override
			public void onMenuItemPressed(boolean isEdit) {
				if(isEdit){
					mFour.setIcon(context.getResources().getDrawable(R.drawable.desktop_add_normal));
					mFour.setPackages(null);
					mfloatPreferebce.setTempSave(mFour.getMenuID(), null);
					mMenu.updateView();
				}else{
					long cooling = mfloatPreferebce.getCoolingTime();
					if(cooling != -1){
						long time = System.currentTimeMillis() - cooling;
						if(time < COOLINGTIME){
							mfloatPreferebce.setCoolingTime(System.currentTimeMillis());
							return;
						}
					}
					if(mFour.getPackages() != null){
						boolean result = Utils.launchApp(context, mFour.getPackages());
						if(!result){
							mFour.setIcon(context.getResources().getDrawable(R.drawable.desktop_add_normal));
							mFour.setPackages(null);
							mfloatPreferebce.setTempSave(mFour.getMenuID(), null);
							mMenu.updateView();
						}
					}else{
						selectApp(mFour.getMenuID());
					}
					mfloatPreferebce.setCoolingTime(System.currentTimeMillis());
				}
			}
		});
		
		mFive.setOnSemiCircularRadialMenuPressed(new OnSemiCircularRadialMenuPressed() {
			@Override
			public void onMenuItemPressed(boolean isEdit) {
				if(isEdit){
					mFive.setIcon(context.getResources().getDrawable(R.drawable.desktop_add_normal));
					mFive.setPackages(null);
					mfloatPreferebce.setTempSave(mFive.getMenuID(), null);
					mMenu.updateView();
				}else{
					long cooling = mfloatPreferebce.getCoolingTime();
					if(cooling != -1){
						long time = System.currentTimeMillis() - cooling;
						if(time < COOLINGTIME){
							mfloatPreferebce.setCoolingTime(System.currentTimeMillis());
							return;
						}
					}
					if(mFive.getPackages() != null){
						boolean result = Utils.launchApp(context, mFive.getPackages());
						if(!result){
							mFive.setIcon(context.getResources().getDrawable(R.drawable.desktop_add_normal));
							mFive.setPackages(null);
							mfloatPreferebce.setTempSave(mFive.getMenuID(), null);
							mMenu.updateView();
						}
					}else{
						selectApp(mFive.getMenuID());
					}
					mfloatPreferebce.setCoolingTime(System.currentTimeMillis());
				}
			}
		});
		
		mSix.setOnSemiCircularRadialMenuPressed(new OnSemiCircularRadialMenuPressed() {
			@Override
			public void onMenuItemPressed(boolean isEdit) {
				if(isEdit){
					mSix.setIcon(context.getResources().getDrawable(R.drawable.desktop_add_normal));
					mSix.setPackages(null);
					mfloatPreferebce.setTempSave(mSix.getMenuID(), null);
					mMenu.updateView();
				}else{
					long cooling = mfloatPreferebce.getCoolingTime();
					if(cooling != -1){
						long time = System.currentTimeMillis() - cooling;
						if(time < COOLINGTIME){
							mfloatPreferebce.setCoolingTime(System.currentTimeMillis());
							return;
						}
					}
					if(mSix.getPackages() != null){
						boolean result = Utils.launchApp(context, mSix.getPackages());
						if(!result){
							mSix.setIcon(context.getResources().getDrawable(R.drawable.desktop_add_normal));
							mSix.setPackages(null);
							mfloatPreferebce.setTempSave(mSix.getMenuID(), null);
							mMenu.updateView();
						}
					}else{
						selectApp(mSix.getMenuID());
					}
					mfloatPreferebce.setCoolingTime(System.currentTimeMillis());
				}
			}
		});
		
		mSeven.setOnSemiCircularRadialMenuPressed(new OnSemiCircularRadialMenuPressed() {
			@Override
			public void onMenuItemPressed(boolean isEdit) {
				if(isEdit){
					mSeven.setIcon(context.getResources().getDrawable(R.drawable.desktop_add_normal));
					mSeven.setPackages(null);
					mfloatPreferebce.setTempSave(mSeven.getMenuID(), null);
					mMenu.updateView();
				}else{
					long cooling = mfloatPreferebce.getCoolingTime();
					if(cooling != -1){
						long time = System.currentTimeMillis() - cooling;
						if(time < COOLINGTIME){
							mfloatPreferebce.setCoolingTime(System.currentTimeMillis());
							return;
						}
					}
					if(mSeven.getPackages() != null){
						boolean result = Utils.launchApp(context, mSeven.getPackages());
						if(!result){
							mSeven.setIcon(context.getResources().getDrawable(R.drawable.desktop_add_normal));
							mSeven.setPackages(null);
							mfloatPreferebce.setTempSave(mSeven.getMenuID(), null);
							mMenu.updateView();
						}
					}else{
						selectApp(mSeven.getMenuID());
					}
					mfloatPreferebce.setCoolingTime(System.currentTimeMillis());
				}
			}
		});
		
		mEight.setOnSemiCircularRadialMenuPressed(new OnSemiCircularRadialMenuPressed() {
			@Override
			public void onMenuItemPressed(boolean isEdit) {
				if(isEdit){
					mEight.setIcon(context.getResources().getDrawable(R.drawable.desktop_add_normal));
					mEight.setPackages(null);
					mfloatPreferebce.setTempSave(mEight.getMenuID(), null);
					mMenu.updateView();
				}else{
					long cooling = mfloatPreferebce.getCoolingTime();
					if(cooling != -1){
						long time = System.currentTimeMillis() - cooling;
						if(time < COOLINGTIME){
							mfloatPreferebce.setCoolingTime(System.currentTimeMillis());
							return;
						}
					}
					if(mEight.getPackages() != null){
						boolean result = Utils.launchApp(context, mEight.getPackages());
						if(!result){
							mEight.setIcon(context.getResources().getDrawable(R.drawable.desktop_add_normal));
							mEight.setPackages(null);
							mfloatPreferebce.setTempSave(mEight.getMenuID(), null);
							mMenu.updateView();
						}
					}else{
						selectApp(mEight.getMenuID());
					}
					mfloatPreferebce.setCoolingTime(System.currentTimeMillis());
				}
			}
		});
		
	}
	
//	@Override
//	public void onBackPressed() {
//		if(mMenu.getEditStatus()){
//			mMenu.setEditStatus(false);
//		}else{
//			super.onBackPressed();			
//		}
//	}
	
	
	/**
	 * open AppList
	 * */
	private void selectApp(String key){
		Intent intent = new Intent(context, AppListAvticity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("Item", key);
		context.startActivity(intent);
		FloatService.showViewStatus = FloatService.NORADIALMENU;
	}
	
	private Drawable getDrawable(String packages){
		Drawable drawable = null;
		try {
			drawable = context.getPackageManager().getApplicationIcon(packages);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		return drawable;
	}
}
