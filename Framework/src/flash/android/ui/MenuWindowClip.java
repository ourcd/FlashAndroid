package flash.android.ui;

import java.util.ArrayList;
import java.util.List;

import flash.android.display.Stage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

/**
 * 
 * @author Alex Wei
 * 
 *         <pre>
 * {
 *     &#064;code
 *     public class MainActivity extends Activity implements Callback
 *     {
 * 
 *         private MenuWindowClip m_MenuClip;
 *         private Handler m_handler = new Handler(this);
 *         private TextView m_tvInfo;
 * 
 *         protected void onCreate(Bundle savedInstanceState)
 *         {
 *             super.onCreate(savedInstanceState);
 *             setContentView(R.layout.activity_main);
 *             this.m_tvInfo = (TextView) findViewById(R.id.tv_info);
 *             this.m_MenuClip = new MenuWindowClip(getApplicationContext());
 *             m_MenuClip.initClip(this, m_handler, 50, 500);
 *             this.m_MenuClip.addMenuItem(m_MenuClip.createDefaultItem(&quot;menu 0&quot;), 0);
 *             this.m_MenuClip.addLine(0xFFCCCCCC, 2, 0.5f, 0);
 *             this.m_MenuClip.addMenuItem(m_MenuClip.createDefaultItem(&quot;menu 1&quot;), 0);
 *             this.m_MenuClip.addLine(0xFFCCCCCC, 2, 0.5f, 0);
 *             this.m_MenuClip.addMenuItem(m_MenuClip.createDefaultItem(&quot;menu 2&quot;), 0);
 *             this.m_MenuClip.addLine(0xFFCCCCCC, 2, 0.5f, 0);
 *             this.m_MenuClip.addMenuItem(m_MenuClip.createDefaultItem(&quot;menu 3&quot;), 0);
 *             this.m_MenuClip.addLine(0xFFCCCCCC, 2, 0.5f, 0);
 *             this.m_MenuClip.addMenuItem(m_MenuClip.createDefaultItem(&quot;menu 4&quot;), 0);
 *             this.m_MenuClip.setMenuBackgroundColor(0xFFFFFFFF);
 *             this.m_MenuClip.setMenuPadding(0, 0, 0, 0);
 *         }
 * 
 *         public boolean onCreateOptionsMenu(Menu menu)
 *         {
 *             // Inflate the menu; this adds items to the action bar if it is
 *             // present.
 *             // getMenuInflater().inflate(R.menu.main, menu);
 *             return true;
 *         }
 * 
 *         public boolean onPrepareOptionsMenu(Menu menu)
 *         {
 *             if (View.VISIBLE == m_MenuClip.getVisibility())
 *             {
 *                 this.m_MenuClip.closeMenu();
 *             }
 *             else
 *             {
 *                 this.m_MenuClip.showMenu();
 *             }
 *             return false;
 *         }
 * 
 *         public void onBackPressed()
 *         {
 *             if (View.VISIBLE == m_MenuClip.getVisibility())
 *             {
 *                 this.m_MenuClip.closeMenu();
 *             }
 *             else
 *             {
 *                 super.onBackPressed();
 *             }
 *         }
 * 
 *         public boolean handleMessage(Message msg)
 *         {
 *             this.m_tvInfo.setText(&quot;you checked menu: &quot; + msg.obj);
 *             return false;
 *         }
 * 
 *     }
 * }
 * </pre>
 */
public class MenuWindowClip extends RelativeLayout implements TweenCallback, OnClickListener
{
    public static final int WHAT_CLICKED = 1;
    private ImageView mBackground;
    private LinearLayout mMenu;
    private List<View> mList = new ArrayList<View>();
    private TweenManager mTweenManager = new TweenManager();
    private Handler mHandler;
    private boolean mInited = false;
    private List<Point> mItemsPos = new ArrayList<Point>();
    private float mMenuY;
    private boolean mClosed = true;
    private int mItemDuration;
    private int mMenuDuration;
    private int mWindowDuration;
    private int mTotalDuration;
    private int mMinDuration;
    private boolean mPlaying = false;
    private int mItemIndex = 0;
    private boolean mIsTouchDown = false;
    private SparseIntArray mIdIndex = new SparseIntArray();

