package main.code.taxi.utils;

/**
 * Created by Steffan on 05/02/2015.
 */
public class Utils {
    public static String mainUrl="https://wicked-castle-9292.herokuapp.com";

    public static String ioEvent_passenger_broadcast="passengerBroadcast";
    public static String ioEvent_driver_broadcast="driverBroadcast";
    public static String ioEvent_passenger_request="traveller-request";

    public static String pref_user_key="userPref";
    public static String pref_name="username";
    public static String pref_userType="userType";
    public static String pref_numberPlate="numberplate";
    public static String unknownString="unknown";

    public static String userTypeDriver="typeDriver";
    public static String userTypePassenger="typePassenger";

    public static String pref_vehicleCap="vehicleCapacity";

    public static String json_key_identifier="user";
    public static String json_key_lat="lat";
    public static String json_key_lng="lng";

    public enum UserType {DriverType, PassengerType};
}
