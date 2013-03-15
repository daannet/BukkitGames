package bukkitgames.utilities;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import bukkitgames.main.Main;
import bukkitgames.utilities.IconMenu.OptionClickEvent;

public class Gamer {

	private Player player;
	private Boolean gamemaker = false;
	private Boolean spectator = false;
	private Boolean winner = false;
	private Kit kit;
	private Ability[] abilities;
	private IconMenu menu;
	
	public Gamer(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setGameMaker(Boolean set) {
		gamemaker = set;
	}
	
	public Boolean isGameMaker() {
		return gamemaker;
	}
	
	public void setSpectator(Boolean set) {
		spectator = set;
	}
	
	public Boolean isSpectator() {
		return spectator;
	}
	
	public void setWinner(Boolean set) {
		winner = set;
	}
	
	public Boolean isWinner() {
		return this.winner;
	}
	
	public void setAbilities(Ability[] abilities) {
		this.abilities = abilities;
	}
	
	public Ability[] hasAbility(Ability ability) {
		return this.abilities;
	}
	
	public void setKit(Kit kit) {
		if(canUseKit(kit)) {
			this.kit = kit;
		} else {
			player.sendMessage(ChatColor.RED + "You don't have permission or not enough coins to use this kit.");
		}
	}
	
	public Boolean canUseKit(Kit kit) {
		if(kit.getCost() == 0) {
			if(player.hasPermission("bg.kit." + kit.getName().toLowerCase()) || player.hasPermission("bg.kit.*") || player.isOp())
				return true;
			else
				return false;
		} else {
			if(getCoins() >= kit.getCost() || player.isOp())
				return true;
			else
				return false;
		}
	}
	
	public void setKit(String kitName) {
		setKit(Main.getInstance().game.getKitByName(kitName));
	}
	
	public void giveKit() {
		if(kit == null)
			return;
		
		if(kit.getCost() > 0) {
			if(getCoins() < kit.getCost())
				return;
			setCoins(getCoins() - kit.getCost());
		}
		player.getInventory().addItem(kit.getContents());
		
		ArrayList<Ability> abilities = new ArrayList<Ability>();
		for(Integer abilityID : kit.getAbilityIDs())
			abilities.add(Main.getInstance().game.getAbilityByID(abilityID));
		this.abilities = abilities.toArray(new Ability[abilities.size()]);
	}
	
	public void reset() {
		player.closeInventory();
		player.setHealth(player.getMaxHealth());
		player.getEnderChest().clear();
		player.setFireTicks(0);
		player.setExp(0);
		player.setLevel(0);
		player.setTotalExperience(0);
		player.setGameMode(GameMode.SURVIVAL);
		if(player.isInsideVehicle())
			player.getVehicle().eject();
		player.setBedSpawnLocation(player.getWorld().getSpawnLocation());
		player.setFoodLevel(20);
		player.setExhaustion(20);
		player.setSaturation(20);
		player.setAllowFlight(false);
		player.getInventory().clear();
		player.getInventory().setArmorContents(new ItemStack[] {});
		for(PotionEffect potionEffect : player.getActivePotionEffects())
			player.removePotionEffect(potionEffect.getType());
	}
	
    public void setCoins(final Integer arg1){ 
        Runnable task = new Runnable() {
            @Override 
            public void run() { 
                try { 
                   
                } catch (Exception ex) { 
                   
                } 
            } 
        }; 
        new Thread(task).start(); 
    }
    
    public Integer getCoins(){ 
        return 0;
    }
    
    public void openKitMenu() {
		if(menu == null) {
			menu = new IconMenu("Select a kit", this.player.getName(), ((Main.getInstance().game.getKits().size()+1)/9 + 1) * 9, new IconMenu.OptionClickEventHandler() {

				@Override
				public void onOptionClick(OptionClickEvent event) {
					if(event.getName().contains("Other available kits"))
						return;
					setKit(event.getName());
					event.setWillClose(true);
					event.setWillDestroy(false);
				}
				
			}, Main.getInstance());
			
			Integer pos = 0;
			ArrayList<String> otherKits = new ArrayList<String>();
	    	for (Entry<String, Kit> entry : Main.getInstance().game.getKits().entrySet()) {
	    		if(entry.getValue() == null) {
	    			player.sendMessage(ChatColor.RED + "No kits available!");
	    			return;
	    		}
	    		
				Kit kit = entry.getValue();
				if(!canUseKit(kit)) {
					otherKits.add(kit.getName());
					continue;
				}
				menu.setOption(pos, kit.getIcon(), kit.getName(), kit.contentsToString());
				pos++;
			}
	    	if(otherKits.size() > 0)
	    		menu.setOption(pos, new ItemStack(Material.PISTON_MOVING_PIECE, 1), ChatColor.GRAY + "Other available kits", otherKits.toString());
	    	
	    	menu.open(player);
		} else {
			menu.open(this.player);
		}
    }
       
}
