package sample.Others;

public class Khachhang {
    private String ma_khach;
    private String ten_khach_hang;
    private String dia_chi;
    private String so_dien_thoai;
    private int so_lan_mua;
    private double tong_tien_da_tra;

    public Khachhang(String ma_khach, String ten_khach_hang, String dia_chi, String so_dien_thoai, int so_lan_mua, double tong_tien_da_tra) {
        this.ma_khach = ma_khach;
        this.ten_khach_hang = ten_khach_hang;
        this.dia_chi = dia_chi;
        this.so_dien_thoai = so_dien_thoai;
        this.so_lan_mua = so_lan_mua;
        this.tong_tien_da_tra = tong_tien_da_tra;
    }

    public String getMa_khach() {
        return ma_khach;
    }

    public void setMa_khach(String ma_khach) {
        this.ma_khach = ma_khach;
    }

    public String getTen_khach_hang() {
        return ten_khach_hang;
    }

    public void setTen_khach_hang(String ten_khach_hang) {
        this.ten_khach_hang = ten_khach_hang;
    }

    public String getDia_chi() {
        return dia_chi;
    }

    public void setDia_chi(String dia_chi) {
        this.dia_chi = dia_chi;
    }

    public String getSo_dien_thoai() {
        return so_dien_thoai;
    }

    public void setSo_dien_thoai(String so_dien_thoai) {
        this.so_dien_thoai = so_dien_thoai;
    }

    public int getSo_lan_mua() {
        return so_lan_mua;
    }

    public void setSo_lan_mua(int so_lan_mua) {
        this.so_lan_mua = so_lan_mua;
    }

    public double getTong_tien_da_tra() {
        return tong_tien_da_tra;
    }

    public void setTong_tien_da_tra(double tong_tien_da_tra) {
        this.tong_tien_da_tra = tong_tien_da_tra;
    }
}
