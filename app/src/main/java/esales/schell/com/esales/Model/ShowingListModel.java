package esales.schell.com.esales.Model;

/**
 * Created by Admin on 08-06-2017.
 */

public class ShowingListModel
{

    public String date;
    public String source;
    public String destination;
    public String vechileType;
    public String travelDisatnce;
    public String rate;
    public String expensAmount;
    public String texpId;

    public ShowingListModel(String date, String source, String destination,
                            String vechileType, String travelDisatnce, String rate, String expensAmount, String texepId) {
        this.date = date;
        this.source = source;
        this.destination = destination;
        this.vechileType = vechileType;
        this.travelDisatnce = travelDisatnce;
        this.rate = rate;
        this.expensAmount = expensAmount;
        this.texpId = texepId;
    }

    public String getDate() {
        return date;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getVechileType() {
        return vechileType;
    }

    public String getTravelDisatnce() {
        return travelDisatnce;
    }

    public String getRate() {
        return rate;
    }

    public String getExpensAmount() {
        return expensAmount;
    }

    public String getTexpId(){return texpId;}
}
