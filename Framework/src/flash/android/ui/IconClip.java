package flash.android.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

public class IconClip extends ImageViewClip
{
    private Drawable mDrawable = null;

    public IconClip(Context p_context)
    {
        super(p_context);
    }

    public IconClip(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public IconClip(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public void setIcon(Drawable drawable)
    {
        this.mDrawable = drawable;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        mDrawable.draw(canvas);
    }

}
