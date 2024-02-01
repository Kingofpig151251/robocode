package Lab02b;

import robocode.*;
//import java.awt.Color;

// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

/**
 * NarrowBeam - a robot by (your name here)
 */
public class NarrowBeam extends AdvancedRobot {
	/**
	 * run: NarrowBeam's default behavior
	 */
	private int direction = 1;

	public void run() {
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
		setAdjustGunForRobotTurn(true);
		turnGunRight(360);
		// Initialization of the robot should be put here

		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		// setColors(Color.red,Color.blue,Color.green); // body,gun,radar

		// Robot main loop
		while (true) {
			if (getRadarTurnRemaining() == 0) {
				setTurnRadarRight(direction * 20);
			}
			execute();
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		setTurnRight(e.getBearing());
		setTurnRadarRight(
				robocode.util.Utils.normalRelativeAngleDegrees(getHeading() - getRadarHeading() + e.getBearing()));
		setTurnGunRight(
				robocode.util.Utils.normalRelativeAngleDegrees(getHeading() - getGunHeading() + e.getBearing()));
		if (e.getDistance() > 100) {
			setAhead(e.getDistance() / 2);
		}
		if (e.getDistance() < 100) {
			setBack(e.getDistance());
		}
		if (Math.abs(getGunTurnRemaining()) < 10) {
			setFire(1.0);
		}

		if (e.getBearing() > 0) {
			direction = 1;
		} else if (e.getBearing() < 0) {
			direction = -1;
		}
	}

	public void onRobotDeath(RobotDeathEvent e) {
		setTurnRight(1000);
	}

}
