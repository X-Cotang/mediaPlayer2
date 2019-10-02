package application;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
//import com.sun.tools.javac.code.Attribute.Array;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import java.nio.file.Paths;

public class MainController implements Initializable{
	@FXML
	private StackPane sp_father; //Stackpane cha
	@FXML
	private BorderPane bdp_feature; // boderpane chứa các thanh âm lượng,thời gian
	@FXML
	private Pane p;
	@FXML 
	private MediaView mv;
	@FXML
	private VBox vb_inBdp;
	@FXML
	private Slider sl; //slider volume
	@FXML
	private Slider seeksl; //slider time
	@FXML
	private HBox hb_inVb_inBdp; //chưa cần thiết có thể xóa
	@FXML
	Label lb_time;
	@FXML
	Label lb_rate;
	@FXML
	Button play;
	@FXML
	Button replay;
	@FXML
	Button pause;
	@FXML
	Button next;
	@FXML
	Button pre;
	@FXML
	Button bt_unFv; //nút trái tim xanh
	@FXML
	Button bt_Fv; //nút trái tim hồng
	@FXML 
	ScrollPane scrP_fvVideo; // scrollp trong ds video yêu thích
	@FXML
	VBox vb_inScrP; //dsvb danh sách trong scrollpane ,chứa các button
	@FXML
	Button bt_listFv; //listFavo button hiển thị danh sách yêu thích
	@FXML
	HBox hb_father; //hbfv chứa hiển thị danh sách yêu thích,mục tiêu:ẩn hiện ds yêu thích khi ấn bt_listFv
	
	@FXML
	BorderPane bdp_lyric1;
	@FXML
	Label lb_lyricMain; //label hien thi lyric
	@FXML
	Label lb_lyric1;
	@FXML
	Label lb_lyric2;
	@FXML
	Label lb_lyric3;
	@FXML
	Label lb_lyric4;
	
	@FXML
	BorderPane bdp_lyric2;
	@FXML
	Label lb_lyricInBdp2;
	
	private MediaPlayer mp;
	//private Media me;
	private String s_rmFv;//rmfv lưu string chứa path cần xóa video khỏi danh sách yêu thích
	private double rate=1;
	String path;
	private boolean ready= false;
	private int id;
	private int len;
	private File dir; //thư mục video hiện hành
	private String[] myFiles;
	
	
	private String ext;
	private File file;
    private String[] st_favorite;
    private FileReader fwt;
    private File f_fv; //fv file favorite
	private List<String> listFv;
	private int int_timeLyric;
	private boolean isLyric=false;
	
