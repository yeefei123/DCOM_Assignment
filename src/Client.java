import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {

    private static void writeFile(String fileName, Object obj) {
        boolean append = new File(fileName).exists();
        try (FileOutputStream fileOut = new FileOutputStream(fileName, true);
             ObjectOutputStream out = append ? new AppendingObjectOutputStream(fileOut) : new ObjectOutputStream(fileOut)) {
            out.writeObject(obj);
            out.flush();
            out.close();
            System.out.println("Serialized data is saved in " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Custom ObjectOutputStream to avoid writing a new header when appending
    private static class AppendingObjectOutputStream extends ObjectOutputStream {
        public AppendingObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            reset(); // Do not write a header when appending
        }
    }

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost");

            OrderInterface stub = (OrderInterface) registry.lookup("OrderInterface");
            System.out.println("Food Menu: \n 1. Coke - RM 20 \n 2. Fried Chicken \n 3. Nothing to order");

            Scanner myObj = new Scanner(System.in);

            int menuId = 0;
            while (menuId != 3) {
                System.out.println("Enter foodId to order");
                menuId = Integer.parseInt(myObj.nextLine());

                if (menuId == 3) {
                    break; // Exit the loop if user chooses "Nothing to order"
                }

                System.out.println("Enter quantity to order");
                int quantity = Integer.parseInt(myObj.nextLine());
                Order order = stub.placeOrder(menuId, quantity);
                System.out.println("Order placed: " + order);

                writeFile("foodOrder.ser", order);
            }

            System.out.println("Thank you for ordering!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
