## ProtectionPvP
A Bukkit/Spigot Plugin

With ProtectionPvP you can give players a period of time of protection against PvP, also offering the possibility of automatically giving this protection only once to new players.
You can also define areas for pvp in which you can decide if players with protection will be able to enter.

[Spigot-Page](https://spigotmc.org/resources/121277) for more information

## API
To use the API you need to add ProtectionPvP to your project and declare it as a dependency in the plugin.yml.

Add the plugin to you project by adding the ProtectionPvP.jar to your build-path

Or you can also:
#### Maven dependency in your pom.xml

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
...
<dependency>
  <groupId>com.github.dewrs28</groupId>
  <artifactId>ProtectionPvP</artifactId>
  <version>1.4</version>
</dependency>
```
Note: Jitpack also supports dependencies for gradle

[ProtectionPvP on Jitpack](https://jitpack.io/#dewrs28/ProtectionPvP)

### How to use the API

Since it is somewhat small at the moment, it is also quite easy to understand. 
For now it includes the "ToggleProtectionEvent", which is called when a player's protection is activated or deactivated.

Example:
```java
@EventHandler
    public void toggleProte(ToggleProtectionEvent event){
        Player player = event.getPlayer();
        boolean status = event.isProtected();
        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendMessage(player.getName()+"'s pvp protection was set as "+status);
        }
    }
```
Along with three methods that I will now show you with an example

Example:
```java
@EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(ProtectionPvPAPI.isProtected(player)){ //Detect if a player has protection
            int time = ProtectionPvPAPI.getActualProtectionTime(player); //Get the remaining protection time of a player (in seconds)
            player.sendMessage("You have "+time+" seconds left of protection against pvp");
            List<String> zones = ProtectionPvPAPI.getPvPZones(); //Obtain a list with all the zones that are defined for pvp
            player.sendMessage("You will not be able to enter the following areas: "+zones.toString());
        }
    }
```
