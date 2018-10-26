package rsdocument.sample.com.rsdocument;

import android.graphics.Point;
import android.graphics.PointF;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.List;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class DrawingDocumentData {

    @Retention(SOURCE)
    @IntDef({VIEWER, HOST})
    public @interface DRAWING_DOCUMENT_TYPE {}
    public static final int VIEWER = 0;
    public static final int HOST = 1;

    protected List<PointF> hostViewPoints = new ArrayList<>();

    private PointF viewerSize; // 뷰어에서 전송되는 이미지 사이즈 (surface view size 를 동일하게 세팅)
    private PointF hostSize; // 기기에 맞춰진 view의 사이즈

    public DrawingDocumentData() {
        this.viewerSize = new PointF();
        this.hostSize = new PointF();
    }

    public DrawingDocumentData(PointF viewerSize, PointF drawingViewSize){
        this.viewerSize = viewerSize;
        this.hostSize = drawingViewSize;
    }

    public void addDrawingViewPoint(PointF point){
        hostViewPoints.add(point);
    }

    public PointF getViewerSize() {
        return viewerSize;
    }

    public void setViewerSize(PointF viewerSize) {
        this.viewerSize = viewerSize;
    }

    public PointF getHostSize() {
        return hostSize;
    }

    public void setHostSize(PointF hostSize) {
        this.hostSize = hostSize;
    }

    public float getRatio(@DRAWING_DOCUMENT_TYPE int type){
        if(type == VIEWER){
            return viewerSize.x / viewerSize.y;
        }else{
            return hostSize.x / hostSize.y;
        }
    }

    public List<PointF> getViewerDrawingPoints(){
        List<PointF> pointF = new ArrayList<>();
        for(PointF point : hostViewPoints){
            float vRatio = getRatio(VIEWER);
            float hRatio = getRatio(HOST);
            pointF.add(new PointF(
                    (point.x * getRatio(VIEWER)) / getRatio(HOST),
                    (point.y * getRatio(VIEWER)) / getRatio(HOST)));
        }

        return pointF;
    }

    public float[] getViewerDrawingFloats(){
        if(hostViewPoints.size() == 0){
            return null;
        }

        float[] points = new float[hostViewPoints.size() * 4];
        int n = 0;

        for(PointF point : hostViewPoints){
            points[n++] = (point.x * viewerSize.x) / hostSize.x;
            points[n++] = (point.y * viewerSize.y) / hostSize.y;
            points[n++] = ((point.x + 5) *  viewerSize.x) / hostSize.x;
            points[n++] = ((point.y + 5) *  viewerSize.y) / hostSize.y;
        }

        return points;
    }

    public List<PointF> getHostViewPoints(){
        return hostViewPoints;
    }

    public void clear(){
        hostViewPoints.clear();
    }

    public int size(){
        return hostViewPoints.size();
    }
}
