import java.io.Serial;
import java.io.Serializable;

public class Cart implements Serializable {
    @Serial
    private static final long serialVersionUID = 3090005108205434182L;
    private String foodID;
    private String customerName;
    private final FoodItems foodItem;
    private final int quantity;
    private final double price;

    public Cart(String foodID, String customerName,FoodItems foodItem, int quantity, double price) {
        this.foodID=foodID;
        this.customerName=customerName;
        this.foodItem = foodItem;
        this.quantity = quantity;
        this.price=price;
    }

    public String getFoodID() {
        return foodID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public FoodItems getFoodItem() {
        return foodItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}