package sample.Others;

public class SellItem {
    private String user_name;
    private String barcode;
    private String item_name;
    private Double price;
    private Double quantity;
    private Double moneyReceived;
    private Double moneySpent;
    private String time_stamp;
    private double gia_nhap;

    public SellItem(String user_name,String barcode,String item_name,Double price,Double quantity,Double moneyReceived,Double moneySpent,String time_stamp, double gia_nhap) {
        this.user_name = user_name;
        this.barcode = barcode;
        this.item_name = item_name;
        this.price = price;
        this.quantity = quantity;
        this.moneyReceived = moneyReceived;
        this.moneySpent = moneySpent;
        this.time_stamp = time_stamp;
        this.gia_nhap = gia_nhap;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getMoneyReceived() {
        return moneyReceived;
    }

    public void setMoneyReceived(Double moneyReceived) {
        this.moneyReceived = moneyReceived;
    }

    public Double getMoneySpent() {
        return moneySpent;
    }

    public void setMoneySpent(Double moneySpent) {
        this.moneySpent = moneySpent;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public double getGia_nhap() {
        return gia_nhap;
    }

    public void setGia_nhap(double gia_nhap) {
        this.gia_nhap = gia_nhap;
    }
}
