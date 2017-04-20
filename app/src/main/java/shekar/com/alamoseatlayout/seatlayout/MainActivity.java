package shekar.com.alamoseatlayout.seatlayout;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;
import shekar.com.alamoseatlayout.R;
import shekar.com.alamoseatlayout.seatlayout.photoview.OnMatrixChangedListener;
import shekar.com.alamoseatlayout.seatlayout.photoview.OnSingleFlingListener;
import shekar.com.alamoseatlayout.seatlayout.photoview.PhotoView;
import shekar.com.alamoseatlayout.seatlayout.photoview.PhotoViewAttacher;

public class MainActivity extends AppCompatActivity {

  static final String PHOTO_TAP_TOAST_STRING = "Photo Tap! X: %.2f %% Y:%.2f %% ID: %d";
  static final String SCALE_TOAST_STRING = "Scaled to: %.2ff";
  static final String FLING_LOG_STRING = "Fling velocityX: %.2f, velocityY: %.2f";

  private PhotoView mPhotoView;
  private TextView mCurrMatrixTv;

  private Toast mCurrentToast;

  private Matrix mCurrentDisplayMatrix = null;
  private ImageButton zoomButton;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    zoomButton = (ImageButton) findViewById(R.id.zoom_view);
    zoomButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (zoomButton.getTag().equals("ZoomedOut")) {
          mPhotoView.setScale(PhotoViewAttacher.DEFAULT_MIN_SCALE, true);
          zoomButton.setTag("ZoomedIn");
          zoomButton.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.zoomin));
        } else if (zoomButton.getTag().equals("ZoomedIn")) {
          mPhotoView.setScale(PhotoViewAttacher.DEFAULT_MID_SCALE, true);
          zoomButton.setTag("ZoomedOut");
          zoomButton.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.zoomout));
        }
      }
    });
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("Simple Sample");
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBackPressed();
      }
    });
    toolbar.inflateMenu(R.menu.main_menu);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
          case R.id.menu_zoom_toggle:
            mPhotoView.setZoomable(!mPhotoView.isZoomEnabled());
            item.setTitle(mPhotoView.isZoomEnabled() ? R.string.menu_zoom_disable : R.string.menu_zoom_enable);
            return true;

          case R.id.menu_scale_fit_center:
            mPhotoView.setScaleType(ImageView.ScaleType.CENTER);
            return true;

          case R.id.menu_scale_fit_start:
            mPhotoView.setScaleType(ImageView.ScaleType.FIT_START);
            return true;

          case R.id.menu_scale_fit_end:
            mPhotoView.setScaleType(ImageView.ScaleType.FIT_END);
            return true;

          case R.id.menu_scale_fit_xy:
            mPhotoView.setScaleType(ImageView.ScaleType.FIT_XY);
            return true;

          case R.id.menu_scale_scale_center:
            mPhotoView.setScaleType(ImageView.ScaleType.CENTER);
            return true;

          case R.id.menu_scale_scale_center_crop:
            mPhotoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return true;

          case R.id.menu_scale_scale_center_inside:
            mPhotoView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            return true;

          case R.id.menu_scale_random_animate:
          case R.id.menu_scale_random:
            Random r = new Random();

            float minScale = mPhotoView.getMinimumScale();
            float maxScale = mPhotoView.getMaximumScale();
            float randomScale = minScale + (r.nextFloat() * (maxScale - minScale));
            mPhotoView.setScale(randomScale, item.getItemId() == R.id.menu_scale_random_animate);

            showToast(String.format(SCALE_TOAST_STRING, randomScale));

            return true;
          case R.id.menu_matrix_restore:
            if (mCurrentDisplayMatrix == null) {
              showToast("You need to capture display matrix first");
            } else {
              mPhotoView.setDisplayMatrix(mCurrentDisplayMatrix);
            }
            return true;
          case R.id.menu_matrix_capture:
            mCurrentDisplayMatrix = new Matrix();
            mPhotoView.getDisplayMatrix(mCurrentDisplayMatrix);
            return true;
        }
        return false;
      }
    });
    mPhotoView = (PhotoView) findViewById(R.id.imageView);
    //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
    //  mPhotoView.setLayerType(LAYER_TYPE_SOFTWARE, null);
    //}
    mPhotoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    ViewTreeObserver vto = mPhotoView.getViewTreeObserver();
    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
      public boolean onPreDraw() {
        mPhotoView.getViewTreeObserver().removeOnPreDrawListener(this);
        new HallTheaterScheme(basicScheme(), mPhotoView, mPhotoView.getMeasuredWidth(), mPhotoView.getMeasuredHeight());
        return true;
      }
    });
    // mCurrMatrixTv = (TextView) findViewById(R.id.tv_current_matrix);

    // Lets attach some listeners, not required though!
    mPhotoView.setOnMatrixChangeListener(new MatrixChangeListener());

    mPhotoView.setOnSingleFlingListener(new SingleFlingListener());
  }


  private void showToast(CharSequence text) {
    if (mCurrentToast != null) {
      mCurrentToast.cancel();
    }

    mCurrentToast = Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT);
    mCurrentToast.show();
  }

  public Seat[][] basicScheme() {
    int rows = 12;
    int columns = 12;
    Seat seats[][] = new Seat[rows][columns];
    for (int i = 0; i < rows; i++)
      for (int j = 0; j < columns; j++) {
        SeatExample seat = new SeatExample();
        seat.id = i * rows + (j + 1);
        seat.seatStatus = HallTheaterScheme.SeatStatus.EMPTY;
        seat.seatStyles = HallTheaterScheme.SeatStyle.NORMAL;
        seat.tableStyle = HallTheaterScheme.TableStyle.SINGLE;
        seat.selectedSeatMarker = String.valueOf(j + 1);
        seats[i][j] = seat;
      }
    return seats;
  }

  private class MatrixChangeListener implements OnMatrixChangedListener {

    @Override public void onMatrixChanged(RectF rect) {
      if (mPhotoView.getScale()==PhotoViewAttacher.DEFAULT_MIN_SCALE) {
        zoomButton.setTag("ZoomedIn");
        zoomButton.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.zoomin));
      } else if (mPhotoView.getScale()>PhotoViewAttacher.DEFAULT_MIN_SCALE) {
        zoomButton.setTag("ZoomedOut");
        zoomButton.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.zoomout));
      }

      //mCurrMatrixTv.setText(rect.toString());
    }
  }

  private class SingleFlingListener implements OnSingleFlingListener {

    @Override public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
      Log.d("PhotoView", String.format(FLING_LOG_STRING, velocityX, velocityY));
      return true;
    }
  }
}