    public MenuWindowClip(Context context)
    {
        super(context);
    }

    public MenuWindowClip(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public MenuWindowClip(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public void initClip(Activity activity, Handler handler, int itemDuration, int minDuration)
    {
        this.mHandler = handler;
        this.mItemDuration = itemDuration;
        this.mMinDuration = minDuration;
        // background
        this.mBackground = new ImageView(getContext());
        this.mBackground.setBackgroundColor(0xFF000000);
        this.addView(mBackground, new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        // menu
        this.mMenu = new LinearLayout(getContext());
        mMenu.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
        this.addView(mMenu, lp);
        // add this to activity
        activity.addContentView(this, new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        // event
        this.mBackground.setOnClickListener(this);
        // init tween
        if (null == mHandler)
        {
            this.mHandler = new Handler();
        }
        Tween.registerAccessor(View.class, new MenuWindowAccessor());
    }

    public void setMenuBackgroundColor(int color)
    {
        this.mMenu.setBackgroundColor(color);
    }

    public void setMenuBackgroundDrawable(Drawable drawable)
    {
        this.mMenu.setBackground(drawable);
    }

    public void setMenuPadding(int left, int top, int right, int bottom)
    {
        this.mMenu.setPadding(left, top, right, bottom);
    }

    public void addMenuItem(View item, int bottomMargin)
    {
        if (NO_ID == item.getId())
        {
            item.setId(Stage.getNewRID() + mItemIndex);
        }
        this.mIdIndex.put(item.getId(), mItemIndex);
        mItemIndex++;
        // add item
        this.mList.add(item);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_VERTICAL;
        lp.bottomMargin = bottomMargin;
        this.mMenu.addView(item, lp);
        // event
        item.setOnClickListener(this);
        // update init pos
        this.mItemsPos.add(new Point());
        this.mInited = false;
    }

    public void addLine(int color, int height, float alpha, int bottomMargin)
    {
        View line = new View(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height);
        lp.gravity = Gravity.CENTER_VERTICAL;
        lp.bottomMargin = bottomMargin;
        this.mMenu.addView(line, lp);
        line.setBackgroundColor(color);
        line.setAlpha(alpha);
        this.mList.add(line);
        // update init pos
        this.mItemsPos.add(new Point());
        this.mInited = false;
    }

    public void showMenu()
    {
        if (mPlaying)
        {
            return;
        }
        if (false == mInited)
        {
            this.mInited = true;
            this.mTotalDuration = mItemDuration * mList.size() + mMinDuration;
            this.mMenuDuration = this.mWindowDuration = mMinDuration;
        }
        if (mClosed)
        {
            this.mClosed = false;
            this.mPlaying = true;
            this.setVisibility(View.VISIBLE);
            // window bg
            this.mBackground.setAlpha(0.0f);
            Tween.to(mBackground, MenuWindowAccessor.WINDOW_BG, mWindowDuration).target(0.7f).start(mTweenManager);
            // menu bg
            this.mMenu.setY(getResources().getDisplayMetrics().heightPixels);
            this.mMenu.setAlpha(0.0f);
            Tween.to(mMenu, MenuWindowAccessor.MENU_BG, mMenuDuration).target(mMenuY, 1.0f).start(mTweenManager);
            // menu item
            View temp;
            for (int i = 0; i < mList.size(); i++)
            {
                // init
                temp = this.mList.get(i);
                temp.setAlpha(0);
                temp.setY(getResources().getDisplayMetrics().heightPixels+mItemsPos.get(i).y);
                // anim
                Tween.to(temp, MenuWindowAccessor.MENU_ITEM, mMinDuration + i * mItemDuration).target(mItemsPos.get(i).y, 1).start(mTweenManager);
            }
            Tween.call(this).delay(mTotalDuration).setCallbackTriggers(TweenCallback.START).start(mTweenManager);
            this.mHandler.post(mRunnableUi);
        }
    }

    public void closeMenu()
    {
        if (mInited)
        {
            if (mPlaying)
            {
                return;
            }
            this.mTweenManager.killAll();
            this.mHandler.removeCallbacks(mRunnableUi);
            this.mPlaying = true;
            // anim----------------------------------------
            int endY = getResources().getDisplayMetrics().heightPixels;
            // window bg
            Tween.to(mBackground, MenuWindowAccessor.WINDOW_BG, mWindowDuration).target(0.0f).start(mTweenManager);
            // menu bg
            Tween.to(mMenu, MenuWindowAccessor.MENU_BG, mMenuDuration).target(endY, 0.0f).start(mTweenManager);
            // menu item
            View temp;
            for (int i = mList.size() - 1; i >= 0; i--)
            {
                temp = this.mList.get(i);
                Tween.to(temp, MenuWindowAccessor.MENU_ITEM, mTotalDuration - i * mItemDuration).target(endY, 0).start(mTweenManager);
            }
            Tween.call(this).delay(mWindowDuration).setCallbackTriggers(TweenCallback.COMPLETE).start(mTweenManager);
            this.mHandler.post(mRunnableUi);
        }
        else
        {
            this.setVisibility(View.GONE);
        }

    }

    public View createDefaultItem(String text)
    {
        Button btn = new Button(getContext());
        btn.setBackgroundResource(android.R.drawable.menuitem_background);
        btn.setText(text);
        btn.setTextColor(0xFF000000);
        btn.setPadding(50, 0, 0, 0);
        btn.setGravity(Gravity.CENTER_VERTICAL);
        return btn;
    }

    // 构建Runnable对象，在runnable中更新界面
    Runnable mRunnableUi = new Runnable()
    {
        @Override
        public void run()
        {
            mTweenManager.update(20);
            mHandler.postDelayed(mRunnableUi, 20);
        }
    };

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);
        if (false == mInited)
        {
            for (int i = 0; i < mItemsPos.size(); i++)
            {
                mItemsPos.get(i).x = (int) mList.get(i).getX();
                mItemsPos.get(i).y = (int) mList.get(i).getY();
            }
            this.mMenuY = this.mMenu.getY();
        }
    }

    @Override
    public void onEvent(int type, BaseTween<?> source)
    {
        this.mTweenManager.killAll();
        this.mHandler.removeCallbacks(mRunnableUi);
        this.mPlaying = false;
        if (TweenCallback.COMPLETE == type)
        {
            this.mClosed = true;
            this.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                this.mIsTouchDown = true;
                break;
            case MotionEvent.ACTION_UP:
                if (mIsTouchDown)
                {
                    this.closeMenu();
                }
                this.mIsTouchDown = false;
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() != mBackground.getId())
        {
            Message msg = new Message();
            msg.what = WHAT_CLICKED;
            msg.obj = mIdIndex.get(v.getId());
            this.mHandler.sendMessage(msg);
        }
        this.closeMenu();
    }

}

class MenuWindowAccessor implements TweenAccessor<View>
{
    public static final int MENU_ITEM = 1;
    public static final int MENU_BG = 2;
    public static final int WINDOW_BG = 3;

    @Override
    public int getValues(View target, int tweenType, float[] returnValues)
    {
        switch (tweenType)
        {
            case MENU_ITEM:
            case MENU_BG:
                returnValues[0] = target.getY();
                returnValues[1] = target.getAlpha();
                return 2;
            case WINDOW_BG:
                returnValues[0] = target.getAlpha();
                return 1;
        }
        return 0;
    }

    @Override
    public void setValues(View target, int tweenType, float[] newValues)
    {
        switch (tweenType)
        {
            case MENU_ITEM:
            case MENU_BG:
                target.setY(newValues[0]);
                target.setAlpha(newValues[1]);
                break;
            case WINDOW_BG:
                target.setAlpha(newValues[0]);
                break;
        }
    }

}
