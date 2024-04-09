package lessons;

import robocode.*;

public class AdvancedNarrowBeam extends AdvancedRobot {

	private EnemyBot enemy = new EnemyBot();
	private boolean updated = false;

	public void run() {

		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
		setAdjustGunForRobotTurn(true);

		enemy.reset();

		while (true) {

			if (enemy.none()) {
				
				setTurnRadarRight(360); // initial scan

			} else {
			
				if (updated) {
					// lock our gun onto our target
					setTurnGunRight(robocode.util.Utils.normalRelativeAngleDegrees(
						getHeading() - getGunHeading() + enemy.getBearing()));
					// turn toward the robot we scanned
					setTurnRight(enemy.getBearing());
	
					// move a little closer
					if (enemy.getDistance() > 200) {
						setAhead(enemy.getDistance() / 2);
					}
					// but not too close
					if (enemy.getDistance() < 100) {
						setBack(enemy.getDistance());
					}
					updated = false;
				} 
				
				// if we've turned toward our enemy...
				if (Math.abs(getGunTurnRemaining()) < 5) {
					setFire(3.0);
				}
			}
			if (getRadarTurnRemaining() == 0)
				setTurnRadarRight(360);
			execute();
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		// out.println("scanned: " + e.getName() + " at " + getTime());
		if (enemy.shouldTrack(e, 50)) {
			enemy.update(e);
			out.println("Enemy updated: " + e.getName());
			updated = true;
		}
	}

	public void onRobotDeath(RobotDeathEvent e) {
		// see if the robot we were tracking died
		if (e.getName().equals(enemy.getName())) {
			enemy.reset();
		}
		setTurnRadarRight(360);
	}
}
