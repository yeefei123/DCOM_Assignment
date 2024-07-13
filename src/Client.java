import java.rmi.Naming;

public class Client {
    public static void main(String[] args) {
        try {
            OrderInterface orderService = (OrderInterface) Naming.lookup("rmi://localhost/OrderService");
            String response = orderService.placeOrder("John Doe", "Pizza", 2, 19.99, "Processing");
            System.out.println(response);
            Order order = orderService.getOrderDetails(1);
            System.out.println("Retrieved Order: " + order.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
