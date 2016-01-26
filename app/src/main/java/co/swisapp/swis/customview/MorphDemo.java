package co.swisapp.swis.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import co.swisapp.swis.R;

public class MorphDemo extends View {

    private Paint paint;

    private int radius;
    private int maxSize;
    private int minSize;

    public MorphDemo(Context context) {
        this(context, null);
    }

    public MorphDemo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MorphDemo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize();
    }

    private void initialize() {
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(getContext(), R.color.login_red));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        radius = 20;
        maxSize = 100;
        minSize = 20;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            canvas.drawRoundRect(getWidth()/2 - maxSize, getHeight()/2 - maxSize, getWidth()/2 + maxSize, getHeight()/2 + maxSize, radius, radius, paint);
//        }else {
//            canvas.drawCircle(getWidth() / 2, getHeight() / 2, 130, paint);
//        }

        if(radius < maxSize){
            canvas.drawPath(drawRoundedRect(getWidth()/2 - maxSize, getHeight()/2 - maxSize, getWidth()/2 + maxSize, getHeight()/2 + maxSize, radius, radius), paint);
        }else {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);
        }

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

    public void setRadius(int radius){
        if(radius < minSize){
            this.radius = minSize;
        } else if (radius > maxSize){
            this.radius = maxSize;
        } else {
            this.radius = radius;
        }
        invalidate();
    }
}
