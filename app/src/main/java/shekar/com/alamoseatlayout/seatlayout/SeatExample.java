package shekar.com.alamoseatlayout.seatlayout;

import android.graphics.Color;

/**
 * Created by Nublo on 05.12.2015.
 * Copyright Nublo
 */
public class SeatExample implements Seat {

    public int id;
    public int color = Color.RED;
    public String marker;
    public String selectedSeatMarker;
    public HallTheaterScheme.SeatStatus status;

    @Override
    public int id() {
        return id;
    }

    @Override
    public int color() {
        return color;
    }

    @Override
    public String marker() {
        return marker;
    }

    @Override
    public String selectedSeat() {
        return selectedSeatMarker;
    }

    @Override
    public HallTheaterScheme.SeatStatus status() {
        return status;
    }

    @Override
    public void setStatus(HallTheaterScheme.SeatStatus status) {
        this.status = status;
    }

}