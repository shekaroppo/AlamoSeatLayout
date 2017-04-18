package shekar.com.alamoseatlayout.seatlayout;

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
  Paint backgroundPaint;
  private Context mContext;
  private Paint testPaint;
  private Scene scene;

  public HallTheaterScheme(Seat[][] seats, ImageView imageView, int measuredWidth, int measuredHeight) {
    init(imageView.getContext());
    imageView.setImageBitmap(getImageBitmap(seats, measuredWidth, measuredHeight));
  }

  private void init(Context context) {
    mContext = context;
    backgroundPaint = new Paint();
    backgroundPaint.setStyle(Paint.Style.FILL);
    backgroundPaint.setColor(Color.BLACK);
    testPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    testPaint.setStyle(Paint.Style.FILL);
    testPaint.setColor(Color.GREEN);
    testPaint.setFilterBitmap(true);
    testPaint.setDither(true);
  }

  public Bitmap getImageBitmap(Seat[][] seats, int measuredWidth, int measuredHeight) {

    int rows = seats.length;
    int columns = seats[0].length;
    int seatWidth = 30;
    int seatGap = 5;
    int offset = (int) DensityUtil.dip2px(mContext, 8);
   // int bitmapHeight = rows * (seatWidth + seatGap) - seatGap + offset;
    //int bitmapWidth = columns * (seatWidth + seatGap) - seatGap + offset;
    Bitmap tempBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
    Canvas tempCanvas = new Canvas(tempBitmap);
    tempCanvas.drawPaint(backgroundPaint);
    //drawScreen(bitmapWidth, tempCanvas);
    scene=new Scene(measuredWidth,mContext,backgroundPaint);
    scene.drawScreen(tempCanvas);



    //Drawing Seats
    float left, right, top, bottom;
    float topOffset=scene.baseLine+(int) DensityUtil.dip2px(mContext, 8);
    for (int row = 0; row < rows; row++) {
      for (int column = 0; column < columns; column++) {
        left = offset / 2 + (seatWidth + seatGap) * column;
        right = left + seatWidth;
        top = offset / 2 + (seatWidth + seatGap) * row +topOffset;
        bottom = top + seatWidth;
        tempCanvas.drawRect(left, top, right, bottom, testPaint);
      }
    }
    return tempBitmap;
    //return scaleBitmap(tempBitmap,0.5f);
  }

  public static Bitmap scaleBitmap(Bitmap src, float factor) {
    int width = src.getWidth();
    int height = src.getHeight();
    int scaledWidth = (int) (width * factor);
    int scaledHeight = (int) (height * factor);
    Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    Bitmap scaled = Bitmap.createScaledBitmap(src, scaledWidth, scaledHeight, false);
    Canvas canvas = new Canvas(bmp);
    Paint paint = new Paint();
    int distX = (width - scaledWidth) / 2;
    int distY = (height - scaledHeight) / 2;
    canvas.drawBitmap(scaled, distX, distY, paint);
    return bmp;
  }

  private void drawScreen(int width, Canvas tempCanvas) {
    //Drawing Screen
    //float widthCenter = width / 2;
    //float screenWidth = width * 6 / 7;
    //float screenHeight = DensityUtil.dip2px(mContext, 10);
    //float left = widthCenter - screenWidth / 2;
    //float top = DensityUtil.dip2px(mContext, 24);
    //RectF screenRect = new RectF(left, top, left + screenWidth, top + screenHeight);
    //tempCanvas.drawRoundRect(screenRect, DensityUtil.dip2px(mContext, screenHeight), DensityUtil.dip2px(mContext, screenHeight), screenPaint);
    //screenRect.top = top + screenHeight / 2;
    //tempCanvas.drawRect(screenRect, backgroundPaint);
    //float textOffsetX = (screenTextPaint.measureText(message) * 0.5f);
    //float textOffsetY = screenTextPaint.getFontMetrics().ascent * -0.8f + screenHeight;
    //tempCanvas.drawText(message, screenRect.centerX() - textOffsetX, screenRect.centerY() + textOffsetY, screenTextPaint);
    //screenOffset = screenRect.centerY() + textOffsetY;
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

  public static class Scene {

    public float screenWidth, screenHeight, left,top,cornerRadius, baseLine;
    private Paint screenPaint;
    private Paint screenTextPaint;
    private String message;
    private Paint backgroundPaint;

    Scene(float totalWidth, Context context, Paint backgroundPaint) {
      screenHeight =  DensityUtil.dip2px(context, 10);
      float widthCenter = totalWidth / 2;
      screenWidth = totalWidth * 6 / 7;
      left = widthCenter - screenWidth / 2;
      top = DensityUtil.dip2px(context, 24);
      cornerRadius=DensityUtil.dip2px(context, screenHeight);

      screenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
      screenPaint.setStyle(Paint.Style.FILL);
      screenPaint.setColor(Color.WHITE);
      screenPaint.setFilterBitmap(true);
      screenPaint.setDither(true);

      screenTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
      screenTextPaint.setColor(Color.WHITE);
      screenTextPaint.setTextSize(DensityUtil.sip2px(context, 14f));
      screenTextPaint.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/FuturaStd-Book.otf"));
      screenTextPaint.setFilterBitmap(true);
      screenTextPaint.setDither(true);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        screenTextPaint.setLetterSpacing(0.13f);
      }
      this.backgroundPaint=backgroundPaint;
      message = "THE SCREEN";
    }

     void drawScreen(Canvas canvas){
      RectF screenRect = new RectF(left, top, left + screenWidth, top + screenHeight);
      canvas.drawRoundRect(screenRect, cornerRadius,cornerRadius, screenPaint);
      screenRect.top = top + screenHeight / 2;
      canvas.drawRect(screenRect, backgroundPaint);
      float textOffsetX = (screenTextPaint.measureText(message) * 0.5f);
      float textOffsetY = screenTextPaint.getFontMetrics().ascent * -0.8f + screenHeight;
       baseLine =screenRect.centerY() + textOffsetY;
      canvas.drawText(message, screenRect.centerX() - textOffsetX, baseLine, screenTextPaint);
    }
    //
    //public void setScenePosition(ScenePosition position, int offset) {
    //  this.position = position;
    //  this.offset = offset;
    //  dimension = 90;
    //  switch (position) {
    //    case NORTH:
    //      dimensionSecond = width * 12;
    //      break;
    //    case SOUTH:
    //      dimensionSecond = width * 12;
    //      break;
    //    case EAST:
    //      dimensionSecond = height * 12;
    //      break;
    //    case WEST:
    //      dimensionSecond = height * 12;
    //      break;
    //    case NONE:
    //      dimensionSecond = 0;
    //      dimension = 0;
    //      break;
    //    default:
    //      dimensionSecond = 0;
    //      dimension = 0;
    //      this.position = ScenePosition.NONE;
    //      break;
    //  }
    //}

    //public int getTopXOffset() {
    //  if (position == ScenePosition.NORTH) {
    //    return dimension + offset;
    //  }
    //  return 0;
    //}
    //
    //public int getLeftYOffset() {
    //  if (position == ScenePosition.EAST) {
    //    return dimension + offset;
    //  }
    //  return 0;
    //}
    //
    //public int getBottomXOffset() {
    //  if (position == ScenePosition.SOUTH) {
    //    return dimension + offset;
    //  }
    //  return 0;
    //}
    //
    //public int getRightYOffset() {
    //  if (position == ScenePosition.WEST) {
    //    return dimension + offset;
    //  }
    //  return 0;
    //}

    //@Override public String toString() {
    //  return String.format("Left - %d, Right- %d, Top - %d, Bottom - %d", getLeftYOffset(), getRightYOffset(), getTopXOffset(), getBottomXOffset());
    //}
  }
}