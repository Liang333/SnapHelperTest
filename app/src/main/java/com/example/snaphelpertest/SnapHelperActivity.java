package com.example.snaphelpertest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SnapHelperActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_helper);
        RecyclerView mRecyclerView1 = findViewById(R.id.rv1);
        mRecyclerView1.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        SnapHelperAdapter mSnapHelperAdapter1 = new SnapHelperAdapter(this);
        /**
         *  SnapHelper通过attachToRecyclerView()方法附着到RecyclerView上，从而实现辅助RecyclerView滚动对齐操作。
         *    在attachToRecyclerView()方法中会清掉SnapHelper之前保存的RecyclerView对象的回调(如果有的话)，
         *    对新设置进来的RecyclerView对象设置回调,然后初始化一个Scroller对象,
         *    最后调用snapToTargetExistingView()方法对SnapView进行滚动调整，以使得SnapView达到对齐效果。
         *   可以看到，snapToTargetExistingView()方法就是先找到SnapView，然后计算SnapView当前坐标到目的坐标之间的距离，
         *   然后调用RecyclerView.smoothScrollBy()方法实现对RecyclerView内容的平滑滚动，
         *   从而将SnapView移到目标位置，达到对齐效果。
         *
         *   RecyclerView设置的回调有两个：一个是OnScrollListener对象mScrollListener.还有一个是OnFlingListener对象。
         *   由于SnapHelper实现了OnFlingListener接口,所以这个对象就是SnapHelper自身了.
         */
        PagerSnapHelper mPagerSnapHelper = new PagerSnapHelper();
        mPagerSnapHelper.attachToRecyclerView(mRecyclerView1);
        mRecyclerView1.setAdapter(mSnapHelperAdapter1);

        RecyclerView mRecyclerView2 = findViewById(R.id.rv2);
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        SnapHelperAdapter mSnapHelperAdapter2 = new SnapHelperAdapter(this);
        LinearSnapHelper mLinearSnapHelper = new LinearSnapHelper();
//        MySnapHelper mLinearSnapHelper = new MySnapHelper();
        mLinearSnapHelper.attachToRecyclerView(mRecyclerView2);
        mRecyclerView2.setAdapter(mSnapHelperAdapter2);
    }
}
