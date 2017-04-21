package shekar.com.alamoseatlayout.seatlayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.widget.ImageView;
import android.widget.Toast;
import shekar.com.alamoseatlayout.R;
import shekar.com.alamoseatlayout.drawing.DensityUtil;
import shekar.com.alamoseatlayout.seatlayout.photoview.OnPhotoTapListener;
import shekar.com.alamoseatlayout.seatlayout.photoview.PhotoView;

import static shekar.com.alamoseatlayout.seatlayout.MainActivity.PHOTO_TAP_TOAST_STRING;

class HallTheaterScheme {
  private int offsetX = 12;
  private int bitmapHeight;
  private int bitmapWidth;
  private Context mContext;
  private Paint seatPaint;
  private Seat[][] seats;
  private int rows;
  private int columns;
  private int  tablePaintStrokeWidth;
  private Paint rectPaint;
  private Paint tablePaintWithRoundButt;
  private Paint tablePaintWithRoundCap;
  private float seatBottomPadding;

  HallTheaterScheme(Seat[][] seats, PhotoView imageView, int measuredWidth, int measuredHeight) {
    init(imageView.getContext(), seats);
    imageView.setOnPhotoTapListener(new PhotoTapListener());
    imageView.setImageBitmap(getImageBitmap(measuredWidth, measuredHeight));
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
    tablePaintWithRoundCap = new Paint(Paint.ANTI_ALIAS_FLAG);
    //tablePaintWithRoundCap.setColor(Color.GREEN);
    tablePaintWithRoundCap.setColor(ContextCompat.getColor(mContext, R.color.table_color));
    tablePaintWithRoundCap.setFilterBitmap(true);
    tablePaintWithRoundCap.setDither(true);
    tablePaintWithRoundCap.setStyle(Paint.Style.FILL); // set to STOKE
    tablePaintWithRoundCap.setStrokeCap(Paint.Cap.ROUND);  // set the paint cap to round too

    tablePaintWithRoundButt = new Paint(Paint.ANTI_ALIAS_FLAG);
   // tablePaintWithRoundButt.setColor(Color.RED);
    tablePaintWithRoundButt.setColor(ContextCompat.getColor(mContext, R.color.table_color));
    tablePaintWithRoundButt.setFilterBitmap(true);
    tablePaintWithRoundButt.setDither(true);
    tablePaintWithRoundButt.setStyle(Paint.Style.FILL); // set to STOKE
    tablePaintWithRoundButt.setStrokeCap(Paint.Cap.BUTT);  // set the paint cap to round too

    seatPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    seatPaint.setStyle(Paint.Style.FILL);
    //seatPaint.setColor(Color.RED);
    seatPaint.setColor(Color.WHITE);
    seatPaint.setFilterBitmap(true);
    seatPaint.setDither(true);

    rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    rectPaint.setStyle(Paint.Style.FILL);
    //rectPaint.setColor(Color.WHITE);
    rectPaint.setColor(Color.TRANSPARENT);
    rectPaint.setFilterBitmap(true);
    rectPaint.setDither(true);

    this.seats = seats;
    rows = seats.length;
    columns = seats[0].length;
  }

  private Bitmap getImageBitmap(int measuredWidth, int measuredHeight) {
    final Screen screen = new Screen(measuredWidth, mContext);
    final int seatGap = 0;
    float computedSeatWidth = (measuredWidth / columns) - seatGap;
    float topOffset = screen.baseLine + (int) DensityUtil.dip2px(mContext, 8);
    final int offsetY = 12;
    final float minSeatWidth = DensityUtil.dip2px(mContext, 30);
    final float seatWidth;
    final float seatHeight;
    final float tableSeatPadding=DensityUtil.dip2px(mContext, 2);
    seatBottomPadding=DensityUtil.dip2px(mContext, 8);
    if (computedSeatWidth > minSeatWidth) {
      seatWidth = minSeatWidth;
      seatHeight= seatWidth+tablePaintStrokeWidth+tableSeatPadding+seatBottomPadding;
      offsetX = (int) (measuredWidth - ((seatWidth + seatGap) * columns));
      bitmapHeight = (int) (measuredHeight + screen.baseLine);
    } else {
      seatWidth = computedSeatWidth;
      seatHeight= seatWidth+tablePaintStrokeWidth+tableSeatPadding+seatBottomPadding;
      bitmapHeight = (int) (rows * (seatHeight + seatGap) + offsetY + topOffset);
    }
    bitmapWidth = measuredWidth;
    tablePaintStrokeWidth= (int) Math.round(seatWidth * 0.15);
    tablePaintWithRoundCap.setStrokeWidth(tablePaintStrokeWidth);
    tablePaintWithRoundButt.setStrokeWidth(tablePaintStrokeWidth);
    Bitmap tempBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
    Canvas tempCanvas = new Canvas(tempBitmap);
    screen.drawScreen(tempCanvas);

    //Drawing Seats
    drawingSeats(seatGap, topOffset, offsetY, seatWidth,seatHeight, tempCanvas);
    return tempBitmap;
  }

