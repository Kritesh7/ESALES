package esales.schell.com.esales.DataBase;

/**
 * Created by Admin on 15-06-2017.
 */

public class AppEmployeeTravelExpenseInsUpdt
{
    public static final String tableName = "AppEmployeeTravelExpenseInsUpdt";
    public static final String TExpID = "TExpID";
    public static final String UserID = "UserID";
    public static final String VehicleTypeID = "VehicleTypeID";
    public static final String StartAtTime = "StartAtTime";
    public static final String SourceName = "SourceName";
    public static final String ReachedAtTime = "ReachedAtTime";
    public static final String DestinationName = "DestinationName";
    public static final String DestinationCustID = "DestinationCustID";
    public static final String StartLattitude = "StartLattitude";
    public static final String StartLongitude = "StartLongitude";
    public static final String EndLattitude = "EndLattitude";
    public static final String EndLongitude = "EndLongitude";
    public static final String TravelledDistance = "TravelledDistance";
    public static final String TravelRemark = "TravelRemark";
    public static final String AuthCode = "AuthCode";
    public static final String Amount = "Amount";

    public static final String appEmployeeTravelExpenseInsUpdtTableData =
            "create table " + tableName +
                    " (" +
                    TExpID + " text, " +
                    UserID + " text, " +
                    VehicleTypeID + " text, " +
                    StartAtTime + " text, " +
                    SourceName + " text, " +
                    ReachedAtTime + " text, " +
                    DestinationName + " text, " +
                    DestinationCustID + " text, " +
                    StartLattitude + " text, " +
                    StartLongitude + " text, " +
                    EndLattitude + " text, " +
                    EndLongitude + " text, " +
                    TravelledDistance + " text, " +
                    TravelRemark + " text, " +
                    AuthCode + " text, " +
                    Amount + " text" +
                    ");";
}
