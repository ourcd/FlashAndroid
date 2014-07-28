package flash.android.demo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerClip extends ViewPager
{
    private List<View> mViews = new ArrayList<View>();
    private View mLeft;
    private View mMiddle;
    private View mRight;
    private int mLastOffsetPixels = 0;

    public ViewPagerClip(Context context)
    {
        super(context);
    }

    public ViewPagerClip(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
        System.out.println("position:" + position + ",positionOffset:" + positionOffset + ",positionOffsetPixels:" + positionOffsetPixels + ",width:" + getWidth());
        this.mMiddle = mViews.get(position);
        if (position > 0)
        {
            mLeft = mViews.get(position - 1);
        }
        if (position + 1 < mViews.size())
        {
            mRight = mViews.get(position + 1);
        }
        if (mLastOffsetPixels > positionOffsetPixels)
        {
            this.doAnimation(positionOffset, true);
        }
        else
        {
            this.doAnimation(positionOffset, false);
        }
        this.mLastOffsetPixels = positionOffsetPixels;
        if (0 == positionOffsetPixels)
        {
            this.endAnimation();
        }
        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    public void setViews(List<View> views)
    {
        this.mViews = views;
    }

    public View getView(int position)
    {
        return mViews.get(position);
    }

    private ArrayList<View> getAllChildren(View v)
    {
        if (!(v instanceof ViewGroup))
        {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }
        ArrayList<View> result = new ArrayList<View>();
        ViewGroup viewGroup = (ViewGroup) v;
        for (int i = 0; i < viewGroup.getChildCount(); i++)
        {
            View child = viewGroup.getChildAt(i);
            result.add(child);
        }
        return result;
    }

    private void doAnimation(float offset, boolean bRight)
    {
        float per;
        this.doViewAnimation(mLeft, bRight);

        if (null != mLeft)
        {
            ArrayList<View> lefts = getAllChildren(mLeft);
            for (int l = lefts.size() - 1; l >= 0; l--)
            {
                // TODO:
                per = 1 - (float) l / (lefts.size() - 1);
                // lefts.get(l).setTranslationX(offset * per);
                if (bRight)
                {
                    lefts.get(l).setAlpha(per * (1 - offset));
                }
                else
                {
                    lefts.get(l).setAlpha(per * offset);
                }
            }
        }
        if (null != mRight)
        {
            ArrayList<View> rights = getAllChildren(mRight);
            for (int r = rights.size() - 1; r >= 0; r--)
            {
                // TODO:
                per = 1 - (float) r / (rights.size() - 1);
                // rights.get(r).setTranslationX(getWidth()+offset);
                if (bRight)
                {
                    rights.get(r).setAlpha(per);
                }
                else
                {
                    rights.get(r).setAlpha(per * (1 - offset));
                }
            }
        }
    }

    private void doViewAnimation(View view, boolean bRight)
    {
        ArrayList<View> views = getAllChildren(view);
        for (int v = 0; v < views.size(); v++)
        {

        }
    }

    private void endAnimation()
    {
        for (int v = mViews.size() - 1; v >= 0; v--)
        {
            ArrayList<View> views = getAllChildren(mViews.get(v));
            for (int i = views.size() - 1; i >= 0; i--)
            {
                views.get(i).setTranslationX(0);
                views.get(i).setAlpha(1);
            }
        }
    }

}
