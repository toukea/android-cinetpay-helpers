package com.istat.cinetpay.helpers;

import android.app.Activity;
import android.os.Handler;

public final class ActivityFocusWatcher extends Thread {
	OnFocusChangeListener mCallBack;
	Activity mActivity;
	private boolean run = false;
	private Handler mHandler;

	boolean lastState = false;

	public static interface OnFocusChangeListener {
		public abstract void onFocusChanged(boolean hasFocus);
	}

	public ActivityFocusWatcher(Activity activity) {
		mHandler = new Handler();
		mActivity = activity;
	}

	public void setOnFocusChangeListener(OnFocusChangeListener callBack) {
		mCallBack = callBack;
	}

	public boolean startWatching(OnFocusChangeListener callBack) {
		boolean out = isWatching();
		if (!out) {
			mCallBack = callBack;
			start();
		}
		return out;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (run) {
			checkActivityFocusState();
		}
	}

	private void checkActivityFocusState() {
		// TODO Auto-generated method stub
		final boolean newState = mActivity.hasWindowFocus();
		if (newState != lastState && mCallBack != null && run) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (mCallBack != null)
						mCallBack.onFocusChanged(newState);
				}
			});
		}
		lastState = newState;
	}

	@Override
	public synchronized void start() {
		// TODO Auto-generated method stub
		run = true;
		super.start();
	}

	public void stopWatching() {
		run = false;
	}

	public boolean isWatching() {
		return run;
	}
}
