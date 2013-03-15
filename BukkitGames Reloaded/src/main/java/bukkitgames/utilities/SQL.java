package bukkitgames.utilities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.scheduler.BukkitRunnable;

import bukkitgames.main.Main;

import lib.PatPeter.SQLibrary.Database;

public class SQL {

	public Database database;
	
	public SQL(Database database) {
		this.database = database;
		if(database.open()) {
			database.writeError("Could not connect to database! Please check your connection.", true);
		}
	}
	
	public void executeStatement(final String statement, final Object... args) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				try {
					synchronized (database) {
						if(database.open())
							return;
						PreparedStatement preparedStatement = database.prepare(statement);
						Integer index = 0;
						for(Object object : args) {
							preparedStatement.setObject(index, object);
							index++;
						}
						database.query(preparedStatement);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(Main.getInstance());
	}
	
	public void executeStatement(final String statement) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				try {
					synchronized (database) {
						if(database.open())
							return;
						PreparedStatement preparedStatement = database.prepare(statement);
						database.query(preparedStatement);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(Main.getInstance());
	}
	
	public ResultSet queryStatement(final String statement, final Object... args) {
		try {
			if(database.open())
				return null;
			PreparedStatement preparedStatement = database.prepare(statement);
			Integer index = 0;
			for(Object object : args) {
				preparedStatement.setObject(index, object);
				index++;
			}
			return database.query(preparedStatement);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ResultSet queryStatement(final String statement) {
		try {
			if(database.open())
				return null;
			PreparedStatement preparedStatement = database.prepare(statement);
			return database.query(preparedStatement);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
