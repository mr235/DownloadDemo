package com.mr235.downloaddemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashSet;

public class DownloadActivity extends AppCompatActivity {

	private MyDownloadListener mDownloadListener = new MyDownloadListener();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		ListView mLv = (ListView) findViewById(R.id.lv);
		mLv.setAdapter(new MyAdapter(this, mDownloadListener));
		DownloadTask.getInstance().addListener(mDownloadListener);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		DownloadTask.getInstance().removeListener(mDownloadListener);
	}

	public static class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		private MyDownloadListener mDownloadListener;

		private Context mContext = null;

		private View.OnClickListener buttonClick = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent service = new Intent(mContext, DownloadService.class);
				service.putExtra(DownloadService.URL_DOWNLOAD, (String) v.getTag(R.layout.item));
				mContext.startService(service);
			}
		};

		public MyAdapter(Context context, MyDownloadListener listener) {
			this.mInflater = LayoutInflater.from(context);
			mDownloadListener = listener;
			mContext = context;
		}

		@Override
		public int getCount() {
			return 10;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item, null);
				holder = new ViewHolder();
	            /*得到各个控件的对象*/
				holder.textView = (TextView) convertView.findViewById(R.id.tv);
				holder.button = (Button) convertView.findViewById(R.id.bt); // to ItemButton

				convertView.setTag(holder); //绑定ViewHolder对象
			} else {
				holder = (ViewHolder) convertView.getTag(); //取出ViewHolder对象
			}
			holder.textView.setTag(R.layout.item, position + "");
			mDownloadListener.addView(holder.textView);
			holder.button.setTag(R.layout.item, position + "");
			holder.button.setOnClickListener(buttonClick);
			holder.textView.setText("%0");
			return convertView;
		}

		static class ViewHolder {
			TextView textView;
			Button button;
		}
	}

	static class MyDownloadListener implements DownloadTask.DownloadListener {

		private HashSet<TextView> views = new HashSet<>();

		public void addView(TextView view) {
			views.add(view);
		}

		@Override
		public void onProgress(String url, long downloadCount, long totalCount) {
			for (TextView tv : views) {
				String tag = (String) tv.getTag(R.layout.item);
				if (url.equals(tag)) {
					tv.setText(downloadCount + "/" + totalCount + "  %" + (100 * downloadCount / totalCount));
				}
			}
		}
	}
}
