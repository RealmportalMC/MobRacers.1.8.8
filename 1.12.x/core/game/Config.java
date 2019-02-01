package me.winterguardian.core.game;

import java.io.File;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class Config
{
	private boolean loaded;
	private File file;
	
	public Config(File file)
	{
		this.file = file;
	}
	
	public boolean reload()
	{
		this.loaded = false;
		return this.load();
	}

	public boolean load()
	{
		if(this.loaded)
			return false;
		try
		{
			YamlConfiguration config = YamlConfiguration.loadConfiguration(getFile());
			this.load(config);
			
			return this.loaded = true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public File getFile()
	{
		if(!file.exists())
			try
			{
				file.createNewFile();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return null;
			}
		
		return file;
	}
	
	public boolean isLoaded()
	{
		return this.loaded;
	}
	protected abstract void load(YamlConfiguration config);
}
