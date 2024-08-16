import java.io.Serial;
import java.io.Serializable;

public class Cart implements Serializable {
    @Serial
    private static final long serialVersionUID = 3090005108205434182L;

    private String foodID;
    private String customerName;
    private MenuItem item; // This can be either a FoodItem or DrinkItem
    private final int quantity;
    private final double price;

    public Cart(String foodID, String customerName, MenuItem item, int quantity, double price) {
        this.foodID = foodID;
        this.customerName = customerName;
        this.item = item;
        this.quantity = quantity;
        this.price = price;
    }

    public String getFoodID() {
        return foodID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public MenuItem getItem() {
        return item;
    }

    @Override
    public String toString() {
        return foodID + " (" + item.getFoodName() + ") (" + quantity + ")";
    }
}
