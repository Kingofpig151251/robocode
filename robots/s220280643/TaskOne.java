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
        // TODO: Keep turning radar for onScannedRobot events
        
    }

    private void doGun() {
        // TODO: If target is updated, adjust gun turn and fire bullet when possible
    }

    private void doMove() {
        // TODO: Move handling
        // If Evader is alive, we chase the Evader
        // Else chase any target
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        // TODO: Scan handling
        // If Evader is alive, we only record the state of Evader
        // Else update taget by distance to the scanned robot.
        // Hint: Use (event.getName().contains("Evader")) to check if the scanned robot is evader
    }

    @Override
    public void onRobotDeath(RobotDeathEvent event) {
        // TODO: Check if the Evader has died, so we can attack other robots 
    }
}
