package bukkitgames.timers;

import org.bukkit.Bukkit;

import bukkitgames.main.Main;

public class PreGameTimer {

	private Integer shed_id = null;
	
	public PreGameTimer() {
		shed_id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {

			}
			
		}, 0, 20*5);
	}
	
	private void cancel() {
		if(shed_id != null)
			Bukkit.getScheduler().cancelTask(shed_id);
		shed_id = null;
	}
	
}
