package com.epicor.canvas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import com.gprinter.command.LabelCommand;

import java.util.Vector;

/**
 * Copyright (C), 2012-2020, 珠海佳博科技股份有限公司
 * FileName: PrintConntent
 * Author: Circle
 * Date: 2020/7/20 10:04
 * Description: 打印内容
 */
public class PrintContent {
    /**
     * 获取图片
     * @param context
     * @return
     */
    @SuppressLint("SetTextI18n")
    public static Bitmap getBitmap(Context context, Bitmap mBitmap ) {
        View v = View.inflate(context, R.layout.lam_page, null);

        ImageView mNum1 = (ImageView) v.findViewById(R.id.mImage);
        mNum1.setImageBitmap(mBitmap);
        mNum1.setScaleType(ImageView.ScaleType.FIT_XY);


        final Bitmap bitmap = convertViewToBitmap(v);
        return bitmap;
    }

    /**
     * mxl转bitmap图片
     * @return
     */
    public static Bitmap convertViewToBitmap(View view){
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }


    public static Vector<Byte> getXmlBitmap(Context context,Bitmap mBitmap ){
        LabelCommand tsc = new LabelCommand();
        // 设置标签尺寸宽高，按照实际尺寸设置 单位mm
        tsc.addUserCommand("\r\n");
        tsc.addSize(110, 68);
        // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0 单位mm
        tsc.addGap(2);
        // 设置打印方向
        tsc.addDirection(LabelCommand.DIRECTION.FORWARD, LabelCommand.MIRROR.NORMAL);
        // 设置原点坐标
        tsc.addReference(0, 0);
        tsc.addSpeed(LabelCommand.SPEED.SPEED12);
        //设置浓度
        tsc.addDensity(LabelCommand.DENSITY.DNESITY12);
        // 撕纸模式开启
        tsc.addTear(LabelCommand.RESPONSE_MODE.ON);
        // 清除打印缓冲区
        tsc.addCls();
        Bitmap bitmap=getBitmap(context,mBitmap);
        // 绘制图片
        tsc.drawXmlImage(0,0,1210,bitmap);
//        tsc.drawImage(0,0,1210,mBitmap);
        // 打印标签
        tsc.addPrint(1);
        return tsc.getCommand();
    }


}
