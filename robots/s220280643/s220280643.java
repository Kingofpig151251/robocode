package LeungTungLam;

import robocode.*;

import java.awt.Color;
import java.awt.geom.Point2D;
import s220280643.AdvancedEnemyBot;

public class s220280643 extends AdvancedRobot {

    private enum MoveState {
        CIRCLERC, // Circler - Circling the target
        STRAFER, // Strafer - Strafing the target
        ESCAPE // Escape - Escaping from the target

    }

    private AdvancedEnemyBot target = new AdvancedEnemyBot();
    private boolean targetUpdated;
    private MoveState moveState = MoveState.STRAFER;
    private byte radarDirection = 1;
    private byte moveDirection = 1;
    private int sentryBorderSize;

    // TODO: Add variables as needed

    @Override
    public void run() {
        setColors(Color.PINK, Color.PINK, Color.PINK);
        sentryBorderSize = getSentryBorderSize();
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
        if (target.none()) {
            setTurnRadarRight(360);
        } else {
            double turn = getHeading() - getRadarHeading() + target.getBearing();
            turn += 10 * radarDirection;
            setTurnRadarRight(normalizeBearing(turn));
            radarDirection *= -1;
        }
    }

    private void doGun() {
        // don't shoot if I've got no enemy
        if (target.none())
            return;

        // calculate firepower based on distance
        double firePower = Math.min(400 / target.getDistance(), 3);

        if (targetUpdated) { // turn gun only when the enemy is recently updated
            // calculate speed of bullet
            double bulletSpeed = 20 - firePower * 3;
            // distance = rate * time, solved for time
            long time = (long) (target.getDistance() / bulletSpeed);

            // calculate gun turn to predicted x,y location
            double futureX = target.getFutureX(time);
            double futureY = target.getFutureY(time);
            double absDeg = absoluteBearing(getX(), getY(), futureX, futureY);

            // turn the gun to the predicted x,y location
            setTurnGunRight(normalizeBearing(absDeg - getGunHeading()));
        }

        // if the gun is cool and we're pointed in the right direction, shoot!
        if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 10) {
            setFire(firePower);
        }
    }

    private void doMove() {
        if (getOthers() == 1) {
            moveState = MoveState.CIRCLERC;
        }
        switch (moveState) {
            case CIRCLERC:
                if (getVelocity() == 0)
                    moveDirection *= -1;
                if (targetUpdated)
                    setTurnRight(normalizeBearing(target.getBearing() + 90));
                setAhead(1000 * moveDirection);
                break;
            case STRAFER:
                if (targetUpdated)
                    setTurnRight(normalizeBearing(target.getBearing() + 90));
                if (getTime() % 20 == 0) {
                    moveDirection *= -1;
                    setAhead(150 * moveDirection);
                }
                break;
            case ESCAPE:
                setTurnRight(90);
                setAhead(100 * moveDirection);
                if (getVelocity() != 0) {
                    moveState = MoveState.STRAFER;
                }
                break;
        }
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        // If the scanned robot is a BorderGuard, do not lock onto it
        if (event.getName().contains("BorderGuard")) {
            return;
        }

        if (target.none() || event.getDistance() < target.getDistance() - 70 ||
                event.getName().equals(target.getName())) {
            target.update(event, this);
            targetUpdated = true;
        }
    }

    @Override
    public void onRobotDeath(RobotDeathEvent event) {
        target.reset();
    }

    @Override
    public void onHitRobot(HitRobotEvent event) {
        if (event.isMyFault()) {
            moveDirection *= -1;
            setBack(100);
        }
    }

    @Override
    public void onHitWall(HitWallEvent event) {
        moveDirection *= -1;
        moveState = MoveState.ESCAPE; // Set the state to ESCAPE when hitting a wall
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

    // Check if the the scanned robot is BorderGuard
    boolean isBorderGuards(ScannedRobotEvent event) {
        return event.getName().contains("BorderGuard");
    }

}
