package com.mr235.downloaddemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadService extends Service {

	private ServiceBinder mBinder = null;
	public static final String URL_DOWNLOAD = "url_download";

	public DownloadService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("-------onBind-------- " + this);
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mBinder = new ServiceBinder();
		System.out.println("--------onCreate------- " + this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("------onStartCommand--------- " + this);
		String url = intent.getStringExtra(URL_DOWNLOAD);
		DownloadTask.getInstance().download(url);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("------onDestroy--------- " + this);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		System.out.println("-------onUnbind-------- " + this);
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
		System.out.println("-------onRebind-------- " + this);
	}

	public class ServiceBinder extends Binder {
		public DownloadService getService() {
			return DownloadService.this;
		}
	}

}
