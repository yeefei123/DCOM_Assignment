import java.io.Serial;
import java.io.Serializable;

public class FoodCategory implements Serializable {
    @Serial
    private static final long serialVersionUID = -1618644082963435396L;
    private String categoryID;
    private String categoryName;

    public FoodCategory(String categoryID, String categoryName) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return categoryName;
    }
}
