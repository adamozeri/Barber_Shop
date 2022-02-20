package sql;

import haircut_Strategy.*;
import interfaces.Haircut;
import models.BarberShop;

import java.sql.*;


public class HaircutQuery {

    private static final String QUERY_HAIRCUT = "SELECT * FROM haircutTable";

    public void loadHaircuts(Connection conn, BarberShop barberShop) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(QUERY_HAIRCUT);
        while (rs.next()) {
            Context c = new Context(findHaircut(rs.getString("name"), rs));
            barberShop.getAllHaircuts().add(c);
        }
    }

    public void updateHaircuts(Connection conn, BarberShop barberShop, Haircut haircut) throws SQLException {
        PreparedStatement pst = conn.prepareStatement("Update haircutTable Set cost = " + haircut.getCost() + " Where name = '" + haircut.getName() + "'");
        pst.execute();
    }

    public Haircut findHaircut(String name, ResultSet next) throws SQLException {
        Haircut haircut = null;
        switch (name) {
            case "Shades":
                haircut = new ShadesHaircut(next.getInt("cost"), name, next.getFloat("duration"));
                break;
            case "Coloring":
                haircut = new HairColoring(next.getInt("cost"), name, next.getFloat("duration"));
                break;
            case "Straightening":
                haircut = new HairStraightening(next.getInt("cost"), name, next.getFloat("duration"));
                break;
            case "Kids":
                haircut = new KidsHaircut(next.getInt("cost"), name, next.getFloat("duration"));
                break;
            case "Short":
                haircut = new ShortHaircut(next.getInt("cost"), name, next.getFloat("duration"));
                break;
            case "Tips":
                haircut = new TipsHaircut(next.getInt("cost"), name, next.getFloat("duration"));
                break;
            default:
                break;
        }
        return haircut;
    }
}
