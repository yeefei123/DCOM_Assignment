import java.io.Serializable;

public class Order implements Serializable {
    private int orderId;
    private int foodId;
    private int quantity;

    public Order(int orderId, int foodId, int quantity) {
        this.orderId = orderId;
        this.foodId=foodId;
        this.quantity = quantity;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId){
        this.orderId=orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity=quantity;
    }

    @Override
    public String toString() {
        return "Order ID: " + orderId + "FoodItem: "+ foodId + ", Quantity: " + quantity ;
    }
}
