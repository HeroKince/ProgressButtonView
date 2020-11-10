package com.widget.progress.button;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 *
 */
public class ProgressTextView extends TextView {

    private Context mContext;

    // Text
    private int mOriginalColor = Color.BLACK;
    private int mChangeColor = Color.RED;
    private Paint mOriginalPaint, mChangePaint;
    private int mBaseLine;
    private float mCurrentProgress;

    // Background
    private Paint mReachedBarPaint;
    private Paint mUnreachedBarPaint;
    private RectF mUnreachedRectF = new RectF(0, 0, 0, 0);
    private RectF mReachedRectF = new RectF(0, 0, 0, 0);
    private int unReachBarColorId;

    public ProgressTextView(Context context) {
        this(context, null);
    }

    public ProgressTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray td = context.obtainStyledAttributes(attrs, R.styleable.ProgressTextView);
        mOriginalColor = td.getColor(R.styleable.ProgressTextView_original_color, mOriginalColor);
        mChangeColor = td.getColor(R.styleable.ProgressTextView_change_color, mChangeColor);
        unReachBarColorId = td.getResourceId(R.styleable.ProgressTextView_unReachBarColor, android.R.color.white);
        mCurrentProgress = td.getFloat(R.styleable.ProgressTextView_textProgress, 0.6f);

        td.recycle();
        //根据颜色获取画笔
        mOriginalPaint = getPaintByColor(mOriginalColor);
        mChangePaint = getPaintByColor(mChangeColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        calculateDrawRectF();
        initializePainters();

        canvas.drawRect(mReachedRectF, mReachedBarPaint);
        canvas.drawRect(mUnreachedRectF, mUnreachedBarPaint);

        float middle = mCurrentProgress * getWidth();
        Paint.FontMetricsInt fontMetricsInt = mOriginalPaint.getFontMetricsInt();
        mBaseLine = (fontMetricsInt.bottom - fontMetricsInt.top) / 2
                - fontMetricsInt.bottom
                + getHeight() / 2
                + getPaddingTop() / 2
                - getPaddingBottom() / 2;

        clipRect(canvas, 0, middle, mChangePaint);
        clipRect(canvas, middle, getWidth(), mOriginalPaint);
    }

    private void clipRect(Canvas canvas, float start, float region, Paint paint) {
        //改变的颜色
        canvas.save();
        canvas.clipRect(start + getPaddingLeft(), 0, region, getHeight());
        canvas.drawText(getText().toString(), getPaddingLeft() + (getWidth() - paint.measureText(getText().toString())) / 2, mBaseLine, paint);
        canvas.restore();
    }

    public void setCurrentProgress(float currentProgress) {
        mCurrentProgress = currentProgress;
        invalidate();
    }

    private Paint getPaintByColor(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setDither(true);//防抖动
        paint.setTextSize(getTextSize());
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        return paint;
    }

    private void initializePainters() {
        LinearGradient reachedLinearGradient = new LinearGradient(
                0, 0,
                mReachedRectF.right, mReachedRectF.top,
                Color.RED, Color.YELLOW,
                Shader.TileMode.MIRROR
        );
        mReachedBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mReachedBarPaint.setShader(reachedLinearGradient);

        mUnreachedBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mUnreachedBarPaint.setColor(mContext.getResources().getColor(unReachBarColorId));
    }

    private void calculateDrawRectF() {
        mReachedRectF.left = 0;
        mReachedRectF.top = 0;
        mReachedRectF.right = getPaddingLeft() + mCurrentProgress * getWidth();
        mReachedRectF.bottom = getHeight() + getPaddingTop();

        mUnreachedRectF.left = mReachedRectF.right;
        mUnreachedRectF.right = getWidth();
        mUnreachedRectF.top = 0;
        mUnreachedRectF.bottom = getHeight() + getPaddingTop();
    }

    public void start(long duration) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                setCurrentProgress(value);
            }
        });
        animator.start();
    }

}