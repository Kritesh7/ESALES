package esales.schell.com.esales.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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

    public MasterDataBase(Context context)
    {
        this.context = context;
        dataBaseHelper = new DataBaseHelper(context ,databaseName,null,databaseVersion);
    }

    // insert data in a databse

    public void setSourceTableData(String sourceLat , String sourceLog, String destinationLat , String destinationLog,
                                   String calculateDistance , String customerName , String timeDuration)
    {

        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(sourceAndDestinationPointTable.sourceLat , sourceLat);
        values.put(sourceAndDestinationPointTable.sourceLog, sourceLog);
        values.put(sourceAndDestinationPointTable.destinationLat ,destinationLat);
        values.put(sourceAndDestinationPointTable.destinationLog ,destinationLog);
        values.put(sourceAndDestinationPointTable.calculateDistance,calculateDistance);
        values.put(sourceAndDestinationPointTable.customerName ,customerName);
        values.put(sourceAndDestinationPointTable.timeDuration,timeDuration);

        sqLiteDatabase.insertWithOnConflict(sourceAndDestinationPointTable.tableName,null,values,SQLiteDatabase.CONFLICT_IGNORE);
    }

}
