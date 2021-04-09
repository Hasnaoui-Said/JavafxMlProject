package sample;


import com.fasterxml.jackson.databind.util.JSONPObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Controller {
    @FXML
    public TextField fieldPclass;
    @FXML
    public TextField fieldSbSp;
    @FXML
    public TextField fieldPaCh;
    @FXML
    public RadioButton radioGenderM;
    @FXML
    public RadioButton radioGenderF;
    @FXML
    public TextField fieldFare;
    @FXML
    public Button btnPredicate;
    @FXML
    private TextField fieldAge;
    @FXML
    private Button predict;
    String male ;
    public void getResult()
    {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:8000/"))
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .join();
    }

    public void nextView(ActionEvent event) throws IOException {

        Stage stage;
        Parent root;
        if( event.getSource() == predict ){
            System.out.println("pressed");
            stage = (Stage) predict.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("predict.fxml"));

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }


       /* FXMLLoader loader = new FXMLLoader(getClass().getResource("predictform.fxml"));
        predict.getScene().setRoot(loader.getRoot());*/

    }

    public void getPrediction(ActionEvent event) throws IOException, InterruptedException {
        if(event.getSource() ==  btnPredicate)
        {
           System.out.println(" Age "+fieldAge.getText()+" PClass "+fieldPclass.getText()+
                   " Fare "+fieldFare.getText()+
                   " Gender "+radioGenderF.isSelected()+" "+radioGenderM.isSelected()+
                   " Parent/Children "+ fieldPaCh.getText()+
                   " Siblinges/Spouses "+ fieldSbSp.getText());

           if(radioGenderF.isSelected())
               male = "false";
            if(radioGenderM.isSelected())
                male = "true";
                var values = new HashMap<String, String>() {{
                    put ("pclass", fieldPclass.getText());
                    put("male", male);
                    put("age", fieldAge.getText());
                    put ("siblings_Spouses", fieldSbSp.getText());
                    put("parents_Children", fieldPaCh.getText());
                    put ("fare", fieldFare.getText());


            }};

            var objectMapper = new ObjectMapper();
            var requestBody = objectMapper
                    .writeValueAsString(values);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:8000/predict"))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .setHeader("Content-Type", "application/json")
                    .build();
         HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
            HttpResponse<InputStream> responses = client.send(request,
                    HttpResponse.BodyHandlers.ofInputStream());

            System.out.println(requestBody);
            System.out.println(response.body());
            ResPredict requestBodydeserialisable = objectMapper.readValue(response.body(),ResPredict.class);

            System.out.println(requestBodydeserialisable);
            System.out.println("Prediction :"+requestBodydeserialisable.getPrediction().toString().substring(1,2));
            String predictionValue = requestBodydeserialisable.getPrediction().toString().substring(1,2);
            if(predictionValue.equals("1")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("MLFXProject");
                alert.setHeaderText("Prediction Result");
                String s ="The Passenger will Servive ";
                alert.setContentText(s);
                alert.show();
                System.out.println("The Passenger you will Servive");
            }
            if(predictionValue.equals("0")){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("MLFXProject");
                alert.setHeaderText("Prediction Result");
                String s ="The Passenger will not Servive ";
                alert.setContentText(s);

                alert.show();
                System.out.println("The Passenger will not Servive ");
            }










            System.out.println(response.body());
        }
    }
}
