package easygiftzonebd.com.secretdevelopersltd.dexian.easygiftzonebd;

public class ShipmentClass {

    private String ShipmentName;
    private String Details;
    private String ShippingDate;
    private String ArivalDate;
    private int totalCost;
    private String ShipmentPicture;

    public ShipmentClass() {
    }

    public String getShipmentName() {
        return ShipmentName;
    }

    public void setShipmentName(String shipmentName) {
        ShipmentName = shipmentName;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getShippingDate() {
        return ShippingDate;
    }

    public void setShippingDate(String shippingDate) {
        ShippingDate = shippingDate;
    }

    public String getArivalDate() {
        return ArivalDate;
    }

    public void setArivalDate(String arivalDate) {
        ArivalDate = arivalDate;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public String getShipmentPicture() {
        return ShipmentPicture;
    }

    public void setShipmentPicture(String shipmentPicture) {
        ShipmentPicture = shipmentPicture;
    }
}
