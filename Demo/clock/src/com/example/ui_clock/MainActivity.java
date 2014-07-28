package com.example.ui_clock;

import java.io.IOException;

import flash.android.ui.ImageViewClip;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener
{
    private final String CLOCK1 = "clock1";
    private final String CLOCK2 = "clock2";
    private ImageViewClip m_mc;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.m_mc = (ImageViewClip) findViewById(R.id.movieClip1);
        try
        {
            m_mc.initClip(CLOCK2, 20, true, null, null);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        this.m_mc.play(true);
        m_mc.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        this.m_mc.destroy();
        if (CLOCK1 == m_mc.getFolder())
        {
            try
            {
                this.m_mc.stop();
                m_mc.initClip(CLOCK2, 20, true, null, new Handler());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                this.m_mc.stop();
                m_mc.initClip(CLOCK2, 15, true, null, new Handler());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        m_mc.gotoAndPlay(0, true);
    }

}
