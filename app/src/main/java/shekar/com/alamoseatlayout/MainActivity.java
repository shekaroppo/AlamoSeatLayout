package shekar.com.alamoseatlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

  private ImageView imageView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    imageView = (ImageView) findViewById(R.id.imageView);
    ViewTreeObserver vto = imageView.getViewTreeObserver();
    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
      public boolean onPreDraw() {
        imageView.getViewTreeObserver().removeOnPreDrawListener(this);
        new HallTheaterScheme(imageView,imageView.getMeasuredWidth(),imageView.getMeasuredHeight());
        return true;
      }
    });
  }

  @Override public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
  }
}
