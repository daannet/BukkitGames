package bukkitgames.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lib.PatPeter.SQLibrary.MySQL;
import lib.PatPeter.SQLibrary.SQLite;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.error.YAMLException;

import bukkitgames.commands.PlayerCommands;
import bukkitgames.enums.GameState;
import bukkitgames.events.GameListener;
import bukkitgames.events.PlayerListener;
import bukkitgames.timers.WaitingTimer;
import bukkitgames.utilities.Game;
import bukkitgames.utilities.Gamer;
import bukkitgames.utilities.Kit;
import bukkitgames.utilities.SQL;

public class Main extends JavaPlugin {

	Map<String, Object> configOptions = new HashMap<String, Object>();
	Map<String, String> configMessages = new HashMap<String, String>();
	public Game game;
	public SQL sql;
	private static Main instance;
	
	public static Main getInstance() {
		return instance;
	}
	
	public Map<String, Object> getConfigOptions() {
		return configOptions;
	}
	
	public Map<String, String> getConfigMessages() {
		return configMessages;
	}
	
	@Override
	public void onLoad() {
		instance = this;
		Bukkit.getServer().unloadWorld("world", false);
		delete(new File("world"));
	}
	
	@Override
	public void onEnable() {
		createConfiguration();
		loadConfiguration();
		registerEvents();
		registerCommands();
		
		new WaitingTimer();
	}
	
	@Override
	public void onDisable() {
		Bukkit.getScheduler().cancelAllTasks();
		Bukkit.getServer().shutdown();
	}
	
	/**
	 * Registers the events.
	 */
	private void registerEvents() {
		getServer().getPluginManager().registerEvents(new GameListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
	}
		
	/**
	 * Registers the commands.
	 */
	private void registerCommands() {
		getCommand("kit").setExecutor(new PlayerCommands()); 
	}
	
	/**
	 * Creates the configuration files if they do not exist.
	 */
	private void createConfiguration() {
		File configFile = new File(getDataFolder(), "config.yml");
		File kitFile = new File(getDataFolder(), "kit.yml");
		
		if (!configFile.exists()) {
			configFile.getParentFile().mkdirs();
			copy(getResource("config.yml"), configFile);
		}
		
		if (!kitFile.exists()) {
			kitFile.getParentFile().mkdirs();
			copy(getResource("kit.yml"), kitFile);
		}
	}
	
	/**
	 * Loads the configuration files from the file system.
	 */
	private void loadConfiguration() {
		try {
			FileConfiguration configConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml"));
			FileConfiguration kitConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "kit.yml"));
			FileConfiguration messagesConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "messages.yml"));
			
			configOptions = configConfig.getValues(true);
			
			for(String path : messagesConfig.getKeys(true)) {
				configMessages.put(path, messagesConfig.getString(path));
			}
			
			if((Boolean) getConfigOptions().get("MYSQL")) {
				MySQL mysql = new MySQL(getLogger(), "MySQL", (String) getConfigOptions().get("HOSTNAME"), (Integer) getConfigOptions().get("PORT"), (String) getConfigOptions().get("DATABASE"), (String) getConfigOptions().get("USERNAME"), (String) getConfigOptions().get("PASSWORD"));
				sql = new SQL(mysql);
			} else {
				SQLite sqlite = new SQLite(getLogger(), "SQLite", (String) getConfigOptions().get("DIRECTORY"), (String) getConfigOptions().get("FILENAME"));
				sql = new SQL(sqlite);
			}
			
			ArrayList<Kit> kits = new ArrayList<Kit>();
			for(String sectionName : kitConfig.getKeys(false)) {
				ConfigurationSection section = kitConfig.getConfigurationSection(sectionName);
				ItemStack stack = new ItemStack(Material.WOOD, 5);
				ItemMeta meta = stack.getItemMeta();
				meta.setDisplayName("WWWOOODDD");
				meta.addEnchant(Enchantment.DIG_SPEED, 1, true);
				List<String> list = new ArrayList<String>();
				list.add("Awesome and Handy!");
				meta.setLore(list);
				stack.setItemMeta(meta);
				section.getConfigurationSection("ITEMS").set("item1", stack);
				try {
					kitConfig.save(new File(getDataFolder(), "kit.yml"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ArrayList<ItemStack> items = new ArrayList<ItemStack>();
				for(String stackName : section.getConfigurationSection("ITEMS").getKeys(false)) {
					items.add(section.getConfigurationSection("ITEMS").getItemStack(stackName));
				}
				
				kits.add(new Kit(sectionName, new ItemStack(section.getInt("ICON"), 1), items.toArray(new ItemStack[items.size()]), section.getIntegerList("ABILITIES").toArray(new Integer[section.getIntegerList("ABILITIES").size()]), section.getInt("COST")));
			}
			
			try {
				ResultSet resultSet = sql.queryStatement("SELECT `ID` FROM `GAMES` ORDER BY `ID` DESC LIMIT 1");
				if(resultSet != null && resultSet.next())
					game = new Game(resultSet.getInt(0), kits.toArray(new Kit[kits.size()]));
				else
					game = new Game(0, kits.toArray(new Kit[kits.size()]));
			} catch (SQLException e) {
				e.printStackTrace();
				Bukkit.getServer().getPluginManager().disablePlugin(this);
			}
			
		} catch(YAMLException exception) {
			getLogger().warning("Error while parsing configuration files! Check your YAML syntax.");
			exception.printStackTrace();
		}
	}
	
	/**
	 * Copies a file from the resources to the file system.
	**/
	private void copy(InputStream in, File file) {
		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes a file for a directory.
	**/
	public static void delete(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				delete(new File(dir, children[i]));
			}
		}
		dir.delete();
	}
		
	public void startGame() {
		game.setGameState(GameState.PREGAME);
		for (Gamer gamer : game.getAllGamers()) { 
			gamer.reset();
			gamer.giveKit();
			gamer.getPlayer().teleport(Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
		}
		game.setCompetedGamers(game.getGamers().size());
	}
	
}