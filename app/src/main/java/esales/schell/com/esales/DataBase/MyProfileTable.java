package esales.schell.com.esales.DataBase;

/**
 * Created by Admin on 21-06-2017.
 */

public class MyProfileTable
{
    public static final String tableName = "MyProfileTable";
    public static final String name = "Name";
    public static final String emailId = "EmailId";
    public static final String phoneNo = "PhoneNumber";
    public static final String zone = "Zone";
    public static final String type = "Type";
    public static final String userId = "UserId";

    public static final String MyProfileTableData =
            "create table " + tableName +
                    " (" +
                    name + " text, " +
                    emailId + " text, " +
                    phoneNo + " text, " +
                    zone + " text, " +
                    type + " text, " +
                    userId + " text" +
                    ");";
}
