package esales.schell.com.esales.Sources;

/**
 * Created by Admin on 09-03-2017.
 */

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs
{
    private static SharedPreferences getSetting(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(
                SettingConstant.SP_NAME, 0);
        return sp;
    }

    // vechiel type shared
    public static String getVechileType(Context context)
    {
        SharedPreferences sp = getSetting(context);
        return  sp.getString(SettingConstant.VechelType, null);
    }
    public static boolean setVechileType(Context context, String authKey)
    {
        SharedPreferences sp = getSetting(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SettingConstant.VechelType, authKey);
        return editor.commit();
    }




  /*  public static String getStatusFirstHomePage(Context context)
    {
        SharedPreferences sp = getSetting(context);
        return  sp.getString(SettingConstant.CustomerName, null);
    }
*/

    public static boolean setCustomerName(Context context, String authKey)
    {
        SharedPreferences sp = getSetting(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SettingConstant.CustomerName, authKey);
        return editor.commit();
    }

    // User is Shared preference
    public static String getUserId(Context context)
    {
        SharedPreferences sp = getSetting(context);
        return  sp.getString(SettingConstant.UserId, null);
    }
    public static boolean setUserId(Context context, String authKey)
    {
        SharedPreferences sp = getSetting(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SettingConstant.UserId, authKey);
        return editor.commit();
    }


    // AuthCode Shared Preference
    public static String getAuthCode(Context context)
    {
        SharedPreferences sp = getSetting(context);
        return  sp.getString(SettingConstant.AuthCode, null);
    }
    public static boolean setAuthCode(Context context, String authKey)
    {
        SharedPreferences sp = getSetting(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SettingConstant.AuthCode, authKey);
        return editor.commit();
    }



    //status Refrence--------------------
    public static String getStatusSecondHomePage(Context context)
    {
        SharedPreferences sp = getSetting(context);
        return  sp.getString(SettingConstant.SecondHomePage, null);
    }

    // Status First Home Page Shared Preference
    public static String getStatusFirstHomePage(Context context)
    {
        SharedPreferences sp = getSetting(context);
        return  sp.getString(SettingConstant.FirstHomePage, null);
    }
    public static boolean setStatusFirstHomePage(Context context, String authKey)
    {
        SharedPreferences sp = getSetting(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SettingConstant.FirstHomePage, authKey);
        return editor.commit();
    }

    public static boolean setStatusSecondHomePage(Context context, String authKey)
    {
        SharedPreferences sp = getSetting(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SettingConstant.SecondHomePage, authKey);
        return editor.commit();
    }


    // Start Time Shard Preference
    public static String getStartTime(Context context)
    {
        SharedPreferences sp = getSetting(context);
        return  sp.getString(SettingConstant.StartTime, null);
    }
    public static boolean setStartTime(Context context, String authKey)
    {
        SharedPreferences sp = getSetting(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SettingConstant.StartTime, authKey);
        return editor.commit();
    }

    // Source Address Shared Prefrence
    public static String getSourceName(Context context)
    {
        SharedPreferences sp = getSetting(context);
        return  sp.getString(SettingConstant.SourceName, null);
    }
    public static boolean setSourceName(Context context, String authKey)
    {
        SharedPreferences sp = getSetting(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SettingConstant.SourceName, authKey);
        return editor.commit();
    }


    // Source let Shared Prefrence
    public static String getSourceLet(Context context)
    {
        SharedPreferences sp = getSetting(context);
        return  sp.getString(SettingConstant.SourceLat, null);
    }
    public static boolean setSourceLat(Context context, String authKey)
    {
        SharedPreferences sp = getSetting(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SettingConstant.SourceLat, authKey);
        return editor.commit();
    }

    // Source Log Shared Prefrence
    public static String getSourceLog(Context context)
    {
        SharedPreferences sp = getSetting(context);
        return  sp.getString(SettingConstant.Sourcelog, null);
    }
    public static boolean setSourceLog(Context context, String authKey)
    {
        SharedPreferences sp = getSetting(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SettingConstant.Sourcelog, authKey);
        return editor.commit();
    }


}