package jk.melee;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Enumeration;
import java.util.Hashtable;
import jk.mega.FastTrig;
import robocode.AdvancedRobot;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class MeleeRadar {
   Hashtable<String, MeleeRadar.EnemyInfo> enemies = new Hashtable();
   AdvancedRobot bot;
   Double myLocation;

   public MeleeRadar(AdvancedRobot var1) {
      this.bot = var1;
   }
      this.enemies.clear();
   }

   public void onTick() {
      if (this.bot.getRadarTurnRemaining() == 0.0D) {
         this.bot.setTurnRadarRightRadians(java.lang.Double.POSITIVE_INFINITY);
      }

      this.myLocation = new Double(this.bot.getX(), this.bot.getY());
   }

   public void onScannedRobot(ScannedRobotEvent var1) {
      String var2 = var1.getName();
      MeleeRadar.EnemyInfo var3;
      if ((var3 = (MeleeRadar.EnemyInfo)this.enemies.get(var2)) == null) {
         this.enemies.put(var2, var3 = new MeleeRadar.EnemyInfo());
         var3.name = var2;
      }

      var3.lastScanTime = (int)this.bot.getTime();
      double var4;
      var3.location = project(this.myLocation, var4 = this.bot.getHeadingRadians() + var1.getBearingRadians(), var1.getDistance());
      if (this.bot.getOthers() <= this.enemies.size()) {
         Enumeration var6 = this.enemies.elements();
         int var7 = var3.lastScanTime;

         while(var6.hasMoreElements()) {
            MeleeRadar.EnemyInfo var8 = (MeleeRadar.EnemyInfo)var6.nextElement();
            if (var8.lastScanTime < var7) {
               var4 = absoluteAngle(this.myLocation, var8.location);
               var7 = var8.lastScanTime;
            }
         }

         if (this.bot.getOthers() == 1 && var7 == var3.lastScanTime) {
            double var10 = Utils.normalRelativeAngle(var4 - this.bot.getRadarHeadingRadians());
            this.bot.setTurnRadarRightRadians(Math.signal(var10) * limit(0.0D, Math.abs(var10) + 0.2181661564992912D, 0.7853981633974483D));
         } else {
            this.bot.setTurnRadarRightRadians(Utils.normalRelativeAngle(var4 - this.bot.getRadarHeadingRadians()) * java.lang.Double.POSITIVE_INFINITY);
         }
      } else {
         this.bot.setTurnRadarRightRadians(java.lang.Double.POSITIVE_INFINITY);
      }

   }

   static Double project(Double var0, double var1, double var3) {
      return new Double(var0.x + var3 * FastTrig.sin(var1), var0.y + var3 * FastTrig.cos(var1));
   }

   static double absoluteAngle(Point2D var0, Point2D var1) {
      return FastTrig.atan2(var1.getX() - var0.getX(), var1.getY() - var0.getY());
   }

   public void onRobotDeath(RobotDeathEvent var1) {
      this.enemies.remove(var1.getName());
   }

   public static double limit(double var0, double var2, double var4) {
      if (var2 > var4) {
         return var4;
      } else {
         return var2 < var0 ? var0 : var2;
      }
   }

   class EnemyInfo {
      String name;
      int lastScanTime;
      Double location = new Double();
   }
}
