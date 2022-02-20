package sql;
import models.BarberShop;
import models.Customer;

import java.sql.*;
public class CustomerQuery {

    private static final String QUERY_CUSTOMER = "SELECT * FROM customerTable";

    public void loadCustomers(Connection conn, BarberShop barberShop) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(QUERY_CUSTOMER);
        while(rs.next()){
            String name = rs.getString("FNAME") + " " + rs.getString("LNAME");
            try {
                @SuppressWarnings("unused")
				Customer newCustomer = new Customer(name,rs.getString("phoneNumber"),barberShop,rs.getString("username")
                ,rs.getString("password"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void insertCustomers(Connection conn, BarberShop barberShop,Customer customer) throws SQLException {
        PreparedStatement pst = conn.prepareStatement("insert INTO customerTable Values  (?, ?, ?, ?, ?)");
        String[] name = customer.getCustomerName().split(" ",2);
        pst.setString(1,name[0]);
        pst.setString(2,name[1]);
        pst.setString(3,customer.getPhoneNumber());
        pst.setString(4,customer.getUsername());
        pst.setString(5,customer.getPassword());
        pst.execute();
    }

}
