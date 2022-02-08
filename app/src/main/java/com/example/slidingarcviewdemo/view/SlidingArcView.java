package com.example.slidingarcviewdemo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.ViewPager;

import com.example.slidingarcviewdemo.R;
import com.example.slidingarcviewdemo.utils.ArcViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 小訾
 * @projectName SlidingArcViewDemo
 * @packageName com.example.slidingarcviewdemo.view
 * @description: 弧形轮盘选择 参考 https://github.com/quietUncle/androidDemo
 * @date :2022/2/7 9:35
 */
public class SlidingArcView extends ViewGroup {
    private List<SignView> views = new ArrayList<>();

    private List<String> titles = new ArrayList<>();

    private Paint bgPaint;
    private Paint mArrowPaint;
    private Paint mTextPaint;
    private Paint linePaint;
    private int mTextSize = 30;
    private Canvas canvas;
    private int DEFAULT_SHOW_SIZE = 5;

    private int strokeWidth = 80;
    private boolean executeListener = true;
    private int mActDataSize = 1;

    public SlidingArcView(Context context) {
        this(context, null);
    }

    public SlidingArcView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingArcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SlidingArcView, defStyleAttr, 0);
        strokeWidth = (int) a.getDimension(R.styleable.SlidingArcView_arcWidth, TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 26,
                        getResources().getDisplayMetrics()));
        a.recycle();
        init();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    private void init() {
        ArcViewUtils.initScreen(getContext());

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.parseColor("#4D68F88A"));
        linePaint.setTextSize(50f);
        linePaint.setStrokeWidth(5);

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setAntiAlias(true);
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setColor(Color.parseColor("#1A68F88A"));
        bgPaint.setStrokeWidth(strokeWidth);

        mArrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArrowPaint.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#68F88A"), PorterDuff.Mode.SRC_IN));

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.parseColor("#68F88A"));
        mTextPaint.setTextSize(mTextSize);

        CentX = ArcViewUtils.getScreenW() / 2;
        CentY = ArcViewUtils.getScreenH() / 2 + 50;

        RADIUS = ArcViewUtils.getScreenW() / 2 + 400;
        for (int i = 0, len = titles.size(); i < len; i++) {
            View v = new View(getContext());
            SignView signView = new SignView(v, i, i % mActDataSize);
            views.add(signView);
            this.addView(v);
        }
        setBackgroundColor(getResources().getColor(android.R.color.transparent));
        this.setClickable(true);
    }

    /**
     * 设置数据
     *
     * @param data     数据列表
     * @param dataSize
     */
    public void setData(List<String> data, int dataSize) {
        if (data == null || data.size() == 0) {
            return;
        }
        mActDataSize = dataSize;
        titles.clear();
        titles.addAll(data);
        while (titles.size() < DEFAULT_SHOW_SIZE) {
            for (String datum : data) {
                if (titles.size() < DEFAULT_SHOW_SIZE) {
                    titles.add(datum);
                }
            }
        }

        init();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        drawBg(canvas);
        // 绘制文本
        for (SignView view : views) {
            view.flush();
        }
        // 绘制分割线
        if (views.size() > 1) {
            for (int i = 0; i < views.size(); i++) {
                int width = ArcViewUtils.getScreenW() / DEFAULT_SHOW_SIZE;
                int lineX = width * (i + 1);
                int lineY = getCentYByX(lineX);
                double lineAngle = ArcViewUtils.getAngleByPoint(new Point(CentX, CentY), new Point(lineX, lineY));
                int lineHeightHalf = (strokeWidth - 30) / 2;
                Point startPoint = ArcViewUtils.getPointByAngle(RADIUS - lineHeightHalf, (int) lineAngle, new Point(CentX, CentY));
                Point endPoint = ArcViewUtils.getPointByAngle(RADIUS + lineHeightHalf, (int) lineAngle, new Point(CentX, CentY));
                canvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, linePaint);
            }
        }
    }

    private void drawBg(Canvas canvas) {
        // 绘制圆环
        RectF rect = new RectF(CentX - RADIUS, CentY - RADIUS,
                CentX + RADIUS, CentY + RADIUS);
        canvas.drawArc(rect, -220, 220, false, bgPaint);
        // 绘制 上下两个箭头
        @SuppressLint("DrawAllocation")
        Bitmap bitmap1 = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.ic_home_arc_down);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.ic_home_arc_up);
        canvas.drawBitmap(bitmap1, CentX - bitmap1.getWidth() / 2, CentY - RADIUS - strokeWidth / 2, mArrowPaint);
        canvas.drawBitmap(bitmap2, CentX - bitmap2.getWidth() / 2,
                CentY - RADIUS + strokeWidth/2 - bitmap2.getHeight(), mArrowPaint);
    }

    boolean canScroll = true;
    int lastX;
    int downPointId;
    int downX;
    int downY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isAnimated) {//正在运行动画中
            return super.onTouchEvent(event);
        }
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();//获得VelocityTracker类实例
        }
        mVelocityTracker.addMovement(event);//将事件加入到VelocityTracker类实例中
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (canScroll) {
                    flushViews((int) (event.getX() - lastX));
                    lastX = (int) event.getX();
                    invalidate();
                }
                return true;
            case MotionEvent.ACTION_UP:
                //先判断是否是点击事件
                final int pi = event.findPointerIndex(downPointId);
                if (isClickable() && (Math.abs(event.getX(pi) - downX) <= 3) || Math.abs(event.getY(pi) - downY) <= 3) {
                    if (isFocusable() && isFocusableInTouchMode() && !isFocused())
                        requestFocus();
                    performViewClick();
                    return true;
                }
                //判断当ev事件是MotionEvent.ACTION_UP时：计算速率
                final VelocityTracker velocityTracker = mVelocityTracker;
                // 1000 provides pixels per second
                velocityTracker.computeCurrentVelocity(1, (float) 0.01);
                velocityTracker.computeCurrentVelocity(1000);//设置units的值为1000，意思为一秒时间内运动了多少个像素  
                if (velocityTracker.getXVelocity() > 2000 || velocityTracker.getXVelocity() < -2000) {//自动滚动最低要求
                    autoTime = (int) (velocityTracker.getXVelocity() / 1000 * 200);
                    autoTime = autoTime > 1500 ? 1500 : autoTime;
                    autoTime = autoTime < -1500 ? -1500 : autoTime;
                    isAnimated = true;
                    executeListener = true;
                    handler.sendEmptyMessageDelayed(1, 10);
                } else {
                    isAnimated = false;
                    resetView();
                }
                return true;
            case MotionEvent.ACTION_DOWN:
                downPointId = event.getPointerId(0);
                downX = lastX = (int) event.getX();
                downY = (int) event.getY();
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;

            case MotionEvent.ACTION_MOVE:
                // 针对不同的滑动冲突，只需要修改这个条件即可，其它均不需做修改并且也不能修改
                getParent().requestDisallowInterceptTouchEvent(false);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;

            default:
        }
        return super.dispatchTouchEvent(ev);
    }

    private void performViewClick() {
        for (SignView signView : views) {
            Rect r = new Rect(signView.centX - signView.size / 2, signView.centY - signView.size / 2 - viewTopChange, signView.centX + signView.size / 2, signView.centY + signView.size / 2 - viewTopChange);
            if (r.contains(downX, downY)) {
                if (qtItemClickListener != null && !isAnimated) {
                    isClick = true;
                    chooseView = signView;
                    autoScrollX = ArcViewUtils.getScreenW() / 2 - signView.centX;
                    executeListener = true;
                    handler.sendEmptyMessageDelayed(0, 10);
                }
            }
        }
    }

    private void flushViews(int scrollX) {
        for (SignView view : views) {
            view.scroll(scrollX);
        }
    }


    //停止滚动，归位
    public void resetView() {
        for (SignView view : views) {
            if (view.centX > CentX && (view.centX - CentX < view.width)) {//屏幕右半部分移动运动，变小
                int dis = view.centX - CentX;
                if (dis > view.width / 2) {
                    autoScrollX = view.width - dis;
                } else {
                    autoScrollX = dis * -1;
                }
                break;
            }

        }
        executeListener = true;
        handler.sendEmptyMessageDelayed(0, 10);
    }

    int veSpeed = 0;//松开自动滚动速度
    int autoTime = 0;//送开自动滚动
    int autoScrollX = 0;//归位滚动

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    if (autoScrollX != 0) {
                        if (Math.abs(autoScrollX) > SPEED) {
                            SPEED = Math.abs(SPEED);
                            if (autoScrollX > 0) {
                                autoScrollX -= SPEED;
                            } else {
                                autoScrollX += SPEED;
                                SPEED = SPEED * -1;
                            }
                            for (SignView view : views) {
                                view.scroll(SPEED);
                            }
                        } else {
                            for (SignView view : views) {
                                view.scroll(autoScrollX);
                            }
                            autoScrollX = 0;
                            isAnimated = false;
                            if (chooseView != null && qtScrollListener != null && lastChooseView != chooseView) {
                                if (!isClick) {
                                    if (executeListener) {
                                        qtScrollListener.onSelect(chooseView.view, chooseView.actIndex);
                                    }
                                    lastChooseView = chooseView;
                                } else {
                                    if (executeListener) {
                                        qtItemClickListener.onClick(chooseView.view, chooseView.actIndex);
                                    }
                                    lastChooseView = chooseView;
                                    isClick = false;
                                }
                            }
                        }
                        invalidate();
                        handler.sendEmptyMessageDelayed(0, 10);
                    }
                    break;
                case 1:
                    if (autoTime > 0) {
                        if (autoTime > 1500) {
                            veSpeed = 80;
                        } else if (autoTime > 1000) {
                            veSpeed = 80;
                        } else if (autoTime > 500) {
                            veSpeed = 40;
                        } else if (autoTime > 200) {
                            veSpeed = 20;
                        } else {
                            veSpeed = 10;
                        }
                        for (SignView view : views) {
                            view.scroll(veSpeed);
                        }
                        autoTime -= 20;
                        if (autoTime < 0) {
                            isAnimated = false;
                            autoTime = 0;
                        }
                        invalidate();
                        handler.sendEmptyMessageDelayed(1, 20);
                    } else if (autoTime < 0) {
                        if (autoTime < -1500) {
                            veSpeed = -80;
                        } else if (autoTime < -1000) {
                            veSpeed = -60;
                        } else if (autoTime < -500) {
                            veSpeed = -40;
                        } else if (autoTime < -200) {
                            veSpeed = -20;
                        } else {
                            veSpeed = -10;
                        }
                        for (SignView view : views) {
                            view.scroll(veSpeed);
                        }
                        autoTime += 20;
                        if (autoTime > 0) {
                            isAnimated = false;
                            autoTime = 0;
                        }
                        invalidate();
                        handler.sendEmptyMessageDelayed(1, 20);
                    } else {
                        resetView();
                        invalidate();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * listener
     */
    QTScrollListener qtScrollListener;
    QTItemClickListener qtItemClickListener;

    public interface QTScrollListener {
        void onSelect(View v, int index);
    }

    public interface QTItemClickListener {
        void onClick(View v, int index);
    }

    public void setQtScrollListener(QTScrollListener qtScrollListener) {
        this.qtScrollListener = qtScrollListener;
    }

    public void setQtItemClickListener(QTItemClickListener qtItemClickListener) {
        this.qtItemClickListener = qtItemClickListener;
    }

    private boolean isAnimated = false;//是否正在动画中
    private int viewTopChange = ArcViewUtils.dp2px(0f);//view往上偏移的位置
    private VelocityTracker mVelocityTracker;//速度跟踪
    private int SPEED = 30;//归位自动滚动速度
    private SignView leftView;//屏幕最左边的viwe
    private SignView rightView;//屏幕最右边的view
    private int CentX;//外层圆中心x
    private int CentY;//外层圆中心Y
    private int RADIUS;//外层圆半径
    private static final int VX = 50;//第一个view的x
    private SignView chooseView;
    private SignView lastChooseView;
    private boolean isClick = false;

    private class SignView {
        private View view;
        private String title;
        private int centX;// view的中心点坐标
        private int centY;
        private int index;
        private int actIndex;
        private int size = 120;// view大小
        private final int width = (ArcViewUtils.getScreenW()) / DEFAULT_SHOW_SIZE;
        private float normalScale = 1.0f;
        private float maxScale = 0.2f;
        private boolean stop;//停止滚动，用来判断是否自动进行归位
        private boolean isChoose = false;

        public SignView(View v, final int index, final int actIndex) {
            this.actIndex = actIndex;
            this.index = index;
            this.view = v;
            this.view.setBackgroundResource(R.mipmap.seal_ic_mine_language);
            this.view.setVisibility(View.INVISIBLE);
            this.title = titles.get(index);
            if (index == 0) {
                leftView = this;
            }
            if (index == titles.size() - 1) {
                rightView = this;
            }
            if (index == 2) {
                isChoose = true;
                chooseView = this;
            }
            initView();
        }

        //计算view的坐标
        private void initView() {
            centX = (width) / 2 + width * index;
            centY = getCentYByX(centX);
        }

        public void scroll(int scrollX) {
            this.centX += scrollX;
            centY = getCentYByX(centX);
        }

        public int getCentX() {
            return centX;
        }

        public int getCentY() {
            return centY;
        }

        public View getView() {
            return view;
        }

        public void flush() {
            clean();
            // 每次计算view的位置
            RectF rectF = new RectF(centX - size / 2, centY - size / 2 - viewTopChange, centX + size / 2, centY + size / 2 - viewTopChange);
            view.layout((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);

            // 以是否靠近中心点 来判断是否变大变小
            if (centX >= CentX && centX - CentX <= width) {// 屏幕右半部分移动运动，变小
                float scale = (float) (centX - width - width / 2) / (float) width - 1.0f;
                view.setScaleX((normalScale + maxScale - maxScale * scale));
                view.setScaleY((normalScale + maxScale - maxScale * scale));
                if (scale >= 0.5) {
                    isChoose = false;
                }
            } else if (centX <= CentX && CentX - centX <= width) {//屏幕左半部分移动运动，变大
                float scale = (float) (centX - width - width / 2) / (float) width;
                view.setScaleX((normalScale + maxScale * scale));
                view.setScaleY((normalScale + maxScale * scale));
                if (scale >= 0.5) {
                    isChoose = true;
                }
            } else {
                isChoose = false;
                view.setScaleX(normalScale);
                view.setScaleY(normalScale);
            }
            if (isChoose) {
                chooseView = this;
            }

            // 设置字体透明度
            int duration = Math.abs(chooseView.index - index);
            if (duration > 2) {
                duration = 2;
            }
            double textAlpha = 1 - duration * 0.3;

            RectF textRectF = new RectF(CentX - RADIUS, CentY - RADIUS,
                    CentX + RADIUS, CentY + RADIUS);
            // 文本的坐标
            int sweepAngle = 20;
            double angle = ArcViewUtils.getAngleByPoint(new Point(CentX, CentY), new Point(centX, centY));
            double finalAngle = angle - 5;

            Path path = new Path();
            path.addArc(textRectF, (float) finalAngle, sweepAngle);
            float textLength = mTextPaint.measureText(title);//获得字体长度
            float hOffset = (float) (RADIUS * Math.PI * (sweepAngle * 1.0 / 360) / 2 - textLength / 2);
            mTextPaint.setAlpha((int) (textAlpha * 255));
            canvas.drawTextOnPath(title, path, hOffset, mTextSize / 3, mTextPaint);
        }


        //无限循环的判断
        private void clean() {
            if (leftView.notLeftView()) {//最左边没有view了，把最右边的移到最左边
                rightView.centX = leftView.centX - width;
                rightView.changeY();
                leftView = rightView;
                rightView = views.get(rightView.index == 0 ? views.size() - 1 : rightView.index - 1);
            }
            if (rightView.notRightView()) {//最右边没有view了，把最左边的移到最右边
                leftView.centX = rightView.centX + width;
                leftView.changeY();
                rightView = leftView;
                leftView = views.get(leftView.index == views.size() - 1 ? 0 : leftView.index + 1);
            }
        }

        //重新计算Y点坐标
        public void changeY() {
            centY = CentY + (int) Math.sqrt(Math.pow(RADIUS, 2) - Math.pow((centX - CentX), 2));
        }

        public boolean notLeftView() {
            return centX - width / 2 > width / 2;
        }

        public boolean notRightView() {
            return centX + width / 2 + width / 2 < ArcViewUtils.getScreenW();
        }
    }

    public int getCentYByX(int centX) {
        return CentY - ((int) Math.sqrt(Math.pow(RADIUS, 2) - Math.pow((centX - CentX), 2)));
    }

    private ViewPager viewPager;

    public void setUpWithViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        setQtItemClickListener((v, index) -> {
            viewPager.setCurrentItem(index);
        });
        setQtScrollListener((v, index) -> {
            viewPager.setCurrentItem(index);
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position >= 0 && position < getChildCount()) {
                    getChildAt(position).performClick();
                }
                for (SignView signView : views) {
                    if (signView.index == position) {
                        chooseView = signView;
                        autoScrollX = ArcViewUtils.getScreenW() / 2 - signView.centX;
                        executeListener = false;
                        handler.sendEmptyMessageDelayed(0, 10);
                        break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        for (SignView signView : views) {
            if (signView.index == 0) {
                chooseView = signView;
                autoScrollX = ArcViewUtils.getScreenW() / 2 - signView.centX;
                executeListener = false;
                handler.sendEmptyMessageDelayed(0, 10);
                break;
            }
        }
    }
}