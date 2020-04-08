package repos;

import entities.Donor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DonorRepository implements IDonorRepository {
    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public DonorRepository(Properties props){
        logger.info("Initializing DonorRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public void save(Donor entity) {
        logger.traceEntry("saving donor {} ",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Donor values (?,?,?,?)")){
            preStmt.setInt(1,entity.getId());
            preStmt.setString(2,entity.getName());
            preStmt.setString(3,entity.getAddress());
            preStmt.setString(4,entity.getTelephone());

            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();

    }

    @Override
    public Donor findOne(Integer integer) {
        logger.traceEntry("finding donor with id {} ",integer);
        Connection con=dbUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select * from Donor where Id=?")){
            preStmt.setInt(1,integer);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("Id");
                    String name = result.getString("Name");
                    String address = result.getString("Address");
                    String telephone=result.getString("Telephone");
                    Donor donor = new Donor(id, name, address,telephone);
                    logger.traceExit(donor);
                    return donor;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit("No donor found with id {}", integer);

        return null;
    }

    @Override
    public Iterable<Donor> findAll() {
        Connection con=dbUtils.getConnection();
        List<Donor> donors=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Donor")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("Id");
                    String name = result.getString("Name");
                    String address = result.getString("Address");
                    String telephone=result.getString("Telephone");
                    Donor donor = new Donor(id, name, address,telephone);
                    donors.add(donor);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(donors);
        return donors;
    }

    public Iterable<Donor> findByName(String word) {
        Connection con=dbUtils.getConnection();
        List<Donor> donors=new ArrayList<>();
        //"select * from Donor Where Name Like '%" + substring + "%'"
        try(PreparedStatement preStmt=con.prepareStatement("select * from Donor where name like '%"+word+"%' ")) {
            //preStmt.setString(1,word);
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("Id");
                    String name = result.getString("Name");
                    String address = result.getString("Address");
                    String telephone=result.getString("Telephone");
                    Donor donor = new Donor(id, name, address,telephone);
                    donors.add(donor);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(donors);
        return donors;
    }
}
