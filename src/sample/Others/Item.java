package sample.Others;

public class Item {
    private String barcode;
    private String item_name;
    private int so_luong;
    private double gia_ban;
    private String item_type;
    private String trang_thai;
    private double gia_nhap;
    public Item(String barcode, String item_name, int so_luong, double gia_ban, String item_type, String trang_thai, double gia_nhap) {
        this.barcode = barcode;
        this.item_name = item_name;
        this.so_luong = so_luong;
        this.gia_ban = gia_ban;
        this.item_type = item_type;
        this.trang_thai = trang_thai;
        this.gia_nhap = gia_nhap;
    }

    public double getGia_nhap() {
        return gia_nhap;
    }

    public void setGia_nhap(double gia_nhap) {
        this.gia_nhap = gia_nhap;
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

    public int getSo_luong() {
        return so_luong;
    }

    public void setSo_luong(int so_luong) {
        this.so_luong = so_luong;
    }

    public double getGia_ban() {
        return gia_ban;
    }

    public void setGia_ban(double gia_ban) {
        this.gia_ban = gia_ban;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public void setItem_type(int item_type_id) {
        this.item_type = item_type;
    }

    public String getTrang_thai() {
        return trang_thai;
    }

    public void setTrang_thai(String trang_thai) {
        this.trang_thai = trang_thai;
    }
}

