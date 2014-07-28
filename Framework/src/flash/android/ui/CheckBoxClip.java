package flash.android.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CheckBoxClip extends CompoundButtonClip
{

    public CheckBoxClip(Context context)
    {
        super(context);
    }

    public CheckBoxClip(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CheckBoxClip(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (isClipMode())
        {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_UP:
                    doCheck(!isChecked());
                    break;
            }
            return true;
        }
        else
        {
            return super.onTouchEvent(event);
        }
    }

}
