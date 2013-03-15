package bukkitgames.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;

import bukkitgames.enums.GameState;
import bukkitgames.main.Main;

public class Game {

	private Integer id;
	private Integer countdown;
	private HashMap<String, Kit> kits = new HashMap<String, Kit>();
	private HashMap<Integer, Ability> abilities = new HashMap<Integer, Ability>();
	private HashMap<Player, Gamer> gamers = new HashMap<Player, Gamer>();
	private GameState gameState;
	private Integer competedGamers;
	
	public Game(Integer id, Kit[] kits) {
		this.id = id;
		this.gameState = GameState.PREGAME;
		
		for(Kit kit : kits)
			addKit(kit);
	}
	
	public GameState getGameState() {
		return gameState;
	}
	
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
	
	public Integer getCompetedGamers() {
		return competedGamers;
	}
	
	public void setCompetedGamers(Integer competedGamers) {
		this.competedGamers = competedGamers;
	}
	
	public void addAbility(Ability ability) {
		if(this.abilities.containsKey(ability.getID())) {
			Main.getInstance().getLogger().warning("Cannot register ability '" + ability.getName() + "'. ID '" + ability.getID() + "' already registerd!");
			return;
		}
		this.abilities.put(ability.getID(), ability);
	}
	
	public void addKit(Kit kit) {
		if(this.kits.containsKey(kit.getName())) {
			Main.getInstance().getLogger().warning("Cannot add kit '" + kit.getName() + "'. Name already registerd!");
			return;
		}
		this.kits.put(kit.getName(), kit);
	}
	
	public Kit getKitByName(String kitName) {
		return kits.get(kitName);
	}
	
	public Ability getAbilityByID(Integer abilityID) {
		return this.abilities.get(abilityID);
	}
	
	public Gamer getGamer(Player player) {
		return gamers.get(player);
	}
	
	public void addGamer(Gamer gamer) {
		if(gamers.containsKey(gamer.getPlayer()))
			removeGamer(gamer.getPlayer());
		gamers.put(gamer.getPlayer(), gamer);
	}
	
	public void removeGamer(Player player) {
		gamers.remove(player);
	}
	
	public ArrayList<Gamer> getGamers() {
		ArrayList<Gamer> gamers = new ArrayList<Gamer>();
		for(Entry<Player, Gamer> entry : this.gamers.entrySet()) {
			if(entry.getValue().isSpectator() || entry.getValue().isGameMaker())
				continue;
			gamers.add(entry.getValue());
		}
		return gamers;
	}
	
	public ArrayList<Gamer> getAllGamers() {
		ArrayList<Gamer> gamers = new ArrayList<Gamer>();
		for(Entry<Player, Gamer> entry : this.gamers.entrySet()) {
			gamers.add(entry.getValue());
		}
		return gamers;
	}
	
	public Integer getCountdown() {
		return this.countdown;
	}
	
	public void setCountdown(Integer countdown) {
		this.countdown = countdown;
	}
	
	public HashMap<String, Kit> getKits() {
		return this.kits;
	}
	
	public String getCountdownTime() {
		String time = getCountdown().toString() + " seconds";
		if(getCountdown() > 60) {
			time = String.format("%d minutes, %d seconds", 
									TimeUnit.SECONDS.toMinutes(getCountdown()),
									getCountdown() - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(getCountdown()))
								);
		}
		return time;
	}
}
