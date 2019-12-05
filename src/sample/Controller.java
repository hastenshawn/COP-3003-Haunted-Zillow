package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Controller {

    private static final String USER_AGENT = "Mozilla/5.0";
    @FXML
    TextField stateText = new TextField();
    @FXML
    TextField cityText = new TextField();
    @FXML
    TextField zipcodeText = new TextField();
    @FXML
    TextField addressText = new TextField();
    @FXML
    Pane paneField = new Pane();

    @FXML
    Pane grid_1_1 = new Pane();
    @FXML
    Pane grid_1_2 = new Pane();
    @FXML
    Pane grid_1_3 = new Pane();

    @FXML
    Pane grid_2_1 = new Pane();
    @FXML
    Pane grid_2_2 = new Pane();
    @FXML
    Pane grid_2_3 = new Pane();

    @FXML
    Pane grid_3_1 = new Pane();
    @FXML
    Pane grid_3_2 = new Pane();
    @FXML
    Pane grid_3_3 = new Pane();
    public void findProperties()throws IOException, SQLException {
        String State = stateText.getText();
        String City = cityText.getText();
        String Zipcode = zipcodeText.getText();
        String Address = addressText.getText();

        connManager conMan = new connManager();
        //Address, City, State, Zip Code
        String[] searchValues = {Address, City, State, Zipcode};
        conMan.selectProperties(searchValues);


        ArrayList<String> listOfStates = conMan.getStates();
        ArrayList<String> listOfCities = conMan.getCities();
        ArrayList<String> listOfAddresses = conMan.getAddresses();
        ArrayList<String> listOfZips = conMan.getZips();
        ArrayList<String> listOfZPIDS = conMan.getZPIDS();

        ArrayList<Pane> gridPanes = new ArrayList<Pane>();
        gridPanes.add(grid_1_1);
        gridPanes.add(grid_2_1);
        gridPanes.add(grid_3_1);

        gridPanes.add(grid_1_2);
        gridPanes.add(grid_2_2);
        gridPanes.add(grid_3_2);

        gridPanes.add(grid_1_3);
        gridPanes.add(grid_2_3);
        gridPanes.add(grid_3_3);

        int xPos = 100;
        int yPos = 100;

        int sizeOfResponse = listOfCities.size();
        if(sizeOfResponse>9){
            sizeOfResponse = 9;
        }
        for(int i = 0; i<gridPanes.size(); i++){
            gridPanes.get(i).getChildren().clear();
        }
        for(int i = 0; i<sizeOfResponse; i++){
            Pane testCard = new Pane();

            testCard.setMinWidth(30);
            testCard.setMinHeight(30);

            Label addressLabel = new Label(listOfAddresses.get(i));
            addressLabel.setLayoutX(20);
            addressLabel.setLayoutY(20);
            addressLabel.setStyle("-fx-alignment: center ;");

            Label cityLabel = new Label(listOfCities.get(i)+", ");
            cityLabel.setLayoutX(20);
            cityLabel.setLayoutY(40);
            cityLabel.setStyle("-fx-alignment: center ;");

            Label zipcodeLabel = new Label(listOfStates.get(i)+" "+listOfZips.get(i));
            zipcodeLabel.setLayoutX(20);
            zipcodeLabel.setLayoutY(60);
            zipcodeLabel.setStyle("-fx-alignment: center ;");

            Button selectButton = new Button("Select");
            selectButton.setLayoutX(20);
            selectButton.setLayoutY(90);
            selectButton.setStyle("-fx-background-color: #4d9dff; -fx-text-fill: #fff;-fx-alignment: center ;");
            selectButton.setPadding(new Insets(12, 12, 12, 12));
            selectButton.setMinWidth(75);
            selectButton.setId(listOfZPIDS.get(i));

            final String zest = listOfZPIDS.get(i);

            Label holderLabel = new Label("");
            holderLabel.setLayoutX(170);
            holderLabel.setLayoutY(20);
            Label zestLabel = new Label("");
            zestLabel.setLayoutX(170);
            zestLabel.setLayoutY(40);

            selectButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    try {
                        getZestimate(zest, holderLabel, zestLabel);
                    } catch (ProtocolException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            testCard.getChildren().add(addressLabel);
            testCard.getChildren().add(cityLabel);
            testCard.getChildren().add(zipcodeLabel);
            testCard.getChildren().add(holderLabel);
            testCard.getChildren().add(zestLabel);
            testCard.getChildren().add(selectButton);

            testCard.setStyle("-fx-background-color: #ccc");
            testCard.setPadding(new Insets(12, 12, 12, 12));

            testCard.setMinWidth(300);
            testCard.setMaxWidth(300);

            gridPanes.get(i).getChildren().add(testCard);
            System.out.println(listOfAddresses.get(i));
            System.out.println(i);
            System.out.println(xPos);
            System.out.println(yPos);
            System.out.println("\n");
            /*System.out.println("Cities = "  + listOfCities.get(i));
            System.out.println("Addresses = "  + listOfAddresses.get(i));
            System.out.println("Zips = "  + listOfZips.get(i));
            System.out.println("\n");*/
        }
        if(sizeOfResponse==0){
            Pane testCard = new Pane();

            testCard.setMinWidth(30);
            testCard.setMinHeight(30);

            Label addressLabel = new Label("No Results!");
            addressLabel.setLayoutX(20);
            addressLabel.setLayoutY(20);
            addressLabel.setStyle("-fx-alignment: center ;");

            testCard.getChildren().add(addressLabel);

            testCard.setMinWidth(300);
            testCard.setMaxWidth(300);

            gridPanes.get(0).getChildren().add(testCard);
            System.out.println("No Records!");
        }
    }
    public void getZestimate(String zest, Label holderLab, Label zestLab) throws IOException {
        final String GET_URL = "http://www.zillow.com/webservice/GetZestimate.htm?zws-id=X1-ZWz17icwdntte3_6ttia&zpid="+zest;
        System.out.println(GET_URL);
        System.out.println(zest);
        URL obj = new URL(GET_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            String fullResponse = response.toString();
            int startChar = fullResponse.indexOf("<amount currency=\"USD\">")+23;
            int endChar = fullResponse.indexOf("</amount>");
            String zestimate = fullResponse.substring(startChar,endChar);
            holderLab.setText("Zestimated");
            zestLab.setText("$"+zestimate+".00");
            System.out.println("Total Zestimate: $"+zestimate);
        } else {
            System.out.println("GET request not worked");
        }
    }
}