package rsdocument.sample.com.rsdocument;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.FrameLayout;

public class DocumentLayout extends FrameLayout {

    DocumentSurfaceView documentSurfaceView;
    DocumentOverlayView documentOverlayView;

    public DocumentLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public DocumentLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public DocumentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DocumentLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // XML 문서 인플레이션한 후 종료
        documentSurfaceView = findViewById(R.id.document_surface_view);
        documentOverlayView = findViewById(R.id.document_overlay_view);
        //addView(documentSurfaceView);
        //documentOverlayView.bringToFront();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 하위 뷰들에 대해 크기 위치를 할당 할 때 호출
        documentSurfaceView.getHolder().addCallback(documentSurfaceHolderCallback);
    }

    public DocumentSurfaceView getDocumentSurfaceView(){
        return documentSurfaceView;
    }

    private final SurfaceHolder.Callback documentSurfaceHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.d("LOG","surfaceCreated");
            documentSurfaceView.repaint();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.d("LOG","surfaceChanged : "+width+", "+height);
            documentSurfaceView.repaint();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.d("LOG","surfaceDestroyed");
        }
    };


}
