package id.unware.poken.models;

/**
 * @author Anwar Pasaribu
 * @since Jun 11 2017
 */

public class OrderStatus {
    public static final int INITIALIZE = -2;
    public static final int SOLD_OUT = -1;
    public static final int ORDERED = 0;
    public static final int PAID = 1;
    public static final int SENT = 2;
    public static final int RECEIVED = 3;
    public static final int SUCCESS = 4;
    public static final int REFUND = 5;
    public static final int ORDER_EXPIRE = 6;
    public static final int COD_ACCEPTED = 7;
    public static final int AUTO_SUCCESS = 8;

    public OrderStatus() {
    }

    public static CharSequence getOrderStatusText(int statusNumber) {
        switch (statusNumber) {
            case ORDERED:
                return "Dipesan";
            case PAID:
                return "Dibayar";
            case SENT:
                return "Dikirim";
            case RECEIVED:
                return "Diterima";
            case SUCCESS:
                return "Pembelian berhasil";
            case REFUND:
                return "Pengembalian barang";
            case ORDER_EXPIRE:
                return "Pesanan Hangus";
            case COD_ACCEPTED:
                return "COD Diterima";
            case AUTO_SUCCESS:
                return "Sistem Auto Sukses";
        }

        return "";
    }
}
