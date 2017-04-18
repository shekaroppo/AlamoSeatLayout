package shekar.com.alamoseatlayout.zoom;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

public class CustomZoomableLayout extends LinearLayout implements OnTouchListener {
	
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	
	private int mode = NONE;
	
	private PointF mStartPoint = new PointF();
	private PointF mMiddlePoint = new PointF();	
	private float oldDist = 1f;
	private float matrixValues[] = {0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
	private float mScaleFactor = 1f;


	public CustomZoomableLayout(Context context) {
		super(context);
		this.setOnTouchListener(this);
	}

	public CustomZoomableLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOnTouchListener(this);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public CustomZoomableLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.setOnTouchListener(this);
	}
	
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
	    int count = getChildCount();
	    for(int i=0;i<count;i++){
	        View child = getChildAt(i); 
	        if(child.getVisibility()!=GONE){
	            LayoutParams params = (LayoutParams)child.getLayoutParams();
	            child.layout(
	                (int)(params.leftMargin * mScaleFactor), 
	                (int)(params.topMargin * mScaleFactor), 
	                (int)((params.leftMargin + child.getMeasuredWidth()) * mScaleFactor), 
	                (int)((params.topMargin + child.getMeasuredHeight()) * mScaleFactor));
	        }
	    }
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event){
		switch (event.getAction() & MotionEvent.ACTION_MASK){
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			if(oldDist > 10f){
				savedMatrix.set(matrix);
				midPoint(mMiddlePoint, event);
				mode = ZOOM;
			}
			break;
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			break;
		case MotionEvent.ACTION_MOVE:
			if(mode == ZOOM){
				float newDist = spacing(event);
				mScaleFactor = newDist/oldDist;
				invalidate();
				requestLayout();
				break;
			}
		}
		return true;
	}
	
	public float getScaleFactor(){
		return mScaleFactor;
	}
	
	/** Determine the space between the first two fingers **/
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		
		return (float)Math.sqrt(x*x + y*y);
	}
	
	/** Calculate the mid point of the first two fingers **/
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x/2, y/2);
	}
	
	/*
	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector){
			mScaleFactor *= detector.getScaleFactor();
			mScaleFactor = Math.max(0.6f, Math.min(mScaleFactor, 1.5f));
			invalidate();
			requestLayout();
			return true;
		}
	}
	*/

}
