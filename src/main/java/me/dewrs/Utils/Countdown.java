package me.dewrs.Utils;

public class Countdown {
    public static String getCountdown(long actualCooldown, long cooldown) {
        if (actualCooldown != 0) {
            long millis = System.currentTimeMillis();
            long cooldownmil = cooldown * 1000;

            long espera = millis - actualCooldown;
            long esperaDiv = espera / 1000;
            long esperatotalseg = cooldown - esperaDiv;
            long esperatotalmin = esperatotalseg / 60;
            long esperatotalhour = esperatotalmin / 60;
            if (((actualCooldown + cooldownmil) > millis)) {
                if (esperatotalseg > 59) {
                    esperatotalseg = esperatotalseg - 60 * esperatotalmin;
                }
                String time = "";
                if (esperatotalseg != 0) {
                    time = esperatotalseg + "s";
                }

                if (esperatotalmin > 59) {
                    esperatotalmin = esperatotalmin - 60 * esperatotalhour;
                }
                if (esperatotalmin > 0) {
                    time = esperatotalmin + "min" + " " + time;
                }

                if (esperatotalhour > 0) {
                    time = esperatotalhour + "h" + " " + time;
                }

                //Aun no se termina el cooldown
                return time;
            }
        }
        return "";
    }

    public static int getSecsLeft(long actualCooldown, long cooldown){
        long esperatotalseg = 0;
        if(actualCooldown != 0) {
            long millis = System.currentTimeMillis();
            long espera = millis - actualCooldown;
            long esperaDiv = espera/1000;
            esperatotalseg = cooldown - esperaDiv;
        }
        return (int) esperatotalseg;
    }
}
