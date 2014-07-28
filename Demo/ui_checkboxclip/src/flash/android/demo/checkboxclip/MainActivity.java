package flash.android.demo.checkboxclip;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.widget.TextView;
import flash.android.ui.CheckBoxClip;

public class MainActivity extends Activity implements Callback
{
    private CheckBoxClip mCBC;
    private TextView mInfo;
    private Handler mHandler=new Handler(this);
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInfo=(TextView) findViewById(R.id.tv_info);
        mCBC=(CheckBoxClip) findViewById(R.id.checkbox1);
        mInfo.setText(String.valueOf(mCBC.isChecked()));
        try
        {
            mCBC.initClip("checkboxclip", 100, true, null, mHandler);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public boolean handleMessage(Message msg)
    {
        mInfo.setText(String.valueOf(mCBC.isChecked()));
        return true;
    }

    

}
