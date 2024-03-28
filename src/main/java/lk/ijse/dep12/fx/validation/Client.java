package lk.ijse.dep12.fx.validation;

import java.time.LocalDate;
import java.util.ArrayList;

public class Client {
    private String id;
    private String name;
    private String address;
    private String gender;
    private LocalDate dob;
    private ArrayList<String> properties;

    public Client(String id, String name, String address, String gender, LocalDate dob, ArrayList<String> properties) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.gender = gender;
        this.dob = dob;
        this.properties = properties;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getDob() {
        return dob;
    }

    public ArrayList<String> getProperties() {
        return properties;
    }
}
