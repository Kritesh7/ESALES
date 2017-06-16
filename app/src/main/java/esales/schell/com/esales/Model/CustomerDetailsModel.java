package esales.schell.com.esales.Model;

/**
 * Created by Admin on 12-06-2017.
 */

public class CustomerDetailsModel
{
    public String customerName;
    public String CustomerId;

    public CustomerDetailsModel(String customerName, String customerId) {
        this.customerName = customerName;
        CustomerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    @Override
    public String toString() {
        return customerName;
    }
}
