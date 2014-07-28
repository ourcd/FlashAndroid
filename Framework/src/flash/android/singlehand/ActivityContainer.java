package flash.android.singlehand;

import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

public class ActivityContainer extends ActivityGroup
{
	private RelativeLayout m_layout;
	
	public void setLayout(RelativeLayout p_layout)
	{
		this.m_layout = p_layout;
	}

	public void launchActivity(String id, Class<?> activityClass)
	{
		m_layout.removeAllViews();

		Intent intent = new Intent(ActivityContainer.this, activityClass);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		Window window = getLocalActivityManager().startActivity(id, intent);
		View view = window.getDecorView();
		m_layout.addView(view);

	}

	public Bitmap getScreenshot(View p_view)
	{
		p_view.clearFocus(); // 清除视图焦点
		p_view.setPressed(false);// 将视图设为不可点击

		boolean willNotCache = p_view.willNotCacheDrawing(); // 返回视图是否可以保存他的画图缓存
		p_view.setWillNotCacheDrawing(false);

		// Reset the drawing cache background color to fully transparent
		// for the duration of this operation //将视图在此操作时置为透明
		int color = p_view.getDrawingCacheBackgroundColor(); // 获得绘制缓存位图的背景颜色
		p_view.setDrawingCacheBackgroundColor(0); // 设置绘图背景颜色
		if (color != 0)
		{ // 如果获得的背景不是黑色的则释放以前的绘图缓存
			p_view.destroyDrawingCache(); // 释放绘图资源所使用的缓存
		}
		p_view.buildDrawingCache(); // 重新创建绘图缓存，此时的背景色是黑色
		Bitmap cacheBitmap = p_view.getDrawingCache(); // 将绘图缓存得到的,注意这里得到的只是一个图像的引用
		if (cacheBitmap == null)
		{
			return null;
		}
		Bitmap bitmap = Bitmap.createBitmap(cacheBitmap); // 将位图实例化
		// Restore the view //恢复视图
		p_view.destroyDrawingCache();// 释放位图内存
		p_view.setWillNotCacheDrawing(willNotCache);// 返回以前缓存设置
		p_view.setDrawingCacheBackgroundColor(color);// 返回以前的缓存颜色设置
		return bitmap;
	}
}
