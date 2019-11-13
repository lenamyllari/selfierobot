package behaviours;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;
import threads.Reader;
/** 
 * Stop-luokka, joka toteuttaa Behavior-rajapinnan. 
 * Käynnistyy, kun lukijasäie vastaanottaa tietokoneelta lopetuskäskyn. 
 * Lopettaa ohjelman suorituksen.
 * @author Ryhmä 6
 * @version 1.0
 */
public class Stop implements Behavior {
	/**
	 * DifferentialPilot-olio 
	 * @see lejos.robotics.navigation.DifferentialPilot
	 */
	private DifferentialPilot pilot;
	/**
	 * Boolean-lippu, joka vastaa käytöksen suppressoinnista.
	 */
	private volatile boolean suppressed = false;

	/**
	 * Luokan konstruktori
	 * @param pilot liikumiseen käytettävä pilootti
	 */
	public Stop(DifferentialPilot pilot) {
		this.pilot = pilot;
	}
	
	//tarkistetaan onko tullut lopetuskäsky -> pitääkö käynnistää tämän käytöksen
	/** 
	 * Metodi, joka palauttaa arvon true, kun robotti haluaa käynnistää tämän käytöksen.
	 * @return <code>true<code/> jos tietokoneelta on saatu lopetuskäsky
	 * @see Reader#StopProgram()
	 * @see lejos.robotics.subsumption.Behavior#takeControl() 
	 */
	public boolean takeControl() {
		return Reader.StopProgram();
	}
	
	//lopetetaan ohjelma
	/** 
	 * Metodi, joka määrittelee robotin toiminnan kun tämä käytös astuu voimaan.
	 * Lopettaa ohjelman suorituksen.
	 * @see lejos.robotics.subsumption.Behavior#action()
	 */
	public void action() {
		suppressed = false;
		System.exit(0);
	}
	/**
	 * Asettaa boolean-lipun suppressed arvoksi true, mikä suppressoi käytöksen.
	 * @see lejos.robotics.subsumption.Behavior#suppress() 
	 */
	public void suppress() {
		suppressed = true;
	}

}
