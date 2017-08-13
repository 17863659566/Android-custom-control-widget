package com.zh.young.customprogress.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.zh.young.customprogress.R;


public class CircleProgressView extends View {


    private final String TAG = "CircleProgressView";
    /**
     * 内部圆的数值的颜色
     */
    private static int mInnerCircleDigitalColor;
    /**
     * 内部圆的颜色
     */
    private static int mInnerCircleBackgroundColor;
    /**
     * 外部弧的开始渐变的颜色
     */
    private static int mArcGradientFromColor;
    /**
     * 外部弧的结束渐变的颜色
     */
    private static int mArcGradientToColor;
    /**
     * 最外部的数据的颜色
     */
    private static int mExternalDigitalColor;
    /**
     * 绘制的中心
     */
    private int mCenter;
    /**
     * 外部弧的外半径
     */
    private int mOutRadius;
    /**
     * 外部弧的内半径
     */
    private int mInnerRadius;
    /**
     * 内部圆的半径
     */
    private int mInnerCircleRadius;
    /**
     * 最外部数据的画笔
     */
    private Paint mExternalDigitalPaint;
    /**
     * 外部弧的画笔
     */
    private Paint mArcPaint;
    /**
     * 内部圆的半径画笔
     */
    private Paint mInnerCirclePaint;
    /**
     * 内部数据画笔
     */
    private Paint mInnerDigitalPaint;
    /**
     * 外部弧的包裹矩形
     */
    private RectF mOutArcRecF;
    /**
     * 内部弧的包裹矩形
     */
    private RectF mInnerArcRecF;
    /**
     * 弧之间的线的画笔
     */
    private Paint mLinePaint;
    /**
     * 开始画的Y值
     */
    private int mStartY;
    /**
     * 结束的Y值
     */
    private int mEndY;
    /**
     * 开始绘制的X值
     */
    private int mStartX;
    /**
     * 结束绘制的值
     */
    private int mEndX;
    private SweepGradient mSweepGradient;
    /**
     * 内部字体
     */
    private Paint mTextPaint;
    private float mValue = 0;
    private Paint mArcCenterPaint;
    private RectF mOutArcCenterRecF;
    private int mCount;

