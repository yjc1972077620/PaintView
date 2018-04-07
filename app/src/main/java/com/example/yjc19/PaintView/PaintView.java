package com.example.yjc19.PaintView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yjc on 2018/3/18.
 */

public class PaintView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder surfaceHolder=null;
    //当前所选画笔的形状
    private BaseAction curAction=null;
    //默认画笔为黑色
    private  int curColor= Color.BLACK;
    //画笔的粗细
    private  int curSize=5;
    private Paint paint;
    private List<BaseAction> actions;
    private Bitmap bitmap;
    private ActionType actionType=ActionType.Path;

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化
     */
    private void init()
    {
        surfaceHolder=this.getHolder();
        surfaceHolder.addCallback(this);
        this.setFocusable(true);
        paint=new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(curSize);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Canvas canvas = surfaceHolder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        surfaceHolder.unlockCanvasAndPost(canvas);
        actions = new ArrayList<>();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action=event.getAction();
        if(action==MotionEvent.ACTION_CANCEL)
        {
            return  false;
        }
        float touchX=event.getX();
        float touchY=event.getY();

        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                setCurAction(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                Canvas canvas=surfaceHolder.lockCanvas();
                canvas.drawColor(Color.WHITE);
                for(BaseAction baseAction:actions)
                {
                    baseAction.draw(canvas);
                }
                curAction.move(touchX,touchY);
                curAction.draw(canvas);
                surfaceHolder.unlockCanvasAndPost(canvas);
                break;
            case MotionEvent.ACTION_UP:
                actions.add(curAction);
                curAction=null;
                break;
        }
        return  true;
    }


    /**
     * 得到当前画笔类型并实例化
     * @param x
     * @param y
     */
    public void setCurAction(float x,float y)
    {
        switch (actionType)
        {
            case Point:
                curAction=new Mypoint(x,y,curColor);
                break;
            case Path:
                curAction = new MyPath(x, y, curSize, curColor);
                break;
            case Line:
                curAction = new MyLine(x, y, curSize, curColor);
                break;
            case Rect:
                curAction = new MyRect(x, y, curSize, curColor);
                break;
            case Circle:
                curAction = new MyCircle(x, y,curSize, curColor);
                break;
            case FillEcRect:
                curAction = new MyFillRect(x, y,curSize, curColor);
                break;
            case FilledCircle:
                curAction = new MyFillCircle(x, y,curSize, curColor);
                break;
            default:
                break;
        }

    }

    /**
     * 设置画笔颜色
     * @param color
     */
    public void setColor(String color)
    {
        this.curColor=Color.parseColor(color);
    }

    /**
     * 设置画笔大小
     * @param size
     */
    public void setSize(int size)
    {
        this.curSize=size;
    }

    /**
     * 设置画笔类型
     * @param type
     */
    public void setType(ActionType type)
    {
        this.actionType=type;
    }

    /**
     * 将当前canvas画布转化成一个bitmap
     * @return
     */
    public Bitmap getBitmap()
    {
        bitmap=Bitmap.createBitmap(getWidth(),getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        doDraw(canvas);
        return bitmap;
    }

    /**
     * 图片的保存路径
     * @param paintView
     * @return
     */
    public String saveBitmap(PaintView paintView,Context context)
    {

        //bitmap存储路径
        String path= Environment.getExternalStorageDirectory().getAbsolutePath()
                +"/PaintView/"+System.currentTimeMillis()+".png";
        if(new File(path).exists())
        {
            new File(path).getParentFile().mkdir();
        }
        savePic(paintView.getBitmap(),path,context);
        return  path;
    }

    /**
     * 保存图片
     * @param bitmap
     * @param path
     */
    public static  void savePic(Bitmap bitmap,String path,Context context)
    {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream=new FileOutputStream(path);
            if(fileOutputStream!=null)
            {
                bitmap.compress(Bitmap.CompressFormat.PNG,90,fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileName=System.currentTimeMillis()+".png";
        try {

            MediaStore.Images.Media.insertImage(context.getContentResolver(),path,fileName,null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));

    }

    /**
     * 在画布上绘制
     * @param canvas
     */
    public void doDraw(Canvas canvas)
    {
        canvas.drawColor(Color.TRANSPARENT);
        for(BaseAction action:actions)
        {
            action.draw(canvas);
        }
        canvas.drawBitmap(bitmap,0,0,paint);
    }


    /**
     * 回退函数
     * @return
     */
    public boolean back()
    {
        if(actions!=null&&actions.size()>0)
        {
            actions.remove(actions.size()-1);
            Canvas canvas=surfaceHolder.lockCanvas();
            canvas.drawColor(Color.WHITE);
            for(BaseAction action:actions)
            {
               action.draw(canvas);
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
            return true;
        }
        return  false;
    }

    /**
     * 重置画布
     */
    public void redraw()
    {
        if(actions!=null&&actions.size()>0)
        {
            actions.clear();
            Canvas canvas=surfaceHolder.lockCanvas();
            canvas.drawColor(Color.WHITE);
            for(BaseAction action:actions)
            {
               action.draw(canvas);
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }



    /**
     * 包含所有画笔类型的枚举类
     */
    enum ActionType {
        Point, Path, Line, Rect, Circle, FillEcRect, FilledCircle
    }
}
