package shekar.com.alamoseatlayout.seatlayout;

public interface Seat {
  int id();
  int color();
  int areaIndex();
  int rowIndex();
  int columnIndex();
  String marker();
  String selectedSeat();
  HallTheaterScheme.SeatStatus status();
  void setSeatStatus(HallTheaterScheme.SeatStatus seatStatus);
  HallTheaterScheme.SeatStyle seatStyle();
  void setSeatStyle(HallTheaterScheme.SeatStyle status);
  HallTheaterScheme.TableStyle tableStyle();
  void setTableStyle(HallTheaterScheme.TableStyle status);
  boolean canSeatPress(float x, float y);
}