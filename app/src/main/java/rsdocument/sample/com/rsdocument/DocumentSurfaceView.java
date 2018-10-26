package rsdocument.sample.com.rsdocument;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class DocumentSurfaceView extends SurfaceView {

    public static final int DEFAULT_FRAME_WIDTH = 480;
    public static final int DEFAULT_FRAME_HEIGHT = 640;

    public boolean isDrawing;

    public ArrayList<PointF> pointFS;

    private Paint defaultPaint;

    private Canvas doubleBufferedCanvas;
    private Bitmap doubleBufferedBitmap;

    DrawingDocumentData drawingDocumentData;

    public DocumentSurfaceView(Context context) {
        super(context);
        init();
    }

    public DocumentSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DocumentSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DocumentSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    protected void init(){
        setWillNotDraw(false);

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.setFixedSize(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT);
        surfaceHolder.setKeepScreenOn(true);

        setDrawing(true);

        defaultPaint = new Paint();
    }

    /*protected int getResolutionWidth(){
        return 480;
    }

    protected int getResolutionHeight(){
        return 640;
    }

    public static int getGCD(int a, int b) {
        while (b != 0) {
            int temp = a % b;
            a = b;
            b = temp;
        }
        return Math.abs(a);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getResolutionWidth() != 0 && getResolutionHeight() != 0) {
            // 최초 뷰 생성시 입력된 비율로 화면의 비율을 맞춤
            // 최대 공약수를 구하고 화면 비율을 구함.
            int gcd = getGCD(getResolutionWidth(), getResolutionHeight());
            int widthRatio = getResolutionWidth() / gcd;
            int heightRatio = getResolutionHeight() / gcd;

            // 현재 뷰의 가로 세로 길이를 구함.
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);

            // 비율에 맞춰 가로 세로 길이를 조정.
            int newWidth;
            int newHeight;
            if ((float) width / (float) height > (float) getResolutionWidth() / (float) getResolutionHeight()) {
                //h를 기준으로 w를 조절해야함.
                newWidth = (int) ((float) height * (float) widthRatio / (float) heightRatio);
                newHeight = height;
            } else {
                //w를 기준으로 h를 조절해야함.
                newWidth = width;
                newHeight = (int) ((float) width * (float) heightRatio / (float) widthRatio);
            }

            // 새로운 가로 세로 Measure를 생성.
            int wMeasureSpec = MeasureSpec.makeMeasureSpec(newWidth, MeasureSpec.getMode(widthMeasureSpec));
            int hMeasureSpec = MeasureSpec.makeMeasureSpec(newHeight, MeasureSpec.getMode(heightMeasureSpec));

            super.onMeasure(wMeasureSpec, hMeasureSpec);

        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }*/

    public void setDrawing(boolean isDrawing){
        this.isDrawing = isDrawing;
    }

    private Paint bitmapPaint = new Paint();
    private Bitmap bitmapHolder;

    public void repaint() {
        repaint(bitmapHolder);
    }

    public void repaint(Bitmap bitmap) {
        if (bitmap == null) return;

        bitmapHolder = bitmap.copy(bitmap.getConfig(), true);
        Canvas canvas = null;
        SurfaceHolder holder = getHolder();
        try {
            canvas = holder.lockCanvas(null);
            if (canvas == null) return;

            synchronized (holder) {
                canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
            }
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    public void repaintDrawing(){
        Canvas canvas = null;
        SurfaceHolder holder = getHolder();
        try {
            canvas = holder.lockCanvas(null);
            if (canvas == null) return;

            synchronized (holder) {
                if(drawingDocumentData.getViewerDrawingFloats() != null) {
                    if(bitmapHolder != null){
                        canvas.drawBitmap(bitmapHolder, 0, 0, bitmapPaint);
                    }
                    canvas.drawLines(drawingDocumentData.getViewerDrawingFloats(), getClientPaint());
                }
            }
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

       /* clientFreeLineDrawingObject.setDrawingConverter(
                new DrawingConverter(new PointF(w, h), new PointF(getResolutionWidth(), getResolutionHeight())));*/

        drawingDocumentData = new DrawingDocumentData(new PointF(480, 640), new PointF(w, h));

        Log.d("LOG", "onSizeChanged: "+w + "," + h);

        doubleBufferedBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        doubleBufferedCanvas = new Canvas(doubleBufferedBitmap);
    }


    // 사용자 터치로 인한 그리기 처리
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isDrawing) {
            return super.onTouchEvent(event);
        }

        PointF point = new PointF(event.getX(), event.getY());

        //그리기
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDown(point.x, point.y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(point.x, point.y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
            default:
                break;
        }

        return true;
    }

    // 그리기
    private Path mPath = new Path();
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4f;

    private void touchDown(float x, float y) {

        // Path 객체 초기화 및 터치 좌표로 이동
        drawingDocumentData.clear();
        mPath.reset();
        mPath.moveTo(x, y);


        mX = x;
        mY = y;

        //doubleBufferedCanvas.drawPath(mPath, getClientPaint());
        // 뷰어에 전송할 정보 담기
        drawingDocumentData.addDrawingViewPoint(new PointF(x, y));

        // 그리기 시작 이벤트 발생
        Log.d("LOG","touchDown");
    }

    private void touchMove(float x, float y) {
        // 가중치에 맞춰 path 정보 담기
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        //if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.lineTo(x, y); // 점선 없어지게 하기 위해

            //mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;

           // doubleBufferedCanvas.drawPath(mPath, getClientPaint());
        drawingDocumentData.addDrawingViewPoint(new PointF(x, y));
        //}

        mPath.moveTo(x, y);

        //doubleBufferedCanvas.drawLines(drawingDocumentData.getViewerDrawingFloats(), getClientPaint());
        //pointFS.add(new PointF(mX, mY));
        Log.d("LOG","touchMove");
    }

    private void touchUp() {
        // 터치 마지막 지점까지 선 그리기
        mPath.lineTo(mX, mY);
        drawingDocumentData.addDrawingViewPoint(new PointF(mX, mY));
        // Path 객체 초기화
        //doubleBufferedCanvas.drawPath(mPath, getClientPaint());
        //repaintDrawing();
        mPath.reset();

        //doubleBufferedCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        //pointFS.add(new PointF(mX, mY));

        Log.d("LOG","touchUp");

        for(PointF pointF : drawingDocumentData.getViewerDrawingPoints()){
            Log.d("LOG",pointF.x+","+pointF.y);
        }

        //doubleBufferedCanvas.drawLines(drawingDocumentData.getViewerDrawingFloats(), getClientPaint());


        repaintDrawing();
    }

    private Paint getClientPaint() {
        Paint clientPaint = new Paint();
        clientPaint.setAntiAlias(true);
        clientPaint.setColor(Color.BLUE);
        clientPaint.setStrokeWidth(5);
        clientPaint.setStyle(Paint.Style.STROKE);
        clientPaint.setStrokeJoin(Paint.Join.ROUND);
        clientPaint.setStrokeCap(Paint.Cap.ROUND);
        return clientPaint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //if(drawingDocumentData.getViewerDrawingFloats() != null)
           // canvas.drawLines(drawingDocumentData.getViewerDrawingFloats(), 0, 0,getClientPaint());
        //canvas.drawBitmap(doubleBufferedBitmap, 0, 0, defaultPaint);
    }
}
