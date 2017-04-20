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
import android.widget.Toast;
import shekar.com.alamoseatlayout.drawing.DensityUtil;
import shekar.com.alamoseatlayout.seatlayout.photoview.OnPhotoTapListener;
import shekar.com.alamoseatlayout.seatlayout.photoview.PhotoView;

import static shekar.com.alamoseatlayout.seatlayout.MainActivity.PHOTO_TAP_TOAST_STRING;

class HallTheaterScheme {
  private int offsetX = 12;
  private int bitmapHeight;
  private int bitmapWidth;
  private Context mContext;
  private Paint testPaint;
  private Seat[][] seats;
  private int rows;
  private int columns;

  HallTheaterScheme(Seat[][] seats, PhotoView imageView, int measuredWidth, int measuredHeight) {
    init(imageView.getContext(), seats);
    imageView.setOnPhotoTapListener(new PhotoTapListener());
    imageView.setImageBitmap(getImageBitmap(measuredWidth, measuredHeight));
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

  private void clickScheme(float xPos, float yPos) {
    for (int row = 0; row < rows; row++) {
      for (int column = 0; column < columns; column++) {
        if (seats[row][column].canSeatPress(xPos, yPos)) {
          Toast.makeText(mContext,seats[row][column].id()+" Clicked",Toast.LENGTH_LONG).show();
        }
      }
    }
  }

  private void init(Context context, Seat[][] seats) {
    mContext = context;
    testPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    testPaint.setStyle(Paint.Style.FILL);
    testPaint.setColor(Color.GREEN);
    testPaint.setFilterBitmap(true);
    testPaint.setDither(true);
    this.seats = seats;
    rows = seats.length;
    columns = seats[0].length;
  }

  private Bitmap getImageBitmap(int measuredWidth, int measuredHeight) {
    final Screen screen = new Screen(measuredWidth, mContext);
    final int seatGap = 5;
    float computedSeatWidth = (measuredWidth / columns) - seatGap;
    float topOffset = screen.baseLine + (int) DensityUtil.dip2px(mContext, 8);
    final int offsetY = 12;
    final int minSeatWidth = 30;
    final float seatWidth;
    if (computedSeatWidth > minSeatWidth) {
      seatWidth = minSeatWidth;
      offsetX = (int) (measuredWidth - ((seatWidth + seatGap) * columns));
      bitmapHeight = (int) (measuredHeight + screen.baseLine);
    } else {
      seatWidth = computedSeatWidth;
      bitmapHeight = (int) (rows * (seatWidth + seatGap) + offsetY + topOffset);
    }
    bitmapWidth = measuredWidth;

    Bitmap tempBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
    Canvas tempCanvas = new Canvas(tempBitmap);
    screen.drawScreen(tempCanvas);

    //Drawing Seats
    float left, right, top, bottom;
    for (int row = 0; row < rows; row++) {
      for (int column = 0; column < columns; column++) {
        left = offsetX / 2 + (seatWidth + seatGap) * column;
        right = left + seatWidth;
        top = offsetY / 2 + (seatWidth + seatGap) * row + topOffset;
        bottom = top + seatWidth;
        System.out.println(
            String.format("======R %d | C %d = Left: %f , Right %f, Top %f , Bottom %f ", row, column, left, right, top, bottom) + "======");
        SeatExample seat = ((SeatExample) seats[row][column]);
        seat.bounds = new RectF(left, top, right, bottom);
        tempCanvas.drawRect(seat.bounds, testPaint);
      }
    }
    return tempBitmap;
  }

  enum SeatStatus {
    NONE, EMPTY, SOLD, RESERVED, BROKEN, PLACEHOLDER, UNKNOWN, BUSY, CHOSEN, INFO;

    public static boolean canSeatBePressed(SeatStatus status) {
      return (status == EMPTY || status == CHOSEN);
    }

    public SeatStatus pressSeat() {
      if (this == EMPTY) return CHOSEN;
      if (this == CHOSEN) return EMPTY;
      return this;
    }
  }

  enum SeatStyle {
    NONE, NORMAL, BARSEAT, HANDICAP, COMPANION, UNKNOWN
  }

  enum TableStyle {
    NONE, SINGLE, PAIR_LEFT, PAIR_RIGHT, SIDE_TABLE_LEFT, SIDE_TABLE_RIGHT, LONG_LEFT, LONG_CENTER, LONG_RIGHT, LONG_GAP, LONG_GAP_LEFT, LONG_GAP_RIGHT, UNKNOWN
  }

  private class Screen {

    float screenWidth, screenHeight, left, top, cornerRadius, baseLine, textOffsetX, textOffsetY;
    private Paint screenPaint;
    private Paint screenTextPaint;
    private String message;
    private Paint backgroundPaint;

    Screen(float totalWidth, Context context) {

      screenHeight = DensityUtil.dip2px(context, 10);
      float widthCenter = totalWidth / 2;
      screenWidth = totalWidth * 6 / 7;
      left = widthCenter - screenWidth / 2;
      top = DensityUtil.dip2px(context, 24);
      cornerRadius = DensityUtil.dip2px(context, screenHeight);

      backgroundPaint = new Paint();
      backgroundPaint.setStyle(Paint.Style.FILL);
      backgroundPaint.setColor(Color.BLACK);

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
      message = "THE SCREEN";
      textOffsetX = (screenTextPaint.measureText(message) * 0.5f);
      textOffsetY = screenTextPaint.getFontMetrics().ascent * -0.8f + screenHeight;
      baseLine = top + screenHeight + textOffsetY;
    }

    void drawScreen(Canvas canvas) {
      RectF screenRect = new RectF(left, top, left + screenWidth, top + screenHeight);
      canvas.drawRoundRect(screenRect, cornerRadius, cornerRadius, screenPaint);
      screenRect.top = top + screenHeight / 2;
      canvas.drawRect(screenRect, backgroundPaint);
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

  private class PhotoTapListener implements OnPhotoTapListener {

    @Override public void onPhotoTap(ImageView view, float x, float y) {
      float xPos = (float) Math.floor(x * bitmapWidth);
      float yPos = (float) Math.floor(y * bitmapHeight);
      clickScheme(xPos, yPos);
      Toast.makeText(mContext, (String.format(PHOTO_TAP_TOAST_STRING, xPos, yPos, view == null ? 0 : view.getId())), Toast.LENGTH_SHORT).show();
    }
  }
}