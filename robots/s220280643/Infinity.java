package s220280643;

import robocode.AdvancedRobot;
import robocode.HitWallEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class Infinity extends AdvancedRobot {
   static final double BASE_MOVEMENT = 153.0D;
   static final double GUN_FACTOR = 500.0D;
   static final double BASE_TURN = 1.5707963267948966D;
   static final double BASE_CANNON_POWER = 20.0D;
   static double movement;
   static double lastHeading;
   static String lastTarget;
   static double lastDistance;

   public void run() {
      this.setAdjustGunForRobotTurn(true);
      movement = Double.POSITIVE_INFINITY;
      this.onRobotDeath((RobotDeathEvent)null);
      this.turnRadarRight(Double.POSITIVE_INFINITY);
   }

   public void onHitWall(HitWallEvent e) {
      if (Math.abs(movement) > 153.0D) {
         movement = 153.0D;
      }

   }

   public void onRobotDeath(RobotDeathEvent e) {
      lastDistance = Double.POSITIVE_INFINITY;
   }

   public void onScannedRobot(ScannedRobotEvent e) {
      double absoluteBearing = e.getDistance();
      if (this.getDistanceRemaining() == 0.0D) {
         this.setAhead(movement = -movement);
         this.setTurnRightRadians(1.5707963267948966D);
      }

      if (lastDistance > absoluteBearing) {
         lastDistance = absoluteBearing;
         lastTarget = e.getName();
      }

      if (lastTarget == e.getName()) {
         if (this.getGunHeat() < 1.0D && (absoluteBearing < 500.0D || e.getEnergy() == 0.0D)) {
            if (this.getGunHeat() == this.getGunTurnRemaining()) {
               this.setFireBullet(this.getEnergy() * 20.0D / absoluteBearing);
               this.onRobotDeath((RobotDeathEvent)null);
            }

            this.setTurnRadarLeft(this.getRadarTurnRemaining());
         }

         absoluteBearing = e.getBearingRadians() + this.getHeadingRadians();
         this.setTurnGunRightRadians(Utils.normalRelativeAngle(absoluteBearing - this.getGunHeadingRadians() + Math.max(1.0D - e.getDistance() / 600.0D, 0.0D) * Math.asin(e.getVelocity() / 11.0D) * Math.sin(e.getHeadingRadians() - absoluteBearing)));
      }

   }
}
