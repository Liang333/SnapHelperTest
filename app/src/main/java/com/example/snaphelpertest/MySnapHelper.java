package com.example.snaphelpertest;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @Description: 左对齐的SnapHelper
 *
 * RecyclerView在24.2.0版本中新增了SnapHelper这个辅助类，用于辅助RecyclerView在滚动结束时将Item对齐到某个位置。
 * 特别是列表横向滑动时，很多时候不会让列表滑到任意位置，而是会有一定的规则限制，通过SnapHelper来定义对齐规则了。
 *
 * SnapHelper是一个抽象类，有三个抽象方法，官方提供了LinearSnapHelper子类，可以让RecyclerView滚动停止时相应的Item停留中间位置。
 *   和PagerSnapHelper子类，可以使RecyclerView像ViewPager一样的效果，一次只能滑一页，而且居中显示。
 *
 *   手指在屏幕上滑动RecyclerView然后松手，RecyclerView中的内容会顺着惯性继续往手指滑动的方向继续滚动直到停止，
 *   这个过程叫做Fling。Fling操作从手指离开屏幕瞬间被触发，在滚动停止时结束。
 *
 * @Author: Linda
 * @Time: 2020/5/7 21:14.
 *
 * 简书详解SnapHelper： https://www.jianshu.com/p/e54db232df62
 */
public class MySnapHelper extends LinearSnapHelper {

    /**
     * 水平、垂直方向的度量
     * 使用RecycleView，想要让列表项竖直展示还是水平展示，
     *     只需要在初始化LayoutManager的时候指定显示方向即可: RecyclerView.HORIZONTAL
     *
     * OrientationHelper其实就是对RecycleView中子View管理的工具类，
     * 并且它只是一个抽象类，类中定义了获取View布局信息的相关方法。
     *
     * createHorizontalHelper（对应水平的LayoutManager）和
     * createVerticalHelper（对应竖直的LayoutManager）方法已经为我们实现了这些方法，
     * 并且返回了OrientationHelper供我们使用。
     * 重点关注这两个方法源码！！！
     */
    @Nullable
    private OrientationHelper mVerticalHelper;
    @Nullable
    private OrientationHelper mHorizontalHelper;

    @NonNull
    private OrientationHelper getVerticalHelper(@NonNull RecyclerView.LayoutManager layoutManager) {
        if (mVerticalHelper == null) {
            mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
        }
        return mVerticalHelper;
    }

    @NonNull
    private OrientationHelper getHorizontalHelper(@NonNull RecyclerView.LayoutManager layoutManager) {
        if (mHorizontalHelper == null) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return mHorizontalHelper;
    }

    /**
     * SnapHelper抽象方法之一。
     * 这个方法会计算第二个参数对应的ItemView当前的坐标与需要对齐的坐标之间的距离。
     * 该方法返回一个大小为2的int数组，分别对应x轴和y轴方向上的距离。
     *
     * 该方法是返回第二个传参对应的view到RecyclerView中间位置的距离，可以支持水平方向滚动和竖直方向滚动两个方向的计算。
     * 最主要的计算距离的这个方法distanceToStart()：
     */
    @Nullable
    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
        int[] out = new int[2];

        // 水平方向滑动时计算x方向，否则偏移为0
        if (layoutManager.canScrollHorizontally()) {
            out[0] = distanceToStart(layoutManager, targetView, getHorizontalHelper(layoutManager));
        } else {
            out[0] = 0;
        }

        // 垂直方向滑动时计算y方向，否则偏移为0
        if (layoutManager.canScrollVertically()) {
            out[1] = distanceToStart(layoutManager, targetView, getVerticalHelper(layoutManager));
        } else {
            out[1] = 0;
        }
        return out;
    }

    /**
     * @param layoutManager
     * @param targetView
     * @param helper
     * @return
     */
    private int distanceToStart(RecyclerView.LayoutManager layoutManager, View targetView, OrientationHelper helper) {
        // RecyclerView的边界x值,也就是左侧Padding值
        final int start;
        if (layoutManager.getClipToPadding()) {
            start = helper.getStartAfterPadding();
        } else {
            start = 0;
        }
        // targetView的start坐标与RecyclerView的paddingStart之间的差值, 就是需要滚动调整的距离
        /**
         * Returns the start of the view including its decoration and margin.
         * <p>
         * For example, for the horizontal helper, if a View's left is at pixel 20, has 2px left
         * decoration and 3px left margin, returned value will be 15px.
         *
         * @param view The view element to check
         * @return The first pixel of the element
         * @see #getDecoratedEnd(android.view.View)
         */
        return helper.getDecoratedStart(targetView) - start;
    }

    /**
     * SnapHelper抽象方法之一。
     * 该方法会找到当前layoutManager上最接近对齐位置的那个view，该view称为SanpView，
     * 对应的position称为SnapPosition。如果返回null，就表示没有需要对齐的View，也就不会做滚动对齐调整。
     */
    @Nullable
    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager.canScrollVertically()) {
            return findStartView(layoutManager, getVerticalHelper(layoutManager));
        } else {
            return findStartView(layoutManager, getHorizontalHelper(layoutManager));
        }
    }

    /**
     * 寻找SnapView，这里的目的坐标就是RecyclerView左边位置坐标，
     * 可以看到会根据layoutManager的布局方式（水平布局方式或者竖向布局方式）区分计算，
     * 但最终都是通过findStartView()方法来找snapView的。
     * @param layoutManager
     * @param helper
     * @return
     */
    private View findStartView(RecyclerView.LayoutManager layoutManager, OrientationHelper helper) {
        int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return null;
        }

        View closestChild = null;
        final int start;
        if (layoutManager.getClipToPadding()) {
            start = helper.getStartAfterPadding();
        } else {
            start = 0;
        }
        int absClosest = Integer.MAX_VALUE;

        for (int i = 0; i < childCount; i++) {
            final View child = layoutManager.getChildAt(i);
            // ItemView 的左侧坐标
            int childStart = helper.getDecoratedStart(child);
            // 计算此ItemView 与 RecyclerView左侧的距离差
            int absDistance = Math.abs(childStart - start);

            // 找到那个最靠近左侧的ItemView然后返回
            if (absDistance < absClosest) {
                absClosest = absDistance;
                closestChild = child;
            }
        }

        return closestChild;
    }

    /**
     * SnapHelper抽象方法之一。
     * 该方法会根据触发Fling操作的速率（参数velocityX和参数velocityY）来找到RecyclerView需要滚动到哪个位置，
     * 该位置对应的ItemView就是那个需要进行对齐的列表项。我们把这个位置称为targetSnapPosition，
     * 对应的View称为targetSnapView。如果找不到targetSnapPosition，就返回RecyclerView.NO_POSITION。
     *
     * SmoothScroller需要设置一个滚动的目标位置，通过findTargetSnapPosition()方法计算得到的targetSnapPosition给它，
     * 告诉滚动器要滚到这个位置，然后就启动SmoothScroller进行滚动操作。
     */
    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        // 左对齐和居中对齐一样，无需自定义处理
        return super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
    }
}
