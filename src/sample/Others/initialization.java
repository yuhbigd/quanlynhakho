package sample.Others;

import sample.ConnectionClass;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class initialization {
    public static Map<String, Item> allItem;
    public static Map<String, String> subAllItem;
    public static Map<String, phieuXuat> allPhieuXuat;
    public static Map<String, phieuNhap> allPhieuNhap;
    public static Connection con;
    public static Map<String, Integer> so_lan_mua;
    public static Map<String, Integer> so_lan_nhap;
    public static Map<String, Khachhang> allKhachHang;
    public static Map<String, Congty> allCongty;
    public static ArrayList<String> idAndName;
    public static ArrayList<String> idAndCongty;
    public static ArrayList<String> itemType;

    public initialization() {
        try {
            con = ConnectionClass.getInstances().getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        allItem = new HashMap<>();
        allPhieuXuat = new HashMap<>();
        allKhachHang = new HashMap<>();
        so_lan_mua = new HashMap<>();
        idAndName = new ArrayList<>();
        allCongty = new HashMap<>();
        so_lan_nhap = new HashMap<>();
        idAndCongty = new ArrayList<>();
        allPhieuNhap = new HashMap<>();
        itemType = new ArrayList<>();
        subAllItem = new HashMap<>();
        setDataForItem();
        setDataForPhieuXuat();
        setDataForKhachHang();
        setDataForCongTy();
    }

    public static void setDataForItem() {

        try {
            String sql = "select i.gia_nhap as gia_nhap,i.barcode as \"barcode\",i.item_name as \"item_name\",i.so_luong as \"so_luong\",i.gia_ban as \"gia_ban\", it.ten as \"loai_hang\",i.trang_thai as \"trang_thai\"\n" +
                    "from item i join item_type it \n" +
                    "on i.Item_type_id = it.id\n" +
                    "order by i.barcode";

            PreparedStatement ptsmt = con.prepareStatement(sql);
            ResultSet rs = ptsmt.executeQuery();
            allItem.clear();
            itemType.clear();
            subAllItem.clear();
            while (rs.next()) {
                String bc = rs.getString("barcode");
                String type = rs.getString("loai_hang");
                allItem.put(bc + " | " + rs.getString("item_name"), new Item(bc, rs.getString("item_name"), rs.getInt("so_luong"), rs.getDouble("gia_ban"), type, rs.getString("trang_thai"), rs.getDouble("gia_nhap")));
                subAllItem.put(bc, rs.getString("item_name"));
                if (!itemType.contains(type)) {
                    itemType.add(type);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void setDataForPhieuXuat() {
        try {
            allPhieuXuat.clear();
            String sql = "select x.*,sum(c.gia_san_pham*c.so_luong_san_pham) as \"tong gia\"from chi_tiet_xuat_hang as c join xuat_hang as x on c.ma_xuat_hang = x.ma_xuat_hang group by x.ma_xuat_hang;";
            PreparedStatement ptsmt = con.prepareStatement(sql);
            ResultSet rs = ptsmt.executeQuery();
            Date date = null;
            while (rs.next()) {
                Timestamp timestamp = rs.getTimestamp(2);
                if (timestamp != null)
                    date = new java.util.Date(timestamp.getTime());
                DateFormat df = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
                allPhieuXuat.put(rs.getString(1), new phieuXuat(rs.getString(1), df.format(date), rs.getString(3), rs.getString(4), rs.getDouble(5)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void setDataForKhachHang() {
        so_lan_mua.clear();
        allKhachHang.clear();
        try {
            String sql = "select k.ma_khach_hang,case when x.ma_xuat_hang = null then 0 else count(x.ma_xuat_hang) end as count from xuat_hang x right join khach_hang k on k.ma_khach_hang = x.ma_khach_hang  group by k.ma_khach_hang;";
            PreparedStatement ptsmt = con.prepareStatement(sql);
            ResultSet rs = ptsmt.executeQuery();
            while (rs.next()) {
                so_lan_mua.put(rs.getString(1), rs.getInt(2));
            }
            String sql1 = "select k.*,if(sum(c.so_luong_san_pham*c.gia_san_pham) is null,0,sum(c.so_luong_san_pham*c.gia_san_pham)) as tong_tien from chi_tiet_xuat_hang c join xuat_hang x on c.ma_xuat_hang = x.ma_xuat_hang right join khach_hang k on k.ma_khach_hang = x.ma_khach_hang  group by k.ma_khach_hang;";
            ptsmt = con.prepareStatement(sql1);
            rs = ptsmt.executeQuery();
            while (rs.next()) {
                allKhachHang.put(rs.getString(1), new Khachhang(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), so_lan_mua.get(rs.getString(1)), rs.getDouble(5)));
                idAndName.add(rs.getString(1) + " | " + rs.getString(2));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void setDataForCongTy() {
        so_lan_nhap.clear();
        allCongty.clear();
        try {
            String sql = "select k.id_cong_ty,case when x.ma_nhap_hang = null then 0 else count(x.ma_nhap_hang) end as count  from nhap_hang x right join ben_ban_hang_cho_kho k on k.id_cong_ty = x.id_cong_ty  group by k.id_cong_ty;";
            PreparedStatement ptsmt = con.prepareStatement(sql);
            ResultSet rs = ptsmt.executeQuery();
            while (rs.next()) {
                so_lan_nhap.put(rs.getString(1), rs.getInt(2));
            }
            String sql1 = "select k.*,if(sum(c.so_luong*c.gia_san_pham) is null,0,sum(c.so_luong*c.gia_san_pham)) as tong_tien from chi_tiet_lan_nhap c join nhap_hang x on c.ma_nhap_hang= x.ma_nhap_hang right join ben_ban_hang_cho_kho k on k.id_cong_ty = x.id_cong_ty  group by k.id_cong_ty;";
            ptsmt = con.prepareStatement(sql1);
            rs = ptsmt.executeQuery();
            while (rs.next()) {
                allCongty.put(rs.getString(1), new Congty(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), so_lan_nhap.get(rs.getString(1)), rs.getDouble(5)));
                idAndCongty.add(rs.getString(1) + " | " + rs.getString(2));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void setDataForPhieuNhap() {
        try {
            allPhieuNhap.clear();
            String sql = "select x.*,sum(c.gia_san_pham*c.so_luong) as \"tong gia\" from chi_tiet_lan_nhap as c join nhap_hang as x on c.ma_nhap_hang = x.ma_nhap_hang group by x.ma_nhap_hang;";
            PreparedStatement ptsmt = con.prepareStatement(sql);
            ResultSet rs = ptsmt.executeQuery();
            Date date = null;
            while (rs.next()) {
                Timestamp timestamp = rs.getTimestamp(2);
                if (timestamp != null)
                    date = new java.util.Date(timestamp.getTime());
                DateFormat df = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
                allPhieuNhap.put(rs.getString(1), new phieuNhap(rs.getString(1), df.format(date), rs.getString(3), rs.getString(4), rs.getDouble(5)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
