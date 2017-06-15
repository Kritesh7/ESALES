package esales.schell.com.esales.DataBase;

/**
 * Created by Admin on 15-06-2017.
 */

public class AppddlCustomer
{
    public static final String tableName = "AppddlCustomer";
    public static final String CustomerID = "CustomerID";
    public static final String CustomerName = "CustomerName";
    public static final String userId = "UserId";

    public static final String AppddlCustomerTableData =
            "create table " + tableName +
                    " (" +
                    CustomerID + " text, " +
                    CustomerName + " text, " +
                    userId + " text" +
                    ");";
}
