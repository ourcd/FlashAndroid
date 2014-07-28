package flash.android.display;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public final class DisplayUtil
{

    /**
     * 读取asset目录下图片文件为Drawable
     * 
     * @param context
     * @param imageFileName
     * @return
     * @throws IOException
     */
    public static Drawable getDrawableFromAssets(Context context, String imageFileName, Options opt) throws IOException
    {
        Drawable result = null;
        InputStream is = null;
        AssetManager assetManager = context.getAssets();
        is = assetManager.open(imageFileName);
        if (null == opt)
        {
            opt = new Options();
            opt.inPurgeable = true;
            opt.inInputShareable = true;
        }
        result = new BitmapDrawable(context.getResources(), BitmapFactory.decodeStream(is, null, opt));
        result.setBounds(0, 0, result.getIntrinsicWidth(), result.getIntrinsicHeight());
        is.close();
        is = null;
        return result;
    }

    /**
     * 加载一张图片
     * 
     * @param context
     * @param id
     * @return
     */
    public static Bitmap getBitmapFromRaw(Context context, int id)
    {
        InputStream is;
        Bitmap tmpBatmap;
        is = context.getResources().openRawResource(id);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        tmpBatmap = BitmapFactory.decodeStream(is, null, opts);
        is = null;
        return tmpBatmap;
    }

    public static Bitmap getBitmapFromDrawable(Context context, int id, Options opts)
    {
        if (null == opts)
        {
            opts = new Options();
            opts.inPurgeable = true;
            opts.inInputShareable = true;
        }
        return BitmapFactory.decodeResource(context.getResources(), id, opts);
    }

    /**
     * 获取Assets目录指定文件夹图片Bitmap[]
     * 
     * @param context
     * @param folder
     * @return
     * @throws IOException
     */
    public static Bitmap[] getBitmapsFromAssets(Context context, String folder) throws IOException
    {
        InputStream is;
        String paths[] = context.getResources().getAssets().list(folder);
        Bitmap[] result = new Bitmap[paths.length];
        for (int i = 0; i < paths.length; i++)
        {
            is = context.getResources().getAssets().open(folder + "/" + paths[i]);
            result[i] = BitmapFactory.decodeStream(is);
        }
        is = null;
        return result;
    }

    /**
     * 根据行列裁切图片
     * 
     * @param bmp
     * @param pieceWidth
     * @param pieceHeight
     * @param col
     * @param row
     * @param canvas
     * @param paint
     */
    public static final void drawFrame(Bitmap bmp, int pieceWidth, int pieceHeight, int col, int row, Canvas canvas, Paint paint)
    {
        canvas.save();
        canvas.clipRect(0, 0, pieceWidth, pieceHeight);
        canvas.drawBitmap(bmp, -pieceWidth * col, -pieceHeight * row, paint);
        canvas.restore();
    }

    /**
     * 根据行列裁切图片
     * 
     * @param bmp
     * @param pieceWidth
     * @param pieceHeight
     * @param col
     * @param row
     * @param canvas
     */
    public static final void drawFrame(Bitmap bmp, int pieceWidth, int pieceHeight, int col, int row, Canvas canvas)
    {
        drawFrame(bmp, pieceWidth, pieceHeight, col, row, canvas, new Paint());
    }

    /**
     * 根据帧数裁切图片
     * 
     * @param bmp
     * @param frame
     * @param pieceWidth
     * @param pieceHeight
     * @param canvas
     */
    public static final void drawFrame(Bitmap bmp, int frame, int pieceWidth, int pieceHeight, Canvas canvas)
    {
        int rows = (int) Math.floor(bmp.getWidth() / pieceWidth);
        canvas.save();
        canvas.clipRect(0, 0, pieceWidth, pieceHeight);
        canvas.drawBitmap(bmp, -pieceWidth * (frame % rows), (float) (-pieceHeight * Math.floor(frame / rows)), new Paint());
        canvas.restore();
    }

}
