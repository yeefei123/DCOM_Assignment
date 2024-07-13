import java.io.Serializable;

public class FoodItems implements Serializable {
    private static final long serialVersionUID = 1L;
    private int foodID;
    private String name;
    private double price;
    private String foodCategory;

    public FoodItems(int foodID, String name, double price, String foodCategory) {
        this.foodID = foodID;
        this.name = name;
        this.price = price;
        this.foodCategory=foodCategory;
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

    public String getFoodCategory(){
        return foodCategory;
    }

    @Override
    public String toString() {
        return name + " - RM " + price;
    }
}