    public CircleProgressView(Context context) {
        this(context,null);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);

    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getCustomProperty(context,attrs,defStyleAttr);
        initData();
        initPaint();
        setBackgroundColor(Color.GRAY);
    }

    /**
     * 创建并优化画笔
     */
    private void initPaint() {
        mArcPaint = createPaint();
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(10);
        mArcPaint.setShader(mSweepGradient);
        EmbossMaskFilter embossMaskFilter = new EmbossMaskFilter(new float[]{1, 1, 1},0.4f,6,3);
        mArcPaint.setMaskFilter(embossMaskFilter);
        mInnerCirclePaint = createPaint();
        mInnerCirclePaint.setColor(Color.WHITE);
        mLinePaint = createPaint();
        mLinePaint.setStrokeWidth(10);
        mLinePaint.setColor(0xffdddddd);

        mTextPaint = createPaint();
        mTextPaint.setColor(Color.RED);
        mTextPaint.setTextSize(100);
        mTextPaint.setStrokeWidth(40);
        mTextPaint.setColor(mInnerCircleDigitalColor);

        mArcCenterPaint = createPaint();
        mArcCenterPaint.setStrokeWidth(80);
        mArcCenterPaint.setShader(mSweepGradient);
        mArcCenterPaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * 创建画笔，并且对画笔进行优化
     * @return
     */
    private Paint createPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true); // 开启抗锯齿
        paint.setDither(true); //防抖动
        paint.setFilterBitmap(true);

        return paint;
    }

    /**
     * 初始化相关需要的数据
     */
    private void initData() {
        //初始化绘制中心
        mCenter = 720 / 2;
        mOutRadius = mCenter;
        mInnerRadius = mCenter - 80;
        mInnerCircleRadius = mInnerRadius - 50;
        mOutArcRecF = new RectF(mCenter - mOutRadius,mCenter - mOutRadius,mCenter + mOutRadius,mCenter+mOutRadius);
        mOutArcCenterRecF = new RectF(mCenter - mOutRadius +40,mCenter - mOutRadius + 40,mCenter + mOutRadius - 40,mCenter+mOutRadius-40);
        mInnerArcRecF = new RectF(mCenter - mInnerRadius, mCenter - mInnerRadius, mCenter + mInnerRadius, mCenter + mInnerRadius);
        mStartX = mCenter;
        mEndX = mCenter;
        mStartY = 0;
        mEndY = mOutRadius - mInnerRadius;
        int[] colors = {0xFFE5BD7D, 0xFFFAAA64,
                0xFFFFFFFF, 0xFF6AE2FD,
                0xFF8CD0E5, 0xFFA3CBCB,
                0xFFBDC7B3, 0xFFD1C299, 0xFFE5BD7D,};
        float[] positions = {0, 1f / 8, 2f / 8, 3f / 8, 4f / 8, 5f / 8, 6f / 8, 7f / 8, 1};
        //渐变色
        mSweepGradient = new SweepGradient(mCenter, mCenter, colors, null);



    }

    /**
     * 获取所有的自定义属性
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public void getCustomProperty(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView, defStyleAttr, 0);
        mInnerCircleDigitalColor = array.getColor(R.styleable.CircleProgressView_inner_circle_digital_color, Color.BLACK);
        mInnerCircleBackgroundColor = array.getColor(R.styleable.CircleProgressView_inner_circle_background, Color.WHITE);
        mArcGradientFromColor = array.getColor(R.styleable.CircleProgressView_arc_gradient_from, Color.BLUE);
        mArcGradientToColor = array.getColor(R.styleable.CircleProgressView_arc_gradient_to, Color.GREEN);
        mExternalDigitalColor = array.getColor(R.styleable.CircleProgressView_external_digital_color, Color.BLACK);
        array.recycle(); //要记得回收
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasure(widthMeasureSpec),widthMeasure(widthMeasureSpec));
    }

    private int widthMeasure(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if(mode == MeasureSpec.AT_MOST){
            return mCenter * 2;
        }else{
            return size;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画外部的弧
        canvas.drawArc(mOutArcRecF,135,270,false, mArcPaint);
        //画里面的弧
        canvas.drawArc(mInnerArcRecF,135,270,false,mArcPaint);

        //每隔3度画一条线
        for (int i = 0;i < 360;i+=3){
            if(i < 135 || i > 225){
                canvas.drawLine(mStartX,mStartY,mEndX,mEndY,mLinePaint);
                //统计线的条数
                mCount += 1;
            }


            canvas.rotate(3,mCenter,mCenter);
        }

        canvas.drawCircle(mCenter,mCenter,mInnerCircleRadius,mInnerCirclePaint);
        String digital = String.valueOf(mValue);
        float digitalPe = mTextPaint.measureText(digital);
        canvas.drawText(digital,mCenter - digitalPe / 2,mCenter,mTextPaint);
        Paint paint = new Paint();

        canvas.drawArc(mOutArcCenterRecF,135,mValue,false,mArcCenterPaint);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_VOLUME_DOWN :
                //计算增加的弧度
                Toast.makeText(getContext(), "音量-", Toast.LENGTH_SHORT).show();
                if(mValue >= 270 / 7)
                    mValue -= 270 / 7;
                invalidate();
                break;
            case KeyEvent.KEYCODE_VOLUME_UP :
                if(mValue < 270 - 270 % 7)
                mValue += 270 / 7;
                Toast.makeText(getContext(), "音量+" + mValue, Toast.LENGTH_SHORT).show();
                invalidate();
                break;
        }
        return true;
    }
}
