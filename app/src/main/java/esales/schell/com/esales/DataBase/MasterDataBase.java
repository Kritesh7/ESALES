package esales.schell.com.esales.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import esales.schell.com.esales.MainActivity.NewManuelAddTravelList;
import esales.schell.com.esales.R;

/**
 * Created by Admin on 01-06-2017.
 */

public class MasterDataBase
{
    public DataBaseHelper dataBaseHelper;
    public static final int databaseVersion = 1;
    public static final String databaseName = "SCHELL_DISTANCE_CALCULATION_DATABASE";
    public SQLiteDatabase sqLiteDatabase;
    public Context context;
    public SourceAndDestinationPointTable sourceAndDestinationPointTable = new SourceAndDestinationPointTable();
    public VechileTypeTable vechileTypeTable = new VechileTypeTable();
    public AppEmployeeTravelExpenseInsUpdt appEmployeeTravelExpenseInsUpdt = new AppEmployeeTravelExpenseInsUpdt();
    public AppddlCustomer appddlCustomer = new AppddlCustomer();
    public MyProfileTable myProfileTable = new MyProfileTable();

    public MasterDataBase(Context context)
    {
        this.context = context;
        dataBaseHelper = new DataBaseHelper(context ,databaseName,null,databaseVersion);
    }

    // insert data in a databse

    public void setSourceTableData(String sourceLat , String sourceLog, String destinationLat , String destinationLog,
                                   String calculateDistance , String cust_id , String startAt,
                                   String reachedAt , String pointType)
    {

        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(sourceAndDestinationPointTable.sourceLat , sourceLat);
        values.put(sourceAndDestinationPointTable.sourceLog, sourceLog);
        values.put(sourceAndDestinationPointTable.destinationLat ,destinationLat);
        values.put(sourceAndDestinationPointTable.destinationLog ,destinationLog);
        values.put(sourceAndDestinationPointTable.calculateDistance,calculateDistance);
        values.put(sourceAndDestinationPointTable.customerId ,cust_id);
        values.put(sourceAndDestinationPointTable.startAt,startAt);
        values.put(sourceAndDestinationPointTable.reachedAt,reachedAt);
        values.put(sourceAndDestinationPointTable.pointType,pointType);

        sqLiteDatabase.insertWithOnConflict(sourceAndDestinationPointTable.tableName,null,values,SQLiteDatabase.CONFLICT_IGNORE);
    }

    // vechile Type get and set Data
    public void setVecheleTypeDate(String VechleTypeId, String VechelTypeName, String Vechelerate,String UserId)
    {
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(vechileTypeTable.vechileTypeId,VechleTypeId);
        values.put(vechileTypeTable.VechileTypeName,VechelTypeName);
        values.put(vechileTypeTable.vechileRate,Vechelerate);
        values.put(vechileTypeTable.userId,UserId);

        sqLiteDatabase.insert(vechileTypeTable.tableName,null,values);
    }
    public Cursor getVecheleTypeData(String userid)
    {
        sqLiteDatabase=dataBaseHelper.getReadableDatabase();
        Cursor cursor=null;
        cursor = sqLiteDatabase.rawQuery("SELECT " + vechileTypeTable.vechileTypeId + ", " + vechileTypeTable.vechileRate + ", " +
                vechileTypeTable.VechileTypeName +
                " FROM " + vechileTypeTable.tableName + " WHERE " + vechileTypeTable.userId + "=?", new String[]
                {userid});
        return cursor;

    }
    public int getVechelTypeCunt(String userid)
    {

        String countQuery = "SELECT  * FROM " + vechileTypeTable.tableName + " WHERE " + vechileTypeTable.userId + "=" + userid;
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }
    public void deleteRecord()
    {
        sqLiteDatabase=dataBaseHelper.getReadableDatabase();
        sqLiteDatabase.delete(vechileTypeTable.tableName,null,null);
    }

    //--------------------------------//-----------------------------

    //My Profile Database get ,set and delete method-----------------

