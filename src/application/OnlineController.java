package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class OnlineController implements Initializable {
	@FXML
	Button bt_go;
	@FXML
	Button bt_cancel;
	@FXML
	TextField tf_url;
	String path;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		/*
		bt_go.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				Stage stage=(Stage)bt_cancel.getScene().getWindow();
				//Stage stage2=(Stage)stage.getScene().getWindow();
				//stage2.
				stage.close();
			}
		});
		*/
		bt_cancel.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				Stage stage=(Stage)bt_cancel.getScene().getWindow();
				stage.close();
				//System.out.println("test");
			}
		});
	}
	
	
	
	
	
	// doc file online
	
}
