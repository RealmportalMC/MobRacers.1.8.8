package me.winterguardian.core.playerstats;

import me.winterguardian.core.util.PlayerUtil;
import org.bukkit.entity.Player;

public class PlayerStats
{
	private MappedData data;
	
	public PlayerStats(MappedData data)
	{
		this.data = data;
	}

	public MappedData getContent()
	{
		return data;
	}

	@SuppressWarnings("deprecation")
	public void join(Player player)
	{
		long time = System.currentTimeMillis();
		setName(player.getName());
		setIp(PlayerUtil.getIp(player));
		if(getFirstConnection() == -1)
		{
			setPoints(1000);
			setFirstConnection(time);
		}
		setLastLogin(time);

	}

	public void leave(Player player)
	{
		setLastLogout(System.currentTimeMillis());
	}

	public boolean isNew()
	{
		return getFirstConnection() == this.getLastLogin();
	}

	public void addPoints(int points)
	{
		this.setPoints(this.getPoints() + Math.abs(points));
	}

	public void removePoints(int points)
	{
		this.setPoints(this.getPoints() - Math.abs(points));
	}
	
	public int getPoints()
	{
		return data.getInt("core.points", 1000);
	}
	
	public void setPoints(int points)
	{
		data.set("core.points", points);
	}
	
	public long getLastLogin()
	{
		return data.getLong("core.lastlogin", -1);
	}
	
	public void setLastLogin(long timestamp)
	{
		data.set("core.lastlogin", timestamp);
	}
	
	public long getLastLogout()
	{
		return data.getLong("core.lastlogout", -1);
	}
	
	public void setLastLogout(long timestamp)
	{
		data.set("core.lastlogout", timestamp);
	}

	public long getFirstConnection()
	{
		return data.getLong("core.firstconnect", -1);
	}

	public void setFirstConnection(long timestamp)
	{
		data.set("core.firstconnect", timestamp);
	}

	public String getName()
	{
		return data.getString("core.name");
	}

	public void setName(String name)
	{
		data.set("core.name", name);
	}

	public String getIp()
	{
		return data.getString("core.ip");
	}

	public void setIp(String ip)
	{
		data.set("core.ip", ip);
	}

}