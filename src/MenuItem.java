import java.io.Serializable;

public interface MenuItem extends Serializable {
    String getFoodID();
    String getFoodName();
    double getFoodPrice();
}
