package flash.android.game;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class AnimView extends SurfaceView implements Callback, Runnable
{
	private boolean mIsRunning = false;

	private SurfaceHolder mSurfaceHolder = null;
	private Canvas mCanvas = null;

	public AnimView(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void run()
	{
		while (mIsRunning)
		{
			// 在这里加上线程安全锁
			synchronized (mSurfaceHolder)
			{
				mCanvas = mSurfaceHolder.lockCanvas();
				draw();
				/** 绘制结束后解锁显示在屏幕上 **/
				mSurfaceHolder.unlockCanvasAndPost(mCanvas);
			}
		}

	}

	private void draw()
	{

	}

}
