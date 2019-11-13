package sensors;

import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
/** *Motor-luokka sisältää motorin toiminnan napin painamista varten*
 * 
 * @author ryhmä6
 * @version 1.0
 */
public class Motor {
	//alustetaan moottori
	/** *
	* Moottori olio joka vastaa napin painamisesta
	*/
	static RegulatedMotor mD = new EV3MediumRegulatedMotor(MotorPort.D);
	//metodi eteenpäinmenoa varten, painaa nappia
	/** *
	* metodi forward napin painamista varten
	*/
	public static void forward() {
		mD.rotate(-45);
	}
	
	//metodi peruuttamiselle, vapautetaan nappi
	/** 
	* backward-metodi napin vapauttamista varten
	*/
	public static void backward() {
		mD.rotate(45);
	}
}
