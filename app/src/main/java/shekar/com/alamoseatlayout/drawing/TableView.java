package shekar.com.alamoseatlayout.drawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Shekar on 4/22/17.
 */

public class TableView extends View {
  private Paint blue;
  private Paint red;
  private Path tableClipPath;
  private Path screenPath;
  private float mRadius=12,centerX,centerY;
  private Paint background;
  private Path tablePath;
  private Paint linePaint;
  private Path lineClipPath;

  public TableView(Context context) {
    super(context);
    init();
  }

  public TableView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    setupPaints();
    setupPaths();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    centerX = getWidth() / 2f;
    centerY = getHeight() / 2f;

    lineClipPath();
    tablePath();
    screenPAth();
  }

  private void tablePath() {
    tableClipPath.reset();
    tablePath.reset();
    float[] radii = {0, 0, 0, 0, 0, 0, 0, 0};
    radii[0] = mRadius;
    radii[1] = mRadius;
    radii[6] = mRadius;
    radii[7] = mRadius;

    radii[2] = mRadius;
    radii[3] = mRadius;
    radii[4] = mRadius;
    radii[5] = mRadius;
    final RectF tableRect = new RectF(centerX - 200f, centerY, centerX + 200f, centerY + 12);
    tablePath.addRoundRect(tableRect, radii, Path.Direction.CW);
    tableClipPath.addRect(new RectF(tableRect.right-mRadius/2, tableRect.top, tableRect.right, tableRect.bottom), Path.Direction.CW);
  }

  private void screenPAth() {
    screenPath.reset();
    float[] radii = {0, 0, 0, 0, 0, 0, 0, 0};
    radii[0] = mRadius;
    radii[1] = mRadius;
    radii[2] = mRadius;
    radii[3] = mRadius;
    screenPath.addRoundRect(new RectF(centerX-200f, centerY, centerX+200f, centerY+12), radii, Path.Direction.CW);

  }

  private void lineClipPath() {
    lineClipPath.reset();
    lineClipPath.addRect(new RectF(centerX + 200f, centerY-mRadius/2, centerX + 200f+mRadius/2,centerY+mRadius/2), Path.Direction.CW);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    //canvas.save();
    //canvas.clipPath(screenPath, Region.Op.DIFFERENCE);
    //canvas.drawPaint(blue);
    //canvas.restore();
   // canvas.drawPath(tableClipPath, blue);
    //canvas.drawPaint(blue);
    //canvas.drawPath(screenPath, red);
    //canvas.clipPath(screenPath);
    canvas.save();
    //canvas.clipPath(tableClipPath, Region.Op.DIFFERENCE);
    //canvas.drawPath(tablePath,background);
    canvas.clipPath(lineClipPath, Region.Op.DIFFERENCE);
    canvas.drawLine(centerX - 200f, centerY, centerX + 200f, centerY,linePaint);
    canvas.drawPaint(red);
    canvas.restore();
  }
  private void setupPaths() {
    tableClipPath = new Path();
    tablePath = new Path();
    screenPath = new Path();
    lineClipPath = new Path();
  }

  private void setupPaints() {
    blue = new Paint();
    blue.setColor(Color.BLUE);
    blue.setStyle(Paint.Style.FILL);
    blue.setAntiAlias(true);

    red = new Paint(Paint.ANTI_ALIAS_FLAG);
    red.setColor(Color.RED);
    red.setStyle(Paint.Style.STROKE);
    red.setStrokeWidth(10f);
    red.setAntiAlias(true);

    background = new Paint();
    background.setColor(Color.BLACK);
    background.setStyle(Paint.Style.FILL);
    background.setAntiAlias(true);

    linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    linePaint.setColor(Color.GREEN);
    linePaint.setFilterBitmap(true);
    linePaint.setDither(true);
    linePaint.setStrokeWidth(mRadius);
    linePaint.setStyle(Paint.Style.FILL); // set to STOKE
    linePaint.setStrokeCap(Paint.Cap.ROUND);  // set the paint cap to round too
  }

}
