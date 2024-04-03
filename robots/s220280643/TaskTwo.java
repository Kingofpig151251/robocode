package s220280643;

import robocode.*;

public class TaskTwo extends AdvancedRobot {

    private enum MoveState {
        IDLE,               // Control state - Used to select which movement action to perform
        FIND_WALL,          // Find wall - Move to the nearest wall
        MOVE_ALONG_WALL,    // Move along wall - Move to the corner of current wall
        CORNER_TURN,        // Corner turn - Turn the robot so that we can move along the next wall
        CHASEING,           // Chasing - When only one robot left, we chase the target
        CRASH,              // Crash - Unstuck myself when we hit something
    }

    private AdvancedEnemyBot target = new AdvancedEnemyBot();
    private boolean targetUpdated;
    private MoveState moveState = MoveState.IDLE;
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
        // TODO: Move handling - use State Machine here
        switch (moveState) {
            case IDLE:
                break;
            case FIND_WALL:
                break;
            case MOVE_ALONG_WALL:
                break;
            case CORNER_TURN:
                break;
            case CHASEING:
                break;
            case CRASH:
                break;
        }
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        // TODO: Scan handling
        // Update existing target, or switch target based on distance (closer)
    }

    @Override
    public void onRobotDeath(RobotDeathEvent event) {
        // TODO: Reset target information if the target has died, so we can switch targets
    }

    @Override
    public void onHitRobot(HitRobotEvent event) {
        // TODO: Collect information about the crash
        moveState = MoveState.CRASH;
    }

    @Override
    public void onHitWall(HitWallEvent event) {
        // TODO: Collect information about the crash
        moveState = MoveState.CRASH;
    }
}
