package repos;

import entities.Case;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CaseRepository  implements ICaseRepository {
    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public CaseRepository(Properties props){
        logger.info("Initializing CaseRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    public Case findOne(Integer integer) {
        logger.traceEntry("finding case with id {} ",integer);
        Connection con=dbUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select * from Cases where Id=?")){
            preStmt.setInt(1,integer);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("Id");
                    String name = result.getString("Name");
                    Double totalSum=result.getDouble("TotalSum");
                    Case donationCase = new Case(id, name, totalSum);
                    logger.traceExit(donationCase);
                    return donationCase;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit("No case found with id {}", integer);

        return null;
    }

    public Iterable<Case> findAll() {
        Connection con=dbUtils.getConnection();
        List<Case> cases=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Cases")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("Id");
                    String name = result.getString("Name");
                    Double totalSum = result.getDouble("TotalSum");
                    Case donationCase = new Case(id, name, totalSum);
                    cases.add(donationCase);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(cases);
        return cases;
    }

    @Override
    public void update(Double sum, Integer id) {
        logger.traceEntry("updating case");
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update Cases set TotalSum=? where Id=?")){

            preStmt.setDouble(1,sum);
            preStmt.setInt(2,id);
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }
}
