package esales.schell.com.esales.DataBase;

/**
 * Created by Admin on 01-06-2017.
 */

public class SourceAndDestinationPointTable
{

    public static final String tableName = "SourceAndDestinationPoints";
    public static final String sourceLat = "Source_Lat";
    public static final String sourceLog = "Source_Log";
    public static final String destinationLat = "Destination_Lat";
    public static final String destinationLog = "Destination_Log";
    public static final String customerName = "Customer_Name";
    public static final String calculateDistance = "Calculate_Distance";
    public static final String timeDuration = "Time_Duration";

    public static final String sourceAndDestinationTableData =
            "create table " + tableName +
                    " (" +
                    sourceLat + " text, " +
                    sourceLog + " text, " +
                    destinationLat + " text, " +
                    destinationLog + " text, " +
                    calculateDistance + " text, " +
                    customerName + " text, " +
                    timeDuration + " text" +
                    ");";
}
