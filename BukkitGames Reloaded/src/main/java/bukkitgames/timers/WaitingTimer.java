package bukkitgames.timers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import bukkitgames.enums.GameState;
import bukkitgames.main.Main;
import bukkitgames.utilities.Game;

public class WaitingTimer {

	private Integer shed_id = null;
	
	public WaitingTimer() {
		shed_id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				Game game = Main.getInstance().game;
				if(game.getGameState() == GameState.PREGAME) {
					if(game.getCountdown() != null) {
						if(!(game.getGamers().size() > 0)) {
							game.setCountdown(null);
							Bukkit.getServer().broadcastMessage(ChatColor.BLUE + "Countdown resetted.");
							return;
						}
						game.setCountdown(game.getCountdown() - 1);
						if(game.getCountdown() > 0) {
							if(game.getCountdown() > 10 && game.getCountdown() % 15 == 0) {
								Bukkit.getServer().broadcastMessage(ChatColor.BLUE + game.getCountdownTime() + " until game begins.");
							} else if(game.getCountdown() < 10) {
								String time = "0" + game.getCountdown().toString() + " seconds";
								Bukkit.getServer().broadcastMessage(ChatColor.BLUE + "" + ChatColor.BOLD + time + " until game begins.");
							}
						} else {
							Main.getInstance().startGame();
						}
					} else {
						if(game.getGamers().size() > 0) {
							game.setCountdown(3*60);
						}
					}
				} else {
					cancel();
				}
			}
			
		}, 0, 20);
	}
	
	private void cancel() {
		if(shed_id != null)
			Bukkit.getScheduler().cancelTask(shed_id);
		shed_id = null;
	}
	
}
