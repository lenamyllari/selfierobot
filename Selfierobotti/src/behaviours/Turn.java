package behaviours;

import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.pathfinding.ShortestPathFinder;
import lejos.robotics.subsumption.Behavior;
import sensors.InfraredSensor;

/**Turn-luokka joka toteuttaa Behavior-rajapinnan.
 * Käynnistyy, kun robotti on liian lähellä rajoja tai saa InfraredSensorilta tiedon esteestä.
 * Peruuttaa ja kääntyy.
 * @author Ryhmä 6 
 * @version 1.0
 */
public class Turn implements Behavior {
	/**
	* Käytössä oleva pilotti-olio.
	*/
	private DifferentialPilot pilot;
	/**
	* Käytössä oleva navigaattori-olio, joka seuraa pathfinderin laskemaa reittiä.
	*/
	private Navigator navi;
	/**
	* Boolean-muuttuja, joka vastaa käytöksen suppressoinnista. 
	*/
	private volatile boolean suppressed = false;
	/**
	 * Pose olio, jossa robotin koordinaatit ja suuntakulma.
	 */
	Pose pose;
	/**
	* Boolean-muuttuja, joka vastaa käytöksen aloittamisesta. 
	*/
	boolean border = false;
	/**
	 * Poseproivider joka antaa tietoa robotin sijainnista.
	 */
	private PoseProvider poseprovider;
	/**
	* Käytössä oleva ShortestPathFinder-olio, joka laskee reittiä. 
	*/
	private ShortestPathFinder pathFinder;
	
	/**
	* Luokan konstruktori.
	* @param pilot käytettävä Pilot-olio
	* @param navi käytettävä Navigator-olio
	* @param poseprovider käytettävä PoseProvider-olio
	* @param pathFinder käytettävä ShortestPathFinder-olio
	*/
	public Turn(DifferentialPilot pilot, Navigator navi, PoseProvider poseprovider, ShortestPathFinder pathFinder) {
		this.pilot = pilot;
		this.navi = navi;
		this.poseprovider = poseprovider;
		this.pathFinder = pathFinder;
	}
	/** * Metodi, joka ilmoittaa haluaako robotti aloittaa tämän käyttäytymisen. * 
	 */
	public boolean takeControl() {
		//saadaan sijannin navigaatorilta
		pose = poseprovider.getPose();
		//jos raja on lähellä tai alin Infrapuna-anturi huomaa esteen, aloitetaan tämän käytöksen
		if(pose.getX()>130 || pose.getY()>130 || pose.getX()<20 || pose.getY()<20 
				|| InfraredSensor.checkDistanceTurn() ) {
			System.out.println(pose);
			border = true;			
		} else if (InfraredSensor.checkDistanceTurn() && !InfraredSensor.checkDistance()) {
			border =true;
		}
		return border;
	}
	
	//robotti peruu, kääntyy ja menee 20 cm eteenpäin
	/** * Metodi, jossa määritellään robotin toiminta tämän kättäytymisen aikana: robotti kääntyy * 
	 */
	public void action() {
		suppressed = false;
		pose = poseprovider.getPose();
		System.out.println(pose);
		pilot.travel(-20);
		pilot.rotate(-120);
		pilot.travel(20);
		border = false;
		while(pilot.isMoving()&&!suppressed) Thread.yield(); 
	}
	/** 
	 * Asettaa boolean-lipun suppressed arvoksi true, mikä suppressoi käytöksen.
	 * @see lejos.robotics.subsumption.Behavior#suppress()
	 */
	public void suppress() {
		suppressed = true;
	}

}

	
	
