package flash.android.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 
 * @author Alex
 * @see ImageViewClip
 */
public class SwitchClip extends CompoundButtonClip
{
    private float mStartX;
    private int mTouchFrame;
    private int mMoveOffset;
    private int mClickOffset;
    private float mOffset;
    private boolean mMoved = false;

    public SwitchClip(Context context)
    {
        super(context);
    }

    public SwitchClip(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public SwitchClip(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public boolean onTouchEvent(MotionEvent ev)
    {
        if (false == isClipMode() || false == isEnabled())
        {
            return false;
        }
        if (isStoped())
        {
            switch (ev.getActionMasked())
            {
                case MotionEvent.ACTION_DOWN:
                    this.mStartX = ev.getX();
                    this.mMoved = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (false == mMoved)
                    {
                        if (getBounds().contains((int) ev.getX(), (int) ev.getY()))
                        {
                            this.mTouchFrame = (int) Math.floor(Math.abs(ev.getX() - mStartX) / this.getBounds().width() * getTotalFrames());
                            if (isChecked())
                            {
                                this.gotoAndStop(getMaxFrame() - mTouchFrame);
                            }
                            else
                            {
                                this.gotoAndStop(mTouchFrame);
                            }
                        }
                        else
                        {
                            this.doMove(ev.getX());
                            this.mMoved = true;
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_OUTSIDE:
                case MotionEvent.ACTION_CANCEL:
                    if (false == mMoved)
                    {
                        this.doMove(ev.getX());
                    }
                    break;
            }
        }
        return true; // 否则其它值ACTION_MOVE,ACTION_UP无效
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mMoveOffset = getMeasuredWidth() / 5;
        this.mClickOffset = mMoveOffset / 2;
    }

    private void doMove(float x)
    {
        mOffset = Math.abs(x - mStartX);
        if (mOffset < mClickOffset || mOffset > mMoveOffset)
        {
            this.doCheck(!isChecked());
        }
        else
        {
            this.doCheck(isChecked());
        }
    }

}
