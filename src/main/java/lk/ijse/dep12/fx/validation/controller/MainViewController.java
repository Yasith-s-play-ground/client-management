package lk.ijse.dep12.fx.validation.controller;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lk.ijse.dep12.fx.validation.Client;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainViewController {
    public TextField txtId;
    public Label lblNIC;
    public TextField txtNIC;
    public Label lblName;
    public TextField txtName;
    public Label lblAddress;
    public TextField txtAddress;
    public Label lblGender;
    public RadioButton rdBtnMale;
    public RadioButton rdBtnFemale;
    public Label lblDOB;
    public DatePicker dtPckrBirthday;
    public Label lblBelow18;
    public Label lblProperty;
    public CheckBox chkLand;
    public CheckBox chkHouse;
    public CheckBox chkCarVan;
    public CheckBox chkMotorBike;
    public Button btnNewClient;
    public Button btnSave;
    public ToggleGroup grpGender;
    public VBox mainVBox;
    public AnchorPane root;

    private ArrayList<Client> clientList = new ArrayList<>();

    private String generateClientId() {
        if (clientList.isEmpty()) return "C-0001";
        else {
            int nextidNum = Integer.parseInt(clientList.getLast().getId().substring(2)) + 1;
            return "C-%04d".formatted(nextidNum);
        }
    }

    private boolean isClientAbove18Years() {
        if (dtPckrBirthday.getValue() != null) {
            LocalDate birthDate = dtPckrBirthday.getValue();
            LocalDate currentDate = LocalDate.now();
            Period period = Period.between(birthDate, currentDate);
            int noOfYears = period.getYears(); // no of years between 2 dates
            if (noOfYears < 18) lblBelow18.setVisible(true);
            else lblBelow18.setVisible(false);
            return noOfYears >= 18;
        }
        return false;
    }

    private boolean checkIsNICValid() {
        boolean valid = true;
        String nic = txtNIC.getText().strip();
        if (nic.isEmpty()) valid = false;
        else {
            if ((nic.endsWith("V") || nic.endsWith("v")) && (nic.length() == 10)) {
                for (int i = 0; i < 9; i++) {
                    if (!Character.isDigit(nic.charAt(i))) {
                        valid = false;
                        break;
                    }
                }
            } else valid = false;
        }
        return valid;
    }

    private boolean checkNameValid() {
        // boolean valid = true;
        String name = txtName.getText().strip();
//        Pattern p = Pattern.compile("^[ A-Za-z]+$");
//        Matcher m = p.matcher(name);
//        valid = m.matches();


//        for (int i = 0; i < name.length(); i++) {
//            if (!Character.isLetter(name.charAt(i))) {
//                valid = false;
//                break;
//            }
//        }
        if (name.isEmpty()) return false;
        //check characters as an array
        for (char c : name.toCharArray()) {
            if (!(Character.isLetter(c) || Character.isSpaceChar(c))) {
                return false;
            }
        }
        return true;
    }

    private boolean isAddressValid() {
        String address = txtAddress.getText();
        return address.isEmpty() || address.strip().length() >= 4;
    }

    private boolean isPropertySelectionValid() {
        boolean valid = false;
        for (CheckBox checkBox : new CheckBox[]{chkMotorBike, chkLand, chkHouse, chkCarVan}) {
            if (checkBox.isSelected()) {
                return true;
//                valid = true;
//                break;
            }
        }
        //return valid;
        return false;
    }

    private void enableUI(boolean enable) {
        for (TextField textField : new TextField[]{txtId, txtNIC, txtAddress, txtName}) {
            textField.setDisable(!enable);
            textField.clear();
        }

        grpGender.selectToggle(null);
        //rdBtnMale.setSelected(false);
        rdBtnMale.setDisable(!enable);
        //rdBtnFemale.setSelected(false);
        rdBtnFemale.setDisable(!enable);

        dtPckrBirthday.setDisable(!enable);
        if (!enable) dtPckrBirthday.setValue(null);

        for (CheckBox checkBox : new CheckBox[]{chkLand, chkHouse, chkCarVan, chkMotorBike}) {
            checkBox.setDisable(!enable);
            checkBox.setSelected(false);
        }
        lblBelow18.setVisible(false);
        btnSave.setDisable(!enable);
    }

    public void initialize() {
        enableUI(false);
        for (Node node : root.lookupAll(",label")) {
            Label lbl = (Label) node;
            lbl.setLabelFor(root.lookup(lbl.getAccessibleText()));
        }

        //when we set accessible text, it can be retrieved like this


//        for (Node node : mainVBox.getChildren()) { // iterate along HBoxes which are in the VBox
//            HBox hBox = (HBox) node;
//            for (int i = 0; i < hBox.getChildren().size(); i++) { // iterate along the controls in the selected HBox
//                if (hBox.getChildren().get(i) instanceof Label lbl && !lbl.equals(lblBelow18)) { // there is no component after below 18 label, there would be array index out of bounds error if it was checked
//                    lbl.setLabelFor(hBox.getChildren().get(i + 1));  // set label for the text field next to it
//                    if (hBox.getChildren().get(i + 1) instanceof VBox vBox) {
//                        lbl.setLabelFor(vBox.getChildren().get(0)); // get first check box in v box
//                    }
//
//                }
//            }
//        }
    }

    public void btnNewClientOnAction(ActionEvent actionEvent) {
        enableUI(true);
        txtId.setText(generateClientId());
        txtNIC.requestFocus();
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        boolean validation = true;
        for (Control control : new Control[]{txtNIC, txtAddress, txtName, rdBtnFemale, rdBtnMale, dtPckrBirthday, chkLand, chkHouse, chkCarVan, chkMotorBike, lblDOB, lblAddress, lblGender, lblName, lblNIC, lblProperty}) {
            control.getStyleClass().remove("error");
        }

        if (!isPropertySelectionValid()) {
            lblProperty.getStyleClass().add("error");
            for (CheckBox checkBox : new CheckBox[]{chkLand, chkHouse, chkCarVan, chkMotorBike}) {
                checkBox.getStyleClass().add("error");
                validation = false;
            }
            chkLand.requestFocus();
        }

        if (!isClientAbove18Years()) {
            validation = false;
            lblDOB.getStyleClass().add("error");
            dtPckrBirthday.requestFocus();
        }

        if (grpGender.getSelectedToggle() == null) {
            validation = false;
            lblGender.getStyleClass().add("error");
            rdBtnMale.getStyleClass().add("error");
            rdBtnFemale.getStyleClass().add("error");
            rdBtnMale.requestFocus();
        }

        if (!isAddressValid()) {
            validation = false;
            lblAddress.getStyleClass().add("error");
            txtAddress.getStyleClass().add("error");
            txtAddress.requestFocus();
        }

        if (!checkNameValid()) {
            validation = false;
            lblName.getStyleClass().add("error");
            txtName.getStyleClass().add("error");
            txtName.requestFocus();
        }

        if (!checkIsNICValid()) {
            validation = false;
            lblNIC.getStyleClass().add("error");
            txtNIC.getStyleClass().add("error");
            txtNIC.requestFocus();
        }

        if (!validation) return;

        ArrayList<String> properties = new ArrayList<>();
        for (CheckBox checkBox : new CheckBox[]{chkLand, chkHouse, chkCarVan, chkMotorBike}) {
            properties.add(checkBox.getText());
        }
        Client client = new Client(txtId.getText(), txtName.getText().strip(), txtAddress.getText().strip(), ((RadioButton) grpGender.getSelectedToggle()).getText(), dtPckrBirthday.getValue(), properties);
        clientList.add(client);

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Client has been successfully added to the list!");
        alert.show();

        enableUI(false);
    }

    public void dtPckrBirthdayOnAction(ActionEvent actionEvent) {
        isClientAbove18Years();
    }


}
