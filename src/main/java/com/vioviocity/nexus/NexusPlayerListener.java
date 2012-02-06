package com.vioviocity.nexus;

import java.text.DecimalFormat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class NexusPlayerListener implements Listener{
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // new player to spawn
        if (!player.hasPlayedBefore()) {
            Location spawn = player.getLocation();
            spawn.setWorld(Bukkit.getServer().getWorld(Nexus.spawnConfig.getString("nexus.spawn.world")));
            spawn.setX(Nexus.spawnConfig.getDouble("nexus.spawn.x"));
            spawn.setY(Nexus.spawnConfig.getDouble("nexus.spawn.y"));
            spawn.setZ(Nexus.spawnConfig.getDouble("nexus.spawn.z"));
            spawn.setYaw((float) Nexus.spawnConfig.getDouble("nexus.spawn.yaw"));
            spawn.setPitch((float) Nexus.spawnConfig.getDouble("nexus.spawn.pitch"));
            player.teleport(spawn);
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        // ensure proper teleport cause
        if (!event.getCause().toString().equals("UNKNOWN"))
            setTpBack(event.getPlayer(), event.getFrom());
    }
    
    @EventHandler
    public void onPlayerDeath(EntityDeathEvent event) {
        // check if entity is player
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            // permission check
            if (NexusCommands.checkPermission("nexus.back.death", player))
                setTpBack(player, player.getLocation());
        }
    }
    
    private void setTpBack(Player player, Location locale) {
        // player current location
        String worldName = locale.getWorld().getName();
        double x = round(locale.getX());
        double y = round(locale.getY());
        double z = round(locale.getZ());
        float yaw = (float) round(locale.getYaw());
        float pitch = (float) round(locale.getPitch());
        
        // change existing location
        String tpName;
        for (String each : NexusCommands.tpBack) {
            tpName = each.substring(0, each.indexOf(','));
            if (player.getName().equals(tpName)) {
                NexusCommands.tpBack.set(NexusCommands.tpBack.indexOf(each), player.getName() + ',' + "map:" +
                        worldName + "x:" + x + "y:" + y + "z:" + z + "yaw:" + yaw + "pitch:" + pitch);
                return;
            }
        }
        
        // add new location
        NexusCommands.tpBack.add(player.getName() + ',' + "map:" + worldName + "x:" + x + "y:" + y + "z:" + z +
                "yaw:" + yaw + "pitch:" + pitch);
    }
    
    private double round(double value) {
        DecimalFormat newFormat = new DecimalFormat("#.#");
        return Double.valueOf(newFormat.format(value));
    }
}