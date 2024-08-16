import java.io.Serial;
import java.io.Serializable;

public class DrinkItems implements MenuItem {
    @Serial
    private static final long serialVersionUID = -1618644082963435396L;
    private String foodID;
    private String foodName;
    private double foodPrice;
    private String foodCategory;

    public DrinkItems(String foodID, String foodName, double foodPrice, String foodCategory) {
        this.foodID = foodID;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodCategory=foodCategory;
    }

    public String getFoodID() {
        return foodID;
    }

    public String getFoodName() {
        return foodName;
    }

    public double getFoodPrice(){
        return foodPrice;
    }

    public String getFoodCategory(){
        return foodCategory;
    }

    public void setFoodID(String foodID) {
        this.foodID = foodID;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setFoodPrice(double foodPrice) {
        this.foodPrice = foodPrice;
    }

    public void setFoodCategory(String foodCategory){this.foodCategory = foodCategory;}

    @Override
    public String toString() {
        return foodName + "("+foodPrice+")" ;
    }
}
