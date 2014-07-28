package flash.android.ui;

import java.io.File;
import java.io.IOException;

import flash.android.display.DisplayUtil;

import android.content.Context;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/**
 * make it like flash MovieClip
 * 
 * @author Alex Wei (515899670@qq.com)
 * 
 */
public class ImageViewClip extends View
{
    public final static int WHAT_FRAME = 1;
    public final static int WHAT_END = 2;
    protected Handler mHandler = null;
    private String mFolder = "";
    private int mDelay;
    private Options mOpt;
    private String[] mFiles = null;
    private Drawable[] mCaches;
    private int mCurrentFrame = 0;
    private int mMaxFrame;
    private boolean mLoop = false;
    private boolean mReverse = false;
    private boolean mStoped = true;
    private boolean mNeedDraw;
    private Rect mRect = null;
    private boolean mClipMode = false;

    public ImageViewClip(Context context)
    {
        super(context);
    }

    public ImageViewClip(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ImageViewClip(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    /**
     * 
     * @param folder
     *            the folder below assets
     * @param frameRate
     *            the frame rate
     * @param needDraw
     *            whether dispatch invalidate()
     * @param handler
     *            can callback WHAT_FRAME and WHAT_END,or just set null
     * @throws IOException
     * 
     */
    public void initClip(String folder, final int frameRate, boolean needDraw, Options opt, Handler handler) throws IOException
    {
        this.initClip(folder, getResources().getAssets().list(folder), frameRate, needDraw, opt, handler);
    }

    public void initClip(String folder, String[] filenames, final int frameRate, boolean needDraw, Options opt, Handler handler)
    {
        this.mFolder = folder;
        this.mFiles = filenames;
        this.initClip(new BitmapDrawable[mFiles.length], frameRate, needDraw, opt, handler);
    }

    public void initClip(int[] res, final int frameRate, boolean needDraw, Options opt, Handler handler)
    {
        if (null != res)
        {
            mCaches = new Drawable[res.length];
            for (int i = 0; i < res.length; i++)
            {
                mCaches[i] = getResources().getDrawable(res[i]);
                mCaches[i].setBounds(0, 0, mCaches[i].getIntrinsicWidth(), mCaches[i].getIntrinsicHeight());
            }
            initClip(mCaches, frameRate, needDraw, opt, handler);
        }
        else
        {
            throw new Error("res is null");
        }
    }

    public void initClip(Drawable[] drawables, final int frameRate, boolean needDraw, Options opt, Handler handler)
    {
        this.mCaches = drawables;
        this.mDelay = 1000 / frameRate;
        this.mNeedDraw = needDraw;
        this.mOpt = opt;
        if (null == handler)
        {
            this.mHandler = new Handler();
        }
        else
        {
            this.mHandler = handler;
        }
        this.mMaxFrame = drawables.length - 1;
        this.setDrawingCacheEnabled(false);
        this.mClipMode = true;
        // draw first frame
        if (needDraw)
        {
            this.drawFrame(0);
        }
    }

    public boolean isClipMode()
    {
        return mClipMode;
    }

    public int getCurrentFrame()
    {
        return mCurrentFrame;
    }

    public int getTotalFrames()
    {
        return mMaxFrame + 1;
    }

    public int getMaxFrame()
    {
        return mMaxFrame;
    }

    public boolean isStoped()
    {
        return mStoped;
    }

    /*
     * 设置是否倒序播放
     */
    public void setReverse(boolean reverse)
    {
        this.mReverse = reverse;
    }

    public void play(boolean loop)
    {
        this.mLoop = loop;
        this.mStoped = false;
        mHandler.postDelayed(runnableUi, mDelay);
    }

    public void stop()
    {
        mStoped = true;
        mHandler.post(runnableUi);
    }

    public void gotoAndPlay(int frame, boolean loop)
    {
        this.drawFrame(frame);
        this.play(loop);
    }

    public void gotoAndStop(int frame)
    {
        this.drawFrame(frame);
        this.stop();
    }

    public void nextFrame()
    {
        this.drawFrame(mCurrentFrame + 1);
    }

    public void prevFrame()
    {
        this.drawFrame(mCurrentFrame - 1);
    }

    public String getFolder()
    {
        return mFolder;
    }

    public Drawable getCurrentDrawable()
    {
        if (null == mCaches[mCurrentFrame])
        {
            mCaches[mCurrentFrame] = getDrawableFromFile(mCurrentFrame);
        }
        return mCaches[mCurrentFrame];
    }

    public Drawable[] cacheClips()
    {
        if (null != mFiles)
        {
            for (int i = 0; i < mMaxFrame; i++)
            {
                if (null == mCaches[i])
                {
                    mCaches[i] = getDrawableFromFile(i);
                }
            }
        }
        return mCaches;
    }

    public Drawable[] getCaches()
    {
        return mCaches;
    }

    public Rect getBounds()
    {
        if (null == mRect)
        {
            this.mRect = getCurrentDrawable().getBounds();
        }
        return mRect;
    }

    public void destroy(boolean cache)
    {
        this.stop();
        this.mFolder = "";
        this.mFiles = null;
        if (cache)
        {
            if (null != mCaches)
            {
                for (int i = 0; i < mCaches.length; i++)
                {
                    mCaches[i] = null;
                }
            }
            this.mCaches = null;
        }
        this.destroyDrawingCache();
    }

    public void cancelClipMode()
    {
        mClipMode = false;
        destroy(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        if (mClipMode)
        {
            this.setMeasuredDimension(getBounds().width(), getBounds().height());
        }
        else
        {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (mClipMode)
        {
            getCurrentDrawable().draw(canvas);
        }
    }

    // ------------------------------------------------------------------------------------
    // private functions
    // ------------------------------------------------------------------------------------
    private void drawFrame(int frame)
    {
        this.mCurrentFrame = frame;
        if (mCurrentFrame > mMaxFrame || mCurrentFrame < 0)
        {
            if (mLoop)
            {
                if (mReverse)
                {
                    mCurrentFrame = mMaxFrame;
                }
                else
                {
                    mCurrentFrame = 0;
                }
            }
            else
            {
                if (mReverse)
                {
                    mCurrentFrame = 0;
                }
                else
                {
                    mCurrentFrame = mMaxFrame;
                }
                this.stop();
            }
            mHandler.sendEmptyMessage(WHAT_END);
        }
        else
        {
            mHandler.sendEmptyMessage(WHAT_FRAME);
        }
        if (mNeedDraw)
        {
            this.invalidate();
        }
    }

    private Drawable getDrawableFromFile(int frame)
    {
        if (null == mFiles)
        {
            throw new Error("not init files!");
        }
        if (frame < 0 || frame > mMaxFrame)
        {
            throw new Error("frame out of bounds!");
        }
        Drawable result = null;
        try
        {
            String filepath = mFolder + File.separator + mFiles[frame];
            result = DisplayUtil.getDrawableFromAssets(getContext(), filepath, mOpt);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    // 构建Runnable对象，在runnable中更新界面
    Runnable runnableUi = new Runnable()
    {
        @Override
        public void run()
        {
            if (true == mStoped)
            {
                mHandler.removeCallbacks(runnableUi);
                return;
            }
            if (mReverse)
            {
                prevFrame();
            }
            else
            {
                nextFrame();
            }
            mHandler.postDelayed(runnableUi, mDelay);
        }
    };

}
