package coms309.items;

/**
 * Defines an item in the walmart store
 *
 * @author Dhairya Kachalia
 */
public class Items {
    private String itemName;
    private float price;
    private int barcode;

    public Items(String itemName, float price, int barcode) {
        this.itemName = itemName;
        this.price = price;
        this.barcode = barcode;
    }

    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public float getPrice() {
        return this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getBarcode() {
        return this.barcode;
    }

    public void setBarcode(int barcode) {
        this.barcode = barcode;
    }

    @Override
    public String toString() {
        return "item{" +
                "itemName='" + itemName + '\'' +
                ", price=" + price +
                ", barcode=" + barcode +
                '}';
    }
}
