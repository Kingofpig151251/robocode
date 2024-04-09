package lessons;

import robocode.*;

public class NarrowBeam extends AdvancedRobot {
	private int direction = 1;

	public void run() {
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
		setAdjustGunForRobotTurn(true);
		turnRadarRight(360); // initial scan

		while (true) {

			// if we stopped moving the radar, move it a tiny little bit
			// so we keep generating scan events
			if (getRadarTurnRemaining() == 0)
				setTurnRadarRight(direction*20);

			execute();
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		//out.println("scanned: " + e.getName() + " at " + getTime());

		// turn toward the robot we scanned
		setTurnRight(e.getBearing());
		// lock our radar onto our target
		setTurnRadarRight(robocode.util.Utils.normalRelativeAngleDegrees(
			getHeading() - getRadarHeading() + e.getBearing()));
		// lock our gun onto our target
		setTurnGunRight(robocode.util.Utils.normalRelativeAngleDegrees(
			getHeading() - getGunHeading() + e.getBearing()));

		// move a little closer
		if (e.getDistance() > 200) {
			setAhead(e.getDistance() / 2);
		}
		// but not too close
		if (e.getDistance() < 100) {
			setBack(e.getDistance());
		}

		// if we've turned toward our enemy...
		if (Math.abs(getGunTurnRemaining()) < 10) {
			setFire(3.0);
		}

		if (e.getBearing()>0) direction = 1;
		else if (e.getBearing()<0) direction = -1;

	}

	// if the robot we were shooting at died, scan around again
	public void onRobotDeath(RobotDeathEvent e) { setTurnRadarRight(1000); }
}
