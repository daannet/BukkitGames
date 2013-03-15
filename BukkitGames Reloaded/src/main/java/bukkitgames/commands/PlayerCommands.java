package bukkitgames.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import bukkitgames.main.Main;
import bukkitgames.utilities.Gamer;

public class PlayerCommands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
		if(!(sender instanceof Player))
			return false;
		
		Gamer gamer = Main.getInstance().game.getGamer((Player) sender);
		
		if(cmd.getName().equalsIgnoreCase("kit")) {
			if(args.length == 0)
				gamer.openKitMenu();
			else
				gamer.setKit(Main.getInstance().game.getKitByName(args[0]));
			return true;
		}
		
		if(!gamer.getPlayer().hasPermission("bg.admin")) {
			gamer.getPlayer().sendMessage(ChatColor.RED + "You are not allowed to use this command.");
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("start")) {
			Main.getInstance().startGame();
			Bukkit.getServer().broadcastMessage(ChatColor.BLUE + "Game started by " + gamer.getPlayer().getName());
		}
		
		return true;
	}
	
}
