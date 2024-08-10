import java.io.Serializable;

public class Customer implements Serializable {
    private String customerId;
    private String username;
    private String password;
    private String phoneNumber;
    private String address;
    private static int customerCount = 1;
    private static final long serialVersionUID = -1618644082963435396L;

    public Customer(String username, String password, String phoneNumber, String address) {
        this.customerId = String.valueOf(customerCount);
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        customerCount++;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        address = address;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", Address='" + address + '\'' +
                '}';
    }
}
