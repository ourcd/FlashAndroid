package com.hikemobile.switchcip;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import flash.android.ui.SwitchClip;

public class MainActivity extends Activity implements Callback
{
    private SwitchClip m_switchclip;
    private TextView m_tvState;
    private boolean mInited = false;
    private Button mBtn;
    private Handler mHandler = new Handler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.m_tvState = (TextView) findViewById(R.id.tv_state);
        this.mBtn = (Button) findViewById(R.id.button1);
        this.m_switchclip = (SwitchClip) findViewById(R.id.switchClip1);

        mBtn.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (false == mInited)
                {
                    m_switchclip.initClip("switchclip", getFiles(12), 25, true, null, mHandler);
                    m_tvState.setText(String.valueOf(m_switchclip.isChecked()));
                    mInited = true;
                }
            }
        });
    }

    public boolean handleMessage(Message msg)
    {
        switch (msg.what)
        {
            case SwitchClip.WHAT_CHECKED:
                m_tvState.setText(String.valueOf(msg.obj));
                break;
        }
        return true;
    }

    private String[] getFiles(int num)
    {
        String[] result = new String[num];
        for (int i = 1; i <= num; i++)
        {
            if (i < 10)
            {
                result[i - 1] = "switch000" + i + ".png";
            }
            else
            {
                result[i - 1] = "switch00" + i + ".png";
            }
        }
        return result;
    }
}
