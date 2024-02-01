package Lab02a;

import java.text.NumberFormat;

import robocode.*;
//import java.awt.Color;

// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

/**
 * WallBanger - a robot by (your name here)
 */
public class WallBanger extends Robot {

	public void run() {
		out.println("Battlefield width = " + getBattleFieldWidth());
		out.println("Battlefield height = " + getBattleFieldHeight());
		while (true) { // move continuously
			turnLeft(getHeading()); // face up
			ahead(10000);
			turnRight(90); // face right
			ahead(10000);
			turnRight(90); // face down
			ahead(10000);
			turnRight(90); // face left
			ahead(10000);
		}
	}

	public void onHitWall(HitWallEvent event) {
		// Need to import java.text.NumberFormat
		NumberFormat f = NumberFormat.getNumberInstance();
		f.setMaximumFractionDigits(2);
		out.println("hit a wall (x, y) = (" +
				f.format(getX()) + ", " + f.format(getY()) + ")");
	}

}
