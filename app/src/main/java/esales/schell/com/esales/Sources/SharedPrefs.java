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
    public static String getLoginStatus(Context context)
    {
        SharedPreferences sp = getSetting(context);
        return  sp.getString(SettingConstant.LoginStatus, null);
    }
    public static String getCustomerId(Context context)
    {
        SharedPreferences sp = getSetting(context);
        return  sp.getString(SettingConstant.CustomerId, null);
    }
    public static String getAuthCode(Context context)
    {
        SharedPreferences sp = getSetting(context);
        return  sp.getString(SettingConstant.AuthCode, null);
    }
  /*  public static String getStatusFirstHomePage(Context context)
    {
        SharedPreferences sp = getSetting(context);
        return  sp.getString(SettingConstant.CustomerName, null);
    }
*/
    public static boolean setLoginStatus(Context context, String authKey)
    {
        SharedPreferences sp = getSetting(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SettingConstant.LoginStatus, authKey);
        return editor.commit();
    }
    public static boolean setCustomerName(Context context, String authKey)
    {
        SharedPreferences sp = getSetting(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SettingConstant.CustomerName, authKey);
        return editor.commit();
    }
    public static boolean setCustomerId(Context context, String authKey)
    {
        SharedPreferences sp = getSetting(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SettingConstant.CustomerId, authKey);
        return editor.commit();
    }

    public static boolean setAuthCode(Context context, String authKey)
    {
        SharedPreferences sp = getSetting(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SettingConstant.AuthCode, authKey);
        return editor.commit();
    }

    public static String getDestinationLog(Context context)
    {
        SharedPreferences sp = getSetting(context);
        return  sp.getString(SettingConstant.DestinationLog, null);
    }

    //status Refrence--------------------
    public static String getStatusSecondHomePage(Context context)
    {
        SharedPreferences sp = getSetting(context);
        return  sp.getString(SettingConstant.SecondHomePage, null);
    }
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


    public static boolean setDestinationLog(Context context, String authKey)
    {
        SharedPreferences sp = getSetting(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SettingConstant.DestinationLog, authKey);
        return editor.commit();
    }
    public static boolean setDestinationLat(Context context, String authKey)
    {
        SharedPreferences sp = getSetting(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SettingConstant.DestinationLat, authKey);
        return editor.commit();
    }

    public static boolean setSourceLog(Context context, String authKey)
    {
        SharedPreferences sp = getSetting(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SettingConstant.Sourcelog, authKey);
        return editor.commit();
    }
    public static boolean setSourceLat(Context context, String authKey)
    {
        SharedPreferences sp = getSetting(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SettingConstant.SourceLat, authKey);
        return editor.commit();
    }
    public static String getSourceLet(Context context)
    {
        SharedPreferences sp = getSetting(context);
        return  sp.getString(SettingConstant.SourceLat, null);
    }
    public static String getSourceLog(Context context)
    {
        SharedPreferences sp = getSetting(context);
        return  sp.getString(SettingConstant.Sourcelog, null);
    }
    public static String getDestinationLat(Context context)
    {
        SharedPreferences sp = getSetting(context);
        return  sp.getString(SettingConstant.DestinationLat, null);
    }

}