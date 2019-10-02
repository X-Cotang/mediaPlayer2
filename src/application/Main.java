package application;
	
import java.io.IOException;
import java.net.URI;
import java.net.URL;

import javax.swing.event.ChangeListener;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
//import javafx.scene.layout.BorderPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
//import javafx.fxml.FXMLController;


public class Main extends Application {
	
	private double xOffset = 0; 
	private double yOffset = 0;
	private String path;
	@Override
	public void start(Stage primaryStage) {
		try {
			Alert alert = new Alert(AlertType.INFORMATION);
			FXMLLoader loader=new FXMLLoader(getClass().getResource("/application/Main.fxml"));
			Parent root = (Parent)loader.load();
			
			Scene scene = new Scene(root);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			MainController controller=loader.getController();
			
		    // appController.setMain(this);
			//scene.setFill(Color.TRANSPARENT);
			//primaryStage.initStyle(StageStyle.TRANSPARENT);
			
			primaryStage.setScene(scene);
			primaryStage.setTitle("From KMA WITH LUV <3");
			//primaryStage.setIconified(true);
			//primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setMinHeight(350.0);
			primaryStage.setMinWidth(580.0);
			//System.out.println(scene.getWidth());
			//System.out.println(primaryStage.getWidth());
			//System.out.println(scene.getHeight());
			//System.out.println(primaryStage.getHeight());
			scene.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {
					// TODO Auto-generated method stub
					if(arg0.getClickCount()==2) {
						primaryStage.setFullScreen(true);
					}
					
				}
				
			});
			
			root.setOnMousePressed(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent event) {
	                xOffset = event.getSceneX();
	                yOffset = event.getSceneY();
	            }
	        });
	        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent event) {
	            	if(primaryStage.isFullScreen()==false) {
	                primaryStage.setX(event.getScreenX() - xOffset);
	                primaryStage.setY(event.getScreenY() - yOffset);
	            	}
	            }
	        });
			
			
			/*
			scene.widthProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
					// TODO Auto-generated method stub
					System.out.println(arg2.toString());
				}
			});
			scene.heightProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
					// TODO Auto-generated method stub
					System.out.println("h:"+arg2.toString());
				}
			});
			*/
	        //System.out.println(controller);
	        primaryStage.getIcons().add(new Image("/images/51169520_1162196227295479_5589004336278536192_n.png"));
	        primaryStage.show();
	        
	        
	        
	        Stage stage2=new Stage();
	        FXMLLoader loader2 =new FXMLLoader(getClass().getResource("/application/Online.fxml"));
	        Parent root2 = (Parent)loader2.load();
	        OnlineController controller2=loader2.getController();
			Scene scene2 = new Scene(root2);
			stage2.setScene(scene2);
			stage2.initStyle(StageStyle.UNDECORATED);
			//String path;
			scene.setOnKeyPressed((KeyEvent event) -> {
	            //System.out.println(event.getCode().toString()=="M");
	            if(event.getCode().toString()=="U") {
	            	stage2.show();
	            	//while(stage2.isShowing()) {
	            		controller2.bt_go.setOnMouseClicked(new EventHandler<MouseEvent>() {

							@Override
							public void handle(MouseEvent arg0) {
								// TODO Auto-generated method stub
								path=controller2.tf_url.getText();			
								controller.path=path;
								try {
									if(isValid(path)) {
										controller.runOnline();
									}else {
										alert.setTitle("Error");
								        alert.setHeaderText(null);
								        alert.setContentText("Đường dẫn không hợp lệ");
								        alert.showAndWait();
									}
								} catch (IOException e) {
									// TODO Auto-generated catch block
									//e.printStackTrace();
									alert.setTitle("Error");
							        alert.setHeaderText(null);
							        alert.setContentText("Lỗi Kết Nối");
							        alert.showAndWait();
								}
								stage2.close();
							}
						});
	            	
	            }           
			});
			//stage2.isShowing()
			//stage2.close();
	        //controller.setPath("https://nil1stest1null.000webhostapp.com/testAnull/Alan%20Walker%20-%20Faded.mp4");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
		
	}
	public static boolean isValid(String url) 
    { 
        /* Try creating a valid URL */
        try { 
            new URL(url).toURI();
            URI test=new URI(url);
            System.out.println(test.getHost());
            if(!test.getHost().equals("nil1stest1null.000webhostapp.com")) {
            	return false;
            }
            return true; 
        } 
          
        // If there was an Exception 
        // while creating URL object 
        catch (Exception e) { 
            return false; 
        }
        
    } 
}
