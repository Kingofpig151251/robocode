package lessons;

import robocode.*;
import java.text.NumberFormat;

public class BearingBot extends Robot {
	
	public void run() {
		while (true) {
			// Turn the scanner until we find an enemy robot
			turnRadarRight(360);
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		NumberFormat f = NumberFormat.getNumberInstance();
		f.setMaximumFractionDigits(2);
		out.println("scanned a robot at bearing " + f.format(e.getBearing()));
		out.println("my heading is currently " + f.format(getHeading()));

		// When we scan a robot, turn toward him
		turnRight(e.getBearing());
		// shoot at him
		fire(3);
		// and ram into him
		ahead(e.getDistance());
	}

	public void onHitRobot(HitRobotEvent e) {
		NumberFormat f = NumberFormat.getNumberInstance();
		f.setMaximumFractionDigits(2);
		out.println("hit a robot at bearing " + f.format(e.getBearing()));
		out.println("my heading is currently " + f.format(getHeading()));

		if (e.isMyFault()) {
			out.println("I hit " + e.getName());
		} else {
			out.println(e.getName() + " hit me");
		}
	}
}
