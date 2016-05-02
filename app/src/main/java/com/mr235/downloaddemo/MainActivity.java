package com.mr235.downloaddemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void start(View view) {
		startService(new Intent(this, DownloadService.class));
	}

	public void stop(View view) {
		stopService(new Intent(this, DownloadService.class));
	}

	public void bind(View view) {

		bindService(new Intent(this, DownloadService.class), conn, BIND_AUTO_CREATE);
	}

	public void unbind(View view) {
		unbindService(conn);
	}

	public void startDownloadActivity(View view) {
		startActivity(new Intent(this, DownloadActivity.class));
	}

	ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			System.out.println("--------onServiceConnected------- " + ((DownloadService.ServiceBinder) service).getService());
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}
	};
}
