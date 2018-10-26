package rsdocument.sample.com.rsdocument;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class DocumentOverlayView extends FrameLayout {

    ImageView draw_icon;
    ImageView eraser_icon;
    ImageView close_icon;

    public DocumentOverlayView(Context context) {
        super(context);
        initView();
    }

    public DocumentOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DocumentOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DocumentOverlayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView(){
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.view_doc_overlay, this, false);
        addView(v);

        draw_icon = v.findViewById(R.id.draw_icon);
        eraser_icon = v.findViewById(R.id.eraser_icon);
        close_icon = v.findViewById(R.id.close_icon);

        draw_icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        eraser_icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        close_icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

}
