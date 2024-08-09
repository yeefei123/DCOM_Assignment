import java.io.Serial;
import java.io.Serializable;

public class DrinkItems implements Serializable {
    @Serial
    private static final long serialVersionUID = -1618644082963435396L;
    private String foodID;
    private String foodName;
    private double foodPrice;

    public DrinkItems(String foodID, String foodName, double foodPrice) {
        this.foodID = foodID;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
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

    public void setFoodID(String foodID) {
        this.foodID = foodID;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setFoodPrice(double foodPrice) {
        this.foodPrice = foodPrice;
    }

    @Override
    public String toString() {
        return foodName + "("+foodPrice+")" ;
    }
}
