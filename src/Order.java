import java.io.Serializable;

public class Order implements Serializable {
    private int orderId;
    private String customerName;
    private String item;
    private int quantity;
    private double price;
    private String status;

    public Order(int orderId, String customerName, String item, int quantity, double price, String status) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.item = item;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Order ID: " + orderId + ", Customer: " + customerName + ", Item: " + item +
                ", Quantity: " + quantity + ", Price: " + price + ", Status: " + status;
    }
}
