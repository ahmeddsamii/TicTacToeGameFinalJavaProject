package DataAccessLayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DAO {

    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;

    public DAO() throws SQLException {
        con = DriverManager.getConnection("jdbc:derby://localhost:1527/PlayerInfo", "root", "root");
    }

    public void insert(PlayerDTO pto) throws SQLException {
        String query = "INSERT INTO PlayerInfo (score, username, password, email, status, player_ip) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            pst = con.prepareStatement(query);
            pst.setInt(1, 0);
            pst.setString(2, pto.getUsername());
            pst.setString(3, pto.getPassword());
            pst.setString(4, pto.getEmail());
            pst.setString(6, pto.getIp());
            pst.setString(5, "Offline");

            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            closeResources();
        }
    }

    public void updateIpAndStatusOnline(PlayerDTO pto) throws SQLException {
        String query = "UPDATE PlayerInfo SET status = 'online', player_ip = ? WHERE username = ?";

        try {
            pst = con.prepareStatement(query);
            pst.setString(2, pto.getUsername());
            pst.setString(1, pto.getIp());

            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            closeResources();
        }
    }
    public void updateStatusOffline(PlayerDTO pto) throws SQLException {
        String query = "UPDATE PlayerInfo SET status = 'offline' WHERE username = ?";
    //    System.out.println(pto.getUsername()); // only check 
        

        try {
            pst = con.prepareStatement(query);
            pst.setString(1, pto.getUsername());

            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            closeResources();
        }
    }

    public boolean checdAndLogin(PlayerDTO pto) throws SQLException {
        boolean result = false;
        String sql = "SELECT * FROM PlayerInfo WHERE username = ? AND password = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, pto.getUsername());
            pst.setString(2, pto.getPassword());
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    result = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        return result;
    }
	public boolean isUsernameExists(String username) throws SQLException 
	{
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean exists = false;

        try {
            String sql = "SELECT COUNT(*) FROM PlayerInfo WHERE username = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                if (count > 0) {
                    exists = true;
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return exists;
    }

    public ArrayList<PlayerDTO> getAllPlayers() throws SQLException

	{
        ArrayList<PlayerDTO> players = new ArrayList<>();
        String query = "SELECT * FROM PlayerInfo";

        try {
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            while (rs.next()) {
                PlayerDTO player = new PlayerDTO();
                player.setScreenIndicator(rs.getInt("score"));
                player.setUsername(rs.getString("username"));
                player.setPassword(rs.getString("password"));
                player.setEmail(rs.getString("email"));
                player.setStatus(rs.getString("status"));
                players.add(player);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            closeResources();
        }

        return players;
    }
    
    public String getRequestedIp(String username) {
        String query = "SELECT player_ip FROM PlayerInfo WHERE username=?";
        String ip = null;

        try {
            pst = con.prepareStatement(query);
            pst.setString(1, username);
            rs = pst.executeQuery();

            if (rs.next()) {
                ip = rs.getString("player_ip");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }

        return ip;
    }

    public ArrayList<PlayerDTO> getOnlinePlayers() throws SQLException {
        ArrayList<PlayerDTO> players = new ArrayList<>();
        String query = "SELECT * FROM PlayerInfo WHERE status='online'";

        try {
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            while (rs.next()) {
                PlayerDTO player = new PlayerDTO();
                player.setScreenIndicator(rs.getInt("score"));
                player.setUsername(rs.getString("username"));
                player.setPassword(rs.getString("password"));
                player.setEmail(rs.getString("email"));
                player.setStatus(rs.getString("status"));
                players.add(player);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            closeResources();
        }

        return players;
    }

    public PlayerDTO getPlayerByUsername(String username) throws SQLException {
        PlayerDTO player = null;
        String query = "SELECT * FROM PlayerInfo WHERE username = ?";

        try {
            pst = con.prepareStatement(query);
            pst.setString(1, username);
            rs = pst.executeQuery();

            if (rs.next()) {
                player = new PlayerDTO();
                player.setScreenIndicator(rs.getInt("score"));
                player.setUsername(rs.getString("username"));
                player.setPassword(rs.getString("password"));
                player.setEmail(rs.getString("email"));
                player.setStatus(rs.getString("status"));
            }
        } finally {
            closeResources();
        }

        return player;
    }

    public void close() {
        closeResources();
    }

    private void closeResources() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