  private void drawingSeats(int seatGap, float topOffset, int offsetY, float seatWidth,float seatHeight, Canvas tempCanvas) {
    float left, right, top, bottom;
    for (int row = 0; row < rows; row++) {
      for (int column = 0; column < columns; column++) {
        left = offsetX / 2 + (seatWidth + seatGap) * column;
        right = left + seatWidth;
        top = offsetY / 2 + (seatHeight + seatGap) * row + topOffset;
        bottom = top + seatHeight;
        SeatExample seat = ((SeatExample) seats[row][column]);
        seat.bounds = new RectF(left, top, right, bottom);
        tempCanvas.drawRect(seat.bounds, rectPaint);
        drawSeat(tempCanvas, seat);
        drawTable(tempCanvas, seat);
        //System.out.println(
        //    String.format("======R %d | C %d = Left: %f , Right %f, Top %f , Bottom %f ", row, column, left, right, top, bottom) + "======");

      }
    }
  }

  private void drawSeat(Canvas tempCanvas, SeatExample seat) {
    if(seat.getTableStyle()==TableStyle.SIDE_TABLE_LEFT||seat.getTableStyle()==TableStyle.SIDE_TABLE_RIGHT){
      return;
    }
    tempCanvas.drawCircle(seat.bounds.centerX(),seat.bounds.bottom-seat.bounds.width()/2-seatBottomPadding,seat.bounds.width()/4, seatPaint);
  }

  private void drawTable(Canvas tempCanvas, SeatExample seat) {
    if(seat.getTableStyle()==TableStyle.NONE ||seat.getTableStyle()==TableStyle.LONG_GAP||seat.getTableStyle()==TableStyle.UNKNOWN ){
      return;
    }else  if(seat.getTableStyle()==TableStyle.SINGLE){
      tempCanvas.drawLine(seat.bounds.left+tablePaintStrokeWidth,seat.bounds.top+tablePaintStrokeWidth/2,seat.bounds.right-tablePaintStrokeWidth,seat.bounds.top+tablePaintStrokeWidth/2,
          tablePaintWithRoundCap);
    }
    else  if(seat.getTableStyle()==TableStyle.PAIR_LEFT){
      drawTableRect(tempCanvas, seat,tablePaintStrokeWidth,0);
    }
    else  if(seat.getTableStyle()==TableStyle.PAIR_RIGHT){
      drawTableRect(tempCanvas, seat,0,tablePaintStrokeWidth);
    }
    else  if(seat.getTableStyle()==TableStyle.SIDE_TABLE_LEFT||seat.getTableStyle()==TableStyle.SIDE_TABLE_RIGHT){
      tempCanvas.drawLine(seat.bounds.left+tablePaintStrokeWidth,seat.bounds.bottom-tablePaintStrokeWidth-seatBottomPadding,seat.bounds.right-tablePaintStrokeWidth,seat.bounds.bottom-tablePaintStrokeWidth-seatBottomPadding,
          tablePaintWithRoundButt);
    }
    else  if(seat.getTableStyle()==TableStyle.LONG_LEFT){
      drawTableRect(tempCanvas, seat,tablePaintStrokeWidth,0);
    }
    else  if(seat.getTableStyle()==TableStyle.LONG_CENTER){
      drawTableRect(tempCanvas, seat,0,0);
    }
    else  if(seat.getTableStyle()==TableStyle.LONG_RIGHT){
      drawTableRect(tempCanvas, seat,0,tablePaintStrokeWidth);
    }
    else  if(seat.getTableStyle()==TableStyle.LONG_GAP_LEFT){
      drawTableRect(tempCanvas, seat,0,tablePaintStrokeWidth);
    }
    else  if(seat.getTableStyle()==TableStyle.LONG_GAP_RIGHT){
      drawTableRect(tempCanvas, seat,tablePaintStrokeWidth,0);
    }
  }

  private void drawTableRect(Canvas tempCanvas, SeatExample seat, int roundedRectLeft, int roundedRectRight) {
    tempCanvas.drawLine(seat.bounds.left+tablePaintStrokeWidth,seat.bounds.top+tablePaintStrokeWidth/2,seat.bounds.right-tablePaintStrokeWidth,seat.bounds.top+tablePaintStrokeWidth/2,
        tablePaintWithRoundCap);
    tempCanvas.drawLine(seat.bounds.left+roundedRectLeft,seat.bounds.top+tablePaintStrokeWidth/2,seat.bounds.right-roundedRectRight,seat.bounds.top+tablePaintStrokeWidth/2,
        tablePaintWithRoundButt);
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