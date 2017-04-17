package shekar.com.alamoseatlayout.seatlayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextPaint;
import android.widget.ImageView;
import shekar.com.alamoseatlayout.drawing.DensityUtil;

public class HallTheaterScheme {
  Paint backgroundPaint;
  private Context mContext;
  private Paint screenPaint;
  private Paint testPaint;
  private Paint screenTextPaint;
  private String message;

  public HallTheaterScheme(Seat[][] seats, ImageView imageView, int measuredWidth, int measuredHeight) {
    init(imageView.getContext());
    imageView.setImageBitmap(getImageBitmap(seats, measuredWidth, measuredHeight));
  }

  private void init(Context context) {

    mContext = context;
    message = "THE SCREEN";
    backgroundPaint = new Paint();
    backgroundPaint.setStyle(Paint.Style.FILL);
    backgroundPaint.setColor(Color.BLACK);
    screenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    screenPaint.setStyle(Paint.Style.FILL);
    screenPaint.setColor(Color.WHITE);
    testPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    testPaint.setStyle(Paint.Style.FILL);
    testPaint.setColor(Color.GREEN);
    screenTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    screenTextPaint.setColor(Color.WHITE);
    screenTextPaint.setTextSize(DensityUtil.sip2px(mContext, 14f));
    screenTextPaint.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/FuturaStd-Book.otf"));
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      screenTextPaint.setLetterSpacing(0.13f);
    }
  }

  public Bitmap getImageBitmap(Seat[][] seats, int width, int height) {

    int rows = seats.length;
    int columns = seats[0].length;
    int seatWidth = 30;
    int seatGap = 1;
    int offset = (int) DensityUtil.dip2px(mContext, 8);
    int left,right,top,bottom;
    int bitmapHeight = rows * (seatWidth + seatGap) - seatGap + offset ;
    int bitmapWidth = columns * (seatWidth + seatGap) - seatGap + offset ;
    Bitmap tempBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
    Canvas tempCanvas = new Canvas(tempBitmap);
    tempCanvas.drawPaint(backgroundPaint);
    ////Drawing Screen
    //float widthCenter = width / 2;
    //float screenWidth = width * 6 / 7;
    //float screenHeight = DensityUtil.dip2px(mContext, 10f);
    //float left = widthCenter - screenWidth / 2;
    //float top = DensityUtil.dip2px(mContext, 24f);
    //RectF screenRect = new RectF(left, top, left + screenWidth, top + screenHeight);
    //tempCanvas.drawRoundRect(screenRect, DensityUtil.dip2px(mContext, screenHeight), DensityUtil.dip2px(mContext, screenHeight), screenPaint);
    //screenRect.top = top + screenHeight / 2;
    //tempCanvas.drawRect(screenRect, backgroundPaint);
    //float textOffsetX = (screenTextPaint.measureText(message) * 0.5f);
    //float textOffsetY = screenTextPaint.getFontMetrics().ascent * -0.8f + screenHeight;
    //tempCanvas.drawText(message, screenRect.centerX() - textOffsetX, screenRect.centerY() + textOffsetY, screenTextPaint);
    //Drawing Seats
    for (int row = 0; row < rows; row++) {
      for (int column = 0; column < columns; column++) {
        left=offset / 2 + (seatWidth + seatGap) * column;
        right= left + seatWidth;
        top= offset / 2 + (seatWidth + seatGap) * row;
        bottom=top+seatWidth;
        tempCanvas.drawRect(left,top,right,bottom,testPaint);
      }
    }

    return tempBitmap;
  }

  public enum SeatStatus {
    FREE, BUSY, EMPTY, CHOSEN, INFO;

    public static boolean canSeatBePressed(SeatStatus status) {
      return (status == FREE || status == CHOSEN);
    }

    public SeatStatus pressSeat() {
      if (this == FREE) return CHOSEN;
      if (this == CHOSEN) return FREE;
      return this;
    }

  }
}