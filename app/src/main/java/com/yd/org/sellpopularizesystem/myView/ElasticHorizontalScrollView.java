package com.yd.org.sellpopularizesystem.myView;

import android.content.Context;
import android.graphics.Rect;
import android.text.style.TtsSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;

/**
 * Created by hejin on 2017/4/25.
 */

public class ElasticHorizontalScrollView extends HorizontalScrollView {
    private View inner;
    private Rect normal = new Rect();
    private float x;
    public ElasticHorizontalScrollView(Context context) {
        super(context);
    }

    public ElasticHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ElasticHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            inner = getChildAt(0);
        }
        super.onFinishInflate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev==null){
            return super.onTouchEvent(ev);
        }else{
            commonOnTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    private void commonOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                x = ev.getX();
                break;
            case MotionEvent.ACTION_UP:
                if (isNeedAnimation()) {
                    animation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final float preX = x;
                float nowX = ev.getX();
                int distanceX = (int) (preX - nowX);
                scrollBy(distanceX, 0);
                x = nowX;
                if (isNeedMove()) {
                    if (normal.isEmpty()) {
                        normal.set(inner.getLeft(), inner.getTop(), inner.getRight(), inner.getBottom());
                    }
                    inner.layout(inner.getLeft() - distanceX, inner.getTop(), inner.getRight() - distanceX, inner.getBottom());
                }

                break;

            default:
                break;
        }
    }

    private void animation() {
        TranslateAnimation mTranslateAnimation = new TranslateAnimation(inner.getLeft(), 0, normal.left, 0);
        mTranslateAnimation.setDuration(50);
        inner.setAnimation(mTranslateAnimation);
        inner.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();
    }

    private boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    private boolean isNeedMove() {
        int offset = inner.getMeasuredWidth() - getWidth();
        int scrollX = getScrollX();
        if (scrollX == 0 || offset == scrollX)
            return true;
        return false;
    }
}
