package esales.schell.com.esales.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Admin on 01-06-2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    public SourceAndDestinationPointTable sourceAndDestinationPointTable = new SourceAndDestinationPointTable();
    public VechileTypeTable vechileTypeTable = new VechileTypeTable();
    public AppEmployeeTravelExpenseInsUpdt appEmployeeTravelExpenseInsUpdt = new AppEmployeeTravelExpenseInsUpdt();
    public AppddlCustomer appddlCustomer = new AppddlCustomer();

    public  DataBaseHelper(Context context, String databaseName , SQLiteDatabase.CursorFactory factory , int databaseVersion)
    {
        super(context,databaseName,factory,databaseVersion);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(sourceAndDestinationPointTable.sourceAndDestinationTableData);
        db.execSQL(vechileTypeTable.vechileTypeTableData);
        db.execSQL(appEmployeeTravelExpenseInsUpdt.appEmployeeTravelExpenseInsUpdtTableData);
        db.execSQL(appddlCustomer.AppddlCustomerTableData);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + sourceAndDestinationPointTable.tableName);
        db.execSQL("DROP TABLE IF EXISTS " + vechileTypeTable.tableName);
        db.execSQL("DROP TABLE IF EXISTS " + appEmployeeTravelExpenseInsUpdt.tableName);
        db.execSQL("DROP TABLE IF EXISTS " + appddlCustomer.tableName);
    }
}
