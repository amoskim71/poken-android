package id.unware.poken.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Anwar Pasaribu
 * @since Jun 01 2017
 */

public class Product implements Parcelable {

    public long id;
    public String name;
    public String description;
    public Seller seller;
    public boolean is_cod;
    public boolean is_new;
    public boolean is_discount;
    public double discount_amount;
    public Date date_created;
    public ProductBrand brand;
    public String category;
    public ArrayList<ProductImage> images;
    public String size;
    public int stock;
    public double price;
    public double weight;

    public static String KEY_PRODUCT_ID = "id";

    public Product() {
    }

    protected Product(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        seller = in.readParcelable(Seller.class.getClassLoader());
        is_cod = in.readByte() != 0;
        is_new = in.readByte() != 0;
        is_discount = in.readByte() != 0;
        discount_amount = in.readDouble();
        date_created = new Date(in.readLong());  // Custom, convert to Date to save
        brand = in.readParcelable(ProductBrand.class.getClassLoader());
        category = in.readString();
        images = in.createTypedArrayList(ProductImage.CREATOR);
        size = in.readString();
        stock = in.readInt();
        price = in.readDouble();
        weight = in.readDouble();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public double getDiscountedPrice() {
        double originalProductPrice = this.price;
        double discountAmount = this.discount_amount;

        return (originalProductPrice - ((originalProductPrice * discountAmount) / 100));

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeParcelable(seller, i);
        parcel.writeByte((byte) (is_cod ? 1 : 0));
        parcel.writeByte((byte) (is_new ? 1 : 0));
        parcel.writeByte((byte) (is_discount ? 1 : 0));
        parcel.writeDouble(discount_amount);
        parcel.writeLong(date_created.getTime());  // Custom, convert to long to save
        parcel.writeParcelable(brand, i);
        parcel.writeString(category);
        parcel.writeTypedList(images);
        parcel.writeString(size);
        parcel.writeInt(stock);
        parcel.writeDouble(price);
        parcel.writeDouble(weight);
    }
}
