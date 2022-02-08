package com.example.slidingarcviewdemo.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.slidingarcviewdemo.R;
import com.example.slidingarcviewdemo.domain.Products;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 小訾
 * @projectName SlidingArcViewDemo
 * @packageName com.example.slidingarcviewdemo.view
 * @description: 自定义泡泡动画
 * @date :2022/2/7 9:51
 */
public class CustomBubbleView extends RelativeLayout {
    private List<View> childViewList;
    private ArrayList<Products> products;
    private int currentIndex = 0;
    private int lineSize = 3;  // 一行展示几个
    private long animationDuration = 4000;  // 动画执行的时常
    private View.OnClickListener mBubbleClickListener;

    public CustomBubbleView(Context context) {
        this(context, null);
    }

    public CustomBubbleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomBubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        childViewList = new ArrayList<>();
        products = new ArrayList<>();
    }

    private void createChildView() {
        // 先找到需要展示的三条数据，如果总数小于三条，则展示两条或一个
        if (products.size() < 3) {
            if (products.size()==1) {
                create1ChildView(products.get(0));
                return;
            }else if(products.size()==2){
                create2ChildView(products.get(0),products.get(1));
                return;
            }
        }

        currentIndex+=1;
        Products products1 = products.get(currentIndex % products.size());
        currentIndex+=1;
        Products products2 = products.get(currentIndex % products.size());
        currentIndex+=1;
        Products products3 = products.get(currentIndex % products.size());
        final View bubbleView1 = getBubbleView(products1);
        final View bubbleView2 = getBubbleView(products2);
        final View bubbleView3 = getBubbleView(products3);
        childViewList.add(bubbleView1);
        childViewList.add(bubbleView2);
        childViewList.add(bubbleView3);

        LayoutParams layoutParams1 = getBubbleLayoutParams();
        layoutParams1.setMargins(0, 0, 150, 0);
        addView(bubbleView1, layoutParams1);
        addView(bubbleView2, getBubbleLayoutParams());
        LayoutParams layoutParams3 = getBubbleLayoutParams();
        layoutParams3.setMargins(150, 0, 0, 0);
        bubbleView3.setLayoutParams(layoutParams3);
        addView(bubbleView3, layoutParams3);

        int beginMargin = 200;

        getAnimSet(bubbleView1, -beginMargin - 100, -500).start();
        getAnimSet(bubbleView2, 0, -500).start();
        getAnimSet(bubbleView3, beginMargin + 100, -500).start();

        handler.sendEmptyMessageDelayed(0, animationDuration/3);
    }

    private void create1ChildView(Products products){
        final View bubbleView1 = getBubbleView(products);
        childViewList.add(bubbleView1);

        LayoutParams layoutParams1 = getBubbleLayoutParams();
        layoutParams1.setMargins(0, 0, 150, 0);
        addView(bubbleView1, layoutParams1);
        getAnimSet(bubbleView1, 0, -500).start();
    }

    private void create2ChildView(Products products1,Products products2){
        final View bubbleView1 = getBubbleView(products1);
        final View bubbleView3 = getBubbleView(products2);
        childViewList.add(bubbleView1);
        childViewList.add(bubbleView3);

        LayoutParams layoutParams1 = getBubbleLayoutParams();
        layoutParams1.setMargins(0, 0, 150, 0);
        addView(bubbleView1, layoutParams1);
        LayoutParams layoutParams3 = getBubbleLayoutParams();
        layoutParams3.setMargins(150, 0, 0, 0);
        bubbleView3.setLayoutParams(layoutParams3);
        addView(bubbleView3, layoutParams3);

        int beginMargin = 200;

        getAnimSet(bubbleView1, -beginMargin - 100, -500).start();
        getAnimSet(bubbleView3, beginMargin + 100, -500).start();
    }

    private LayoutParams getBubbleLayoutParams() {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        return layoutParams;
    }

    private View getBubbleView(Products products) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_home_bubble, null);
        ImageView mIvProductImage = view.findViewById(R.id.mIvProductImage);
        if (products != null) {
            String goodsPicUrl = products.getUrl();
            if(this.getContext() instanceof Activity){
                Activity activity = (Activity) this.getContext();
                if (!activity.isDestroyed()) {
                    Glide.with(this.getContext())
                            .applyDefaultRequestOptions(new RequestOptions()
                                    .placeholder(R.mipmap.ic_home_bubble_inner).error(R.mipmap.ic_home_bubble_inner))
                            .load(goodsPicUrl)
                            .into(mIvProductImage);
                }

            }

        }
        view.setTag(products);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBubbleClickListener != null) {
                    mBubbleClickListener.onClick(view);
                }
            }
        });
        return view;
    }

    public AnimatorSet getAnimSet(View targetView, float translationX, float translationY) {
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(targetView, "scaleX", 1.0F, 2.0F);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(targetView, "scaleY", 1.0F, 2.0F);
        ObjectAnimator translationXAnim = ObjectAnimator.ofFloat(targetView, "translationX", 0.0f, translationX);
        ObjectAnimator translationYAnim = ObjectAnimator.ofFloat(targetView, "translationY", 0.0F, translationY);

        scaleXAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();

            }
        });

        scaleXAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                childViewList.remove(targetView);
                removeView(targetView);
            }
        });

        AnimatorSet animSet = new AnimatorSet();
        animSet.play(scaleXAnim).with(scaleYAnim).with(translationXAnim).with(translationYAnim);
        animSet.setDuration(animationDuration);
        return animSet;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case 0:
                    start();
                    break;
            }
        }
    };

    public void setProduct(ArrayList<Products> data) {
        products.clear();
        removeAllViews();
        handler.removeCallbacksAndMessages(null);
        if (data != null) {
            products.addAll(data);
        }
        start();
    }

    private void start() {
        if (products != null && products.size() > 0) {
            createChildView();
        } else {
            removeAllViews();
        }
    }

    public void setmBubbleClickListener(View.OnClickListener onClickListener){
        this.mBubbleClickListener = onClickListener;
    }
}