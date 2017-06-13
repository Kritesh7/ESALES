package esales.schell.com.esales.DataBase;

/**
 * Created by Admin on 12-06-2017.
 */

public class VechileTypeTable
{

    public static final String tableName = "VechileTypeTable";
    public static final String vechileTypeId = "vechileTypeId";
    public static final String VechileTypeName = "VechileTypeName";
    public static final String vechileRate = "vechileRate";
    public static final String userId = "UserId";

    public static final String vechileTypeTableData =
            "create table " + tableName +
                    " (" +
                    vechileTypeId + " text, " +
                    VechileTypeName + " text, " +
                    vechileRate + " text, " +
                    userId + " text" +
                    ");";
}
