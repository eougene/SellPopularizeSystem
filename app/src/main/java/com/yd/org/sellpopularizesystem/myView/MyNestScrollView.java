package com.yd.org.sellpopularizesystem.myView;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

/**
 * Created by hejin on 2017/9/8.
 */

public class MyNestScrollView extends NestedScrollView {
    private ScrollViewListener scrollViewListener;//提供外部调用的接口

    private OnScrollToBottomListener mOnScrollToBottomListener;

    public MyNestScrollView(Context context) {
        super(context);
    }

    public MyNestScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNestScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener){
        this.scrollViewListener = scrollViewListener;
    }

    //定义一个接口
    public interface ScrollViewListener{
        void onScrollChanged(int x, int y, int oldx, int oldy);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        /*if(scrollViewListener != null){
            scrollViewListener.onScrollChanged(l, t, oldl, oldt);
        }*/
        // 滑动的距离加上本身的高度与子View的高度对比
        Log.e("TAG", "onScrollChanged: "+ getChildAt(0).getMeasuredHeight());
        if(t + getHeight() >=  getChildAt(0).getMeasuredHeight()){
            // ScrollView滑动到底部
            if(mOnScrollToBottomListener != null) {
                mOnScrollToBottomListener.onScrollToBottom();
            }
        } else {
            if(mOnScrollToBottomListener != null) {
                mOnScrollToBottomListener.onNotScrollToBottom();
            }
        }
    }

    public void setScrollToBottomListener(OnScrollToBottomListener listener) {
        this.mOnScrollToBottomListener = listener;
    }

    public interface OnScrollToBottomListener {
        void onScrollToBottom();
        void onNotScrollToBottom();
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

   /* public void setNestedScrollChild(final View nestedScrollChild) {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                nestedScrollChild.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getHeight()));
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }*/

  /*  @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        //super.onNestedPreScroll(target, dx, dy, consumed);
        int[] childLocation = new int[2];
        target.getLocationInWindow(childLocation);
        int[] parentLocation = new int[2];
        getLocationInWindow(parentLocation);
        boolean upNeedParent = dy > 0 && childLocation[1] - parentLocation[1] > 0;
        boolean downNeedParent = dy < 0 && childLocation[1] - parentLocation[1] < 0;
        if (upNeedParent || downNeedParent) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }*/

   /* @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return flingWithNestedDispatch((int) velocityY);
    }*/

    /*private boolean flingWithNestedDispatch(int velocityY) {
        final int scrollY = getScrollY();
        final boolean canFling = (scrollY > 0 || velocityY > 0) &&
                (scrollY < getScrollRange() || velocityY < 0);
        if (!dispatchNestedPreFling(0, velocityY)) {
            dispatchNestedFling(0, velocityY, canFling);
            if (canFling) {
                fling(velocityY);
            }
        }
        return canFling;
    }

    private int getScrollRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            scrollRange = Math.max(0,
                    child.getHeight() - (getHeight() - getPaddingBottom() - getPaddingTop()));
        }
        return scrollRange;
    }*/

}
