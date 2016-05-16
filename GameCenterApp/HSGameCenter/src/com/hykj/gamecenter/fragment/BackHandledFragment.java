package com.hykj.gamecenter.fragment;

import android.app.Activity;
import android.app.Fragment;

/**
 * Created by OddsHou on 2016/3/8.
 */
public abstract class BackHandledFragment extends Fragment {

	public abstract boolean onBackPressed();
	protected BackHandledInterface mBackHandledInterface;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(!(getActivity() instanceof BackHandledInterface)){
			throw new ClassCastException("Hosting Activity must implement BackHandledInterface");
		}else{
			this.mBackHandledInterface = (BackHandledInterface)getActivity();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
	//告诉FragmentActivity，当前Fragment在栈顶
		mBackHandledInterface.setSelectedFragment(this);
	}

	public interface BackHandledInterface{
		void setSelectedFragment(BackHandledFragment selectedFragment);
	}
}
