package sample.Others;


public class lichSuNhapHang {
    private String barcode;
    private String maNhapHang;
    private String item_name;
    private Integer quantity;
    private String name;
    private String time;

    public lichSuNhapHang(String barcode, String maNhapHang, String item_name, Integer quantity, String name, String time) {
        this.barcode = barcode;
        this.maNhapHang = maNhapHang;
        this.item_name = item_name;
        this.quantity = quantity;
        this.name = name;
        this.time = time;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMaNhapHang() {
        return maNhapHang;
    }

    public void setMaNhapHang(String maNhapHang) {
        this.maNhapHang = maNhapHang;
    }
}