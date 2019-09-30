package application;
	
import javax.swing.event.ChangeListener;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
//import javafx.scene.layout.BorderPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;


public class Main extends Application {
	
	private double xOffset = 0; 
	private double yOffset = 0;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/Main.fxml"));
			
			Scene scene = new Scene(root);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
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
	        primaryStage.getIcons().add(new Image("/images/51169520_1162196227295479_5589004336278536192_n.png"));
	        primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
		
	}
}
