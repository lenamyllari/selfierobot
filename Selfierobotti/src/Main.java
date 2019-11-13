import behaviours.Forward;
import behaviours.Invite;
import behaviours.Stop;
import behaviours.TakePhoto;
import behaviours.Turn;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.geometry.Line;
import lejos.robotics.geometry.Rectangle;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.pathfinding.ShortestPathFinder;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import threads.Reader;

/**
 * Main-luokka. Käynnistää ohjelman suorituksen. 
 * @author Ryhmä 6 
 * @version 1.0
 */
public class Main {
	/**
	 * Main-metodi. Luo moottorit, pilotti-, navigaattori-, PoseProvider- ja ShortestPathFinder-oliot, 
	 * joita käytetään liikkumiseen ja navigointiin.
	 * Määrittelee renkaan halkaisijan ja raideleveyden. 
	 * Luo toimintaympäristön rajaavan suorakulmion. 
	 * Määrittelee robotin lähtöasennon.
	 * Luo ja käynnistää lukijasäikeen tietokoneen kanssa kommunikointia varten.
	 * Luo kaikki Behavior-luokat ja asettaa ne listaan prioriteettijärjestyksessä.
	 * Luo ja käynnistää Arbritator-olion hallitsemaan käytöksiä.
	 */
	public static void main(String[] args) {
		//alustetaan moottorit liikkumista varten
		RegulatedMotor mB = new EV3LargeRegulatedMotor(MotorPort.B);
		RegulatedMotor mC = new EV3LargeRegulatedMotor(MotorPort.C);
		
		//renkaan halkasija
		final double DIAMETER = 3.17f;
		//renkaiden välinen etäisyys
		final double WIDTH = 19f;
		//alustetaan pilotin, parametrina moottorit, renkaan halkasija ja renkien välinen matka, pilotti on Navigaatorin parametri
		DifferentialPilot pilot = new DifferentialPilot(DIAMETER, WIDTH, mB, mC);
		//huoneen koko (145x150 cm)
		Rectangle rectangle = new Rectangle(0,0,145,150);
		
		//taulukko, jossa kaikki seinät erillisinä janoina
		Line[] lines = new Line[4];
		
		// rajaavan suorakulmion sivut 
		lines[0] = new Line(30,0,145,0); 
		lines[1] = new Line(145,30,145,150); 
		lines[2] = new Line(0,150,115,150); 
		lines[3] = new Line(0,30,0,150);
		//kartta, jossa tiedot kaikista janoista ja huoneen koosta
		LineMap map = new LineMap(lines, rectangle);
		//alustetaan navigaattori, saa pilotin parametrina
		Navigator navi = new Navigator(pilot); 
		//alustetaan polunetsijä, parametrina kartta
		ShortestPathFinder pathFinder = new ShortestPathFinder(map);
		//pidennetään kaikki janat, koska pitää ottaa robotin koko huomioon
		pathFinder.lengthenLines(10);
		//asennetetaan alkuposition 
		Pose pose = new Pose(30,30,90);
		PoseProvider poseprovider = new OdometryPoseProvider(pilot);
		navi.setPoseProvider(poseprovider);
		poseprovider.setPose(pose);
		
		//alustetaan reader-säie tiedonsiirtoyhteyden muodostamista ja datan vastaanottoa varten.
		Reader reader = new Reader();
		reader.start();
		
		//luodaan kaikki käytökset
		Behavior b1 = new Forward(pilot, poseprovider);
		Behavior b2 = new Turn(pilot, navi, poseprovider, pathFinder);
		Behavior b3 = new Stop(pilot);
		Behavior b4 = new TakePhoto(pilot);
		Behavior b5 = new Invite(pilot, navi, pathFinder);
		
		//luodaan lista, joka sisältää kaikki käytökset
		//mitä suurempi indeksi sitä korkeampi prioriteetti käytöksellä
		Behavior[] behaviors = {b1, b5, b4, b2, b3};
		
		//luodaan arbitraattori, jolle annetaan parametrina lista käytöksistä
		//ja käynnistetään se
		Arbitrator arby = new Arbitrator(behaviors);
		arby.go();
		
		
	}
}
