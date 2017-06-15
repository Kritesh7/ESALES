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
