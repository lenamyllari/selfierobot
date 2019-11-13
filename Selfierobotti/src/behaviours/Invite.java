package behaviours;

import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.pathfinding.ShortestPathFinder;
import lejos.robotics.subsumption.Behavior;
import threads.Reader;

/**
* Luokka, joka toteuttaa Behavior-rajapinnan. 
* Käynnistyy, kun lukijasäie vastaanottaa tietokoneelta kutsun selfiepisteelle. 
* Navigoi robotin lyhintä reittiä halutulle selfiepisteelle ja käynnistää TakePhoto-käytöksen.
* @see TakePhoto#takeControl()
* @author Ryhmä 6
* @version 1.0
*/
public class Invite implements Behavior {
	/**
	* Käytössä oleva pilotti-olio.
	*/
	private DifferentialPilot pilot;
	/**
	* Boolean-muuttuja, joka vastaa käytöksen suppressoinnista. 
	*/
	private volatile boolean suppressed = false;
	/**
	* Käytössä oleva navigaattori-olio, joka seuraa pathfinderin laskemaa reittiä.
	*/
	private Navigator navi;
	/**
	* Käytössä oleva ShortestPathFinder-olio, joka laskee reittiä. 
	*/
	private ShortestPathFinder pathFinder;
	
	/**
	 * Waypoint [] selfiepoints taulukko, jossa selfiepisteiden koordinaatit
	 */
	//luodaan taulukko selfiepisteille
	Waypoint [] selfiepoints = {
	//luodaan selfiepisteet ja lisätään ne taulukkoon
	new Waypoint(105,110),
	new Waypoint(95,40),
	new Waypoint(40,110)};
	
	/**
	* Luokan konstruktori.
	* @param pilot käytettävä pilotti-olio
	* @param navi käytettävä navigaattori-olio
	* @param pathFinder käytettävä ShortestPathFinder-olio
	*/
	public Invite(DifferentialPilot pilot, Navigator navi, ShortestPathFinder pathFinder) {
		this.pilot = pilot;
		this.navi = navi;
		this.pathFinder = pathFinder;
	}
	/** Palauttaa arvon true, kun käytös haluaa päästä esiin.
	 *  @return invited <code>true</code> jos tietokoneelta saatu selfiepisteen indeksi
	 *  @see Reader#invited()
	 *  @see lejos.robotics.subsumption.Behavior#takeControl()
	 */
	public boolean takeControl() {
		return Reader.invited();
	}
	/** 
	 * Metodi, joka aktivoituu kun tämä käytös astuu voimaan.
	 * Luo selfiepisteiden taulukon ja saa Reader-säikeeltä halutun selfiepisteen indeksin.
	 * @see Reader#run()
	 * Hakee indeksillä oikean pisteen, laskee lyhimmän reitin ja navigoi sille. 
	 * Palauttaa muuttujan invited arvon falseksi ja käynnistää TakePhoto-metodin.
	 * @see TakePhoto#takeControl()
	 * @see lejos.robotics.subsumption.Behavior#action()
	 */
	public void action() {
		suppressed = false;
		System.out.println("Message: " + Reader.getMessage() + ", Index: " + Reader.getSelfiepointIndex());
		//alustetaan polkumuuttuja
		while (!suppressed) {
			Path path;
			try {
				//etsitään lyhin reitti nykyisestä sijainnista halutulle selfiepisteelle
				System.out.println(navi.getPoseProvider().getPose() + "invitessa");
				path = pathFinder.findRoute(navi.getPoseProvider().getPose(), selfiepoints[Reader.getSelfiepointIndex()]);
				//annetaan laskettu reitti navigatorille
				navi.setPath(path); 
				//seurataan reittiä
				navi.followPath(); 
				//kunnes saavutaan perille
				navi.waitForStop();
			} catch (DestinationUnreachableException e) {
				e.printStackTrace();
			}
			if(navi.pathCompleted()) {
				//otetaan kuva
				TakePhoto.estehavaittu = true;
				//palautetaan invited-muuttuja takaisin falseksi
				Reader.setInvitedFalse();
			}
		}
	}
	/** 
	 * Asettaa boolean-lipun suppressed arvoksi true, mikä suppressoi käytöksen.
	 * @see lejos.robotics.subsumption.Behavior#suppress()
	 */
	public void suppress() {
		navi.stop();
		suppressed = true;
	}
}
