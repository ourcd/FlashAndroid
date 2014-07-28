package flash.android.display;

import java.util.Timer;

import android.content.Context;

public final class Stage
{
    private static Timer sTimer = null;
    private static int sStageWidth = 240;
    private static int sStageHeight = 320;
    private static int sCellWH;
    private static boolean sInited = false;
    private static int sRid = 0x7f085000;

    public static void initStage(Context context, int cellWH)
    {
        sCellWH = cellWH;
        sStageWidth = context.getResources().getDisplayMetrics().widthPixels / sCellWH;
        sStageHeight = context.getResources().getDisplayMetrics().heightPixels / sCellWH;
        sInited = true;
    }

    public static int getNewRID()
    {
        sRid++;
        return sRid;
    }

    public static Timer getTimer()
    {
        if (null == sTimer)
        {
            sTimer = new Timer(true);
        }
        return sTimer;
    }

    public static int getStageWidth()
    {
        if (sInited)
        {
            return sStageWidth;
        }
        else
        {
            throw new Error("not initStage");
        }
    }

    public static int getStageHeight()
    {
        if (sInited)
        {
            return sStageHeight;
        }
        else
        {
            throw new Error("not initStage");
        }
    }

    public static int getPixels(int cell)
    {
        return cell * sCellWH;
    }

}
