package s220280643;

import robocode.*;

public class TaskOne extends AdvancedRobot {

    private AdvancedEnemyBot target = new AdvancedEnemyBot();
    private boolean targetUpdated;
    // TODO: Add variables as needed

    @Override
    public void run() {
        setAdjustRadarForGunTurn(true);
        setAdjustGunForRobotTurn(true);

        while (true) {
            doRadar();
            doGun();
            doMove();

            if (targetUpdated)
                targetUpdated = false;

            execute();
        }
    }

    private void doRadar() {
        // Turn the radar for a full sweep
        turnRadarRight(360);
    }

    private void doGun() {
        if (targetUpdated) {
            // Calculate the angle to turn the gun
            double gunTurnAngle = getHeading() + target.getBearing() - getGunHeading();
                    // Turn the gun and fire
                    turnGunRight(gunTurnAngle);
            fire(1);
            targetUpdated = false;
        }
    }

    private void doMove() {
        // Move towards the target
        ahead(100);
        // If the robot hits a wall, turn a certain degree and move forward, then turn back
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        // Check if the scanned robot is a target
        if (event.getName().contains("Walls")) {
            // Update the target's position
            targetUpdated = true;
            // Save other necessary information about the target (e.g., distance, bearing,
            // velocity)
        }
    }

    @Override
    public void onRobotDeath(RobotDeathEvent event) {
        // TODO: Check if the Evader has died, so we can attack other robots
    }
}
