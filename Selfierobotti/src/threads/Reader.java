package threads;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
* Luokka toteuttaa Thread-rajapinnan. 
* Luo tiedonsiirtoyhteyden robotin ja tietokoneen välille ja lukee tietokoneelta saatua dataa.
* Käynnistää joko Stop- tai Invite-käytöksen tietokoneelta saadun datan perusteella.
* @see Stop#takeControl()
* @see Invite#takeControl()
* @author Ryhmä 6
* @version 1.0
*/
public class Reader extends Thread {
	/**
	* Serveri-soketti, joka kuuntelee yhteydenottoja haluttuun porttiin.
	*/
	private ServerSocket server = null;
	/**
	* Soketti, joka odottaa yhteydenottoa.
	*/
	private Socket socket = null;
	/**
	* Tietovirta tietokoneelta robotille. 
	*/
	private DataInputStream in = null;
	/**
	* Halutun selfiepisteen indeksi selfiepistetaulukossa. Alustettu arvoon -1.
	* @see Invite#action()
	*/
	private static int index = -1;
	/**
	* Viesti, joka kertoo säikeelle millaista dataa on tulossa seuraavaksi.
	*/
	private static String message;
	/**
	* Saa arvon true, kun tietokoneelta tulee tieto, että pysäytysnappulaa on painettu. Saa aikaan ohjelman suorituksen lopetuksen.
	* @see Stop#takeControl()
	* @see Stop#action()
	*/
	private static boolean stop = false;
	/**
	* Saa arvon true, kun tietokoneelta tulee kutsu selfiepisteelle. Saa robotin navigoimaan pisteelle.
	* @see Invite#takeControl()
	*/
	private static boolean invited = false;
	
	/** 
	 * Luo kommunikointipisteen ja odottaa yhteydenottoa tietokoneelta.
	 * Kun yhteys on muodostunut, vastaanottaa selfiepisteiden indeksejä tai lopetuskäskyn.
	 *
	 */
	public void run() {
		try {
			//luodaan serverisoketti ja soketti ja odotetaan yhteydenottoa
			server = new ServerSocket(1111);
			socket = server.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//kun ohjelma on käynnissä
		while(!stop) {
			try {
				//luodaan tiedonvastaanottovirta
				in = new DataInputStream(socket.getInputStream());
				//vastaanotetaan viesti tietokoneelta, joka kertoo millaista dataa ollaan lähettämässä
				message = in.readUTF();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//jos tietokoneelta tulee kutsu selfiepisteelle
			if (message.equals("Selfiepiste")) {
				try {
					//vastaanotetaan selfiepisteen indeksi
					index = in.readInt();
				} catch (IOException e) {
					e.printStackTrace();
				}
				//käynnistetään invited-käytös
				invited = true;
			//jos tietokoneelta tulee lopetuskäsky
			} else if (message.equals("Stop")) {
				//käynnistetään stop-käytös
				stop = true;
			}
		}
	}
	
	/** 
	 * Palauttaa muuttujan stop
	 * @return stop <code>true</code> jos tietokoneelta on saatu lopetuskäsky
	 */
	public static boolean StopProgram() {
		return stop;
	}
	
	/** 
	 * Palauttaa muuttujan index
	 * @return index selfiepisteen indeksi taulukossa
	 */
	public static int getSelfiepointIndex() {
		return index;
	}
	
	/** 
	 * Palauttaa muuttujan invited
	 * @return invited <code>true</code> jos tietokoneelta on saatu selfiepisteen indeksi
	 */
	public static boolean invited() {
		return invited;
	}
	
	/** 
	 * Asettaa muuttujan invited arvoksi <code>false</code>
	 */
	public static void setInvitedFalse() {
		invited = false;
	}
	
	/** 
	 * Palauttaa muuttujan message
	 * @return message viesti tietokoneelta, joka kertoo saapuvan datan tyypin
	 */
	public static String getMessage() {
		return message;
	}
}
