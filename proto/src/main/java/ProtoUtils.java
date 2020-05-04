import entities.Case;
import entities.Donor;
import entities.Volunteer;

import java.util.ArrayList;
import java.util.List;

public class ProtoUtils {
    public static Model.Request createLoginRequest(Volunteer volunteer){
        Model.Volunteer protoVolunteer= Model.Volunteer.newBuilder().setUsername(volunteer.getUsername()).setPassword(volunteer.getPassword()).build();
        Model.Request request= Model.Request.newBuilder().setType(Model.Request.Type.Login).setVolunteer(protoVolunteer).build();
        return request;
    }

    public static Model.Request createLogoutRequest(){
        Model.Request request= Model.Request.newBuilder().setType(Model.Request.Type.Logout).build();
        return request;
    }

    public static Model.Request createNewDonationRequest(Integer donorId, String name, String address, String telephone, Double sum, Integer caseId){
        Model.Request request= Model.Request.newBuilder().setType(Model.Request.Type.NewDonation).setDonorId(donorId).setName(name).setAddress(address).setTelephone(telephone).setSum(sum).setCaseId(caseId).build();
        return request;
    }

    public static Model.Request createGetAllDonorsRequest(String substring){
        Model.Request request= Model.Request.newBuilder().setType(Model.Request.Type.GetAllDonors).setSubstring(substring).build();
        return request;
    }

    public static Model.Request createGetAllCasesRequest(){
        Model.Request request= Model.Request.newBuilder().setType(Model.Request.Type.GetAllCases).build();
        return request;
    }

    public static String getError(Model.Response response){
        String text=response.getError();
        return text;
    }

    public static List<Case> getCases(Model.Response response) {
        List<Case> cases = new ArrayList<>();
        for (Model.Case actualCase:
                response.getCasesList()) {
            Case myCase = new Case(actualCase.getId(), actualCase.getName(), actualCase.getSum());
            cases.add(myCase);
        }
        return cases;
    }

    public static List<Donor> getDonors(Model.Response response){
        List<Donor> donors=new ArrayList<>();
        for(Model.Donor actualDonor: response.getDonorsList()){
            Donor myDonor=new Donor(actualDonor.getId(),actualDonor.getName(),actualDonor.getAddress(),actualDonor.getTelephone());
            donors.add(myDonor);
        }
        return  donors;
    }
}
