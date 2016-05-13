package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javafx.application.Platform.exit;

public class Controller {

    @FXML private Button skip;
    @FXML private Button splitbutton;
    @FXML private Button skip2;
    @FXML private Button savebutton;
    @FXML private Button exitbutton;
    @FXML private Button combinebutton;

    @FXML private TextArea secretbox;
    @FXML private TextArea outputbox;

    @FXML private TextField sharebox;
    @FXML private TextField threshbox;

    @FXML private TextField shareboxnumcombine;
    @FXML private TextArea shareboxcombine;
    @FXML private TextField keyboxcombine;
    @FXML private TextArea outputboxcombine;


    @FXML private CheckBox tik1;


    @FXML
    public void printHello(ActionEvent event) throws IOException{
        Stage stage;
        Parent root;
        if(event.getSource()==skip){
            //get reference to the button's stage
            stage=(Stage) skip.getScene().getWindow();
            //load up OTHER FXML document
            root = FXMLLoader.load(getClass().getResource("sample2.fxml"));
        }
        else{
            stage=(Stage) skip2.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        }
        //create a new scene with root and set the stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    ///////////////////////////////////////////////////////////////////////
    /////////////////////////SPLIT/////////////////////////////////////////
    @FXML
    public void dothesplit(ActionEvent e) {


        ////////////////////////////////////////////////////////////
        //invalid input handling
        if (secretbox.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("No secret found");
            alert.showAndWait();

        } else if (sharebox.getText().isEmpty() || !isInt(sharebox.getText())
                || Integer.parseInt(sharebox.getText()) <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Enter a valid number of shares");
            alert.showAndWait();


        } else if (threshbox.getText().isEmpty() || !isInt(threshbox.getText())
                || Integer.parseInt(threshbox.getText()) <= 0
                || Integer.parseInt(threshbox.getText()) > Integer.parseInt(sharebox.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Enter a valid threshold =< number of shares");
            alert.showAndWait();
            ////////////////////////////////////////////////////////////

        } else {
            String secret = secretbox.getText();
            System.out.println("Secret: " + secret);
            int shares = Integer.parseInt(sharebox.getText());
            System.out.println("Shares: " + shares);
            int thresh = Integer.parseInt(threshbox.getText());
            System.out.println("Threshold: " + thresh);

            String[] s = MainSplit.split(secret, shares, thresh);
            outputbox.setText("Shares:\n" + s[0] + "\nThreshold:\n" + s[2] + "\n\nKey:\n" + s[1]);
        }
    }




    @FXML
    public void saveFile(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        fileChooser.setInitialFileName("Shares.txt");
        File file = fileChooser.showSaveDialog(Main.stage);

        if(file != null){
            SaveFile(outputbox.getText(), file);
        }

    }


    private void SaveFile(String content, File file) {
        try {
            FileWriter fileWriter = null;

            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    ///////////////////////////////////////////////////////////////////////
    /////////////////////////RECONSTRUCTION////////////////////////////////

    @FXML
    public void dothecombine(ActionEvent e) {

        if (shareboxnumcombine.getText().isEmpty() || !isInt(shareboxnumcombine.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Share number not valid");
            alert.showAndWait();

            //TODO: implement share validation
        } else if (shareboxcombine.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Shares are not valid");
            alert.showAndWait();


        } else if (keyboxcombine.getText().isEmpty() || !isInt(keyboxcombine.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Key not valid");
            alert.showAndWait();

        } else {
            int numshares = Integer.parseInt(shareboxnumcombine.getText());
            System.out.println("Number of shares: " + numshares);
            String shares = shareboxcombine.getText();
            System.out.println("Shares: \n" + shares);
            BigInteger key = new BigInteger(keyboxcombine.getText());
            System.out.println("Key: " + key);

            String s = MainReconstruct.reconstruct(numshares, shares, key);
            outputboxcombine.setText(s);
        }
    }




    ///////////////////////////////////////////////////////////////////////
    /////////////////////////MAIN//////////////////////////////////////////
    @FXML
    public void quit(ActionEvent e) {
        exit();
    }



    private boolean isInt(String text)
    {
        return text.matches("[0-9]*");
    }


}
