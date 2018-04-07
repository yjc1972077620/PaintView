package com.example.yjc19.PaintView;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.yjc19.myapplication.R;

public class PaintViewActivity extends AppCompatActivity {

    private PaintView paintView;
    private AlertDialog ColorDialog;
    private AlertDialog PaintDialog;
    private AlertDialog ShapDialog;
    private static final String TGA="PaintViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paintview);
        paintView=(PaintView)findViewById(R.id.paintView);
        paintView.setSize(dip2px(5));

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return paintView.onTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.main_color:
                showColorDialog();
                break;
            case R.id.main_size:
                showSizeDialog();
                break;
            case R.id.main_action:
                showShapeDialog();
                break;
            case R.id.main_reset:
                paintView.redraw();
            case R.id.main_back:
                paintView.back();
                break;
            case R.id.main_save:
                String path = paintView.saveBitmap(paintView,this);
                paintView.savePic(paintView.getBitmap(),path,this);
                Log.d(TGA, "onOptionsItemSelected: " + path);
                Toast.makeText(this, "保存图片的路径为：" + path,  Toast.LENGTH_SHORT).show();
                break;
        }
        return true;

    }

    /**
     * 显示选择画笔颜色的对话框
     */
    private void showColorDialog() {
        if(ColorDialog == null){
            ColorDialog = new AlertDialog.Builder(this)
                    .setTitle("选择颜色")
                    .setSingleChoiceItems(new String[]{"蓝色", "红色", "黑色"}, 0,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case 0:
                                            paintView.setColor("#0000ff");
                                            break;
                                        case 1:
                                            paintView.setColor("#ff0000");
                                            break;
                                        case 2:
                                            paintView.setColor("#272822");
                                            break;
                                        default:break;
                                    }
                                    dialog.dismiss();
                                }
                            }).create();
        }
        ColorDialog.show();
    }

    /**
     * 显示选择画笔粗细的对话框
     */
    private void showSizeDialog(){
        if(PaintDialog == null){
            PaintDialog = new AlertDialog.Builder(this)
                    .setTitle("选择画笔粗细")
                    .setSingleChoiceItems(new String[]{"细", "中", "粗"}, 0,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case 0:
                                            paintView.setSize(dip2px(5));
                                            break;
                                        case 1:
                                            paintView.setSize(dip2px(10));
                                            break;
                                        case 2:
                                            paintView.setSize(dip2px(15));
                                            break;
                                        default:
                                            break;
                                    }
                                    dialog.dismiss();
                                }
                            }).create();
        }
        PaintDialog.show();
    }

    /**
     * 显示选择画笔形状的对话框
     */
    private void showShapeDialog(){
        if(ShapDialog == null){
            ShapDialog = new AlertDialog.Builder(this)
                    .setTitle("选择形状")
                    .setSingleChoiceItems(new String[]{"路径", "直线", "矩形", "圆形","实心矩形", "实心圆"}, 0,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case 0:
                                            paintView.setType(PaintView.ActionType.Path);
                                            break;
                                        case 1:
                                            paintView.setType(PaintView.ActionType.Line);
                                            break;
                                        case 2:
                                            paintView.setType(PaintView.ActionType.Rect);
                                            break;
                                        case 3:
                                            paintView.setType(PaintView.ActionType.Circle);
                                            break;
                                        case 4:
                                            paintView.setType(PaintView.ActionType.FillEcRect);
                                            break;
                                        case 5:
                                            paintView.setType(PaintView.ActionType.FilledCircle);
                                            break;
                                        default:
                                            break;
                                    }
                                    dialog.dismiss();
                                }
                            }).create();
        }
        ShapDialog.show();
    }


    private int dip2px(float dp)
    {
        final float scale=getResources().getDisplayMetrics().density;
        return (int)(dp*scale+0.5f);
    }
}
