package me.winterguardian.core.playerstats;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import me.winterguardian.core.util.TextUtil;
import org.bukkit.Bukkit;

public class DBUserDataLoader implements UserDataLoader
{
	private Map<String, String> tables;
	
	private String driver, url, user, password;
	
	public DBUserDataLoader(String driver, String url, String user, String password)
	{
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.password = password;
		
		this.tables = new HashMap<>();
	}
	
	@Override
	public void init()
	{
		Connection connection = connect();

		if(connection == null)
			return;
		
		for(String table : new ArrayList<>(tables.keySet())) try
        {
            connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (id VARCHAR(40), " + tables.get(table) + ", PRIMARY KEY(id))");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
		
		close(connection);
	}

	@Override
	public MappedData load(UUID id)
	{
		MappedData data = new MappedData();

		Connection connection = connect();

		if(connection == null)
			return data;
		
		for(String table : new ArrayList<>(tables.keySet()))
			try
			{
				Statement statement = connection.createStatement();
				ResultSet result = statement.executeQuery("SELECT * FROM " + table + " WHERE id = '" + id.toString() + "'");
				
				if(result.first())
                {
                    ResultSetMetaData meta = result.getMetaData();
                    for (int i = 2; i <= meta.getColumnCount(); i++)
                        data.set(table + "." + meta.getColumnName(i), result.getObject(i));
                }
					
				result.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		
		close(connection);
		
		return data;
	}

	@Override
	public void save(UUID id, MappedData data)
	{
		Connection connection = connect();

		if(connection == null)
			return;
		
		for(String table : new ArrayList<>(tables.keySet()))
		{
			if(!data.hasSection(table))
				continue;

			try
			{
				List<String> keys = new LinkedList<>();
				List<String> values = new LinkedList<>();
				List<String> keysAssignment = new ArrayList<>();
				
				for(String path : data.getKeys(table))
				{
					if(!data.isSet(path) || data.isNull(path))
						continue;

					String key, value;
					
					keys.add(key = path.replace(table + ".", ""));
					if(data.isBoolean(path))
						values.add(value = "'" + (data.getBoolean(path) ? 1 : 0) + "'");
					else
						values.add(value = "'" + data.get(path) + "'");
					keysAssignment.add(key + " = " + value);
				}

				if(keys.size() <= 0)
					continue;

				connection.createStatement().executeUpdate("INSERT INTO " + table + " "
						+ "(id, " + TextUtil.toString(keys, ", ") + ") "
						+ "VALUES('" + id.toString() + "', " + TextUtil.toString(values, ", ") + ") "
						+ "ON DUPLICATE KEY UPDATE " + TextUtil.toString(keysAssignment, ", "));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		close(connection);
	}

	@Override
	public Set<UUID> listUsers()
	{
		Set<UUID> data = new HashSet<>();

		Connection connection = connect();

		if(connection == null)
			return null;
		
		try
		{
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery("SELECT id FROM " + tables.keySet().iterator().next());
			
			while(result.next())
				data.add(UUID.fromString(result.getString(1)));
				
			result.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		close(connection);
		
		return data;
	}

	@Override
	public void merge(UserDataLoader loader)
	{
		if(loader instanceof DBUserDataLoader)
		{
			for(Map.Entry<String, String> entry : ((DBUserDataLoader)loader).getTables().entrySet())
				if(!tables.containsKey(entry.getKey()))
					tables.put(entry.getKey(), entry.getValue());
            init();
		}
	}
	
	private Connection connect()
	{
		try
		{
			Class.forName(driver);
		}
		catch(Throwable e)
		{
			new Exception("DRIVER " + driver + " NOT FOUND.", e).printStackTrace();
		}

		try
		{
			if(this.user != null && this.password != null)
				return DriverManager.getConnection(this.url, this.user, this.password);
			else
				return DriverManager.getConnection(this.url);
		}
		catch(Exception e)
		{
			new Exception("COULDN'T CONNECT TO DATABASE", e).printStackTrace();
			return null;
		}
	}
	
	private void close(Connection connection)
	{
		try
		{
			connection.close();
		}
		catch(Exception e)
		{
			new Exception("Warning, couldn't close correctly database connection", e).printStackTrace();
		}
	}
	
	public Map<String, String> getTables()
	{
		return tables;
	}

	public String getDriver()
	{
		return driver;
	}

	public void setDriver(String driver)
	{
		this.driver = driver;
	}

	@Override
	public Map.Entry<UUID, MappedData> getFirstByValue(String path, Object value)
	{
		Connection connection = connect();

		if(connection == null)
			return null;

		String table = path.split(Pattern.quote("."))[0];
		String column = path.replaceFirst(Pattern.quote(table + "."), "");

		UUID id = null;

		try
		{
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery("SELECT id FROM " + table + " WHERE " + column + " = '" + value + "'");

			if(result.first())
				id = UUID.fromString(result.getString(1));

			result.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		close(connection);

		if(id == null)
			return null;

		return TextUtil.newEntry(id, load(id));
	}

	@Override
	public Map<UUID, MappedData> getByValue(String path, Object value)
	{
		Connection connection = connect();

		if(connection == null)
			return new HashMap<>();

		String table = path.split(Pattern.quote("."))[0];
		String column = path.replaceFirst(Pattern.quote(table + "."), "");

		Set<UUID> ids = new HashSet<>();

		try
		{
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery("SELECT id FROM " + table + " WHERE " + column + " = '" + value + "'");

			if(result.first())
				ids.add(UUID.fromString(result.getString(1)));

			result.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		close(connection);

		HashMap<UUID, MappedData> datas = new HashMap<>();

		for(UUID id : ids)
			datas.put(id, load(id));

		return datas;
	}
}
