package lessons;

import robocode.*;
import java.awt.geom.Point2D;
import java.awt.Color;

public class Circler extends AdvancedRobot {

	private EnemyBot enemy = new EnemyBot();
	private byte radarDirection = 1;
	private byte moveDirection = 1;
	private boolean updated = false;

	public void run() {
		setColors(Color.orange, Color.gray, Color.white);
		setAdjustRadarForGunTurn(true);
		setAdjustGunForRobotTurn(true);

		while (true) {
			doRadar();
			doMove();
			doGun();
			updated = false;
			execute();
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {

		// track if we have no enemy, the one we found is significantly
		// closer, or we scanned the one we've been tracking.
		if ( enemy.none() || e.getDistance() < enemy.getDistance() - 70 ||
				e.getName().equals(enemy.getName())) {

			// track him using the NEW update method
			enemy.update(e);
			updated = true;
		}
	}

	public void onRobotDeath(RobotDeathEvent e) {
		// see if the robot we were tracking died
		if (e.getName().equals(enemy.getName())) {
			enemy.reset();
		}
	}

	/* -- don't do it this way
	public void onHitWall(HitWallEvent e) { moveDirection *= -1; }
	public void onHitRobot(HitRobotEvent e) { moveDirection *= -1; }
	*/

	void doRadar() {
		setTurnRadarRight(360);
	/*	Implementing Radar Oscillation - lock radar to a certain direction
		if (enemy.none()) {
			// look around if we have no enemy
			setTurnRadarRight(360);
		} else {
			// oscillate the radar
			double turn = getHeading() - getRadarHeading() + enemy.getBearing();
			turn += 30 * radarDirection;
			setTurnRadarRight(normalizeBearing(turn));
			radarDirection *= -1;
		}
	*/
	}

	public void doMove() {
		// switch directions if we've stopped
		if (getVelocity() == 0)
			moveDirection *= -1;
		// always square off against our enemy
		if (updated) setTurnRight(normalizeBearing(enemy.getBearing() + 90));
		// circle our enemy
		setAhead(1000 * moveDirection);
	}

	public void doGun() {
		if (enemy.none()) return;		// don't shoot if I've got no enemy
		if (updated) 
			setTurnGunRight(
			normalizeBearing(getHeading() - getGunHeading() + enemy.getBearing()));
		// if the gun is cool and we're pointed in the right direction, shoot!
		if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 5)
			setFire(3.0);
	}


	// normalizes a bearing to between +180 and -180
	double normalizeBearing(double angle) {
		while (angle >  180) angle -= 360;
		while (angle < -180) angle += 360;
		return angle;
	}
}
