package com.vioviocity.nexus.commands;

import com.vioviocity.nexus.Nexus;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReplyCommand implements CommandExecutor{
    
    static public Map<Player,Player> msgReply = new HashMap<Player,Player>(200);
    
    private Nexus plugin;
    public ReplyCommand(Nexus plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("[Nexus] Command must be issued within game.");
            return true;
        }
        
        // initialize variables
        Player player = (Player) sender;
        
        // command handler
        String cmd = command.getName().toLowerCase();
        if (cmd.equals("reply")) {
	    
            // check permission
            if (!Nexus.checkPermission("nexus.msg", player, true))
                return true;
            
            // check mute
            if (MuteCommand.msgMute.contains(player)) {
                player.sendMessage(ChatColor.RED + "You are muted.");
                return true;
            }
            
            // <comamnd>
            String message = "";
	    for (String each : args)
                message += each + " ";
            message = message.substring(0, message.length() - 1);
            
            // check request
            for (Map.Entry<Player,Player> entry : msgReply.entrySet()) {
                if (entry.getValue().equals(player)) {
                    
                    // check online
                    if (!entry.getKey().isOnline()) {
                        player.sendMessage(ChatColor.RED + "Player is not online.");
                        return true;
                    }
                    
                    // send message
                    entry.getKey().sendMessage(ChatColor.GREEN + "[" + player.getName() + " -> me] " + ChatColor.WHITE + message);
                    player.sendMessage(ChatColor.GREEN + "[me -> " + entry.getKey().getName() + "] " + ChatColor.WHITE + message);
                    msgReply.put(player, entry.getKey());
                    return true;
                }
            }
            
            // player not found
            player.sendMessage(ChatColor.RED + "You have not received a message.");
            return true;
        }
        
        // end of command
        return false;
    }
}