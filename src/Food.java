import java.io.Serializable;

public class Food implements Serializable {
    private static final long serialVersionUID = 1L;
    private int foodID;
    private String name;
    private double price;

    public Food(int foodID, String name, double price) {
        this.foodID = foodID;
        this.name = name;
        this.price = price;
    }

    public int getFoodID() {
        return foodID;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return name + " - RM " + price;
    }
}
