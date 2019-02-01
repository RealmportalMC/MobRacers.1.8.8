package me.winterguardian.core.playerstats;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import org.bukkit.configuration.file.YamlConfiguration;

public class MappedData
{
	private Map<String, Object> data;
	
	public MappedData()
	{
		this.data = new HashMap<>();
	}

	public MappedData(YamlConfiguration yaml)
	{
		this.data = yaml.getValues(true);
	}

	public boolean isBoolean(String path)
	{
		if(!data.containsKey(path))
			return false;

		if(data.get(path) instanceof Boolean)
			return true;

		if(("" + data.get(path)).equals("1") || ("" + data.get(path)).equals("0"))
			return true;

		return (data.get(path) + "").equalsIgnoreCase("true") || (data.get(path) + "").equalsIgnoreCase("false");
	}

	public boolean getBoolean(String path)
	{
		return getBoolean(path, false);
	}

	public boolean getBoolean(String path, boolean defaultValue)
	{
		if(!data.containsKey(path))
			return defaultValue;
			
		if(data.get(path) instanceof Boolean)
			return (boolean)data.get(path);

		if(("" + data.get(path)).equals("1"))
			return true;

		try
		{
			return Boolean.parseBoolean(data.get(path) + "");
		}
		catch(Exception e)
		{
			return defaultValue;
		}
	}

	public boolean isInt(String path)
	{
		if(!data.containsKey(path))
			return false;

		if(data.get(path) instanceof Integer)
			return true;

		try
		{
			Integer.parseInt(data.get(path) + "");
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public int getInt(String path)
	{
		return getInt(path, 0);
	}
	
	public int getInt(String path, int defaultValue)
	{
		if(!data.containsKey(path))
			return defaultValue;
			
		if(data.get(path) instanceof Integer)
			return (int)data.get(path);
			
		try
		{
			return Integer.parseInt(data.get(path) + "");
		}
		catch(Exception e)
		{
			return defaultValue;
		}
	}

	public boolean isLong(String path)
	{
		if(!data.containsKey(path))
			return false;

		if(data.get(path) instanceof Long)
			return true;

		try
		{
			Long.parseLong(data.get(path) + "");
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public long getLong(String path)
	{
		return getLong(path, 0);
	}

	public long getLong(String path, long defaultValue)
	{
		if(!data.containsKey(path))
			return defaultValue;
			
		if(data.get(path) instanceof Long)
			return (long)data.get(path);
			
		try
		{
			return Long.parseLong(data.get(path) + "");
		}
		catch(Exception e)
		{
			return defaultValue;
		}
	}

	public boolean isFloat(String path)
	{
		if(!data.containsKey(path))
			return false;

		if(data.get(path) instanceof Float)
			return true;

		try
		{
			Float.parseFloat(data.get(path) + "");
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public float getFloat(String path)
	{
		return getFloat(path, 0.0f);
	}

	public float getFloat(String path, float defaultValue)
	{
		if(!data.containsKey(path))
			return defaultValue;
			
		if(data.get(path) instanceof Float)
			return (float)data.get(path);

		try
		{
			return Float.parseFloat(data.get(path).toString());
		}
		catch(Exception e)
		{
			return defaultValue;
		}
	}

	public boolean isDouble(String path)
	{
		if(!data.containsKey(path))
			return false;

		if(data.get(path) instanceof Integer)
			return true;

		try
		{
			Double.parseDouble(data.get(path) + "");
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public double getDouble(String path)
	{
		return getDouble(path, 0.0);
	}

	public double getDouble(String path, double defaultValue)
	{
		if(!data.containsKey(path))
			return defaultValue;
			
		if(data.get(path) instanceof Double)
			return (double)data.get(path);

		try
		{
			return Double.parseDouble(data.get(path) + "");
		}
		catch(Exception e)
		{
			return defaultValue;
		}
	}

	public boolean isString(String path)
	{
		if(!data.containsKey(path) || data.get(path) == null)
			return false;

		return data.get(path) instanceof String;

	}

	public String getString(String path)
	{
		return getString(path, null);
	}
	
	public String getString(String path, String defaultValue)
	{
		if(!data.containsKey(path) || data.get(path) == null)
			return defaultValue;
			
		if(data.get(path) instanceof String)
			return (String)data.get(path);
			
		return data.get(path).toString() != null ? data.get(path).toString() : defaultValue;
	}

	public boolean isSet(String path)
	{
		return data.containsKey(path);
	}

	public Object get(String path)
	{
		return get(path, null);
	}
	
	public Object get(String path, Object defaultValue)
	{
		if(!data.containsKey(path))
			return defaultValue;
		
		return data.get(path);
	}

	public boolean isNull(String path)
	{
		if(!isSet(path))
			return false;

		return data.get(path) == null;
	}
	
	public boolean hasSection(String path)
	{
		for(String key : data.keySet())
			if(key.startsWith(path + "."))
				return true;
		return false;
	}
	
	public Set<String> getKeys()
	{
		return data.keySet();
	}
	
	public Set<String> getKeys(String section)
	{
		Set<String> keys = new HashSet<>();

		for(String key : data.keySet())
			if(key.startsWith(section + "."))
				keys.add(key);

		return keys;
	}

	public Set<String> getSubKeys(String section)
	{
		Set<String> keys = new HashSet<>();

		for(String key : data.keySet())
			if(key.startsWith(section + "."))
				keys.add(key.replace(section + ".", ""));

		return keys;
	}
	
	public void set(String path, Object value)
	{
		data.put(path, value);
	}

	public void remove(String path)
	{
		data.remove(path);
	}



	public YamlConfiguration toYaml()
	{
		YamlConfiguration config = new YamlConfiguration();

		for(String key : this.data.keySet())
			config.set(key, this.data.get(key));

		return config;
	}
}