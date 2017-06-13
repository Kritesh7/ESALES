package esales.schell.com.esales.Model;

/**
 * Created by Admin on 12-06-2017.
 */

public class VehicleTypeModel
{
    public String vehicleTypeId;
    public String vehicleTyperate;
    public String vehicleTypeName;

    public VehicleTypeModel(String vehicleTypeId, String vehicleTyperate, String vehicleTypeName) {
        this.vehicleTypeId = vehicleTypeId;
        this.vehicleTyperate = vehicleTyperate;
        this.vehicleTypeName = vehicleTypeName;
    }

    public String getVehicleTypeId() {
        return vehicleTypeId;
    }

    public String getVehicleTyperate() {
        return vehicleTyperate;
    }

    public String getVehicleTypeName() {
        return vehicleTypeName;
    }
}
