package repos;

import entities.Volunteer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class VolunteerRepository implements IVolunteerRepository {
    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public VolunteerRepository(Properties props){
        logger.info("Initializing VolunteerRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    public Volunteer findOne(String username, String password) throws TeledonException {
        logger.traceEntry("finding volunteer with username {} ",username);
        Connection con=dbUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select * from Volunteer where Username=? and Password=?")){
            preStmt.setString(1,username);
            preStmt.setString(2,password);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {

                    String user = result.getString("Username");
                    String pass = result.getString("Password");
                    Volunteer volunteer=new Volunteer(user,pass);
                    logger.traceExit(volunteer);
                    return volunteer;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit("No volunteer found with username {}", username);

        //return null;
        throw  new TeledonException("No volunteer found with username "+username);
    }
}
