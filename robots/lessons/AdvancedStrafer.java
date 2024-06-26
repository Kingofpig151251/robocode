package lessons;

import robocode.*;
import java.awt.geom.Point2D;
import java.awt.Color;

public class AdvancedStrafer extends AdvancedRobot {

	private AdvancedEnemyBot enemy = new AdvancedEnemyBot();
	private byte radarDirection = 1;
	private byte moveDirection = 1;
	private boolean updated = true;
	
	public void run() {
		setColors(Color.yellow, Color.cyan, Color.gray);
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
			enemy.update(e, this);
			updated = true;
		}
	}

	public void onRobotDeath(RobotDeathEvent e) {
		// see if the robot we were tracking died
		if (e.getName().equals(enemy.getName())) {
			enemy.reset();
		}
	}

	void doRadar() {
			setTurnRadarRight(360);
	}

	public void doMove() {

		// always square off against our enemy
		if (updated) setTurnRight(normalizeBearing(enemy.getBearing() + 90));

		// strafe by changing direction every 20 ticks
		if (getTime() % 20 == 0) {
			moveDirection *= -1;
			setAhead(150 * moveDirection);
		}
	}

	void doGun() {

		// don't shoot if I've got no enemy
		if (enemy.none())
			return;

		// calculate firepower based on distance
		double firePower = Math.min(400 / enemy.getDistance(), 3);

		if(updated) { // turn gun only when the enemy is recently updated
			// calculate speed of bullet
			double bulletSpeed = 20 - firePower * 3;
			// distance = rate * time, solved for time
			long time = (long)(enemy.getDistance() / bulletSpeed);
	
			// calculate gun turn to predicted x,y location
			double futureX = enemy.getFutureX(time);
			double futureY = enemy.getFutureY(time);
			double absDeg = absoluteBearing(getX(), getY(), futureX, futureY);
	
			// turn the gun to the predicted x,y location
			setTurnGunRight(normalizeBearing(absDeg - getGunHeading()));
		}

		// if the gun is cool and we're pointed in the right direction, shoot!
		if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 10) {
			setFire(firePower);
		}
	}

	// computes the absolute bearing between two points
	double absoluteBearing(double x1, double y1, double x2, double y2) {
		double xo = x2-x1;
		double yo = y2-y1;
		double hyp = Point2D.distance(x1, y1, x2, y2);
		double arcSin = Math.toDegrees(Math.asin(xo / hyp));
		double bearing = 0;

		if (xo > 0 && yo > 0) { // both pos: lower-Left
			bearing = arcSin;
		} else if (xo < 0 && yo > 0) { // x neg, y pos: lower-right
			bearing = 360 + arcSin; // arcsin is negative here, actually 360 - ang
		} else if (xo > 0 && yo < 0) { // x pos, y neg: upper-left
			bearing = 180 - arcSin;
		} else if (xo < 0 && yo < 0) { // both neg: upper-right
			bearing = 180 - arcSin; // arcsin is negative here, actually 180 + ang
		}

		return bearing;
	}

	// normalizes a bearing to between +180 and -180
	double normalizeBearing(double angle) {
		while (angle >  180) angle -= 360;
		while (angle < -180) angle += 360;
		return angle;
	}
}
