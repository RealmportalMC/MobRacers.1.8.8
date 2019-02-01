package me.winterguardian.core.world;

import java.util.HashMap;

import me.winterguardian.core.DynamicComponent;
import me.winterguardian.core.message.Message;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

public class Wand extends DynamicComponent implements Listener
{
	private Permission permission = null;
	private Message wandMessage = null;
	
	private HashMap<Player, SerializableLocation> pos1 = new HashMap<>();
	private HashMap<Player, SerializableLocation> pos2 = new HashMap<>();
	
	public Wand()
	{
		this.permission = null;
		this.wandMessage = Message.NULL;
	}

	@Override
	public void enable(Plugin plugin, Object... args)
	{
		super.enable(plugin);

		if(args.length > 0 && args[0] instanceof Permission)
			setPermission((Permission)args[0]);

		if(args.length > 1 && args[1] instanceof Message)
			setWandMessage((Message) args[1]);
	}

	@Override
	public void register(Plugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(this, plugin);

		if(getPermission() != null)
			if(Bukkit.getPluginManager().getPermission(getPermission().getName()) == null)
				Bukkit.getPluginManager().addPermission(getPermission());
	}

	@Override
	public void unregister(Plugin plugin)
	{
		HandlerList.unregisterAll(this);
	}
	
	
	public SerializableLocation getPos1(Player player)
	{
		if(pos1.get(player) != null)
			return pos1.get(player);
		return null;
	}
	
	public SerializableLocation getPos2(Player player)
	{
		if(pos2.get(player) != null)
			return pos2.get(player);
		return null;
	}
	
	public SerializableRegion getRegion(Player player)
	{
		if(getPos1(player) != null && getPos2(player) != null)
			return new SerializableRegion(getPos1(player), getPos2(player));
		return null;
	}

	public Permission getPermission()
	{
		return permission;
	}

	public void setPermission(Permission permission)
	{
		this.permission = permission;
	}

	public Message getPermissionMessage()
	{
		return wandMessage;
	}

	public void setWandMessage(Message message)
	{
		this.wandMessage = message;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(permission == null || event.getPlayer().hasPermission(permission))
		{
			if(event.getItem() != null) 
				if(event.getItem().getType().equals(Material.WOOD_AXE))
				{
					if(event.getAction().equals(Action.LEFT_CLICK_BLOCK))
					{
						pos1.put(event.getPlayer(), new SerializableLocation(event.getClickedBlock().getLocation()));
						
						if(!Bukkit.getPluginManager().isPluginEnabled("WorldEdit"))
						{
							wandMessage.say(event.getPlayer(), "#", "1", "<click>", "left", "<x>", event.getClickedBlock().getX() + "", "<y>", event.getClickedBlock().getY() + "", "<z>", event.getClickedBlock().getZ() + "");
							event.setCancelled(true);
						}
					}
					else if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
					{
						pos2.put(event.getPlayer(), new SerializableLocation(event.getClickedBlock().getLocation()));
						
						if(!Bukkit.getPluginManager().isPluginEnabled("WorldEdit"))
						{
							wandMessage.say(event.getPlayer(), "#", "2", "<click>", "right", "<x>", event.getClickedBlock().getX() + "", "<y>", event.getClickedBlock().getY() + "", "<z>", event.getClickedBlock().getZ() + "");
							event.setCancelled(true);
						}
					}
				}
		}
	}
}
