package com.hikemobile.singlehand;

import java.io.IOException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import flash.android.display.singlehand.SingleHandActivity;
import flash.android.ui.ImageViewClip;

public class ClockActivity extends SingleHandActivity implements Callback
{
	private final String CLOCK1 = "clock1";
	private final String CLOCK2 = "clock2";
	private ImageViewClip m_mc;
	private Handler m_handler;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clock_layout);
		this.m_handler = new Handler(this);
		m_mc = (ImageViewClip) findViewById(R.id.movieClip1);
		try
		{
			m_mc.initClip(CLOCK2, 20, null, m_handler);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		m_mc.play(true);
		m_mc.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				m_mc.destroy();
				if (CLOCK1 == m_mc.getFolder())
				{
					try
					{
						m_mc.stop();
						m_mc.initClip(CLOCK2, 20, null, m_handler);
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
						m_mc.stop();
						m_mc.initClip(CLOCK1, 15, null, m_handler);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				m_mc.gotoAndPlay(0, true);
			}

		});
	}

	@Override
	public boolean handleMessage(Message msg)
	{
		switch (msg.what)
		{
			case ImageViewClip.WHAT_FRAME:

				break;
			case ImageViewClip.WHAT_END:

				break;
		}
		return true;
	}

}
