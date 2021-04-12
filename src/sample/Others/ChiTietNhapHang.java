package sample.Others;

public class ChiTietNhapHang {
    private String ma_nhap_hang;
    private String item_barcode;
    private double gia_san_pham;
    private int so_luong;

    public ChiTietNhapHang(String ma_nhap_hang, String item_barcode, double gia_san_pham, int so_luong) {
        this.ma_nhap_hang = ma_nhap_hang;
        this.item_barcode = item_barcode;
        this.gia_san_pham = gia_san_pham;
        this.so_luong = so_luong;
    }

    public String getMa_nhap_hang() {
        return ma_nhap_hang;
    }

    public void setMa_nhap_hang(String ma_nhap_hang) {
        this.ma_nhap_hang = ma_nhap_hang;
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
