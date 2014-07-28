package flash.android.ui;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;

/**
 * 
 * @author Alex(515899670@qq.com)
 * 
 */
public class CompoundButtonClip extends ImageViewClip
{
    public final static int WHAT_CHECKED = 11;
    private boolean mChecked = false;

    public CompoundButtonClip(Context context)
    {
        super(context);
    }

    public CompoundButtonClip(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CompoundButtonClip(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public void setChecked(boolean checked)
    {
        if (checked != isChecked())
        {
            mChecked = checked;
            if (mChecked)
            {
                this.gotoAndStop(getMaxFrame());
            }
            else
            {
                this.gotoAndStop(0);
            }
        }
    }

    public boolean isChecked()
    {
        return mChecked;
    }

    protected void doCheck(boolean checked)
    {
        if (isStoped())
        {
            if (checked != mChecked)
            {
                this.setReverse(mChecked);
                mChecked = checked;
                this.play(false);
                // handle msg
                if (null != mHandler)
                {
                    Message msg = new Message();
                    msg.what = WHAT_CHECKED;
                    msg.obj = checked;
                    mHandler.sendMessage(msg);
                }
            }
        }
    }

}
