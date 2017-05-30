package com.twowater.qqmsgnotify;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.twowater.qqmsgnotify.util.CommonUtil;
import com.twowater.qqmsgnotify.util.GeometryUtil;


/**
 * 为了能够到处拖拽,需要将该控件添加到windowManager中
 */
public class GooView extends View {

    private PointF mInitCenter;
    /**
     * 拖拽圆的中心点坐标
     **/
    private PointF mDragPoint;
    float dragRadius = 0;
    /**
     * 固定圆的中心点坐标
     **/
    private PointF mStickPoint;
    float stickRadius = 0;

    float stickMinRadius = 0;
    /**
     * 用于记录在拖拽过程中固定圆的临时半径
     **/
    float tempRadius = stickRadius;
    float maxDistnace = 0;
    String text = "";

    private Paint mPaint;
    private Paint mTextPaint;
    private ValueAnimator mAnim;
    /**
     * 记录是否超出指定范围
     **/
    private boolean isOutOfRange = false;
    /**
     * 记录控件是否消失
     **/
    private boolean isDisappear = false;

    private OnDisappearListener mListener;
    private Rect rect;
    private int mStatusBarHeight;
    private float resetDistance;

    public GooView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        rect = new Rect(0, 0, 50, 50);
        // 初始化拖拽圆和固定圆
        stickRadius = CommonUtil.dp2px(10.0f, context);
        dragRadius = CommonUtil.dp2px(10.0f, context);
        stickMinRadius = CommonUtil.dp2px(3.0f, context);
        maxDistnace = CommonUtil.dp2px(80.0f, context);
        resetDistance = CommonUtil.dp2px(40.0f, getContext());
        // 初始化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // 设置颜色
//        mPaint.setColor(Color.RED);
        mPaint.setColor(getResources().getColor(R.color.color_message));
        // 初始化控件上要显示的文本
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Align.CENTER);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(dragRadius * 1.2f);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (isAnimRunning())
            return false;
        // 如果此时没有执行回弹效果则正常处理TouchEvent
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        // 去除状态栏高度偏差
        canvas.translate(0, -mStatusBarHeight);
        if (!isDisappear) {
            if (!isOutOfRange) {
                // 画两圆连接处
                ShapeDrawable drawGooView = drawGooView();
                drawGooView.setBounds(rect);
                drawGooView.draw(canvas);
                // 画固定圆
                canvas.drawCircle(mStickPoint.x, mStickPoint.y, tempRadius, mPaint);
            }
            // 画拖拽圆
            canvas.drawCircle(mDragPoint.x, mDragPoint.y, dragRadius, mPaint);
            // 画数字
            float textY = mDragPoint.y + dragRadius / 2f;
            canvas.drawText(text, mDragPoint.x, textY, mTextPaint);
        }
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionMasked = MotionEventCompat.getActionMasked(event);
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                if (isAnimRunning()) {
                    return false;
                }
                isDisappear = false;
                isOutOfRange = false;
                updateDragCenter(event.getRawX(), event.getRawY());
                break;
            case MotionEvent.ACTION_MOVE:
                // 如果两圆间距大于最大距离farest，执行拖拽结束动画
                PointF p0 = new PointF(mDragPoint.x, mDragPoint.y);
                PointF p1 = new PointF(mStickPoint.x, mStickPoint.y);
                if (GeometryUtil.getDistanceBetween2Points(p0, p1) > maxDistnace) {
                    isOutOfRange = true;
                    updateDragCenter(event.getRawX(), event.getRawY());
                    return false;
                }
                updateDragCenter(event.getRawX(), event.getRawY());
                break;
            case MotionEvent.ACTION_UP:
                handleActionUp();
                break;
            default:
                isOutOfRange = false;
                break;
        }
        return true;
    }

    /**
     * 判断回弹动画是否还在执行
     **/
    private boolean isAnimRunning() {
        if (mAnim != null && mAnim.isRunning()) {
            return true;
        }
        return false;
    }

    /**
     * 让控件消失
     **/
    private void disappeared() {
        isDisappear = true;
        invalidate();
        if (mListener != null) {
            mListener.onDisappear(mDragPoint);
        }
    }

    /**
     * 处理手指抬起时的逻辑
     **/
    private void handleActionUp() {
        if (isOutOfRange) {
            float dis = GeometryUtil.getDistanceBetween2Points(mDragPoint, mInitCenter);
            if (dis < resetDistance) {
                // 在指定范围之内则还原
                if (mListener != null)
                    mListener.onReset(isOutOfRange);
                return;
            }
            // 如果手指抬起时在指定范围之外则消失
            disappeared();
        } else {
            // 如果还没断开 则回弹
            showGoovAnimation();
        }
    }

    /**
     * 展示粘性控件的回弹效果
     **/
    private void showGoovAnimation() {
        mAnim = ValueAnimator.ofFloat(1.0f);
        mAnim.setInterpolator(new OvershootInterpolator(4.0f));
        final PointF startPoint = new PointF(mDragPoint.x, mDragPoint.y);
        final PointF endPoint = new PointF(mStickPoint.x, mStickPoint.y);
        mAnim.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                PointF pointByPercent = GeometryUtil.getPointByPercent(startPoint,
                        endPoint, fraction);
                updateDragCenter((float) pointByPercent.x, (float) pointByPercent.y);
            }
        });
        mAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mListener != null)
                    mListener.onReset(isOutOfRange);
            }
        });
        if (GeometryUtil.getDistanceBetween2Points(startPoint, endPoint) < 10) {
            mAnim.setDuration(10);
        } else {
            mAnim.setDuration(500);
        }
        mAnim.start();
    }

    /**
     * 设置固定圆的半径
     **/
    public void setDargCircleRadius(float r) {
        dragRadius = r;
    }

    /**
     * 设置拖拽圆的半径
     **/
    public void setStickCircleRadius(float r) {
        stickRadius = r;
    }

    /**
     * 设置数字
     **/
    public void setNumber(int num) {
        text = String.valueOf(num);
    }

    /**
     * 初始化圆的圆心坐标
     **/
    public void initCenter(float x, float y) {
        mDragPoint = new PointF(x, y);
        mStickPoint = new PointF(x, y);
        mInitCenter = new PointF(x, y);
        invalidate();
    }

    /**
     * 更新拖拽圆的圆心坐标，重绘View
     **/
    private void updateDragCenter(float x, float y) {
        this.mDragPoint.x = x;
        this.mDragPoint.y = y;
        invalidate();
    }

    /**
     * 通过绘制Path构建一个ShapeDrawable，用来绘制到画布Canvas上
     **/
    private ShapeDrawable drawGooView() {
        Path path = new Path();
        // 1. 根据当前两圆圆心的距离计算出固定圆的半径
        float distance = GeometryUtil.getDistanceBetween2Points(mDragPoint, mStickPoint);
        tempRadius = getCurrentRadius(distance);
        // 2. 计算出经过两圆圆心连线的垂线的dragLineK（对边比临边）。求出四个交点坐标
        float xDiff = mStickPoint.x - mDragPoint.x;
        Double dragLineK = null;
        if (xDiff != 0) {
            dragLineK = (double) ((mStickPoint.y - mDragPoint.y) / xDiff);
        }
        // 分别获得经过两圆圆心连线的垂线与圆的交点（两条垂线平行，所以dragLineK相等）。
        PointF[] dragPoints = GeometryUtil.getIntersectionPoints(mDragPoint, dragRadius,
                dragLineK);
        PointF[] stickPoints = GeometryUtil.getIntersectionPoints(mStickPoint,
                tempRadius, dragLineK);
        // 3. 以两圆连线的0.618处作为 贝塞尔曲线 的控制点。（选一个中间点附近的控制点）
        PointF newPoint = GeometryUtil.getPointByPercent(mDragPoint, mStickPoint, 0.618f);
        // 绘制两圆连接
        path.moveTo(stickPoints[0].x, stickPoints[0].y);
        path.quadTo(newPoint.x, newPoint.y, dragPoints[0].x, dragPoints[0].y);
        path.lineTo(dragPoints[1].x, dragPoints[1].y);
        path.quadTo(newPoint.x, newPoint.y, stickPoints[1].x, stickPoints[1].y);
        path.close();
        // 将四个交点画到屏幕上
        // drawAssistPoint(path, dragPoints, stickPoints);
        // 构建ShapeDrawable
        ShapeDrawable shapeDrawable = new ShapeDrawable(new PathShape(path, 50f, 50f));
        shapeDrawable.getPaint().setColor(getResources().getColor(R.color.color_message));
        return shapeDrawable;
    }

    /**
     * 绘制辅助点
     **/
    @SuppressWarnings("unused")
    private void drawAssistPoint(Path path, PointF[] dragPoints, PointF[] stickPoints) {
        path.addCircle(dragPoints[0].x, dragPoints[0].y, 1, Direction.CW);
        path.addCircle(dragPoints[1].x, dragPoints[1].y, 1, Direction.CW);
        path.addCircle(stickPoints[0].x, stickPoints[0].y, 1, Direction.CW);
        path.addCircle(stickPoints[1].x, stickPoints[1].y, 1, Direction.CW);
    }

    /**
     * 根据距离获得当前固定圆的半径
     **/
    private float getCurrentRadius(float distance) {
        distance = Math.min(distance, maxDistnace);
        float fraction = 0.2f + 0.8f * distance / maxDistnace;
        float value = GeometryUtil.evaluateValue(fraction, stickRadius, stickMinRadius);
        return value;
    }

    public OnDisappearListener getOnDisappearListener() {
        return mListener;
    }

    public void setOnDisappearListener(OnDisappearListener mListener) {
        this.mListener = mListener;
    }

    public void setStatusBarHeight(int statusBarHeight) {
        this.mStatusBarHeight = statusBarHeight;
    }

    /**
     * 外部监听接口
     **/
    interface OnDisappearListener {
        /**
         * 当空间消失事调用
         **/
        void onDisappear(PointF mDragCenter);

        /**
         * 当控件被重新拖回原位置时调用
         **/
        void onReset(boolean isOutOfRange);
    }

}
