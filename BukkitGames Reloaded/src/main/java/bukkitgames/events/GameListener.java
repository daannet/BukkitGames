package bukkitgames.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.server.ServerListPingEvent;

import bukkitgames.enums.GameState;
import bukkitgames.main.Main;

public class GameListener implements Listener {
	//event.getEntity().getLocation().getWorld().spawnFallingBlock(event.getEntity().getLocation().add(0, 3, 0), Material.RED_ROSE, (byte) 0);
	
	@EventHandler(priority = EventPriority.HIGH)
	private void onEntityDamage(EntityDamageEvent event) {
		if(Main.getInstance().game.getGameState() != GameState.RUNNING) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	private void onEntityInteract(EntityInteractEvent event) {
		if(Main.getInstance().game.getGameState() == GameState.PREGAME) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	private void onEntityExplode(EntityExplodeEvent event) {
		if(Main.getInstance().game.getGameState() == GameState.PREGAME) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	private void onServerListPing(ServerListPingEvent event) {
		switch(Main.getInstance().game.getGameState()) {
			case PREGAME:
				if(Main.getInstance().game.getGamers().size() > 0)
					event.setMotd("Game starting in " + Main.getInstance().game.getCountdownTime());
				else
					event.setMotd("Waiting for players");
				break;
			case INVINCIBILITY:
				event.setMotd("Game is in progress");
				if((Boolean) Main.getInstance().getConfigOptions().get("GAMERS_IN_LIST"))
					event.setMaxPlayers(Main.getInstance().game.getCompetedGamers());
				break;
			case RUNNING:
				event.setMotd("Game is in progress");
				if((Boolean) Main.getInstance().getConfigOptions().get("GAMERS_IN_LIST"))
					event.setMaxPlayers(Main.getInstance().game.getCompetedGamers());
				break;
			default:
				break;
		}
	}
	
}
