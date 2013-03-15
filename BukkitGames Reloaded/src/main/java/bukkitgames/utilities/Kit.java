package bukkitgames.utilities;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import bukkitgames.main.Main;

public class Kit {

	private String name;
	private ItemStack icon;
	private ItemStack[] contents;
	private Integer[] abilities;
	private Integer cost;
	
	public Kit(String name, ItemStack icon, ItemStack[] contents, Integer[] abilities, Integer cost) {
		this.name = name;
		this.icon = icon;
		this.contents = contents;
		this.abilities = abilities;
		this.cost = cost;
	}

	public ItemStack[] getContents() {
		return contents;
	}
	
	public Integer[] getAbilityIDs() {
		return abilities;
	}
	
	public String getName() {
		return name;
	}
	
	public ItemStack getIcon() {
		return icon;
	}
	
	public Integer getCost() {
		return cost;
	}
	
	public String[] contentsToString() {
		ArrayList<String> kitString = new ArrayList<String>();
		for(ItemStack item : getContents()) {
			if(item.hasItemMeta() && item.getItemMeta().hasDisplayName())
				kitString.add(ChatColor.BLUE + "- " + item.getAmount() + "x \"" + item.getItemMeta().getDisplayName() + "\" (" + item.getType().toString() + ")");
			else
				kitString.add(ChatColor.BLUE + "- " + item.getAmount() + "x " + item.getType().toString());
			if(item.hasItemMeta() && item.getItemMeta().hasEnchants())
				for(Entry<Enchantment, Integer> enchantmentEntry : item.getItemMeta().getEnchants().entrySet())
					kitString.add(ChatColor.DARK_BLUE + "  w/ " + enchantmentEntry.getKey().getName() + " " + enchantmentEntry.getValue());
			if(item.hasItemMeta() && item.getItemMeta().hasLore())
				for(String lore : item.getItemMeta().getLore())
					kitString.add(ChatColor.DARK_BLUE + "  \"" + lore + "\"");
		}
		for(Integer abilityID : getAbilityIDs()) {
			Ability ability =  Main.getInstance().game.getAbilityByID(abilityID);
			if(ability == null)
				continue;
			kitString.add(ChatColor.LIGHT_PURPLE + "* " + ability.getName());
			kitString.add(ChatColor.LIGHT_PURPLE + "    " + ability.getDescription());
		}
		return kitString.toArray(new String[kitString.size()]);
	}	
}
