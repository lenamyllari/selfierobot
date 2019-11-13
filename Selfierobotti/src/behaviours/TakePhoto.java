package behaviours;

import java.io.File;
import lejos.hardware.Sound;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;
import sensors.InfraredSensor;
import sensors.Motor;

/**
 * TakePhoto luokka joka toteuttaa Behavior rajapinnan. Tämän käyttäyttämisen tarkoitus on ottaa kuva. Sen jälkeen kun kuva on otettu käyttäminen päättyy. 
 * @author Ryhmä 6 
 * @version 1.0
 */

public class TakePhoto implements Behavior {

	
	/**
	 * DifferentialPilot luokan olio 
	 * @see lejos.robotics.navigation.DifferentialPilot
	 */
	private DifferentialPilot pilot;
	/**
	* Boolean-muuttuja <code> suppressed</code>, joka vastaa käytöksen suppressoinnista. 
	*/
	private volatile boolean suppressed = false;

	/**
	* Boolean-muuttuja <code> estehavaittu</code>, joka vastaa käytöksen aloittamisesta. 
	*/
	static boolean estehavaittu = false;
	
	/**
	* Luokan konstruktori
	* @param pilot liikumiseen käytettävä pilootti
	*/
	public TakePhoto(DifferentialPilot pilot) {
		this.pilot = pilot;
	
	}
	
	/** *Metodi, joka ilmoittaa haluaako robotti aloittaa tämän käyttäytymisen, käyttää InfraredLuokan metodia checkDistance päätökseen* 
	 *  @return <code>true</code> jos robotin edessä on este ja robotti haluaa aloittaa tämän käyttäytymisen; 
	 *  		<code>false</code> muissa tapauksissa
	 *  @see InfraredSensor#checkDistance()
	 *  @see lejos.robotics.subsumption.Behavior#takeControl()
	 */
	public boolean takeControl() {
		//jos ylempi Infrapuna-anturi huomaa jotain, aloitetaan tämän käytöksen
		if(InfraredSensor.checkDistance() || estehavaittu) {
			estehavaittu = true;
		}
		return estehavaittu;
		
	}
	
	//robotti kääntyy, soittaa audiotiedoston ja painaa nappia ottaakseen kuva
	/** * Metodi, jossa määritellään robotin toiminta tämän kättäytymisen aikana: 
	 *  robotti kääntyy, soittaa audiotiedoston ja painaa nappia ottaakseen kuva* 
	 *  @see lejos.robotics.subsumption.Behavior#action()*/
	public void action() {
		pilot.rotate(180);
		File file = new File(chooseSound());
		Sound.playSample(file, 100);
		Delay.msDelay(1000);
		//painaa nappia
		Motor.forward();
		Motor.backward();
		Delay.msDelay(3000);
		estehavaittu = false;
	}
	
	/** * suppress()-metodi pyytää käyttäytymisen lopetusta asettamalla instanssimuuttujaksi määritellyn boolean-lipun, 
	 *  joka saa aikaan actionin ja kaiken siellä aloitetun toiminnan päättymisen *  
	 *  @see lejos.robotics.subsumption.Behavior#suppress()*/
	public void suppress() {
		suppressed = true;
	}
	
	//valitaan soitettavan audiotiedoston
	/** * Metodi jossa lista ääneistä, joista yhden soitetaan ennen kuin otetaan kuva. metodi kutsutaan action() metodista *
	 *  @see #action()  
	 *  @return sattunaisen tiedoston nimi */
	public String chooseSound() {
		String [] sounds = new String[5];
		sounds[0]="o1.wav";
		sounds[1]="02002.wav";
		sounds[2]="03002.wav";
		sounds[3]="04002.wav";
		sounds[4]="05001.wav";
		return sounds[(int) Math.floor(Math.random()*5)];
	}
}
