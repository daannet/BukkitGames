package bukkitgames.timers;

import org.bukkit.Bukkit;

import bukkitgames.main.Main;

public class GameTimer {

	public GameTimer() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				
			}
			
		}, 0, 20*5);
	}
	
}
