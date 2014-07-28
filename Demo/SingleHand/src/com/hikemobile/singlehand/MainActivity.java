package com.hikemobile.singlehand;

import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends ActivityGroup
{
	private final int WIDTH = 204;
	private final int HEIGHT = 363;
	private LinearLayout m_hand;
	private ImageView m_content;
	private View m_view;
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		m_hand = (LinearLayout) findViewById(R.id.layHand);
		m_content = (ImageView) findViewById(R.id.imageView1);
		launchActivity("clock", ClockActivity.class);
		handler.post(runnableUi);
	}

	private void launchActivity(String id, Class<?> activityClass)
	{
		m_hand.removeAllViews();
		Intent intent = new Intent(MainActivity.this, activityClass);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		Window window = getLocalActivityManager().startActivity(id, intent);
		View view = window.getDecorView();
		this.m_hand.addView(view);
		this.m_view = view;
		this.m_view.setDrawingCacheEnabled(true);

		// ----------------------------------------------------------------
	}

	private Bitmap convertViewToBitmap(View view)
	{
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		// view.layout(0, 0, view.getWidth(), view.getHeight());
		view.buildDrawingCache(true);
		Bitmap bitmap = view.getDrawingCache();
		return bitmap;
	}

	private Bitmap getBitmap(View v)
	{
		Bitmap bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bitmap);
		v.draw(c);
		return bitmap;
	}

	// 构建Runnable对象，在runnable中更新界面
	Runnable runnableUi = new Runnable()
	{
		@Override
		public void run()
		{
			m_view.destroyDrawingCache();
			m_content.setImageBitmap(convertViewToBitmap(m_view));
			handler.postDelayed(runnableUi, 50);
		}

	};
}
