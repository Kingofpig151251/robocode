package A_template;

import EnemyBot.EnemyBot;
import robocode.*;
//import java.awt.Color;

// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

abstract public class Template extends AdvancedRobot {

	EnemyBot enemy = new EnemyBot();
	boolean enemyUpdated = false;

	public void run() {
		setAdjustGunForRobotTurn((true));
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
		while (true) {
			doRadar();
			doGun();
			doMove();
			if (enemyUpdated == true)
				enemyUpdated = false;
			execute();
		}
	}

	abstract public void doRadar();

	abstract public void doGun();

	abstract public void doMove();

}
