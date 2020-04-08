package repos;

import entities.Donation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class DonationRepository implements IDonationRepository {
    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public DonationRepository(Properties props){
        logger.info("Initializing DonationRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    public void save(Donation entity) {
        logger.traceEntry("saving donation {} ",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Donation values (?,?,?)")){
            preStmt.setInt(1,entity.getDonorId());
            preStmt.setDouble(2,entity.getSum());
            preStmt.setInt(3,entity.getCaseId());

            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();

    }

}
