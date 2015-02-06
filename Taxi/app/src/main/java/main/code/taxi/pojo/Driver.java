package main.code.taxi.pojo;

import org.json.JSONObject;

/**
 * Created by Steffan on 06/02/2015.
 */
public class Driver {
    private String numberPlate;
    private String identifier;
    private String vehicleCapaity;
    private double lat;
    private double lng;
    public Driver(String numberPlate, String identifier, String vehicleCapaity) {
        this.numberPlate = numberPlate;
        this.identifier = identifier;
        this.vehicleCapaity = vehicleCapaity;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getVehicleCapaity() {
        return vehicleCapaity;
    }

    public void setVehicleCapaity(String vehicleCapaity) {
        this.vehicleCapaity = vehicleCapaity;
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
            obj.put("numberPlate",numberPlate);
            obj.put("vehicleCapacity",vehicleCapaity);
            obj.put("lat",lat);
            obj.put("lng",lng);
        }catch (Exception e){
            e.printStackTrace();
        }
        return obj;
    }
}
