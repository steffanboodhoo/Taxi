package main.code.taxi.taxi;

/**
 * Created by xytr3k on 2/6/15.
 */
public class Drivers {

    private String name;
    private String seats;
    private String distance;
    private String number_pate;

    public Drivers(){
        name ="";
        seats ="";
        distance ="";
        number_pate ="";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getNumber_pate() {
        return number_pate;
    }

    public void setNumber_pate(String number_pate) {
        this.number_pate = number_pate;
    }
}
