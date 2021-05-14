package sample.Others;

public class phieuNhap {
    private String ma_nhap_hang;
    private String thoi_gian_nhap;
    private String ma_cong_ty;
    private String nguoi_nhap;
    private double tong_tien;
    public phieuNhap(String ma_nhap_hang, String thoi_gian_nhap, String ma_cong_ty, String nguoi_nhap, double tong_tien) {
        this.ma_nhap_hang = ma_nhap_hang;
        this.thoi_gian_nhap = thoi_gian_nhap;
        this.ma_cong_ty = ma_cong_ty;
        this.nguoi_nhap = nguoi_nhap;
        this.tong_tien = tong_tien;
    }

    public String getMa_nhap_hang() {
        return ma_nhap_hang;
    }

    public void setMa_nhap_hang(String ma_nhap_hang) {
        this.ma_nhap_hang = ma_nhap_hang;
    }

    public String getThoi_gian_nhap() {
        return thoi_gian_nhap;
    }

    public void setThoi_gian_nhap(String thoi_gian_nhap) {
        this.thoi_gian_nhap = thoi_gian_nhap;
    }

    public String getMa_cong_ty() {
        return ma_cong_ty;
    }

    public void setMa_cong_ty(String ma_cong_ty) {
        this.ma_cong_ty = ma_cong_ty;
    }

    public String getNguoi_nhap() {
        return nguoi_nhap;
    }

    public void setNguoi_nhap(String nguoi_nhap) {
        this.nguoi_nhap = nguoi_nhap;
    }

    public double getTong_tien() {
        return tong_tien;
    }

    public void setTong_tien(double tong_tien) {
        this.tong_tien = tong_tien;
    }
}
