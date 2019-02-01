package me.winterguardian.core.playerstats;

import java.io.File;
import java.util.*;

import me.winterguardian.core.util.TextUtil;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * Created by Alexander Winter on 2015-11-06.
 */
public class YamlUserDataLoader implements UserDataLoader
{
	private Map<Object, UUID> singleShortCuts;
	private Map<Object, Set<UUID>> shortCuts;

	private File directory;

	public YamlUserDataLoader(File directory)
	{
		this.singleShortCuts = new HashMap<>();
		this.shortCuts = new HashMap<>();

		this.directory = directory;
	}

	@Override
	public void init()
	{
		if(directory.isFile())
			directory.delete();

		if(!directory.exists())
			directory.mkdirs();
	}

	@Override
	public MappedData load(UUID id)
	{
		return new MappedData(YamlConfiguration.loadConfiguration(getFile(id)));
	}

	@Override
	public void save(UUID id, MappedData data)
	{
		try
		{
			data.toYaml().save(getFile(id));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public Set<UUID> listUsers()
	{
		Set<UUID> uuids = new HashSet<UUID>();

		for(File file : this.directory.listFiles())
			if(file.getName().endsWith(".ud"))
				uuids.add(UUID.fromString(file.getName().replace(".ud", "")));

		return uuids;
	}

	@Override
	public void merge(UserDataLoader loader)
	{

	}

	private File getFile(UUID id)
	{
		return new File(this.directory, id.toString() + ".ud");
	}

	@Override
	public Map.Entry<UUID, MappedData> getFirstByValue(String path, Object value)
	{
		for(Object current : this.singleShortCuts.keySet())
		{
			if(current.equals(value) || current instanceof String && value instanceof String && ((String)current).equalsIgnoreCase((String)value))
			{
				return TextUtil.newEntry(this.singleShortCuts.get(current), this.load(this.singleShortCuts.get(current)));
			}
		}

		for(UUID id : this.listUsers())
		{
			MappedData data = this.load(id);

			if(data.get(path) == null)
				continue;

			if(data.get(path, new Object()).equals(value) || data.get(path, new Object()) instanceof String && value instanceof String && ((String)data.get(path, new Object())).equalsIgnoreCase((String)value))
			{
				singleShortCuts.put(value, id);
				return TextUtil.newEntry(id, data);
			}
		}

		return null;
	}

	@Override
	public Map<UUID, MappedData> getByValue(String path, Object value)
	{
		Map<UUID, MappedData> datas = new HashMap<>();

		for(Object current : this.shortCuts.keySet())
		{
			if(current.equals(value)
					|| current instanceof String && value instanceof String && ((String)current).equalsIgnoreCase((String)value))
			{
				for(UUID id : this.shortCuts.get(current))
					datas.put(id, this.load(id));

				return datas;
			}
		}

		for(UUID id : this.listUsers())
		{
			MappedData data = this.load(id);

			if(data.get(path) == null)
				continue;

			if(data.get(path, new Object()).equals(value))
				datas.put(id, data);
			else if(data.get(path, new Object()) instanceof String && value instanceof String && ((String)data.get(path, new Object())).equalsIgnoreCase((String)value))
				datas.put(id, data);
		}

		return datas;
	}

}
