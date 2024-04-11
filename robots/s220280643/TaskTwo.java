package s220280643;

import robocode.*;

public class TaskTwo extends AdvancedRobot {

    private enum MoveState {
        IDLE, // Control state - Used to select which movement action to perform
        FIND_WALL, // Find wall - Move to the nearest wall
        MOVE_ALONG_WALL, // Move along wall - Move to the corner of current wall
        CORNER_TURN, // Corner turn - Turn the robot so that we can move along the next wall
        CHASEING, // Chasing - When only one robot left, we chase the target
        CRASH, // Crash - Unstuck myself when we hit something
    }

    private MeleeRadar meleeRadar =new MeleeRadar(this);
    private AdvancedEnemyBot target = new AdvancedEnemyBot();
    private boolean targetUpdated;
    private MoveState moveState = MoveState.IDLE;
    private double moveAmount;
    private boolean hasTurned = false;
    private byte radarDirection = 1;

    // TODO: Add variables as needed

    @Override
    public void run() {
        setAdjustRadarForGunTurn(true);
        setAdjustGunForRobotTurn(true);
        moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight());
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
        if (target.none()) {
            setTurnRadarLeft(360);
        } else {
            // oscillate the radar
            double turn = getHeading() - getRadarHeading() + target.getBearing();
            turn += 30 * radarDirection;
            setTurnRadarRight(normalizeBearing(turn));
            radarDirection *= -1;
        }
    }

    private void doGun() {
        // TODO: If target is updated, adjust gun turn and fire bullet when possible
        if (targetUpdated) {
            double turn = getHeading() - getGunHeading() + target.getBearing();
            setTurnGunRight(normalizeBearing(turn));
            if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 10 )
                setFire(Math.min(400 / target.getDistance(), 3));
        }
    }

    private void doMove() {
        // TODO: Move handling - use State Machine here
        switch (moveState) {
            case IDLE:
                moveState = MoveState.FIND_WALL;
                break;
            case FIND_WALL:
                System.out.println("Finding wall");
                double distToTopWall = getBattleFieldHeight() - getY();
                double distToBottomWall = getY();
                double distToLeftWall = getX();
                double distToRightWall = getBattleFieldWidth() - getX();

                double minDist = Math.min(Math.min(distToTopWall, distToBottomWall),
                        Math.min(distToLeftWall, distToRightWall));

                if (minDist == distToTopWall) {
                    setTurnRight(normalizeBearing(0 - getHeading()));
                } else if (minDist == distToBottomWall) {
                    setTurnRight(normalizeBearing(180 - getHeading()));
                } else if (minDist == distToLeftWall) {
                    setTurnRight(normalizeBearing(-90 - getHeading()));
                } else if (minDist == distToRightWall) {
                    setTurnRight(normalizeBearing(90 - getHeading()));
                }
                setAhead(moveAmount);
                break;
            case MOVE_ALONG_WALL:
                setAhead(moveAmount);
                break;
            case CORNER_TURN:
                if (!hasTurned) {
                    setTurnRight(90);
                    hasTurned = true;
                }
                if (getTurnRemaining() == 0) {
                    moveState = MoveState.MOVE_ALONG_WALL;
                    hasTurned = false;
                    target.reset();
                }
                break;
            case CHASEING:
                break;
            case CRASH:
                moveState = MoveState.CORNER_TURN;
                break;
        }
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        // TODO: Scan handling
        // Update existing target, or switch target based on distance (closer)
        if ((target.none() || event.getDistance() < target.getDistance())) {
            target.update(event, this);
            targetUpdated = true;
        }
    }

    @Override
    public void onRobotDeath(RobotDeathEvent event) {
        // TODO: Reset target information if the target has died, so we can switch
        // targets
        target.reset();
    }

    @Override
    public void onHitRobot(HitRobotEvent event) {
        // TODO: Collect information about the crash
        if (moveState != MoveState.FIND_WALL) {
            moveState = MoveState.CRASH;
        }
    }

    @Override
    public void onHitWall(HitWallEvent event) {
        // TODO: Collect information about the crash
        moveState = MoveState.CRASH;
    }

    double normalizeBearing(double angle) {
        while (angle > 180)
            angle -= 360;
        while (angle < -180)
            angle += 360;
        return angle;
    }
}
