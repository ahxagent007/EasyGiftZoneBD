package easygiftzonebd.com.secretdevelopersltd.dexian.easygiftzonebd;

public class Product {

    private Long productID;
    private String productName;
    private int productCost;
    private int productPrice;
    private String productPicture;
    private String productLastUpdate;
    private int productStockQuantity;

    public Product() {
    }

    public Product(Long productID, String productName, int productCost, int productPrice, String productPicture, String productLastUpdate, int productStockQuantity) {
        this.productID = productID;
        this.productName = productName;
        this.productCost = productCost;
        this.productPrice = productPrice;
        this.productPicture = productPicture;
        this.productLastUpdate = productLastUpdate;
        this.productStockQuantity = productStockQuantity;
    }

    public Long getProductID() {
        return productID;
    }

    public void setProductID(Long productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductCost() {
        return productCost;
    }

    public void setProductCost(int productCost) {
        this.productCost = productCost;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductPicture() {
        return productPicture;
    }

    public void setProductPicture(String productPicture) {
        this.productPicture = productPicture;
    }

    public String getProductLastUpdate() {
        return productLastUpdate;
    }

    public void setProductLastUpdate(String productLastUpdate) {
        this.productLastUpdate = productLastUpdate;
    }

    public int getProductStockQuantity() {
        return productStockQuantity;
    }

    public void setProductStockQuantity(int productStockQuantity) {
        this.productStockQuantity = productStockQuantity;
    }
}
