package main.code.taxi.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Steffan on 05/02/2015.
 */
public class PreferenceManager {
    //Name of user
    public static String getUserName(Activity activity){
        SharedPreferences pref = (activity.getApplicationContext())
                .getSharedPreferences(Utils.pref_user_key, Context.MODE_PRIVATE);
        return pref.getString(Utils.pref_name,Utils.unknownString);//if does not exist returns unknown String
    }
    public static void saveUserName(Activity activity,String username){
        SharedPreferences pref = activity.getSharedPreferences(Utils.pref_user_key,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Utils.pref_name, username);
        editor.commit();
    }
    //Type of user
    public static String getUserType(Activity activity){
        SharedPreferences pref = (activity.getApplicationContext())
                .getSharedPreferences(Utils.pref_user_key, Context.MODE_PRIVATE);
        return pref.getString(Utils.pref_userType,Utils.unknownString);//if does not exist returns unknown String
    }
    public static void saveUserType(Activity activity,String userType){
        SharedPreferences pref = activity.getSharedPreferences(Utils.pref_user_key,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Utils.pref_userType, userType);
        editor.commit();
    }
    //Type of user
    public static String getNumberPlate(Activity activity){
        SharedPreferences pref = (activity.getApplicationContext())
                .getSharedPreferences(Utils.pref_user_key, Context.MODE_PRIVATE);
        return pref.getString(Utils.pref_numberPlate,Utils.unknownString);//if does not exist returns unknown String
    }
    public static void saveNumberPlate(Activity activity,String numberplate){
        SharedPreferences pref = activity.getSharedPreferences(Utils.pref_user_key,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Utils.pref_numberPlate, numberplate);
        editor.commit();
    }

    //Vehicle Capacity
    public static int getVehicleCapacity(Activity activity){
        SharedPreferences pref = (activity.getApplicationContext())
                .getSharedPreferences(Utils.pref_user_key, Context.MODE_PRIVATE);
        return pref.getInt(Utils.pref_vehicleCap,-1);//if does not exist returns -1
    }
    public static void storeVehicleCapacity(Activity activity,int capacity){
        SharedPreferences pref = activity.getSharedPreferences(Utils.pref_user_key,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(Utils.pref_vehicleCap, capacity);
        editor.commit();
    }
}
