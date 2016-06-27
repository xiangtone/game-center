package com.hykj.gamecenter.fragment;

import android.app.Fragment;
import android.os.Handler;

public abstract class BaseFragment extends Fragment
{
     public abstract Handler getHandler();
     public abstract boolean hasLoadedData();
     public abstract void setHasLoadedData(boolean loaded);
     public abstract void initFragmentListData();      //弃用
     public abstract boolean isLoading();         //弃用
//     public int getActivitySelectedItem(){
//    	 if(getActivity() instanceof HomePageActivity){
//    		 return ((HomePageActivity)getActivity()).getCurrentItem();
//    	 }
//    	 return 0;
//     }
}
