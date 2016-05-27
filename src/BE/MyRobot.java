package BE;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.NavigationListener;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.utility.Delay;

public class MyRobot {

	
	private DifferentialPilot pilot;
	private final double wheelDiameterInches = 1.70;
	private final int roboSpeed = 5;
	private Navigator navigator;

	private EV3LargeRegulatedMotor motorLeft, motorRight;

	public MyRobot() {
		Brick brick = BrickFinder.getDefault();
		
		this.motorLeft = new EV3LargeRegulatedMotor(brick.getPort("B"));
		this.motorRight = new EV3LargeRegulatedMotor(brick.getPort("C"));

		pilot = new DifferentialPilot(2.2F, 11.25F, motorLeft, motorRight);
		navigator = new Navigator(pilot);
		
		pilot.setLinearSpeed(roboSpeed);
		
		/*navigator.addNavigationListener(new NavigationListener() {
			
			@Override
			public void pathInterrupted(Waypoint waypoint, Pose pose, int sequence) {
				System.out.println("Path interrupted");
				
			}
			
			@Override
			public void pathComplete(Waypoint waypoint, Pose pose, int sequence) {
				System.out.println("Path complete");
				
			}
			
			@Override
			public void atWaypoint(Waypoint waypoint, Pose pose, int sequence) {
				System.out.println("At waypoint");
				pilot.stop();
				
			}
		});*/
	
		
	}


	public void close() {
		motorLeft.stop();
		motorRight.stop();
		motorLeft.close();
		motorRight.close();
	}

	public DifferentialPilot getPilot() {
		return pilot;
	}
	
	public void goToDestination(float x, float y, float heading){		
		navigator.goTo(x,y,heading);
	}

	

}