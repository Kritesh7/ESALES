package esales.schell.com.esales.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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



}
