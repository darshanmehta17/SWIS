package co.swisapp.swis.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import co.swisapp.swis.R;

public class RecordButton extends View {

    private static long SECONDS_IN_MILLIS = 1000;

    private Paint recordButtonPaint;
    private Paint outerRingPaint;
    private Paint percentRingPaint;

    private int radius;

    private boolean isRecording = false;

    private int ANIMATION_DURATION = 200;

    private int MAX_RADIUS_WHITE_RING = 60;
    private int STROKE_WIDTH_WHITE_RING = 4;

    private int MAX_RADIUS_RECORD_BUTTON = MAX_RADIUS_WHITE_RING - 3 * STROKE_WIDTH_WHITE_RING;
    private int MIN_RADIUS_RECORD_BUTTON = MAX_RADIUS_RECORD_BUTTON - 3 * STROKE_WIDTH_WHITE_RING;

    private int MIN_RADIUS_STOP_BUTTON = MIN_RADIUS_RECORD_BUTTON - 5 * STROKE_WIDTH_WHITE_RING;
    private int MIN_SIZE_STOP_BUTTON = MIN_RADIUS_RECORD_BUTTON;

    private long MAX_RECORDING_LENGTH = 20;

    private double currentRecordingLength = 0;

    private float density;
    private float height;
    private float width;

    private CountDownTimer countDownTimer;

    private OnStartRecordListener onStartRecordListener;
    private OnStopRecordListener onStopRecordListener;

    public RecordButton(Context context) {
        this(context, null);
    }