    public void setMyprofileDetail(String name, String emailId, String phone, String zone , String type , String userId)
    {
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(myProfileTable.name,name);
        values.put(myProfileTable.emailId,emailId);
        values.put(myProfileTable.phoneNo,phone);
        values.put(myProfileTable.zone,zone);
        values.put(myProfileTable.type,type);
        values.put(myProfileTable.userId,userId);

        sqLiteDatabase.insert(myProfileTable.tableName,null,values);
    }
    public Cursor getMyProfileDetail(String userid)
    {
        sqLiteDatabase=dataBaseHelper.getReadableDatabase();
        Cursor cursor=null;
        cursor = sqLiteDatabase.rawQuery("SELECT " + myProfileTable.name + ", " + myProfileTable.emailId + ", " +
                myProfileTable.phoneNo + ", " + myProfileTable.zone + ", " + myProfileTable.type +
                " FROM " + myProfileTable.tableName + " WHERE " + myProfileTable.userId + "=?", new String[]
                {userid});
        return cursor;

    }
    public void deleteRecordMyProfileDetail()
    {
        sqLiteDatabase=dataBaseHelper.getReadableDatabase();
        sqLiteDatabase.delete(myProfileTable.tableName,null,null);
    }

    //-----------------------------//---------------------------------


    //AppEmployeeTravelExpenseInsUpdt  insert and update table
    public void setInsertTravelRecords(String TExpID  ,String UserID, String VehicleTypeID, String StartAtTime,
                                       String SourceName, String ReachedAtTime, String DestinationName,
                                       String DestinationCustID, String StartLattitude, String StartLongitude,
                                       String EndLattitude, String EndLongitude, String TravelledDistance,
                                       String TravelRemark, String AuthCode, String amount)
    {
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(appEmployeeTravelExpenseInsUpdt.TExpID,TExpID);
        values.put(appEmployeeTravelExpenseInsUpdt.UserID,UserID);
        values.put(appEmployeeTravelExpenseInsUpdt.VehicleTypeID,VehicleTypeID);
        values.put(appEmployeeTravelExpenseInsUpdt.StartAtTime,StartAtTime);
        values.put(appEmployeeTravelExpenseInsUpdt.SourceName,SourceName);
        values.put(appEmployeeTravelExpenseInsUpdt.ReachedAtTime,ReachedAtTime);
        values.put(appEmployeeTravelExpenseInsUpdt.DestinationName,DestinationName);
        values.put(appEmployeeTravelExpenseInsUpdt.DestinationCustID,DestinationCustID);
        values.put(appEmployeeTravelExpenseInsUpdt.StartLattitude,StartLattitude);
        values.put(appEmployeeTravelExpenseInsUpdt.StartLongitude,StartLongitude);
        values.put(appEmployeeTravelExpenseInsUpdt.EndLattitude,EndLattitude);
        values.put(appEmployeeTravelExpenseInsUpdt.EndLongitude,EndLongitude);
        values.put(appEmployeeTravelExpenseInsUpdt.TravelledDistance,TravelledDistance);
        values.put(appEmployeeTravelExpenseInsUpdt.TravelRemark,TravelRemark);
        values.put(appEmployeeTravelExpenseInsUpdt.AuthCode,AuthCode);
        values.put(appEmployeeTravelExpenseInsUpdt.Amount,amount);

        sqLiteDatabase.insert(appEmployeeTravelExpenseInsUpdt.tableName,null,values);

        //sqLiteDatabase.insertWithOnConflict(appEmployeeTravelExpenseInsUpdt.tableName,null,values,SQLiteDatabase.CONFLICT_IGNORE);

       // Toast.makeText(context, "Data Insert Successfully", Toast.LENGTH_SHORT).show();

        final Toast toast = Toast.makeText(context, "Data Insert Successfully", Toast.LENGTH_LONG);
        View view = toast.getView();
        view.setBackgroundResource(R.drawable.button_rounded_shape);
        TextView text = (TextView) view.findViewById(android.R.id.message);
        text.setTextColor(Color.parseColor("#ffffff"));
        text.setPadding(20, 20, 20, 20);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 3000);
    }

    public Cursor getInsertTravelRecords(String userid)
    {
        sqLiteDatabase=dataBaseHelper.getReadableDatabase();
        Cursor cursor=null;
        cursor = sqLiteDatabase.rawQuery("SELECT " + appEmployeeTravelExpenseInsUpdt.TExpID + ", " +
                appEmployeeTravelExpenseInsUpdt.VehicleTypeID  + ", " + appEmployeeTravelExpenseInsUpdt.StartAtTime +
                ", " + appEmployeeTravelExpenseInsUpdt.SourceName + ", " + appEmployeeTravelExpenseInsUpdt.ReachedAtTime +
                ", " + appEmployeeTravelExpenseInsUpdt.DestinationName + ", " + appEmployeeTravelExpenseInsUpdt.DestinationCustID +
                ", " + appEmployeeTravelExpenseInsUpdt.StartLattitude + ", " + appEmployeeTravelExpenseInsUpdt.StartLongitude +
                ", " + appEmployeeTravelExpenseInsUpdt.EndLattitude + ", " + appEmployeeTravelExpenseInsUpdt.EndLongitude +
                ", " + appEmployeeTravelExpenseInsUpdt.TravelledDistance + ", " + appEmployeeTravelExpenseInsUpdt.TravelRemark +
                ", " + appEmployeeTravelExpenseInsUpdt.AuthCode + ", " + appEmployeeTravelExpenseInsUpdt.Amount +
                " FROM " + appEmployeeTravelExpenseInsUpdt.tableName + " WHERE " + appEmployeeTravelExpenseInsUpdt.UserID + "=?", new String[]
                {userid});
        return cursor;

    }

    public int getInsertTravelRecordsCunt(String userid)
    {

        String countQuery = "SELECT  * FROM " + appEmployeeTravelExpenseInsUpdt.tableName + " WHERE " +
                appEmployeeTravelExpenseInsUpdt.UserID + "=" + userid;
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public void deleteInsertTravelRecords()
    {
        sqLiteDatabase=dataBaseHelper.getReadableDatabase();
        sqLiteDatabase.delete(appEmployeeTravelExpenseInsUpdt.tableName,null,null);
    }
    public void delete_byID(int id){
        sqLiteDatabase.delete(appEmployeeTravelExpenseInsUpdt.tableName, appEmployeeTravelExpenseInsUpdt.KEY_ID+"="+id, null);

        Toast.makeText(context, "Delete Records", Toast.LENGTH_SHORT).show();
    }
//-------------------------------//----------------------------------------


    // Customer Detail get and set Data
    public void setCustomerDetail(String CustomerID, String CustomerName,String UserId)
    {
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(appddlCustomer.CustomerID,CustomerID);
        values.put(appddlCustomer.CustomerName,CustomerName);
        values.put(appddlCustomer.userId,UserId);

        sqLiteDatabase.insert(appddlCustomer.tableName,null,values);
    }
    public Cursor getCustomerDetail(String userid)
    {
        sqLiteDatabase=dataBaseHelper.getReadableDatabase();
        Cursor cursor=null;
        cursor = sqLiteDatabase.rawQuery("SELECT " + appddlCustomer.CustomerID + ", " + appddlCustomer.CustomerName  +
                " FROM " + appddlCustomer.tableName + " WHERE " + appddlCustomer.userId + "=?", new String[]
                {userid});
        return cursor;

    }
    public int getCustomerDetailCunt(String userid)
    {

        String countQuery = "SELECT  * FROM " + appddlCustomer.tableName + " WHERE " + appddlCustomer.userId + "=" + userid;
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }
  /*  public int getCustomerDetailCunt(String userid)
    {

        String countQuery = "SELECT  * FROM " + vechileTypeTable.tableName + " WHERE " + vechileTypeTable.userId + "=" + userid;
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }*/
    public void deleteCustomerDetailRecord()
    {
        sqLiteDatabase=dataBaseHelper.getReadableDatabase();
        sqLiteDatabase.delete(appddlCustomer.tableName,null,null);
    }
    //------------------------//------------------------

}
