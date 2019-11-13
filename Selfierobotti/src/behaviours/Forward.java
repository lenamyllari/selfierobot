package behaviours;

import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Pose;
import lejos.robotics.subsumption.Behavior;

/**Forward luokka joka toteuttaa Behavior rajapinnan. Tämän käyttäyttämisen tarkoitus on mennä eteenpäin, 
 * sillä on alin prioriteetti käyttäytymislistalla, siksi takeControl aina palauttaa <code>true</code>.
 * @author Ryhmä 6 
 * @version 1.0
 */
public class Forward implements Behavior {
	/**
	 * DifferentialPilot luokan olio 
	 * @see lejos.robotics.navigation.DifferentialPilot
	 */
	private DifferentialPilot pilot;
	/**
	* Boolean-muuttuja, joka vastaa käytöksen suppressoinnista. 
	*/
	private volatile boolean suppressed = false;
	/**
	 * Poseproivider joka antaa tietoa robotin sijainnista.
	 */
	private PoseProvider poseprovider;
	/**
	 * Pose olio, jossa robotin koordinaatit ja suuntakulma.
	 */
	Pose pose;
	
	//Luokan konstruktori
	/** *Luokan konstruktori* 
	 * @param poseprovider 
	 * @param pilot liikumiseen käytettävä pilootti
	*/
	public Forward(DifferentialPilot pilot, PoseProvider poseprovider) {
		this.pilot = pilot;
		this.poseprovider = poseprovider;
	}

	//palauttaa aina true
	//robotti on aina valmis menemään eteenpäin eli tällä käytöksellä alin prioriteetti
	/** * Metodi, joka ilmoittaa haluaako robotti aloittaa tämän käyttäytymisen. *
	 *  @return aina palauttaa <code>true</code> koska tällä käytöksellä alin prioriteetti*/
	public boolean takeControl() {	
		return true;
	}
	
	/** * suppress()-metodi pyytää käyttäytymisen lopetusta asettamalla instanssimuuttujaksi määritellyn boolean-lipun, 
	 *  joka saa aikaan actionin ja kaiken siellä aloitetun toiminnan päättymisen *  
	 *  @see lejos.robotics.subsumption.Behavior#suppress()*/
	public void suppress() {
		suppressed = true;	
	}

	//robotti kulkee eteenpäin kunnes jokin muu käytös suppressoi sen
	/** * Metodi, jossa määritellään robotin toiminta tämän kättäytymisen aikana: robotti menee eteenpäin. * 
	 *  @see lejos.robotics.subsumption.Behavior#action() */
	public void action() {
		suppressed = false;
		//tarkistetaan missä robotti on
		pose = poseprovider.getPose();
		System.out.println(pose);
		//robotti menee eteenpäin niin kauan kunnes se on suppressoitu
		while(!suppressed) {
			pose = poseprovider.getPose();
			pilot.travel(10);
			System.out.println(pose);
		}
	}
}
