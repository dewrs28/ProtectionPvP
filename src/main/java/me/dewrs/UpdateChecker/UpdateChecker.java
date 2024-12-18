package me.dewrs.UpdateChecker;

import me.dewrs.ProtectionPvP;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker {
    private ProtectionPvP plugin;
    private String version;
    private String latestVersion;

    public UpdateChecker(String version) {
        this.version = version;
    }

    public UpdateCheckerResult check(){
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(
                    "https://api.spigotmc.org/legacy/update.php?resource=121277").openConnection();
            int timed_out = 1250;
            con.setConnectTimeout(timed_out);
            con.setReadTimeout(timed_out);
            latestVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if (latestVersion.length() <= 7) {
                if(!version.equals(latestVersion)){
                    return UpdateCheckerResult.noErrors(latestVersion);
                }
            }
            return UpdateCheckerResult.noErrors(null);
        } catch (Exception ex) {
            return UpdateCheckerResult.error();
        }
    }

    public String getLatestVersion() {
        return latestVersion;
    }
}