package com.hikemobile.menuclip;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import flash.android.ui.MenuWindowClip;

public class MainActivity extends Activity implements Callback
{

	private MenuWindowClip m_MenuClip;
	private Handler m_handler = new Handler(this);
	private TextView m_tvInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.m_tvInfo = (TextView) findViewById(R.id.tv_info);
		this.m_MenuClip = new MenuWindowClip(getApplicationContext());
		m_MenuClip.initClip(this, m_handler, 100, 400);
		this.m_MenuClip.addMenuItem(m_MenuClip.createDefaultItem("menu 0"), 0);
		this.m_MenuClip.addLine(0xFFCCCCCC, 2, 0.5f, 0);
		this.m_MenuClip.addMenuItem(m_MenuClip.createDefaultItem("menu 1"), 0);
		this.m_MenuClip.addLine(0xFFCCCCCC, 2, 0.5f, 0);
		this.m_MenuClip.addMenuItem(m_MenuClip.createDefaultItem("menu 2"), 0);
		this.m_MenuClip.addLine(0xFFCCCCCC, 2, 0.5f, 0);
		this.m_MenuClip.addMenuItem(m_MenuClip.createDefaultItem("menu 3"), 0);
		this.m_MenuClip.addLine(0xFFCCCCCC, 2, 0.5f, 0);
		this.m_MenuClip.addMenuItem(m_MenuClip.createDefaultItem("menu 4"), 0);
		this.m_MenuClip.setMenuBackgroundColor(0xFFFFFFFF);
		this.m_MenuClip.setMenuPadding(0, 0, 0, 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		// TODO Auto-generated method stub
		if (View.VISIBLE == m_MenuClip.getVisibility())
		{
			this.m_MenuClip.closeMenu();
		}
		else
		{
			this.m_tvInfo.setText("");
			this.m_MenuClip.showMenu();
		}
		return false;
	}

	@Override
	public void onBackPressed()
	{
		if (View.VISIBLE == m_MenuClip.getVisibility())
		{
			this.m_MenuClip.closeMenu();
		}
		else
		{
			super.onBackPressed();
		}
	}

	public boolean handleMessage(Message msg)
	{
		this.m_tvInfo.setText("you checked menu: " + msg.obj);
		return false;
	}

}
