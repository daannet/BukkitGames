package bukkitgames.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import bukkitgames.enums.GameState;
import bukkitgames.main.Main;
import bukkitgames.utilities.Gamer;

public class PlayerListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	private void onPlayerJoin(PlayerJoinEvent event) {
		Main.getInstance().game.addGamer(new Gamer(event.getPlayer()));
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	private void onPlayerQuit(PlayerQuitEvent event) {
		Main.getInstance().game.removeGamer(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	private void onPlayerKick(PlayerKickEvent event) {
		Main.getInstance().game.removeGamer(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	private void onBlockBreak(BlockBreakEvent event) {
		if(Main.getInstance().game.getGameState() == GameState.PREGAME) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	private void onBlockPlace(BlockPlaceEvent event) {
		if(Main.getInstance().game.getGameState() == GameState.PREGAME) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	private void onFoodLevelChange(FoodLevelChangeEvent event) {
		if(Main.getInstance().game.getGameState() == GameState.PREGAME) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	private void onPlayerInteract(PlayerInteractEvent event) {
		if(Main.getInstance().game.getGameState() == GameState.PREGAME) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	private void onPlayerDropItem(PlayerDropItemEvent event) {
		if(Main.getInstance().game.getGameState() == GameState.PREGAME) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	private void onPlayerPickupItem(PlayerPickupItemEvent event) {
		if(Main.getInstance().game.getGameState() == GameState.PREGAME) {
			event.getItem().remove();
			event.setCancelled(true);
		}
	}
}
