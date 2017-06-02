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
    public static String getGallerryImage(Context context)
    {
        SharedPreferences sp = getSetting(context);
        return  sp.getString(SettingConstant.GalleryImage, null);
    }
    public static boolean setProfilePic(Context context, String authKey)
    {
        SharedPreferences sp = getSetting(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SettingConstant.ProfilePic, authKey);
        return editor.commit();
    }
    public static boolean setGalleryImage(Context context, String authKey)
    {
        SharedPreferences sp = getSetting(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SettingConstant.GalleryImage, authKey);
        return editor.commit();
    }
    public static boolean setMembershipId(Context context, String authKey)
    {
        SharedPreferences sp = getSetting(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SettingConstant.MemberShipId, authKey);
        return editor.commit();
    }

    public static boolean setMemberShipCode(Context context, String authKey)
    {
        SharedPreferences sp = getSetting(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SettingConstant.CodeName, authKey);
        return editor.commit();
    }
    public static boolean setRegistrationDate(Context context, String authKey)
    {
        SharedPreferences sp = getSetting(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SettingConstant.RegistartionDate, authKey);
        return editor.commit();
    }
    public static String getRegistrationDate(Context context)
    {
        SharedPreferences sp = getSetting(context);
        return  sp.getString(SettingConstant.RegistartionDate, null);
    }
    public static String getMemberShipCode(Context context)
    {
        SharedPreferences sp = getSetting(context);
        return  sp.getString(SettingConstant.CodeName, null);
    }
    public static String getMemberShipId(Context context)
    {
        SharedPreferences sp = getSetting(context);
        return  sp.getString(SettingConstant.MemberShipId, null);
    }
}