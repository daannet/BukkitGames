package bukkitgames.utilities;

public class Ability {

	Integer id;
	String name;
	String description;
	
	public Ability(Integer id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}
	
	public Integer getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
}
