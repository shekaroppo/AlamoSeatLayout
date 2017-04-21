package shekar.com.alamoseatlayout.java2s;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Shekar on 4/20/17.
 */
class GraphicsActivity extends Activity {
  // set to true to test Picture
  private static final boolean TEST_PICTURE = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void setContentView(View view) {
    if (TEST_PICTURE) {
      ViewGroup vg = new PictureLayout(this);
      vg.addView(view);
      view = vg;
    }

    super.setContentView(view);
  }
}
