package sensors;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

/** *InfraredSensor-luokka sisältää infrapuna-anturien toiminnan ja mittaustulosten käsittelyn*
 * 
 * @author ryhmä6
 * @version 1.0
 */
public class InfraredSensor {
	
	//luodaan porttiolio, anturiolio ja moodiolio
	/** *Porttiolioon port tallennetaan robotin keskusyksikön portti, 
	 * jota ylempi infrapuna-anturi käyttää.*
	 */
	static Port port = LocalEV3.get().getPort("S4");
	
	/** *Porttiolioon portTurn tallennetaan robotin keskusyksikön portti, 
	 * jota alempi infrapuna-anturi käyttää.*
	 */
	static Port portTurn = LocalEV3.get().getPort("S3");
	
	/** *Anturiolio sensor vastaa robotin ylempää infrapuna-anturia.*
	 */
	static SensorModes sensor = new EV3IRSensor(port);
	
	/** *Anturiolio sensorTurn vastaa robotin alempaa infrapuna-anturia.*
	 */
	static SensorModes sensorTurn = new EV3IRSensor(portTurn);
	
	/** *Moodiolion distance avulla voidaan kutsua ylempänä olevan infrapuna-anturin mittaustuloksia.*
	 */
	static SampleProvider distance = ((EV3IRSensor)sensor).getDistanceMode();
	
	/** *Moodiolion distanceTurn avulla voidaan kutsua alempana olevan infrapuna-anturin mittaustuloksia.*
	 */
	static SampleProvider distanceTurn = ((EV3IRSensor)sensorTurn).getDistanceMode();
	
	/** *checkDistance-metodi tarkastaa robotissa ylempänä olevan infrapuna-anturin avulla, 
	 * onko lähellä kuvattavaa mielenkiinnon kohdetta (ihmistä).*
	 * 
	 * @return true, kun mielenkiinnon kohde on lähempänä kuin 40 cm;
	 * 		   false, kun mielenkiinnon kohdetta ei ole lähellä.
	 * 
	 * @see TakePhoto
	 */
	public static boolean checkDistance (){
		
		/**	*sampleTurn on float-lukutaulukko, 
		* johon infrapuna-anturi syöttää mittaustuloksia mahdollisen mielenkiinnon kohteen etäisyydestä.*
		*/
		float[] sample = new float[distance.sampleSize()];
		distance.fetchSample(sample, 0);
		if (sample[0] < 45){
			//lähellä jotain
			return true;
		} else {
			//vielä voi mennä eteenpäin
			return false;
		}
	}
	
	/** *checkDistanceTurn-metodi tarkastaa robotissa alempana olevan infrapuna-anturin avulla, 
	* onko lähellä estettä, joka pitää kiertää.*
	* 
	* @return true, kun este on lähempänä kuin 30 cm;
	* 		  false, kun estettä ei ole lähellä.
	* @see#Turn
	*/
	public static boolean checkDistanceTurn (){
		
		/** *sampleTurn on float-lukutaulukko, 
		* johon infrapuna-anturi syöttää mittaustuloksia mahdollisen esteen etäisyydestä.*
		*/
		float[] sampleTurn = new float[distanceTurn.sampleSize()];
		distanceTurn.fetchSample(sampleTurn, 0);
		if (sampleTurn[0] < 25){
			//liian lähellä
			return true;
		} else {
			//vielä voi mennä eteenpäin
			return false;
		}
	}
}
