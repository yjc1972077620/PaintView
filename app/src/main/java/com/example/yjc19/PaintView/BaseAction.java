package com.example.yjc19.PaintView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by yjc19 on 2018/3/18.
 */

abstract class BaseAction {
    public int color;
    BaseAction()
    {
        color= Color.WHITE;
    }
    BaseAction(int color)
    {
        this.color=color;
    }
    //声明一个绘制笔画的函数，接受一个Canvas对象
    public abstract void draw(Canvas canvas);
    //声明一个检测手指滑动的函数
    public abstract void move(float mx,float my);

}

class Mypoint extends BaseAction
{
    private float x;
    private float y;

    Mypoint(float px,float py, int color)
    {
        super(color);
        this.x=px;
        this.y=py;
    }
    @Override
    public void draw(Canvas canvas) {
        Paint paint=new Paint();
        paint.setColor(color);
        canvas.drawPoint(x,y,paint);
    }

    @Override
    public void move(float mx, float my) {

    }
}

//曲线
class  MyPath extends BaseAction
{

    //手指滑动的路径
    private Path path;
    //画笔的粗细
    private  int size;

    MyPath()
    {
        path=new Path();
        size=1;
    }
    //构造函数用于绘制
    MyPath(float x, float y, int size, int color)
    {
        super(color);
        path=new Path();
        this.size=size;
        path.moveTo(x,y);
        path.lineTo(x,y);
    }
    @Override
    public void draw(Canvas canvas) {
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        //设置画笔颜色
        paint.setColor(color);
        //设置及画笔粗细
        paint.setStrokeWidth(size);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path,paint);
    }

    @Override
    public void move(float mx, float my) {
        path.lineTo(mx,my);
    }
}

//直线
class MyLine extends BaseAction
{
    private float startX;
    private float startY;
    private float stopX;
    private float stopY;
    private  int size;

    MyLine()
    {
        startX=0;
        startY=0;
        stopX=0;
        stopY=0;
    }
    MyLine(float x,float y,int size,int color)
    {
        super(color);
        startX=x;
        startY=y;
        stopX=x;
        stopY=y;
        this.size=size;
    }
    @Override
    public void draw(Canvas canvas) {
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(size);
        canvas.drawLine(startX,startY,stopX,stopY,paint);
    }

    @Override
    public void move(float mx, float my) {
        stopX=mx;
        stopY=my;
    }
}
//矩形
class MyRect extends BaseAction
{
    private float startX;
    private float startY;
    private float stopX;
    private float stopY;
    private  int size;

    MyRect()
    {
        startX=0;
        startY=0;
        stopX=0;
        stopY=0;
    }
    MyRect(float x,float y,int size,int color)
    {
        super(color);
        startX=x;
        startY=y;
        stopX=x;
        stopY=y;
        this.size=size;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(size);
        canvas.drawLine(startX,startY,stopX,stopY,paint);
    }

    @Override
    public void move(float mx, float my) {
        stopX=mx;
        stopY=my;
    }
}

/**
 * 圆框
 */
class MyCircle extends BaseAction {
    private float startX;
    private float startY;
    private float stopX;
    private float stopY;
    private float radius;
    private int size;

    MyCircle() {
        startX = 0;
        startY = 0;
        stopX = 0;
        stopY = 0;
        radius = 0;
    }

    MyCircle(float x, float y, int size, int color) {
        super(color);
        this.startX = x;
        this.startY = y;
        this.stopX = x;
        this.stopY = y;
        this.radius = 0;
        this.size = size;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(size);
        canvas.drawCircle((startX + stopX) / 2, (startY + stopY) / 2, radius, paint);
    }

    @Override
    public void move(float mx, float my) {
        stopX = mx;
        stopY = my;
        radius = (float) ((Math.sqrt((mx - startX) * (mx - startX)
                + (my - startY) * (my - startY))) / 2);
    }
}

//实心矩形
class MyFillRect extends BaseAction {
    private float startX;
    private float startY;
    private float stopX;
    private float stopY;
    private int size;

    MyFillRect() {
        this.startX = 0;
        this.startY = 0;
        this.stopX = 0;
        this.stopY = 0;
    }

    MyFillRect(float x, float y, int size, int color) {
        super(color);
        this.startX = x;
        this.startY = y;
        this.stopX = x;
        this.stopY = y;
        this.size = size;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        paint.setStrokeWidth(size);
        canvas.drawRect(startX, startY, stopX, stopY, paint);
    }

    @Override
    public void move(float mx, float my) {
        stopX = mx;
        stopY = my;
    }
}

/**
 * 实心圆
 */
class MyFillCircle extends BaseAction {
    private float startX;
    private float startY;
    private float stopX;
    private float stopY;
    private float radius;
    private int size;


    public MyFillCircle(float x, float y, int size, int color) {
        super(color);
        this.startX = x;
        this.startY = y;
        this.stopX = x;
        this.stopY = y;
        this.radius = 0;
        this.size = size;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        paint.setStrokeWidth(size);
        canvas.drawCircle((startX + stopX) / 2, (startY + stopY) / 2, radius, paint);
    }

    @Override
    public void move(float mx, float my) {
        stopX = mx;
        stopY = my;
        radius = (float) ((Math.sqrt((mx - startX) * (mx - startX)
                + (my - startY) * (my - startY))) / 2);
    }
}






