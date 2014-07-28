package flash.android.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.nineoldandroids.view.ViewHelper;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerClip extends ViewPager
{
    private HashMap<Integer, Object> mObjs = new LinkedHashMap<Integer, Object>();
    private View mLeft;
    private View mRight;
    private State mState;
    private int oldPage;

    private enum State
    {
        IDLE, GOING_LEFT, GOING_RIGHT
    }

    public ViewPagerClip(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public ViewPagerClip(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0)
    {
        // TODO Auto-generated method stub
        return super.onTouchEvent(arg0);
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
        if (mState == State.IDLE && positionOffset > 0)
        {
            oldPage = getCurrentItem();
            mState = position == oldPage ? State.GOING_RIGHT : State.GOING_LEFT;
        }
        boolean goingRight = position == oldPage;
        if (mState == State.GOING_RIGHT && !goingRight)
        {
            mState = State.GOING_LEFT;
        }
        else if (mState == State.GOING_LEFT && goingRight)
        {
            mState = State.GOING_RIGHT;
        }
        float effectOffset = isSmall(positionOffset) ? 0 : positionOffset;
        mLeft = findViewFromObject(position);
        mRight = findViewFromObject(position + 1);
        this.doAnimation(mLeft, mRight, effectOffset);
    }

    public void setObjectForPosition(Object obj, int position)
    {
        mObjs.put(Integer.valueOf(position), obj);
    }

    private View findViewFromObject(int position)
    {
        Object o = mObjs.get(Integer.valueOf(position));
        if (o == null)
        {
            return null;
        }
        PagerAdapter a = getAdapter();
        View v;
        for (int i = 0; i < getChildCount(); i++)
        {
            v = getChildAt(i);
            if (a.isViewFromObject(v, o))
            {
                return v;
            }
        }
        return null;
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
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));
            result.addAll(viewArrayList);
        }
        return result;
    }

    private boolean isSmall(float positionOffset)
    {
        return Math.abs(positionOffset) < 0.0001;
    }

    private void doAnimation(View left, View right, float positionOffset)
    {

        if (left != null) {
            ViewHelper.setAlpha(left, 1-positionOffset);
        }
        if (right != null) {
            ViewHelper.setAlpha(right, positionOffset);
        }
       /* switch (mState)
        {
            case GOING_LEFT:
                
                break;
            case GOING_RIGHT:
                
                break;
            case IDLE:
                
                break;
        }*/

    }

}
