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
    public static String getCustomerName(Context context)
    {
        SharedPreferences sp = getSetting(context);
        return  sp.getString(SettingConstant.CustomerName, null);
    }

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
    public static String getProfilepic(Context context)
    {
        SharedPreferences sp = getSetting(context);
        return  sp.getString(SettingConstant.ProfilePic, null);
    }
    public static String getDestinationLog(Context context)
    {
        SharedPreferences sp = getSetting(context);
        return  sp.getString(SettingConstant.DestinationLog, null);
    }
    public static boolean setProfilePic(Context context, String authKey)
    {
        SharedPreferences sp = getSetting(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SettingConstant.ProfilePic, authKey);
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