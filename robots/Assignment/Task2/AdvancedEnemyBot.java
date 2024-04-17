package Task2;

import robocode.Robot;
import robocode.ScannedRobotEvent;

public class AdvancedEnemyBot extends EnemyBot {

	private double x;
	private double y;

	public AdvancedEnemyBot() {
		reset();
	}

	public void reset() {
		super.reset();
		x = 0.0;
		y = 0.0;
	}

	public void update(ScannedRobotEvent e, Robot robot) {
		super.update(e);

		double absBearingDeg = (robot.getHeading() + e.getBearing());
		if (absBearingDeg < 0)
			absBearingDeg += 360;

		x = robot.getX() + Math.sin(Math.toRadians(absBearingDeg)) * e.getDistance();
		y = robot.getY() + Math.cos(Math.toRadians(absBearingDeg)) * e.getDistance();
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getFutureX(long when) {
		return x + Math.sin(Math.toRadians(getHeading())) * getVelocity() * when;
	}

	public double getFutureY(long when) {
		return y + Math.cos(Math.toRadians(getHeading())) * getVelocity() * when;
	}
}
