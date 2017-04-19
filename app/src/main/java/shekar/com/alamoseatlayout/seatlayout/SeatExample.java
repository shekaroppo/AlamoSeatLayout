package shekar.com.alamoseatlayout.seatlayout;

import android.graphics.Color;
import android.graphics.RectF;

public class SeatExample implements Seat {

  public int id;
  public int color = Color.WHITE;
  public int areaIndex;
  public int rowIndex;
  public int columnIndex;
  public RectF bounds;
  public HallTheaterScheme.SeatStatus seatStatus;
  public HallTheaterScheme.SeatStyle seatStyles;
  public HallTheaterScheme.TableStyle tableStyle;
  public String marker;
  public String selectedSeatMarker;

  @Override public int id() {
    return id;
  }

  @Override public int color() {
    return color;
  }

  @Override public String marker() {
    return marker;
  }

  @Override public String selectedSeat() {
    return selectedSeatMarker;
  }

  @Override public HallTheaterScheme.SeatStatus status() {
    return seatStatus;
  }

  public void setSeatStatus(HallTheaterScheme.SeatStatus seatStatus) {
    this.seatStatus = seatStatus;
  }

  @Override public int areaIndex() {
    return areaIndex;
  }

  @Override public int rowIndex() {
    return rowIndex;
  }

  @Override public int columnIndex() {
    return columnIndex;
  }

  @Override public HallTheaterScheme.SeatStyle seatStyle() {
    return seatStyles;
  }

 public void setSeatStyle(HallTheaterScheme.SeatStyle seatStyle) {
    this.seatStyles = seatStyle;
  }

  @Override public HallTheaterScheme.TableStyle tableStyle() {
    return tableStyle;
  }

  @Override public void setTableStyle( HallTheaterScheme.TableStyle tableStyle) {
    this.tableStyle = tableStyle;
  }

  @Override public boolean canSeatPress(float x, float y) {
    return bounds.contains(x,y);
  }
}