	public void openfile(ActionEvent event) {
		rate=1;
		FileChooser filechooser = new FileChooser();
		FileChooser.ExtensionFilter filter=new FileChooser.ExtensionFilter("Select file mp4", "*.mp4","*.mp3");
		filechooser.getExtensionFilters().add(filter);
		file = filechooser.showOpenDialog(null);
		try {
			path = file.toURI().toString();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		//System.out.println(path.hashCode());
		if(path != null) {
			if(ready==true) {
				mp.stop();
				mp.dispose();		
			}
			else {
				ready=true;
			}
			ext=file.getName().substring(file.getName().lastIndexOf('.')); //get extension trong file
			//System.out.println(file.getParent().toString());
			dir=new File(file.getParent().toString());
			myFiles=dir.list(new FilenameFilter() {
	    	    public boolean accept(File directory, String fileName) {
	    	        return fileName.endsWith(ext);
	    	    }
	    	});
			
			id=(Arrays.asList(myFiles).indexOf(file.getName()));
			len=myFiles.length; //lưu len để next bài
			
			
			setNode(false);
			try {
				run();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	
	private void setNode(boolean isFv) {
		if(isFv==true) {
			next.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {
					nextFv();
				}
			});
			pre.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {
					// TODO Auto-generated method stub
					preFv();
				}
			});
			
		}
		else {
			next.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {
					// TODO Auto-generated method stub
					try {
						nextMD();
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			pre.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {
					// TODO Auto-generated method stub
					preMD();
				}
			});
		}
		
		
		vb_inBdp.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				ex();
			}
		});
		
		next.setOnMouseExited(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				ex();
			}
		});
		next.setOnMouseMoved(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				move();
			}
		});
		
		
		pre.setOnMouseMoved(new EventHandler<MouseEvent>() {
	
			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				move();
			}
		});
		
		pre.setOnMouseExited(new EventHandler<MouseEvent>() {
	
			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				ex();
			}
		});
		bt_unFv.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				try {
					favorite();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		bt_Fv.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				try {
					rmFavo();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
	}



	public void run() throws IOException {
		//path="https://nil1stest1null.000webhostapp.com/testAnull/Charlie%20Puth%20-%20Attention%20(%20cover%20by%20J.Fla%20).mp4";
		for(String s:listFv) {	
			if(s.equals(path)) {
				s_rmFv=s;
				bt_unFv.setVisible(false);
				bt_Fv.setVisible(true);
				break;
			}else {
				bt_unFv.setVisible(true);
				bt_Fv.setVisible(false);
			}
		}
		System.out.println(path);
		isLyric=false;
		rate=1;
		bdp_lyric1.setVisible(false);
		bdp_lyric2.setVisible(false);
		lb_rate.setText("x"+Double.toString(rate));
		play.setVisible(false);
		bdp_feature.setStyle("-fx-opacity:0");
		pause.setVisible(true);
		Media me = new Media(path);
		mp = new MediaPlayer(me);
		mv.setMediaPlayer(mp);
		mv.setCache(true);
		//System.out.println(mv.getCacheHint().values().clone().toString());
		//System.out.println(sp_father.getStyle());
		DoubleProperty width = mv.fitWidthProperty();
		DoubleProperty height = mv.fitHeightProperty();
		width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
		height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));
		mp.setAutoPlay(true);
		mp.setVolume(0.5);
		sl.setMin(0);
		sl.setMax(100);
		sl.setValue(50);
		sl.valueProperty().addListener(new ChangeListener<Number>() {
			 
	        @Override
	        public void changed(ObservableValue<? extends Number> observable, //
	                Number oldValue, Number newValue) {
	
	        	mp.setVolume(sl.getValue()/100);
	        }
		});
		
		seeksl.setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				//double svl=seeksl.getValue();
				mp.seek(Duration.millis(seeksl.getValue()*(mp.getTotalDuration().toMillis()/100)));
				
			
			}
		});
		int_timeLyric=0;
		
		
		//file=new File(path);
		
		//listFv=new ArrayList<String>();
		sp_father.setStyle("-fx-background-color:black;-fx-background-image:none");
		System.out.println(file.getPath().replace("mp3", "txt"));
		File tlr=null;
		if(file.getName().substring(file.getName().lastIndexOf('.')).equals(".mp3")) {
			tlr=new File(file.getPath().replace("mp3", "txt"));
			sp_father.setStyle("-fx-background-image:url(\"./images/vinilovyj_proigryvatel_vinil_plastinka_103512_1280x720.jpg\");");}
		if(file.getName().substring(file.getName().lastIndexOf('.')).equals(".mp4")) {
			tlr=new File(file.getPath().replace("mp4", "txt"));
			}
		
		
		ArrayList<ArrayList<String>> lyricc=new ArrayList<ArrayList<String>>();
		if(tlr.exists()) {
			//System.out.println("ton tai");
			String line;
			isLyric=true;
			//lb_lyricMain.setVisible(true);
			if(file.getName().substring(file.getName().lastIndexOf('.')).equals(".mp3")) {
				bdp_lyric1.setVisible(true);
				bdp_lyric2.setVisible(false);
			}
			else {
				bdp_lyric1.setVisible(false);
				bdp_lyric2.setVisible(true);
			}
			FileInputStream f1;
			try {
				f1 = new FileInputStream(tlr);
				InputStreamReader f2=new InputStreamReader(f1, "UTF-8");
				BufferedReader br = new BufferedReader(f2);
				while ((line = br.readLine()) != null){
					  //System.out.println(line.hashCode());
					  ArrayList<String> lr=new ArrayList<String>();
					  lr.add(line);
					  //System.out.println(line);
					  line=br.readLine();
					  lr.add(line);
					  //System.out.println(line);
					  lyricc.add(lr);
					  
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {isLyric=false;lb_lyricMain.setVisible(false);}
		
		
		mp.currentTimeProperty().addListener((ChangeListener<? super Duration>) new ChangeListener<Duration>() {
	
			@Override
			public void changed(ObservableValue<? extends Duration> arg0, Duration arg1, Duration arg2) {
				// TODO Auto-generated method stub
				//seeksl.setValue((arg2.toMillis()/mp.getTotalDuration().toMillis())/100);
				//seeksl.setValue((mediaPlayer.getCurrentTime().toSeconds()/mediaPlayer.getTotalDuration().toSeconds())*100);
				/*
				System.out.println((mediaPlayer.getCurrentTime().toSeconds()/mediaPlayer.getTotalDuration().toSeconds())*100);
				System.out.println(mediaPlayer.getCurrentTime());
				System.out.println(mediaPlayer.totalDurationProperty());
				*/
				/*
				if(mp.getCurrentTime().toMillis()==mp.getStopTime().toMillis()) {
					play.setVisible(true);
					pause.setVisible(false);
				}*/
				//System.out.println(mp.getStatus());
				
					String phut=Integer.toString((int)mp.getCurrentTime().toMinutes());
					String giay=Integer.toString(((int)mp.getCurrentTime().toSeconds())%60);
					if(phut.length()==1) {
						phut="0"+phut;
					}
					
					if(giay.length()==1) {
						giay="0"+giay;
					}
					
				String s_testTime=phut+giay;
				if(isLyric) {
					int_timeLyric=0;	
					
					//System.out.println(s_testTime);
					//System.out.println(int_timeLyric);
					for(ArrayList<String>s:lyricc) {
						
						//System.out.println(Integer.parseInt(s_testTime));
						//System.out.println((s.get(0).replace(":","")));
						//System.out.println(Integer.parseInt(s.get(0).replace(":","")));
						
						if(Integer.parseInt(s_testTime)>=Integer.parseInt(s.get(0).replace(":",""))&&int_timeLyric<lyricc.size()-1) {
							int_timeLyric+=1;
						}else {
							if(int_timeLyric>0&&int_timeLyric<lyricc.size()-1) {
							int_timeLyric-=1;}
							//System.out.println(int_timeLyric);
							break;
						}
					}
					if(file.getName().substring(file.getName().lastIndexOf('.')).equals(".mp3")) {
						setLyricMp3(lyricc);
					}else {
						try {
						if(Integer.parseInt(s_testTime)>=Integer.parseInt(lyricc.get(0).get(0).replace(":", ""))) {
						setLyricMp4(lyricc);
						}else {
							lb_lyricInBdp2.setText("");
						}
						}catch (Exception e) {
							// TODO: handle exception
							//loi out of bounds
						}
					}
					
				}
				
				
				
				
				//System.out.println(arg2.toString());
				lb_time.setText(phut+":"+giay);
				
				seeksl.setValue((mp.getCurrentTime().toMillis()/(mp.getStopTime().toMillis()))*100);
				//System.out.println(mediaPlayer.getCurrentTime());
				//System.out.println(mediaPlayer.getStopTime());
				//mp.seek(Duration.seconds(seeksl.getValue()*(mp.getTotalDuration().toSeconds()/100)));
				
			}
			
		});
	/*
	seeksl.valueChangingProperty().addListener((InvalidationListener) new ChangeListener<Number>() {

		@Override
		public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
			// TODO Auto-generated method stub
			//System.out.println(arg2.toString());
		}
	});
	*/
		/*
		seeksl.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				// TODO Auto-generated method stub
				//System.out.print("test");
			
				
			}
		});
	*/	

		
		
		
		mp.setOnEndOfMedia(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				play.setVisible(false);
				pause.setVisible(false);
				replay.setVisible(true);
			}
		});
	//System.out.println(bd.getWidth());
	//System.out.println(bd.getHeight());
	
	/*
	bd.widthProperty().addListener(new ChangeListener<Number>() {

		@Override
		public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
			// TODO Auto-generated method stub
			sp.setAlignment(Pos.CENTER);
			//System.out.print(arg2.toString());
			
			
		}
	});
	bd.heightProperty().addListener(new ChangeListener<Number>() {

		@Override
		public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
			// TODO Auto-generated method stub
			sp.setAlignment(Pos.CENTER);
			//System.out.print(arg2.toString());
			
		}
	});*/
	
		
	}
	
	/*
	public boolean check(String s1,String s2) {
		if(s1.length()!=s2.length()) {
			return false;
		}
		else {
			for(int i=0;i<s1.length();i++) {
			//System.out.print(Character.getNumericValue(s.charAt(i)));
			if(Character.getNumericValue(s1.charAt(i))!=Character.getNumericValue(s2.charAt(i))) {
				return false;
				
			}
			
			}
		}
		return true;
	}
	*/
	public void nextFv() {
		if(ready==true) {
			mp.stop();
			mp.dispose();
			
		}
		else {
			ready=true;
		}
		
		id=listFv.indexOf(path);
		id++;
		if(id>=listFv.size()) {
			id=0;
		}
		path=listFv.get(id);
		try {
			run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void preFv() {
		if(ready==true) {
			mp.stop();
			mp.dispose();
			
		}
		else {
			ready=true;
		}
		id=listFv.indexOf(path);
		id--;
		if(id<0) {
			id=listFv.size()-1;
		}
		
		path=listFv.get(id);
		try {
			run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void nextMD() throws UnsupportedEncodingException {
		if(ready==true) {
			mp.stop();
			mp.dispose();
			
		}
		else {
			ready=true;
		}
		if(id<len-1) {id+=1;}
		else {
			id=0;
		}
		
		path=file.getParent()+"\\"+myFiles[id];
		file=new File(path);
		path=file.toURI().toString();
		try {
			run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println(file.toURI().toString());
	}
	
	
	public void preMD() {
		if(ready==true) {
			mp.stop();
			mp.dispose();
			
		}
		else {
			ready=true;
		}
		if(id<len&&0<id) {id-=1;}
		else {
			id=len-1;
		}
		path=file.getParent()+"\\"+myFiles[id];
		file=new File(path);
		path=file.toURI().toString();
		try {
			run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println(file.toURI().toString());
	}
	
	public void player(ActionEvent event) {
		mp.play();
		mp.setRate(rate);
		pause.setVisible(true);
		play.setVisible(false);

	}
	
	public void stop(ActionEvent event) {
		mp.stop();
	}
	
	public void pauser(ActionEvent event) {
		mp.pause();
		pause.setVisible(false);
		play.setVisible(true);
		
	}
	public void replay() {
		mp.seek(Duration.millis(0));
		mp.play();
		play.setVisible(false);
		pause.setVisible(true);
		replay.setVisible(false);
	}

    
    public void slow(ActionEvent event) {
    	rate-=0.25;
    	if(rate<0.25) {
    		rate=1;
    	}
		mp.setRate(rate);
		lb_rate.setText("x"+Double.toString(rate));
	}
    
    
    public void fast(ActionEvent event) {
    	rate+=0.25;
    	if(rate>5) {
    		rate=1;
    	}
    	mp.setRate(rate);
    	lb_rate.setText("x"+Double.toString(rate));
    	
    }
    
    public void reload(ActionEvent event) {
		mp.seek(mp.getStartTime());
		mp.play();
	}

    public void start(ActionEvent event) {
		mp.seek(mp.getStartTime());
		mp.stop();
	}
    
    public void last(ActionEvent event) {
		mp.seek(mp.getTotalDuration());
	}
    
    public void move() {
    	bdp_feature.setStyle("-fx-opacity:1");
    }
    public void ex() {
    	bdp_feature.setStyle("-fx-opacity:0");
    }
    
    public void favorite() throws IOException {
    	f_fv=new File("./favorite.txt");
    	if (!f_fv.exists()) {
			f_fv.createNewFile();
		}
    	listFv.add(path);
    	//FileWriter fwt=new FileWriter(fv.getAbsoluteFile(),true);
    	FileOutputStream f1=new FileOutputStream(f_fv,true);
    	OutputStreamWriter f2=new OutputStreamWriter(f1, "UTF-8");
    	BufferedWriter bw = new BufferedWriter(f2);
    	bw.write(path);
    	bw.newLine();
    	System.out.println(path);
    	//fwt.write(path);
    	bw.close();
    	f1.close();
    	f2.close();
    	//fwt.close();
    	bt_unFv.setVisible(false);
    	bt_Fv.setVisible(true);
    	//listFv.contains(path);
    	
    	File fv=new File(path);
    	String ss=URLDecoder.decode(fv.getName(), StandardCharsets.UTF_8.toString());
		Button new_bt=new Button(ss);
		new_bt.setId(path);
		new_bt.setOnAction(event -> checkId((Button) event.getSource()));
    	  //????
    	vb_inScrP.getChildren().add(new_bt);
    	
    }
    public void rmFavo() throws IOException {
    	
    	listFv.remove(s_rmFv);
    	f_fv=new File("./favorite.txt");
    	
    	/*
    	FileWriter fwt=new FileWriter(fv.getAbsoluteFile());
    	BufferedWriter bw = new BufferedWriter(fwt);
    	*/
    	FileOutputStream f1=new FileOutputStream(f_fv);
    	OutputStreamWriter f2=new OutputStreamWriter(f1, "UTF-8")
  ;  	BufferedWriter bw = new BufferedWriter(f2);
    	for(String s:listFv) {
    		bw.write(s);
    		bw.newLine();
    	}
    	bw.close();
    	f1.close();
    	f2.close();
    	//
    	//fwt.close();
    	bt_Fv.setVisible(false);
    	bt_unFv.setVisible(true);
    	
    }
    public void close_listFv() {//exlistfv-> close_listFv
    	hb_father.setVisible(false);
    }
    
    //ArrayList<testButton> testb=new ArrayList<testButton>();
    
    public void open_listFv() {
    	hb_father.setVisible(true);
    	vb_inScrP.getChildren().clear(); 
    	System.out.println(listFv.size());
    	//testButton[] d=new testButton[listFv.size()];
    	Button[] btlist=new Button[listFv.size()];
    	//String test="";
    	/*
    	for(int j=0;j<listFv.get(0).length();j++) {
        	System.out.print(Character.getNumericValue(listFv.get(0).charAt(j)));
        	test+=(char)Character.getNumericValue(listFv.get(0).charAt(j));
        	
        }
    	System.out.println(test);
    	*/
    	
    	
    	for(int i=0;i<listFv.size();i++) {
    		File fv=new File(listFv.get(i));
    		System.out.println(listFv.get(i));
    		System.out.println(fv.getPath());
    		
    		//System.out.println(fv.toURI().toString().replace("file:/C:/Users/Xco/eclipse-workspace/mediaPlayer/", ""));
    		try {
				String ss=URLDecoder.decode(fv.getName(), StandardCharsets.UTF_8.toString());
				btlist[i]=new Button(ss);
				//d[i].setId(fv.t);
				
				
				
				
				
				btlist[i].setId(listFv.get(i));
				btlist[i].setOnAction(event -> checkId((Button) event.getSource()));
				/*
				testButton fvbt=new testButton(ss);
				fvbt.setValue(fv.toURI().toString());
				//System.out.println(fv.toURI().toString());
				fvbt.setOnMouseClicked(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent arg0) {
						// TODO Auto-generated method stub
						if(ready==true) {
							mp.stop();
							mp.dispose();
							
						}
						else {
							ready=true;
						}
						path=fvbt.getValue();
						run();
					}
				});
				*/
				//testb.add(fvbt);
				vb_inScrP.getChildren().add(btlist[i]);
				System.out.println(ss);
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    	}
    }
  
    public void setPath(String path) {
    	this.path=path;
    }

    private void checkId(Button button) {
        path=button.getId().toString();
        //System.out.println(path);
        if(ready==false) {
        	setNode(true);	
        	ready=true;
        }else {
        	mp.stop();
        	mp.dispose();
        }
        
     
        //System.out.println(path);
       
        try {
        	file=new File(path.replace("file:/", ""));
        	
        	System.out.println(file.getPath());
			run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
    }

    public void setLyricMp3(ArrayList<ArrayList<String>> lyricc) {
    	try {
			if(lyricc.size()>=3) {
				if(int_timeLyric==0) {
					lb_lyric1.setVisible(false);
					lb_lyric2.setVisible(false);
					lb_lyric3.setVisible(true);
					lb_lyric4.setVisible(true);
					lb_lyric3.setText(lyricc.get(1).get(1));
					lb_lyric4.setText(lyricc.get(2).get(1));
				}
				if(int_timeLyric==1) {
					lb_lyric1.setVisible(false);
					lb_lyric2.setVisible(true);
					lb_lyric3.setVisible(true);
					lb_lyric4.setVisible(true);
					lb_lyric2.setText(lyricc.get(0).get(1));
					lb_lyric3.setText(lyricc.get(2).get(1));
					lb_lyric4.setText(lyricc.get(3).get(1));
				}
				if(int_timeLyric>1&&int_timeLyric<lyricc.size()-3) {
					lb_lyric1.setVisible(true);
					lb_lyric2.setVisible(true);
					lb_lyric3.setVisible(true);
					lb_lyric4.setVisible(true);
					lb_lyric1.setText(lyricc.get(int_timeLyric-2).get(1));
					lb_lyric2.setText(lyricc.get(int_timeLyric-1).get(1));
					lb_lyric3.setText(lyricc.get(int_timeLyric+1).get(1));
					lb_lyric4.setText(lyricc.get(int_timeLyric+2).get(1));
				}
				if(int_timeLyric==lyricc.size()-2) {
					lb_lyric1.setText(lyricc.get(int_timeLyric-2).get(1));
					lb_lyric2.setText(lyricc.get(int_timeLyric-1).get(1));
					lb_lyric3.setText(lyricc.get(int_timeLyric+1).get(1));
					lb_lyric4.setVisible(false);
				}
				if(int_timeLyric==lyricc.size()-1) {
					lb_lyric1.setText(lyricc.get(int_timeLyric-2).get(1));
					lb_lyric2.setText(lyricc.get(int_timeLyric-1).get(1));
					lb_lyric3.setVisible(false);
					lb_lyric4.setVisible(false);
					
				}
			}else {
				lb_lyricMain.setText(lyricc.get(int_timeLyric).get(1));
				lb_lyric3.setVisible(false);
				lb_lyric4.setVisible(false);
				lb_lyric1.setVisible(false);
				lb_lyric2.setVisible(false);
			}
		lb_lyricMain.setText(lyricc.get(int_timeLyric).get(1));
		}
		catch (Exception e) {
			// TODO: handle exception
			
			
			//Loi khong xac dinh
		}
    }
    public void setLyricMp4(ArrayList<ArrayList<String>> lyricc) {
    	lb_lyricInBdp2.setText(lyricc.get(int_timeLyric).get(1));
    }
    
    public void runOnline() throws IOException {
    	if(ready==true) {
    		mp.stop();
    		mp.dispose();
    		
    	}else {
    		ready=true;
    	}
    	System.out.println(path);

    	try {
			ext = Paths.get(new URI(path).getPath()).getName(0).toString();
			System.out.println(ext);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	vb_inBdp.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				ex();
			}
		});
    	rate=1;
		bdp_lyric1.setVisible(false);
		bdp_lyric2.setVisible(false);
		lb_rate.setText("x"+Double.toString(rate));
		play.setVisible(false);
		bdp_feature.setStyle("-fx-opacity:0");
		pause.setVisible(true);
		Media me = new Media(path);
		mp = new MediaPlayer(me);
		mv.setMediaPlayer(mp);
		mv.setCache(true);
		DoubleProperty width = mv.fitWidthProperty();
		DoubleProperty height = mv.fitHeightProperty();
		width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
		height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));
		mp.setAutoPlay(true);
		mp.setVolume(0.5);
		sl.setMin(0);
		sl.setMax(100);
		sl.setValue(50);
		//System.out.println(mp.getStatus());
		sl.valueProperty().addListener(new ChangeListener<Number>() {
			 
	        @Override
	        public void changed(ObservableValue<? extends Number> observable, //
	                Number oldValue, Number newValue) {
	
	        	mp.setVolume(sl.getValue()/100);
	        }
		});
		
		seeksl.setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				mp.seek(Duration.millis(seeksl.getValue()*(mp.getTotalDuration().toMillis()/100)));
				
			
			}
		});
		int_timeLyric=0;
		sp_father.setStyle("-fx-background-color:black;-fx-background-image:none");
		if(ext.equals("mp3")) {
			sp_father.setStyle("-fx-background-image:url(\"./images/vinilovyj_proigryvatel_vinil_plastinka_103512_1280x720.jpg\");");
			bdp_lyric1.setVisible(true);
			bdp_lyric2.setVisible(false);
		}else {
			bdp_lyric1.setVisible(false);
			bdp_lyric2.setVisible(true);
		}

		isLyric=true;
		ArrayList<ArrayList<String>> lyricc=new ArrayList<ArrayList<String>>();
		BufferedReader in=null;
		InputStreamReader ip_reader=null;
		try {
	    URL oracle = new URL(path.replace(".mp4", ".txt").replace(".mp3", ".txt"));
	    ip_reader=new InputStreamReader(oracle.openStream(),"UTF-8");
	    in= new BufferedReader(ip_reader);
		}catch (Exception e) {
			// TODO: handle exception
			isLyric=false;
		}
	    String inputLine;
	    
	    while ((inputLine = in.readLine()) != null) {
	        //System.out.println(inputLine);
	        ArrayList<String> lr=new ArrayList<String>();
			  lr.add(inputLine);
			  //System.out.println(line);
			  inputLine=in.readLine();
			  lr.add(inputLine);
			  //System.out.println(line);
			  lyricc.add(lr);
	  	}
	    
	    in.close();
		ip_reader.close();
		
		mp.currentTimeProperty().addListener((ChangeListener<? super Duration>) new ChangeListener<Duration>() {
			@Override
			public void changed(ObservableValue<? extends Duration> arg0, Duration arg1, Duration arg2) {
					String phut=Integer.toString((int)mp.getCurrentTime().toMinutes());
					String giay=Integer.toString(((int)mp.getCurrentTime().toSeconds())%60);
					if(phut.length()==1) {
						phut="0"+phut;
					}
					
					if(giay.length()==1) {
						giay="0"+giay;
					}
				String s_testTime=phut+giay;
				if(isLyric) {
					int_timeLyric=0;	
					for(ArrayList<String>s:lyricc) {
						if(Integer.parseInt(s_testTime)>=Integer.parseInt(s.get(0).replace(":",""))&&int_timeLyric<lyricc.size()-1) {
							int_timeLyric+=1;
						}else {
							if(int_timeLyric>0&&int_timeLyric<lyricc.size()-1) {
							int_timeLyric-=1;}
							//System.out.println(int_timeLyric);
							break;
						}
					}
					
					
					if(ext.equals("mp3")) {
						setLyricMp3(lyricc);
					}else {
						try {
						if(Integer.parseInt(s_testTime)>=Integer.parseInt(lyricc.get(0).get(0).replace(":", ""))) {
						setLyricMp4(lyricc);
						}else {
							lb_lyricInBdp2.setText("");
						}
						}catch (Exception e) {
							// TODO: handle exception
							//loi out of bounds
						}
					}	
				}
				lb_time.setText(phut+":"+giay);
				seeksl.setValue((mp.getCurrentTime().toMillis()/(mp.getStopTime().toMillis()))*100);
			}
			
		});
		mp.setOnEndOfMedia(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				play.setVisible(false);
				pause.setVisible(false);
				replay.setVisible(true);
			}
		});
    	
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
    	
    	hb_father.setVisible(false); //hiện feature khi khỏi chạy
    	bt_Fv.setVisible(false);	//ẩn nút trái tim hồng khi khởi chạy
    	scrP_fvVideo.setHbarPolicy(ScrollBarPolicy.NEVER);        //ẩn thanh kéo ngang
		scrP_fvVideo.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);	  //hiển thị khi cần thiết
    	next.setStyle("-fx-background-color:rgba(255, 255, 255,0.1)");
    	pre.setStyle("-fx-background-color:rgba(255, 255, 255,0.1)");
    	
    	
    	f_fv=new File("./favorite.txt");
    	if (!f_fv.exists()) {
		 try {
			f_fv.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
    	try {
    		String line;
    		listFv=new ArrayList<String>();
    		FileInputStream f1=new FileInputStream(f_fv);
    		InputStreamReader f2=new InputStreamReader(f1, "UTF-8");
    		BufferedReader br = new BufferedReader(f2);
    		while ((line = br.readLine()) != null){
  			  //System.out.println(line.hashCode());
  			  listFv.add(line);
  			  
  			}
    		f1.close();
    		f2.close();
    		br.close();
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	bt_listFv.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				open_listFv();
				
			}
		});
    	
	}
}