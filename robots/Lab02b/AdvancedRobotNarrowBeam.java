package Lab02b;
import robocode.*;
//import java.awt.Color;

// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

/**
 * AdvancedRobotNarrowBeam - a robot by (your name here)
 */
public class AdvancedRobotNarrowBeam extends AdvancedRobot {
	private EnemyBot enemy = new EnemyBot();
	private boolean updated = false;

	public void run() {
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
		setAdjustGunForRobotTurn(true);
		enemy.reset();
		while (true) {
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
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		if (enemy.shouldTrack(e, 50)) {
			enemy.update(e);
			System.out.println("Enemy name : " + enemy.getName());
			updated = true;
		}
		fire(1);
	}

	public void onRobotDeath(RobotDeathEvent e) {
		if (e.getName().equals(enemy.getName())) { // if the tracked bot died.
			enemy.reset();
		}
		setTurnRadarRight(360);
	}

}
