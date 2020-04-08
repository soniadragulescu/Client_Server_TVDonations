package controllers;

import entities.Case;
import entities.Donor;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import service.ITeledonObserver;
import service.ITeledonService;
import repos.TeledonException;

import java.util.List;

public class DonationController implements ITeledonObserver {
    //private DonationService donationService;
    private ITeledonService server;
    private ObservableList<Donor> donors= FXCollections.observableArrayList();
    private ObservableList<Case> cases=FXCollections.observableArrayList();

    @FXML
    TableView<Donor> tableDonor;

    @FXML
    TableColumn<Donor,Integer> columnDonorId;

    @FXML
    TableColumn<Donor,String> columnDonorName;

    @FXML
    TableColumn<Donor,String> columnAddress;

    @FXML
    TableColumn<Donor,String> columnTelephone;

    @FXML
    TextField textFieldDonor;

    @FXML
    Button buttonSearch;

    @FXML
    TableView<Case> tableCase;

    @FXML
    TableColumn<Case,Integer> columnCaseId;

    @FXML
    TableColumn<Case,String> columnCaseName;

    @FXML
    TableColumn<Case,Double> columnTotalSum;

    @FXML
    TextField textFieldDonorId;

    @FXML
    TextField textFieldDonorName;

    @FXML
    TextField textFieldAddress;

    @FXML
    TextField textFieldTelephone;

    @FXML
    TextField textFieldCaseId;

    @FXML
    TextField textFieldSum;

    @FXML
    Button buttonDonate;

    @FXML
    Button buttonLogout;


    public void init() throws TeledonException {
       donors.setAll(this.server.searchDonorByName(""));
       cases.setAll(this.server.getAllCases());

        columnDonorId.setCellValueFactory(new PropertyValueFactory<Donor,Integer>("id"));
        columnDonorName.setCellValueFactory(new PropertyValueFactory<Donor,String>("name"));
        columnAddress.setCellValueFactory(new PropertyValueFactory<Donor,String>("address"));
        columnTelephone.setCellValueFactory(new PropertyValueFactory<Donor,String>("telephone"));
        tableDonor.setItems(donors);

        columnCaseId.setCellValueFactory(new PropertyValueFactory<Case,Integer>("id"));
        columnCaseName.setCellValueFactory(new PropertyValueFactory<Case,String>("name"));
        columnTotalSum.setCellValueFactory(new PropertyValueFactory<Case,Double>("totalSum"));
        tableCase.setItems(cases);

        tableDonor.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            Donor donor=newValue;
            textFieldDonorId.setText(""+donor.getId().toString());
            textFieldDonorName.setText(""+donor.getName());
            textFieldAddress.setText(""+donor.getAddress());
            textFieldTelephone.setText(""+donor.getTelephone());

        }));

        tableCase.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            Case caz=newValue;
            textFieldCaseId.setText(""+caz.getId().toString());
            textFieldSum.setText("");
        }));

        buttonLogout.setOnAction(x-> {
            try {
                server.logout(this);
            } catch (TeledonException e) {
                e.printStackTrace();
            }
            Platform.exit();});
        buttonSearch.setOnAction(x-> {
            try {
                searchByName(textFieldDonor.getText());
            } catch (TeledonException e) {
                e.printStackTrace();
            }
        });
        buttonDonate.setOnAction(x->donate());

    }

    public void searchByName(String substring) throws TeledonException {
        donors.setAll(this.server.searchDonorByName(substring));
    }

    public void donate(){
        Integer donorId=Integer.parseInt(textFieldDonorId.getText());
        String name=textFieldDonorName.getText();
        String address=textFieldAddress.getText();
        String telephone=textFieldTelephone.getText();

        Integer caseId=Integer.parseInt(textFieldCaseId.getText());
        Double sum=Double.parseDouble(textFieldSum.getText());
        try
            {
                this.server.saveDonation(donorId,name, address, telephone,sum,caseId);
//                donors.setAll(server.searchDonorByName(""));
//                cases.setAll(server.getAllCases());
            }
        catch(Exception ex){
            showErrorMessage(ex.getMessage());
        }
    }

    private static void showErrorMessage(String err){
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Error message!");
        message.setContentText(err);
        message.showAndWait();
    }
    public void setService(ITeledonService server) throws TeledonException {
        //this.donationService=donationService;
        this.server=server;
    }

    @Override
    public void donationDone(List<Donor> donors, List<Case> cases) throws TeledonException {
       //donors.setAll(server.searchDonorByName(""));
       //cases.setAll(server.getAllCases());
        this.donors.setAll(donors);
        this.cases.setAll(cases);
    }
}
