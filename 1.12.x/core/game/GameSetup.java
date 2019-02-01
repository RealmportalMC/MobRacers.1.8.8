package me.winterguardian.core.game;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

public abstract class GameSetup
{
	private boolean loadFile;
	private File file;
	
	public GameSetup(File file)
	{
		this.file = file;
		this.loadFile = true;
	}

	public GameSetup(File file, boolean loadFile)
	{
		this.file = file;
		this.loadFile = loadFile;
	}
	
	public void load()
	{
		try
		{
			YamlConfiguration config;
			if(loadFile)
				config = YamlConfiguration.loadConfiguration(getFile());
			else
				config = new YamlConfiguration();

			this.load(config);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void save()
	{
		try
		{
			YamlConfiguration config = new YamlConfiguration();
			this.save(config);
			config.save(getFile());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	protected abstract void load(YamlConfiguration config);
	protected abstract void save(YamlConfiguration config);
	
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
}
