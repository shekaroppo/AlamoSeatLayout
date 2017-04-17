package shekar.com.alamoseatlayout.seatlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import shekar.com.alamoseatlayout.R;

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
        new HallTheaterScheme(basicScheme(),imageView,imageView.getMeasuredWidth(),imageView.getMeasuredHeight());
        return true;
      }
    });
  }

  public Seat[][] basicScheme() {
    Seat seats[][] = new Seat[10][5];
    for (int i = 0; i < 5; i++)
      for(int j = 0; j < 5; j++) {
        SeatExample seat = new SeatExample();
        seat.id = i * 10 + (j+1);
        seat.selectedSeatMarker = String.valueOf(j+1);
        seat.status = HallTheaterScheme.SeatStatus.FREE;
        seats[i][j] = seat;
      }
    return seats;
  }
}
