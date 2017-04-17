package shekar.com.alamoseatlayout.seatlayout;

/**
 * Created by Nublo on 28.10.2015.
 * Copyright Nublo
 */
public interface Seat {

    int id();
    int color();
    String marker();
    String selectedSeat();
    HallTheaterScheme.SeatStatus status();
    void setStatus(HallTheaterScheme.SeatStatus status);

}