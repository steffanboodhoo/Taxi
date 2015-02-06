package main.code.taxi.pojo;

import org.json.JSONObject;

/**
 * Created by Steffan on 06/02/2015.
 */
public class Passenger {
    private String identifier;
    private double lat;
    private double lng;

    public Passenger(String identifier,double lat, double lng){
        this.identifier=identifier;
        this.lat=lat;
        this.lng=lng;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public JSONObject toJson(){
        JSONObject obj=new JSONObject();
        try {
            obj.put("id", identifier);
            obj.put("lat",lat);
            obj.put("lng",lng);
        }catch (Exception e){
            e.printStackTrace();
        }
        return obj;
    }
}