    public RecordButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize();
    }

    private void initialize() {

        density = getContext().getResources().getDisplayMetrics().density;

        STROKE_WIDTH_WHITE_RING *= density;
        MAX_RADIUS_WHITE_RING *= density;

        MAX_RADIUS_RECORD_BUTTON *= density;
        MIN_RADIUS_RECORD_BUTTON *= density;

        MIN_RADIUS_STOP_BUTTON *= density;
        MIN_SIZE_STOP_BUTTON *= density;
        MIN_SIZE_STOP_BUTTON -= 4 * density;

        recordButtonPaint = new Paint();
        recordButtonPaint.setColor(ContextCompat.getColor(getContext(), R.color.login_red));
        recordButtonPaint.setAntiAlias(true);
        recordButtonPaint.setStyle(Paint.Style.FILL);

        outerRingPaint = new Paint();
        outerRingPaint.setColor(ContextCompat.getColor(getContext(), R.color.record_button_outer_ring_grey));
        outerRingPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        outerRingPaint.setStrokeWidth(STROKE_WIDTH_WHITE_RING);
        outerRingPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        percentRingPaint = new Paint();
        percentRingPaint.setColor(ContextCompat.getColor(getContext(), R.color.login_red));
        percentRingPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        percentRingPaint.setStrokeWidth(STROKE_WIDTH_WHITE_RING);
        percentRingPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        radius = MAX_RADIUS_RECORD_BUTTON;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        width = getWidth();
        height = getHeight();

        canvas.drawPath(getOuterRingPath(), outerRingPaint);

        if(isRecording){
            canvas.drawPath(getPercentageRingPath(), percentRingPaint);
        }

        drawRecordButton(canvas);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            canvas.drawRoundRect(getWidth()/2 - maxSize, getHeight()/2 - maxSize, getWidth()/2 + maxSize, getHeight()/2 + maxSize, radius, radius, recordButtonPaint);
//        }else {
//            canvas.drawCircle(getWidth() / 2, getHeight() / 2, 130, recordButtonPaint);
//        }

//        if(radius < maxSize){
//            canvas.drawPath(drawRoundedRect(getWidth()/2 - maxSize, getHeight()/2 - maxSize, getWidth()/2 + maxSize, getHeight()/2 + maxSize, radius, radius), recordButtonPaint);
//        }else {
//            canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, recordButtonPaint);
//        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        if (width > height) {
            size = height;
        } else {
            size = width;
        }

        setMeasuredDimension(size, size);
    }

    private void drawRecordButton(Canvas canvas) {
        if(isRecording){
            canvas.drawPath(drawRoundedRect(width / 2 - MIN_SIZE_STOP_BUTTON, height / 2 - MIN_SIZE_STOP_BUTTON, width / 2 + MIN_SIZE_STOP_BUTTON, height / 2 + MIN_SIZE_STOP_BUTTON, radius, radius), recordButtonPaint);
        } else {
            canvas.drawCircle(width / 2, height / 2, radius, recordButtonPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP:
                if(isRecording){
                    stopRecording();
                }else {
                    startRecording();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void startRecording() {

        currentRecordingLength = 0;

        radius = MAX_RADIUS_RECORD_BUTTON;

        Animation circleReduceAnimation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                radius = MIN_RADIUS_RECORD_BUTTON + (int) ((MAX_RADIUS_RECORD_BUTTON  - MIN_RADIUS_RECORD_BUTTON) * (1 - interpolatedTime));
                invalidate();
            }
        };

        final Animation morphAnimation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                radius = MIN_RADIUS_STOP_BUTTON + (int) ((MIN_RADIUS_RECORD_BUTTON - MIN_RADIUS_STOP_BUTTON) * ( 1 - interpolatedTime));
                invalidate();
            }
        };

        circleReduceAnimation.setDuration(ANIMATION_DURATION);
        circleReduceAnimation.setInterpolator(getContext(), android.R.interpolator.anticipate);

        morphAnimation.setDuration(ANIMATION_DURATION);
        morphAnimation.setInterpolator(getContext(), android.R.interpolator.decelerate_cubic);

        circleReduceAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isRecording = true;
                startAnimation(morphAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        morphAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(onStartRecordListener != null){
                    onStartRecordListener.onStartRecord();
                }

                startTimer();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        startAnimation(circleReduceAnimation);
    }

    private void stopRecording() {
        radius = MIN_RADIUS_STOP_BUTTON;

        if (onStopRecordListener != null){
            onStopRecordListener.onStopRecord();
        }

        stopTimer();

        Animation reverseMorphAnimation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                radius = MIN_RADIUS_RECORD_BUTTON - (int) ((MIN_RADIUS_RECORD_BUTTON - MIN_RADIUS_STOP_BUTTON) * ( 1 - interpolatedTime));
                invalidate();
            }
        };

        final Animation circleEnlargeAnimation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                radius = MAX_RADIUS_RECORD_BUTTON - (int) ((MAX_RADIUS_RECORD_BUTTON  - MIN_RADIUS_RECORD_BUTTON) * (1 - interpolatedTime));
                invalidate();
            }
        };

        reverseMorphAnimation.setDuration(ANIMATION_DURATION);
        reverseMorphAnimation.setInterpolator(getContext(), android.R.interpolator.accelerate_cubic);

        circleEnlargeAnimation.setDuration(ANIMATION_DURATION);
        circleEnlargeAnimation.setInterpolator(getContext(), android.R.interpolator.anticipate_overshoot);

        reverseMorphAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isRecording = false;
                startAnimation(circleEnlargeAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        startAnimation(reverseMorphAnimation);

    }

    private void startTimer() {

        stopTimer();

        countDownTimer = new CountDownTimer(MAX_RECORDING_LENGTH * SECONDS_IN_MILLIS, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                currentRecordingLength = ((MAX_RECORDING_LENGTH * SECONDS_IN_MILLIS * 1.0) - millisUntilFinished ) / SECONDS_IN_MILLIS;
                invalidate();
            }

            @Override
            public void onFinish() {
                currentRecordingLength = 0;
                invalidate();
                // TODO: 04/02/16 call interface to stop recording
                stopRecording();
            }
        }.start();
    }

    private void stopTimer() {
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
    }

    private Path getPercentageRingPath() {

        float minX = Math.min(width / 2, MAX_RADIUS_WHITE_RING - STROKE_WIDTH_WHITE_RING);
        float minY = Math.min(height / 2, MAX_RADIUS_WHITE_RING - STROKE_WIDTH_WHITE_RING);

        float radius = Math.min(minX, minY);

        float centerX = width / 2;
        float centerY = height / 2;

        double percent = ( currentRecordingLength / MAX_RECORDING_LENGTH ) * 360;

        Path mPath = new Path();
        RectF rectF = new RectF();
        rectF.set(centerX - radius, centerY - radius,
                centerX + radius, centerY + radius);

        for (int i = 0; i < percent; i++)
        {
            mPath.addArc(rectF, i - 90, 1);
        }

        return mPath;
    }

    private Path getOuterRingPath() {
        float minX = Math.min(width / 2, MAX_RADIUS_WHITE_RING - STROKE_WIDTH_WHITE_RING);
        float minY = Math.min(height / 2, MAX_RADIUS_WHITE_RING - STROKE_WIDTH_WHITE_RING);

        float radius = Math.min(minX, minY);

        float centerX = width / 2;
        float centerY = height / 2;

        Path mPath = new Path();
        RectF rectF = new RectF();
        rectF.set(centerX - radius, centerY - radius,
                centerX + radius, centerY + radius);
        for (int i = 0; i <= 360; i++)
        {
            mPath.addArc(rectF, i, 1);
        }

        return mPath;
    }

    public Path drawRoundedRect(float left, float top, float right, float bottom, float rx, float ry) {
        Path path = new Path();
        if (rx < 0) rx = 0;
        if (ry < 0) ry = 0;
        float width = right - left;
        float height = bottom - top;
        if (rx > width/2) rx = width/2;
        if (ry > height/2) ry = height/2;
        float widthMinusCorners = (width - (2 * rx));
        float heightMinusCorners = (height - (2 * ry));

        path.moveTo(right, top + ry);
        path.rQuadTo(0, -ry, -rx, -ry);//top-right corner
        path.rLineTo(-widthMinusCorners, 0);
        path.rQuadTo(-rx, 0, -rx, ry); //top-left corner
        path.rLineTo(0, heightMinusCorners);

        path.rQuadTo(0, ry, rx, ry);//bottom-left corner
        path.rLineTo(widthMinusCorners, 0);
        path.rQuadTo(rx, 0, rx, -ry); //bottom-right corner

        path.rLineTo(0, -heightMinusCorners);

        path.close();//Given close, last lineto can be removed.

        return path;
    }

    public void setOnStartRecordListener(OnStartRecordListener onStartRecordListener) {
        this.onStartRecordListener = onStartRecordListener;
    }

    public void setOnStopRecordListener(OnStopRecordListener onStopRecordListener) {
        this.onStopRecordListener = onStopRecordListener;
    }

    public interface OnStartRecordListener {
        void onStartRecord();
    }

    public interface OnStopRecordListener {
        void onStopRecord();
    }

}
