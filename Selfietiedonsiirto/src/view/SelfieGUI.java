package view;
	
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

/** SelfieGUI-luokassa on tiedonsiirron javafx-käyttöliittymä
 * 
 * @author ryhmä6
 * @version 1.0
 */
public class SelfieGUI extends Application {
	
	/** Yhteyssoketin esittely
	 */
	Socket socket = null;
	
	/** Tiedonlähetyksen olion esittely
	 */
	DataOutputStream out = null;
	
	//yhteyden luominen
	/** init-metodia kutsutaan ohjelman käynnistyessä, siinä luodaan soketti- ja DataOutputStream-oliot.
	*/
	public void init() {
		try {
			socket = new Socket("10.0.1.1",1111);
			out = new DataOutputStream(socket.getOutputStream());
		} catch (UnknownHostException e) {
			System.out.println("IP-osoitetta ei löytynyt.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//luodaan näkymä, gridpane, napit ja nappien toiminta, jossa tiedonsiirto tapahtuu
	/** start-metodissa asetetaan näkymä, luodaan gridpane-, scene- ja nappiaoliot.
	 * Metodissa asetetaan myös tapahtumakäsittelyt napeille, hoidetaan tapahtumakäsittelyssä tiedonsiirto ja asetellaan napit gripanelle. 
	 * Metodissa näkymä kutsuu css-muotoiluja, ja näkymä asetetaan ja näytetään.
	*/
	@Override
	public void start(Stage primaryStage) {
		try {
			GridPane root = new GridPane();
			Scene scene = new Scene(root,600,600);
			
			Button selfie1 = new Button("Selfie- \npiste \n1");
			selfie1.setOnAction(new EventHandler<ActionEvent>() {
			
				/** handle-metodissa napille asetetaan tiedonsiirto tehtäväksi. Se siirtää tiedon selfiepisteen numerosta robotille.
				*/
				public void handle(ActionEvent event) {
					try {
						out.writeUTF("Selfiepiste");
						out.flush();
						out.writeInt(0);
						out.flush();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		
			Button selfie2 = new Button("Selfie- \npiste \n2");
			selfie2.setOnAction(new EventHandler<ActionEvent>() {
				
				/** handle-metodissa napille asetetaan tiedonsiirto tehtäväksi. Se siirtää tiedon selfiepisteen numerosta robotille.
				*/
				public void handle(ActionEvent event) {
					try {
						out.writeUTF("Selfiepiste");
						out.flush();
						out.writeInt(1);
						out.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			
			Button selfie3 = new Button("Selfie- \npiste \n3");
			selfie3.setOnAction(new EventHandler<ActionEvent>() {
				
				/** handle-metodissa napille asetetaan tiedonsiirto tehtäväksi. Se siirtää tiedon selfiepisteen numerosta robotille.
				*/
				public void handle(ActionEvent event) {
					try {
						out.writeUTF("Selfiepiste");
						out.flush();
						out.writeInt(2);
						out.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			
			Button stop = new Button("Lopeta");
			stop.setOnAction(new EventHandler<ActionEvent>() {
				
				/** handle-metodissa napille asetetaan käsky pysähtyä ja se lähetetään robotille.
				*/
				public void handle(ActionEvent event) {
					try {
						out.writeUTF("Stop");
						out.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}	
				}
			});
			
			//asetellaan napit
			root.setAlignment(Pos.CENTER);
			root.setHgap(10);
			root.setVgap(10);
			root.add(selfie1, 0, 0);
			root.add(selfie2, 1, 0);
			root.add(selfie3, 0, 1);
			root.add(stop, 1, 1);
			
			//muotoilua css-tiedostossa
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			//näkymä asetetaan
			primaryStage.setScene(scene);
			//näkymä näytetään
			primaryStage.show();
		
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/** Main-metodi
	 * Käynnistää ohjelman.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}

