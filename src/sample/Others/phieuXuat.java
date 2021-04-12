package sample.Others;

public class phieuXuat {
    private String ma_xuat_hang;
    private String thoi_gian_xuat;
    private String ma_khach_hang;
    private String nguoi_ban;
    private double tong_tien;

    public phieuXuat(String ma_xuat_hang, String thoi_gian_xuat, String ma_khach_hang, String nguoi_ban, double tong_tien) {
        this.ma_xuat_hang = ma_xuat_hang;
        this.thoi_gian_xuat = thoi_gian_xuat;
        this.ma_khach_hang = ma_khach_hang;
        this.nguoi_ban = nguoi_ban;
        this.tong_tien = tong_tien;
    }

    public String getMa_xuat_hang() {
        return ma_xuat_hang;
    }

    public void setMa_xuat_hang(String ma_xuat_hang) {
        this.ma_xuat_hang = ma_xuat_hang;
    }

    public String getThoi_gian_xuat() {
        return thoi_gian_xuat;
    }

    public void setThoi_gian_xuat(String thoi_gian_xuat) {
        this.thoi_gian_xuat = thoi_gian_xuat;
    }

    public String getMa_khach_hang() {
        return ma_khach_hang;
    }

    public void setMa_khach_hang(String ma_khach_hang) {
        this.ma_khach_hang = ma_khach_hang;
    }

    public String getNguoi_ban() {
        return nguoi_ban;
    }

    public void setNguoi_ban(String nguoi_ban) {
        this.nguoi_ban = nguoi_ban;
    }

    public double getTong_tien() {
        return tong_tien;
    }

    public void setTong_tien(double tong_tien) {
        this.tong_tien = tong_tien;
    }
}
