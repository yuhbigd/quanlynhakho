package sample.Others;

public class Congty {
    private String ma_cong_ty;
    private String ten_cong_ty;
    private String dia_chi;
    private String so_dien_thoai;
    private int so_lan_nhap;
    private double tong_tien_da_tra;

    public Congty(String ma_con_ty, String ten_cong_ty, String dia_chi, String so_dien_thoai, int so_lan_nhap, double tong_tien_da_tra) {
        this.ma_cong_ty = ma_con_ty;
        this.ten_cong_ty = ten_cong_ty;
        this.dia_chi = dia_chi;
        this.so_dien_thoai = so_dien_thoai;
        this.so_lan_nhap = so_lan_nhap;
        this.tong_tien_da_tra = tong_tien_da_tra;
    }

    public String getMa_cong_ty() {
        return ma_cong_ty;
    }

    public void setMa_cong_ty(String ma_con_ty) {
        this.ma_cong_ty = ma_con_ty;
    }

    public String getTen_cong_ty() {
        return ten_cong_ty;
    }

    public void setTen_cong_ty(String ten_cong_ty) {
        this.ten_cong_ty = ten_cong_ty;
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

    public int getSo_lan_nhap() {
        return so_lan_nhap;
    }

    public void setSo_lan_nhap(int so_lan_nhap) {
        this.so_lan_nhap = so_lan_nhap;
    }

    public double getTong_tien_da_tra() {
        return tong_tien_da_tra;
    }

    public void setTong_tien_da_tra(double tong_tien_da_tra) {
        this.tong_tien_da_tra = tong_tien_da_tra;
    }
}
