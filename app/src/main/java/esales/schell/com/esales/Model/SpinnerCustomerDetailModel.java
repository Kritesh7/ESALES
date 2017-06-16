package esales.schell.com.esales.Model;

/**
 * Created by Aditya on 10-06-2017.
 */

public class SpinnerCustomerDetailModel {

    public String customerName;
    public String CustomerId;
    public SpinnerCustomerDetailModel(){
        super();
    }

    public SpinnerCustomerDetailModel(String customerName, String CustomerId) {
        super();
        this.customerName = customerName;
        this.CustomerId=CustomerId;
    }
}
