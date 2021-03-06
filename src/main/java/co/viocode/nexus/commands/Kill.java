package co.viocode.nexus.commands;

import co.viocode.nexus.Nexus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Kill implements CommandExecutor {

	private Nexus plugin;
	public Kill(Nexus plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

		// invalid args
		if (args.length > 1)
			return false;

		// check if player
		Boolean isPlayer = true;
		if (!(sender instanceof Player))
			isPlayer = false;

		// init vars
		Player player = null;
		if (isPlayer)
			player = (Player) sender;

		// <command>
		if (args.length == 0) {

			// check if player
			if (!isPlayer)
				return false;

			// check permission
			if (isPlayer)
				if (!Nexus.checkPermission("nexus.kill", player))
					return true;

			player.setHealth(0);
			return true;
		}

		// <command> (player)
		if (args.length == 1) {

			// check permission
			if (isPlayer)
				if (!Nexus.checkPermission("nexus.kill.others", player))
					return true;

			// init vars
			Player target = Nexus.findOnlinePlayer(args[0].toLowerCase());

			// check if player is offline
			if (target == null) {
				sender.sendMessage(ChatColor.RED + "Player is not online.");
				return true;
			}

			// check if kill is blocked
			if (isPlayer) {
				if (!player.hasPermission("nexus.kill.bypass")) {
					if (target.hasPermission("nexus.kill.block")) {
						sender.sendMessage(ChatColor.RED + "Player cannot be killed.");
						return true;
					}
				}
			}

			// kill player
			target.setHealth(0);
			target.sendMessage(ChatColor.RED + sender.getName() + " killed you.");
			return true;
		}

		// end of command
		return false;
	}
}