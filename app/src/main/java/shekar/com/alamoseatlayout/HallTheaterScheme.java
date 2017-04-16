package shekar.com.alamoseatlayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextPaint;
import android.widget.ImageView;
import shekar.com.alamoseatlayout.drawing.DensityUtil;

public class HallTheaterScheme {
  private Context mContext;
  Paint backgroundPaint ;
  private Paint screenPaint;
  private Paint screenTextPaint;
  private String message;


  public HallTheaterScheme(ImageView imageView, int measuredWidth, int measuredHeight) {
    init(imageView.getContext());
    imageView.setImageBitmap(getImageBitmap(measuredWidth,measuredHeight));
  }

  private void init(Context context) {
    mContext=context;
    message = "THE SCREEN";
    backgroundPaint = new Paint();
    backgroundPaint.setStyle(Paint.Style.FILL);
    backgroundPaint.setColor(Color.BLACK);
    screenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    screenPaint.setStyle(Paint.Style.FILL);
    screenPaint.setColor(Color.WHITE);
    screenTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    screenTextPaint.setColor(Color.WHITE);
    screenTextPaint.setTextSize(DensityUtil.sip2px(mContext,14f));
    screenTextPaint.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/FuturaStd-Book.otf"));
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      screenTextPaint.setLetterSpacing(0.13f);
    }
  }

  public Bitmap getImageBitmap(int width, int height) {
    Bitmap tempBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    Canvas tempCanvas = new Canvas(tempBitmap);
    tempCanvas.drawPaint(backgroundPaint);
    float widthCenter=width/2;
    float screenWidth=width* 6/7;
    float screenHeight=DensityUtil.dip2px(mContext,10f);
    float left=widthCenter-screenWidth/2;
    float top = DensityUtil.dip2px(mContext,24f);
    RectF rect =new RectF (left,top,left+screenWidth,top+screenHeight);
    tempCanvas.drawRoundRect(rect, DensityUtil.dip2px(mContext,screenHeight), DensityUtil.dip2px(mContext,screenHeight), screenPaint);
    rect.top=top+screenHeight/2;
    tempCanvas.drawRect(rect, backgroundPaint);
    float textOffsetX=(screenTextPaint.measureText(message) * 0.5f);
    float textOffsetY=screenTextPaint.getFontMetrics().ascent*-0.8f +screenHeight;
    tempCanvas.drawText(message,rect.centerX()-textOffsetX,rect.centerY()+textOffsetY,screenTextPaint);
    return tempBitmap;
  }
}