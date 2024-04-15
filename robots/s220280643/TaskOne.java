package s220280643;

import java.awt.Color;
import robocode.*;

public class TaskOne extends AdvancedRobot {

    private AdvancedEnemyBot target = new AdvancedEnemyBot();
    private boolean targetUpdated;
    private byte radarDirection = 1;

    // TODO: Add variables as needed

    @Override
    public void run() {
        setColors(Color.PINK, Color.BLUE, Color.GRAY);
        setAdjustRadarForGunTurn(true);
        setAdjustGunForRobotTurn(false);
        while (true) {
            doRadar();
            doGun();
            doMove();

            if (targetUpdated)
                targetUpdated = false;
            if (getVelocity() == 0) {
                setTurnRadarRight(360);
            }
            execute();
        }
    }

    private void doRadar() {
        // TODO: Keep turning radar for onScannedRobot events
        if (target.none()) {
            // look around if we have no enemy
            setTurnRadarRight(360);
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
            if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 10 && target.getDistance() < 100)
                setFire(Math.min(400 / target.getDistance(), 3));
        }
    }

    private void doMove() {
        // TODO: Move handling
        // If Evader is alive, we chase the Evader
        // Else chase any target
        if (targetUpdated) {
            setTurnRight(target.getBearing());
            setAhead(target.getDistance());
        }
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        // TODO: Scan handling
        // If Evader is alive, we only record the state of Evader
        // Else update taget by distance to the scanned robot.
        // Hint: Use (event.getName().contains("Evader")) to check if the scanned robot
        // is evader
        if (!event.getName().contains("Walls")) {
            return;
        }
        if (target.none() || target.getName().equals(event.getName())) {
            target.update(event, this);
            targetUpdated = true;
        }
    }

    @Override
    public void onRobotDeath(RobotDeathEvent event) {
        // TODO: Check if the Evader has died, so we can attack other robots
        if (target.getName().equals(event.getName())) {
            target.reset();
        }
    }

    // normalizes a bearing to between +180 and -180
    double normalizeBearing(double angle) {
        while (angle > 180)
            angle -= 360;
        while (angle < -180)
            angle += 360;
        return angle;
    }

    @Override
    public void onHitRobot(HitRobotEvent event) {
        // Check if the hit robot is not "Walls"
        if (!event.getName().contains("Walls")) {
            // Code to avoid the robot
            // For example, back up a bit and then turn to the right
            back(20);
            turnRight(90);
        }
    }
}
