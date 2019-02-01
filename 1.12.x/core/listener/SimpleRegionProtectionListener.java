package me.winterguardian.core.listener;

import me.winterguardian.core.world.SerializableRegion;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;

public class SimpleRegionProtectionListener implements Listener
{
	private SerializableRegion region;
	
	public SimpleRegionProtectionListener(SerializableRegion region)
	{
		this.region = region;
	}
	
	@EventHandler
	public void onBlockBurn(BlockBurnEvent event)
	{
		if(region != null && region.contains(event.getBlock().getLocation()))
		{
			event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onBlockGrow(BlockGrowEvent event)
	{
		if(region != null && region.contains(event.getBlock().getLocation()))
		{
			event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onBlockFade(BlockFadeEvent event)
	{
		if(region != null && region.contains(event.getBlock().getLocation()))
		{
			event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onBlockSpread(BlockSpreadEvent event)
	{
		if(region != null && region.contains(event.getBlock().getLocation()))
		{
			event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onLeavesDecay(LeavesDecayEvent event)
	{
		if(region != null && region.contains(event.getBlock().getLocation()))
		{
			event.setCancelled(true);
			return;
		}
	}
}
