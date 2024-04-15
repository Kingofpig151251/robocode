package s220280643;

import robocode.*;

import java.awt.Color;
import java.awt.geom.Point2D;

public class TaskTwo extends AdvancedRobot {

    private enum MoveState {
        IDLE, // Control state - Used to select which movement action to perform
        FIND_WALL, // Find wall - Move to the nearest wall
        MOVE_ALONG_WALL, // Move along wall - Move to the corner of current wall
        CORNER_TURN, // Corner turn - Turn the robot so that we can move along the next wall
        CHASING, // Chasing - When only one robot left, we chase the target
        REVERSE_DIRECTION, // Reverse direction - When we hit the other robot, we reverse the direction
    }

    private AdvancedEnemyBot target = new AdvancedEnemyBot();
    private boolean targetUpdated;
    private MoveState moveState = MoveState.IDLE;
    private double moveAmount;
    private boolean hasTurned = false;
    private byte radarDirection = 1;
    private byte moveDirection = 1;

    // TODO: Add variables as needed

    @Override
    public void run() {
        setColors(Color.PINK, Color.PINK, Color.PINK);
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
        if (target.none()) {
            setTurnRadarRight(360);
        } else {
            // oscillate the radar
            double turn = getHeading() - getRadarHeading() + target.getBearing();
            turn += 10 * radarDirection;
            setTurnRadarRight(normalizeBearing(turn));
            radarDirection *= -1;
        }
    }

    private void doGun() {
        // TODO: If target is updated, adjust gun turn and fire bullet when possible
        if (target.none())
            return;

        double firePower = Math.min(400 / target.getDistance(), 3);

        if (targetUpdated) { // turn gun only when the enemy is recently updated
            double bulletSpeed = 20 - firePower * 3;
            long time = (long) (target.getDistance() / bulletSpeed);
            double futureX = target.getFutureX(time);
            double futureY = target.getFutureY(time);
            double absDeg = absoluteBearing(getX(), getY(), futureX, futureY);
            setTurnGunRight(normalizeBearing(absDeg - getGunHeading()));
        }

        if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 10) {
            setFire(firePower);
        }
    }

    private void doMove() {
        // TODO: Move handling - use State Machine here
        if (getOthers() == 1) {
            moveState = MoveState.CHASING;
        }
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
                System.out.println("Moving along wall");
                setTurnLeft(getHeading() % 90);
                setAhead(moveAmount * moveDirection);
                break;
            case CORNER_TURN:
                System.out.println("Corner turn");
                if (!hasTurned) {
                    setTurnRight(90 * moveDirection);
                    hasTurned = true;
                } else if (hasTurned) {
                    if (getTurnRemaining() == 0) {
                        hasTurned = false;
                        moveState = MoveState.MOVE_ALONG_WALL;
                    }
                }
                break;
            case CHASING:
                System.out.println("Chasing");
                if (targetUpdated) {
                    setTurnRight(normalizeBearing(normalizeBearing(target.getBearing())));
                    setAhead(target.getDistance());
                }
                break;
            case REVERSE_DIRECTION:
                System.out.println("Reverse direction");
                    moveDirection *= -1;
                    moveState = MoveState.MOVE_ALONG_WALL;
                break;
        }
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        if (target.none() || target.getDistance() < target.getDistance() - 70 ||
                event.getName().equals(target.getName())) {

            // track him using the NEW update method
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
        moveState = MoveState.REVERSE_DIRECTION;
    }

    @Override
    public void onHitWall(HitWallEvent event) {
        // TODO: Collect information about the crash
        moveState = MoveState.CORNER_TURN;
    }

    // computes the absolute bearing between two points
    double absoluteBearing(double x1, double y1, double x2, double y2) {
        double xo = x2 - x1;
        double yo = y2 - y1;
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
        while (angle > 180)
            angle -= 360;
        while (angle < -180)
            angle += 360;
        return angle;
    }
}
