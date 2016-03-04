package com.dean.quickindexview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.view.MotionEventCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2016/3/3.
 */
public class QuickIndexView extends View {

    char[] mLetters = new char[26];
    private Paint mPaint;
    private float mCellHight;
    private int mCellWidth;

    public QuickIndexView(Context context) {
        this(context, null);
    }

    public QuickIndexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化26个英文字母
        for (byte i = 65; i < 65 + 26; i++) {
            mLetters[i - 65] = (char) i;
        }
        //初始化画笔
        mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(Color.WHITE);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    /**
     * 设置当选中的字母更改时的监听器
     *
     * @param onLetterSelectedListener
     */
    public void setOnLetterSelectedListener(OnLetterSelectedListener onLetterSelectedListener) {
        mOnLetterSelectedListener = onLetterSelectedListener;
    }

    private OnLetterSelectedListener mOnLetterSelectedListener;

    public interface OnLetterSelectedListener {
        /**
         * 当字母被选中时引发
         *
         * @param view   引发事件的QuickIndexView对象
         * @param letter 被选中的字母
         * @param index  字母的索引，A对应为0
         */
        void onLetterSelected(View view, char letter, int index);
    }

    private int mLastIndex = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionMasked = MotionEventCompat.getActionMasked(event);
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_MOVE:
                int curIndex = (int) (event.getY() / mCellHight);
                if (curIndex != mLastIndex) {
                    mLastIndex = curIndex;
                    if (curIndex >= 0 && curIndex < mLetters.length) {
                        invalidate();
                        char selChar = mLetters[curIndex];
                        if (mOnLetterSelectedListener != null) {
                            mOnLetterSelectedListener.onLetterSelected(this, selChar, curIndex);
                        }
                    }
                }
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCellHight = (getMeasuredHeight() - getPaddingTop() - getPaddingBottom()) * 1.0f / mLetters.length;
        mCellWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < mLetters.length; i++) {
            String letter = String.valueOf(mLetters[i]);
            Rect rect = new Rect();
            mPaint.getTextBounds(letter, 0, 1, rect);
            int fontHeight = rect.height();
            int x = getPaddingLeft() + mCellWidth / 2;
            int y = (int) (getPaddingTop() + mCellHight / 2 + fontHeight / 2 + i * mCellHight);
            mPaint.setColor(mLastIndex == i ? Color.RED : Color.WHITE);
            canvas.drawText(letter, x, y, mPaint);
        }
    }

    /**
     * 设置选中的字母
     *
     * @param letter
     */
    public void setSelection(char letter) {
        int index = Character.toUpperCase(letter) - 'A';
        if (index >= 0 && index < mLetters.length) {
            if(index!=mLastIndex) {
                mLastIndex = index;
                invalidate();
                if(mOnLetterSelectedListener!=null)
                    mOnLetterSelectedListener.onLetterSelected(this,letter,index);
            }
        }
    }

}
