package flash.android.game;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import flash.android.display.DisplayUtil;

public class AnimClip
{

	/** 上一帧播放时间 **/
	private long m_lastPlayTime = 0;
	/** 播放当前帧的ID **/
	private int m_currentFrame = 0;
	/** 动画frame数量 **/
	private int m_totalFrames = 0;
	/** 用于储存动画资源图片 **/
	private Bitmap[] m_bitmaps = null;
	/** 是否循环播放 **/
	private boolean m_bLoop = false;
	/** 播放结束 **/
	private boolean m_bEnd = false;
	private int m_delay;

	public void initAnimation(Context p_context, String p_assetsPath, int p_delay, boolean p_isLoop) throws IOException
	{
		this.m_bEnd = false;
		this.m_currentFrame = 0;
		this.m_delay = p_delay;
		this.m_bitmaps = DisplayUtil.getBitmapsFromAssets(p_context, p_assetsPath);
		this.m_bLoop = p_isLoop;
		this.m_totalFrames = m_bitmaps.length;

	}

	public void drawAnimation(Canvas p_canvas, int p_x, int p_y, Paint p_paint)
	{
		if (!m_bEnd)
		{
			p_canvas.drawBitmap(m_bitmaps[m_currentFrame], p_x, p_y, p_paint);
			long time = System.currentTimeMillis();
			if (time - m_lastPlayTime > m_delay)
			{
				this.m_currentFrame++;
				if (m_currentFrame >= m_totalFrames)
				{
					if (m_bLoop)
					{
						m_currentFrame = 0;
					}
					else
					{
						this.m_bEnd = true;
					}
				}
			}
		}
	}

}
