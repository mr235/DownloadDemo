package com.mr235.downloaddemo;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/5/2.
 */
public class DownloadTask {

	private ExecutorService mExecutor = Executors.newFixedThreadPool(5);
	private HashSet<DownloadListener> mListeners = new HashSet<>();
	private Map<String, TaskImpl> mRunningTasks = Collections.synchronizedMap(new HashMap<String, TaskImpl>());

	private Handler mHandler = new Handler(Looper.getMainLooper());

	private static DownloadTask ourInstance = new DownloadTask();

	public static DownloadTask getInstance() {
		return ourInstance;
	}

	private DownloadTask() {

	}

	public void addListener(DownloadListener listener) {
		if (listener != null) {
			mListeners.add(listener);
		}
	}

	public void removeListener(DownloadListener listener) {
		mListeners.remove(listener);
	}

	public void download(String url) {
		if (!isDownloading(url)) {
			TaskImpl impl = new TaskImpl(url);
			impl.setDownloadListener(mInnerDownloadListener);
			mExecutor.submit(impl);
		}
	}

	private DownloadListener mInnerDownloadListener = new DownloadListener() {
		@Override
		public void onProgress(final String url, final long downloadCount, final long totalCount) {
			for (final DownloadListener listener : mListeners) {

				if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
					listener.onProgress(url, downloadCount, totalCount);
				} else {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							listener.onProgress(url, downloadCount, totalCount);
						}
					});
				}
			}
		}
	};

	private boolean isDownloading(String url) {
		return mRunningTasks.get(url) != null;
	}

	public interface DownloadListener {
		void onProgress(String url, long downloadCount, long totalCount);
	}

	private static class TaskImpl implements Runnable {
		private int downloadCount = 0;
		private int totalCount = 0;
		private DownloadListener mListener = null;
		private Random rand = new Random();
		private String mUrl = null;

		public void setDownloadListener(DownloadListener mListener) {
			this.mListener = mListener;
		}

		public TaskImpl(String url) {
			mUrl = url;
			totalCount = 200 + rand.nextInt(300);
		}

		@Override
		public void run() {
			while (true) {
				downloadCount += rand.nextInt(8);
				if (downloadCount >= totalCount) {
					downloadCount = totalCount;
				}
				if (mListener != null) {
					mListener.onProgress(mUrl, downloadCount, totalCount);
				}
				if (downloadCount == totalCount) {
					break;
				}
				SystemClock.sleep(1000 + rand.nextInt(2000));
			}
		}
	}
}
