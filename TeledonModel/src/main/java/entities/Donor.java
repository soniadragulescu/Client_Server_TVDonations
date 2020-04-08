package entities;

import java.io.Serializable;

public class Donor extends Entity<Integer>{
    private String name;
    private String address;
    private String telephone;

    public Donor(Integer id, String name, String address, String telephone) {
        this.setId(id);
        this.name = name;
        this.address = address;
        this.telephone = telephone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
