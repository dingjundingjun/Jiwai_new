package com.sounuo.jiwai.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.sounuo.jiwai.R;



public class ChangeColorTextView extends View {

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mPaint;
    /**
     * 颜色
     */
    private int mColor = 0xFF45C01A;
    /**
     * 透明度 0-255
     */
    private int mAlpha;
    /**
     * 图标
     */
    private Bitmap mIconBitmap;
    /**
     * 限制绘制icon的范围
     */
    private Rect mIconRect;
    /**
     * icon底部文本
     */
    private String mText = "历史典故";
    private int mTextSize = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics());
    private Paint mTextPaint;
    private Rect mTextBound = new Rect();
    /**
     * 文字透明度 0-255
     */
    private int mTextAlpha = 255;
    /**
     * 文字颜色值
     */
    private int mTextColor = 0x000000;

    public ChangeColorTextView(Context context) {
        super(context);
    }

    /**
     * 初始化自定义属性值
     *
     * @param context
     * @param attrs
     */
    public ChangeColorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 获取设置的图标
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ChangeColorIconView);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {

            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.ChangeColorIconView_check_icon:
                    BitmapDrawable drawable = (BitmapDrawable) a.getDrawable(attr);
                    mIconBitmap = drawable.getBitmap();
                    break;
                case R.styleable.ChangeColorIconView_check_color:
                    mColor = a.getColor(attr, 0x45C01A);
                    break;
                case R.styleable.ChangeColorIconView_text_color:
                    mTextColor = a.getColor(attr, 0x000000);
                    break;
                case R.styleable.ChangeColorIconView_text:
                    mText = a.getString(attr);
                    break;
                case R.styleable.ChangeColorIconView_check_alpha:
                    mAlpha = a.getInt(attr, 0);
                    break;
                case R.styleable.ChangeColorIconView_text_size:
                    mTextSize = (int) a.getDimension(attr, TypedValue
                            .applyDimension(TypedValue.COMPLEX_UNIT_SP, 10,
                                    getResources().getDisplayMetrics()));
                    break;
            }
        }

        a.recycle();

        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
        // 得到text绘制范围
        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 得到绘制icon的宽
        int bitmapWidth = Math.min(getMeasuredWidth() - getPaddingLeft()
                - getPaddingRight(), getMeasuredHeight() - getPaddingTop()
                - getPaddingBottom() - mTextBound.height());

        int left = getMeasuredWidth() / 2 - bitmapWidth / 2;
        int top = (getMeasuredHeight() - mTextBound.height()) / 2 - bitmapWidth
                / 2;
        // 设置icon的绘制范围
        mIconRect = new Rect(left, top, left + bitmapWidth, top + bitmapWidth);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        int alpha = mAlpha;
        int textalpha = mTextAlpha;
        canvas.drawBitmap(mIconBitmap, null, mIconRect, null);
        setupTargetBitmap(alpha);
//        drawSourceText(canvas, textalpha);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        drawTargetText(canvas, textalpha);

    }

    private void setupTargetBitmap(int alpha) {
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
                Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setAlpha(alpha);
        mCanvas.drawRect(mIconRect, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setAlpha(255);
        mCanvas.drawBitmap(mIconBitmap, null, mIconRect, mPaint);
    }

    private void drawSourceText(Canvas canvas, int alpha) {
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setAlpha(255 - alpha);
        canvas.drawText(mText, mIconRect.left + mIconRect.width() / 2 - mTextBound.width() / 2,
                mIconRect.bottom + mTextBound.height(), mTextPaint);
    }

    private void drawTargetText(Canvas canvas, int alpha) {
        mTextPaint.setColor(mTextColor);
        mTextPaint.setAlpha(alpha);
        canvas.drawText(mText, mIconRect.left + mIconRect.width() / 2 - mTextBound.width() / 2,
                mIconRect.bottom - 2 * mTextBound.height(), mTextPaint);

    }

    public void setText(String text) {
        this.mText = text;
    }

    public void setIconAlpha(int alpha) {
        this.mAlpha = alpha;
        invalidateView();
    }

    public void changeSelectColor() {
        if (!isSelected()) {
            setIconAlpha(255);
        } else {
            setIconAlpha(0);
        }
    }

    public boolean isSelected() {
        if (mAlpha == 255) {
            return true;
        } else {
            return false;
        }
    }

    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    /**
     * 设置图标的颜色
     *
     * @param color
     */
    public void setIconColor(int color) {
        mColor = color;
    }

    /**
     * 设置图标通过资源Id
     *
     * @param resId
     */
    public void setIcon(int resId) {
        this.mIconBitmap = BitmapFactory.decodeResource(getResources(), resId);
        if (mIconRect != null)
            invalidateView();
    }

    /**
     * 设置图标通过Bitmap
     *
     * @param iconBitmap
     */
    public void setIcon(Bitmap iconBitmap) {
        this.mIconBitmap = iconBitmap;
        if (mIconRect != null)
            invalidateView();
    }

    private static final String INSTANCE_STATE = "instance_state";
    private static final String STATE_ALPHA = "state_alpha";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putInt(STATE_ALPHA, mAlpha);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mAlpha = bundle.getInt(STATE_ALPHA);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
        } else {
            super.onRestoreInstanceState(state);
        }

    }

}
