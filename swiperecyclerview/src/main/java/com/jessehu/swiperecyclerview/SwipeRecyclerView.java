package com.jessehu.swiperecyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jessehu.swiperecyclerview.menu.MenuView;

import java.util.ArrayList;
import java.util.List;

/**
 * SwipeRecyclerView
 *
 * @author JesseHu
 * @date 2019/9/10
 */
public class SwipeRecyclerView extends RecyclerView {
    /**
     * 点击坐标不再内容视图的范围内（没有点击到ItemView）
     */
    private static final int INVALID_POSITION = -1;
    /**
     * 最小的滑动速度
     */
    private static final int SNAP_VELOCITY = 600;
    /**
     * 速度监听
     */
    private VelocityTracker mVelocityTracker;
    /**
     * 最小滑动距离
     */
    private int mTouchSlop;
    /**
     * 滑动管理
     */
    private Scroller mScroller;
    /**
     * 滑动过程中记录上次触碰点X
     */
    private float mLastX;
    /**
     * 手指触碰屏幕的起始位置
     */
    private float mFirstX, mFirstY;
    /**
     * 内容视图所在的区域范围(ItemView的范围)
     */
    private Rect mTouchFrame;
    /**
     * 当前点击的ItemView的位置
     */
    private int mPosition;
    /**
     * 当前滑动的视图
     */
    private ViewGroup mFlingView;
    /**
     * 菜单视图的宽度(所有菜单的总宽度)
     */
    private int mMenuWidth;
    /**
     * 是否滑动状态
     */
    private boolean isSlide;

    /**
     * 展开开始
     */
    private static final int STATUS_OPEN_START = 1;
    /**
     * 展开结束
     */
    private static final int STATUS_OPEN_FINISH = 2;
    /**
     * 关闭开始
     */
    private static final int STATUS_CLOSE_SATRT = 3;
    /**
     * 关闭结束
     */
    private static final int STATUS_CLOSE_FINISH = 4;

    /**
     * 当前状态，默认为关闭状态
     */
    private int mCurrentStatus = STATUS_CLOSE_FINISH;

    private OnMenuStatusListener mMenuStatusListener;

    public SwipeRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public SwipeRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutManager(new LinearLayoutManager(context));
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();
        getVelocity(e);

