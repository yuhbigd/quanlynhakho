package sample.Others;

public class Employee {
    private String user_name;
    private String password;
    private String ten_chuc_vu;
    private String gioi_tinh;
    private String dien_thoai;
    private String noi_o;
    private String ho_ten;

    public Employee(String user_name, String password, String ten_chuc_vu, String gioi_tinh, String dien_thoai, String noi_o, String ho_ten) {
        this.user_name = user_name;
        this.password = password;
        this.ten_chuc_vu = ten_chuc_vu;
        this.gioi_tinh = gioi_tinh;
        this.dien_thoai = dien_thoai;
        this.noi_o = noi_o;
        this.ho_ten = ho_ten;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTen_chuc_vu() {
        return ten_chuc_vu;
    }

    public void setTen_chuc_vu(String ten_chuc_vu) {
        this.ten_chuc_vu = ten_chuc_vu;
    }

    public String getGioi_tinh() {
        return gioi_tinh;
    }

    public void setGioi_tinh(String gioi_tinh) {
        this.gioi_tinh = gioi_tinh;
    }

    public String getDien_thoai() {
        return dien_thoai;
    }

    public void setDien_thoai(String dien_thoai) {
        this.dien_thoai = dien_thoai;
    }

    public String getNoi_o() {
        return noi_o;
    }

    public void setNoi_o(String noi_o) {
        this.noi_o = noi_o;
    }

    public String getHo_ten() {
        return ho_ten;
    }

    public void setHo_ten(String ho_ten) {
        this.ho_ten = ho_ten;
    }
}
