import java.io.Serializable;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L; // For serialization

    private int orderId;
    private String itemType; // "F" for food, "D" for drinks
    private String itemId;   // Format: "F1", "D1", etc.
    private double itemPrice; // Store the item price
    private int quantity;

    public Order(int orderId, String itemType, String itemId, double itemPrice, int quantity) {
        this.orderId = orderId;
        this.itemType = itemType;
        this.itemId = itemId;
        this.itemPrice = itemPrice;
        this.quantity = quantity;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Order ID: " + orderId + ", Item Type: " + itemType + ", Item ID: " + itemId + ", Quantity: " + quantity + ", Item Price: RM " + itemPrice;
    }
}