        Log.e("MotionEvent", "onInterceptTouchEvent: " + e.getAction());
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    // 如果滑动没有停止，则停止对应的动画
                    mScroller.abortAnimation();
                }
                mFirstX = x;
                mFirstY = y;
                mLastX = x;
                mPosition = pointToPosition(x, y);
                if (mPosition != INVALID_POSITION) {
                    // view作为上次滑动的视图
                    View view = mFlingView;
                    // 获取当前滑动的视图
                    mFlingView = (ViewGroup) getChildAt(mPosition - getFirstPosition());
                    // 如果当前滑动的item和上次滑动的item不是同一个且上次滑动的item依然处于展开状态则将上次展开的item关闭
                    if (view != null && mFlingView != view && view.getScrollX() != 0) {
                        view.scrollTo(0, 0);
                        if (mMenuStatusListener != null && mCurrentStatus != STATUS_CLOSE_FINISH) {
                            View itemView = getItemView(view);
                            List<View> menuViews = getMenuViews(view);
                            mMenuStatusListener.onCloseFinish(itemView, menuViews, mPosition);
                            mCurrentStatus = STATUS_CLOSE_FINISH;
                        }
                    }
                    // 获取菜单部分宽度
                    mMenuWidth = mFlingView.findViewById(R.id.ll_menu_layout).getWidth();
                }

                break;
            case MotionEvent.ACTION_MOVE:
                // 滑动时获取1s内滑动速度
                mVelocityTracker.computeCurrentVelocity(1000);
                float xVelocity = mVelocityTracker.getXVelocity();
                float yVelocity = mVelocityTracker.getYVelocity();
                // 水平速度>最小速度且水平速度大于垂直速度或者水平移动距离>最小移动距离且水平移动距离大于垂直移动距离
                boolean slide = (Math.abs(xVelocity) > SNAP_VELOCITY
                        && Math.abs(xVelocity) > Math.abs(yVelocity))
                        || (Math.abs(x - mFirstX) >= mTouchSlop
                        && Math.abs(x - mFirstX) > Math.abs(y - mFirstY));
                if (slide) {
                    if (mMenuStatusListener != null) {
                        View itemView = getItemView(mFlingView);
                        List<View> menuViews = getMenuViews(mFlingView);
                        if ((xVelocity < 0 || x - mFirstX < 0) && mCurrentStatus != STATUS_OPEN_START) {
                            mMenuStatusListener.onOpenStart(itemView, menuViews, mPosition);
                            mCurrentStatus = STATUS_OPEN_START;
                        }
                        if ((xVelocity > 0 || x - mFirstX > 0) && mCurrentStatus != STATUS_CLOSE_SATRT) {
                            mMenuStatusListener.onCloseStart(itemView, menuViews, mPosition);
                            mCurrentStatus = STATUS_CLOSE_SATRT;
                        }
                    }
                    isSlide = true;
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                releaseVelocity();
                break;
            default:
        }

        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (isSlide && mPosition != INVALID_POSITION) {
            float x = e.getX();
            getVelocity(e);
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mMenuWidth > 0) {
                        // item随着手指滑动
                        // 滑动偏移量
                        float dx = mLastX - x;
                        float scrollX = mFlingView.getScrollX();
                        float newX = scrollX + dx;

                        if (newX < 0) {
                            newX = 0;
                            dx = -scrollX;
                            if (mMenuStatusListener != null && mCurrentStatus != STATUS_CLOSE_FINISH) {
                                View itemView = getItemView(mFlingView);
                                List<View> menuViews = getMenuViews(mFlingView);
                                mMenuStatusListener.onCloseFinish(itemView, menuViews, mPosition);
                                mCurrentStatus = STATUS_CLOSE_FINISH;
                            }
                        }

                        // 滑动距离不能大于菜单的宽度
                        if (newX >= mMenuWidth) {
                            dx = mMenuWidth - scrollX;
                            if (mMenuStatusListener != null && mCurrentStatus != STATUS_OPEN_FINISH) {
                                View itemView = getItemView(mFlingView);
                                List<View> menuViews = getMenuViews(mFlingView);
                                mMenuStatusListener.onOpenFinish(itemView, menuViews, mPosition);
                                mCurrentStatus = STATUS_OPEN_FINISH;
                            }
                        }

                        if (mMenuStatusListener != null) {
                            View itemView = getItemView(mFlingView);
                            List<View> menuViews = getMenuViews(mFlingView);
                            if (dx > 0 && mCurrentStatus != STATUS_OPEN_START) {
                                mMenuStatusListener.onOpenStart(itemView, menuViews, mPosition);
                                mCurrentStatus = STATUS_OPEN_START;
                            }
                            if (dx < 0 && mCurrentStatus != STATUS_CLOSE_SATRT) {
                                mMenuStatusListener.onCloseStart(itemView, menuViews, mPosition);
                                mCurrentStatus = STATUS_CLOSE_SATRT;
                            }
                        }

                        mFlingView.scrollBy((int) dx, 0);
                        mLastX = x;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (mMenuWidth > 0) {
                        int scrollX = mFlingView.getScrollX();
                        // 滑动时获取1s内滑动速度
                        mVelocityTracker.computeCurrentVelocity(1000);
                        // 获取水平方向上的速度
                        float xVelocity = mVelocityTracker.getXVelocity();
                        if (xVelocity <= -SNAP_VELOCITY) {
                            // 负数表示向左滑动，如果速度大于最小速度则展开
                            mScroller.startScroll(scrollX, 0, mMenuWidth - scrollX, 0,
                                    Math.abs(mMenuWidth - scrollX));
                            if (mMenuStatusListener != null && mCurrentStatus != STATUS_OPEN_FINISH) {
                                View itemView = getItemView(mFlingView);
                                List<View> menuViews = getMenuViews(mFlingView);
                                mMenuStatusListener.onOpenFinish(itemView, menuViews, mPosition);
                                mCurrentStatus = STATUS_OPEN_FINISH;
                            }
                        } else if (xVelocity >= SNAP_VELOCITY) {
                            // 正数表示向右滑动，如果速度大于最小速度则关闭
                            mScroller.startScroll(scrollX, 0, -scrollX, 0, Math.abs(mMenuWidth - scrollX));
                            if (mMenuStatusListener != null && mCurrentStatus != STATUS_CLOSE_FINISH) {
                                View itemView = getItemView(mFlingView);
                                List<View> menuViews = getMenuViews(mFlingView);
                                mMenuStatusListener.onCloseFinish(itemView, menuViews, mPosition);
                                mCurrentStatus = STATUS_CLOSE_FINISH;
                            }
                        } else if (scrollX >= mMenuWidth / 2) {
                            // 如果滑动距离大于菜单宽度的一般则展开
                            mScroller.startScroll(scrollX, 0, mMenuWidth - scrollX, 0,
                                    Math.abs(mMenuWidth - scrollX));
                            if (mMenuStatusListener != null && mCurrentStatus != STATUS_OPEN_FINISH) {
                                View itemView = getItemView(mFlingView);
                                List<View> menuViews = getMenuViews(mFlingView);
                                mMenuStatusListener.onOpenFinish(itemView, menuViews, mPosition);
                                mCurrentStatus = STATUS_OPEN_FINISH;
                            }
                        } else {
                            // 其他情况全部关闭
                            mScroller.startScroll(scrollX, 0, -scrollX, 0, Math.abs(mMenuWidth - scrollX));
                            if (mMenuStatusListener != null && mCurrentStatus != STATUS_CLOSE_FINISH) {
                                View itemView = getItemView(mFlingView);
                                List<View> menuViews = getMenuViews(mFlingView);
                                mMenuStatusListener.onCloseFinish(itemView, menuViews, mPosition);
                                mCurrentStatus = STATUS_CLOSE_FINISH;
                            }
                        }
                        invalidate();
                    }
                    // 清除所有滑动状态的数据
                    mMenuWidth = 0;
                    isSlide = false;
                    mPosition = INVALID_POSITION;
                    releaseVelocity();
                    break;
                default:
            }
            return true;
        } else {
            closeMenu();
            // Velocity，这里的释放是防止RecyclerView正常拦截了，但是在onTouchEvent中却没有被释放；
            // 有三种情况：1.onInterceptTouchEvent并未拦截，在onInterceptTouchEvent方法中，DOWN和UP一对获取和释放；
            // 2.onInterceptTouchEvent拦截，DOWN获取，但事件不是被侧滑处理，需要在这里进行释放；
            // 3.onInterceptTouchEvent拦截，DOWN获取，事件被侧滑处理，则在onTouchEvent的UP中释放。
            releaseVelocity();
        }
        return super.onTouchEvent(e);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            // 实时滑动
            mFlingView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        } else {
            super.computeScroll();
        }
    }

    /**
     * 获取速度监听并绑定触摸事件
     *
     * @param event 触摸事件
     */
    private void getVelocity(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 释放速度监听
     */
    private void releaseVelocity() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * 获取当前点击的位置所对应的ItemView的position
     * 该方法来自于{@link android.widget.AbsListView}
     *
     * @param x
     * @param y
     * @return
     */
    public int pointToPosition(int x, int y) {
        int firstPosition = getFirstPosition();
        Rect frame = mTouchFrame;
        if (frame == null) {
            mTouchFrame = new Rect();
            frame = mTouchFrame;
        }

        final int count = getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            // 遍历获取所有的子View并获取其对应的Rect
            final View child = getChildAt(i);
            if (child.getVisibility() == View.VISIBLE) {
                // 获取view的Rect
                child.getHitRect(frame);
                // 判断点击的点是否在该view的范围内
                if (frame.contains(x, y)) {
                    return firstPosition + i;
                }
            }
        }
        return INVALID_POSITION;
    }

    /**
     * 获取当前可见的第一个item的position
     *
     * @return 第一个ItemView的position
     */
    private int getFirstPosition() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        if (layoutManager == null) {
            return INVALID_POSITION;
        } else {
            return layoutManager.findFirstVisibleItemPosition();
        }
    }

    /**
     * 关闭已展开的菜单
     */
    public void closeMenu() {
        if (mFlingView != null && mFlingView.getScrollX() != 0) {
            mFlingView.scrollTo(0, 0);
            if (mMenuStatusListener != null && mCurrentStatus != STATUS_CLOSE_FINISH) {
                View itemView = getItemView(mFlingView);
                List<View> menuViews = getMenuViews(mFlingView);
                mMenuStatusListener.onCloseFinish(itemView, menuViews, mPosition);
                mCurrentStatus = STATUS_CLOSE_FINISH;
            }
        }
    }

    /**
     * 获取所有的菜单视图
     *
     * @return
     */
    private List<View> getMenuViews(View rootView) {
        int childCount = ((ViewGroup) rootView.findViewById(R.id.ll_menu_layout)).getChildCount();
        List<View> menuViews = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            menuViews.add(((ViewGroup) rootView.findViewById(R.id.ll_menu_layout)).getChildAt(i));
        }
        return menuViews;
    }

    /**
     * 获取ItemView
     *
     * @return
     */
    private View getItemView(View rootView) {
        return ((ViewGroup) rootView.findViewById(R.id.fl_content_layout)).getChildAt(0);
    }

    public void setOnMenuStatusListener(OnMenuStatusListener mMenuStatusListener) {
        this.mMenuStatusListener = mMenuStatusListener;
    }

    /**
     * 菜单打开或关闭状态监听
     */
    public interface OnMenuStatusListener {
        /**
         * 菜单打开起始状态
         *
         * @param itemView     ItemView
         * @param menuViewList 菜单View
         * @param position     item对应的position
         */
        void onOpenStart(View itemView, List<View> menuViewList, int position);

        /**
         * 菜单打开结束
         *
         * @param itemView     ItemView
         * @param menuViewList 菜单View
         * @param position     item对应的position
         */
        void onOpenFinish(View itemView, List<View> menuViewList, int position);

        /**
         * 菜单关闭起始状态
         *
         * @param itemView     ItemView
         * @param menuViewList 菜单View
         * @param position     item对应的position
         */
        void onCloseStart(View itemView, List<View> menuViewList, int position);

        /**
         * 菜单关闭结束
         *
         * @param itemView     ItemView
         * @param menuViewList 菜单View
         * @param position     item对应的position
         */
        void onCloseFinish(View itemView, List<View> menuViewList, int position);
    }

}
