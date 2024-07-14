import java.io.Serial;
import java.io.Serializable;

public class Order implements Serializable {
    @Serial
    private static final long serialVersionUID = -7097736854740125550L;
    private int orderId;
    private String customerName;
    private String item;
    private int quantity;
    private double price;
    private String orderType;
    private String status;

    public Order(int orderId, String customerName, String item, int quantity, double price, String orderType, String status) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.item = item;
        this.quantity = quantity;
        this.price = price;
        this.orderType = orderType;
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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    @Override
    public String toString() {
        return "Order ID: " + orderId + ", Customer: " + customerName + ", Item: " + item +
                ", Quantity: " + quantity + ", Price: " + price + ", Status: " + status;
    }
}