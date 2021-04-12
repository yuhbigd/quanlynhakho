package sample.Others;

public class ChiTietXuatHang {
    private String ma_xuat_hang;
    private String item_barcode;
    private double gia_san_pham;
    private int so_luong;

    public ChiTietXuatHang(String ma_xuat_hang, String item_barcode, double gia_san_pham, int so_luong) {
        this.ma_xuat_hang = ma_xuat_hang;
        this.item_barcode = item_barcode;
        this.gia_san_pham = gia_san_pham;
        this.so_luong = so_luong;
    }

    public String getMa_xuat_hang() {
        return ma_xuat_hang;
    }

    public void setMa_xuat_hang(String ma_xuat_hang) {
        this.ma_xuat_hang = ma_xuat_hang;
    }

    public String getItem_barcode() {
        return item_barcode;
    }

    public void setItem_barcode(String item_barcode) {
        this.item_barcode = item_barcode;
    }

    public double getGia_san_pham() {
        return gia_san_pham;
    }

    public void setGia_san_pham(double gia_san_pham) {
        this.gia_san_pham = gia_san_pham;
    }

    public int getSo_luong() {
        return so_luong;
    }

    public void setSo_luong(int so_luong) {
        this.so_luong = so_luong;
    }
}
