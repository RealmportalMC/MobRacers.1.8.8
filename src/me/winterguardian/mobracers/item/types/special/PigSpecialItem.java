package me.winterguardian.mobracers.item.types.special;

import me.winterguardian.core.particle.ParticleType;
import me.winterguardian.core.particle.ParticleUtil;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.item.ItemResult;
import me.winterguardian.mobracers.item.types.SpecialItem;
import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.vehicle.Vehicle;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class PigSpecialItem extends SpecialItem
{
	
	public PigSpecialItem()
	{
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.ITEM_SPECIAL_PIG.toString();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{
		if(vehicle == null || vehicle.getEntity() == null || game.getPlayerData(player).isFinished())
			return ItemResult.KEEP;
		
		
		vehicle.getEntity().setVelocity(new Vector(Math.sin(Math.toRadians(-player.getLocation().getYaw())) * 1.5, 0.5, Math.cos(Math.toRadians(-player.getLocation().getYaw())) * 1.5));
		
		Location loc = player.getEyeLocation();
		
		for(Player p : MobRacersPlugin.getGame().getPlayers())
		{
			p.playSound(player.getLocation(), Sound.DIG_GRAVEL, 1f, 1f);
			
			p.playSound(player.getLocation().clone().add(2, 0, 0), Sound.DIG_GRAVEL, 1f, 1f);
			p.playSound(player.getLocation().clone().add(-2, 0, 0), Sound.DIG_GRAVEL, 1f, 1f);
			p.playSound(player.getLocation().clone().add(0, 0, 2), Sound.DIG_GRAVEL, 1f, 1f);
			p.playSound(player.getLocation().clone().add(0, 0, -2), Sound.DIG_GRAVEL, 1f, 1f);
			
			ParticleUtil.playBlockParticles(p, player.getLocation(), ParticleType.BLOCK_CRACK, 1.5f, 1.5f, 1.5f, 0, 100, Material.DIRT.getId(), 0);
		}
		
		
		for(int x = loc.getBlockX() - 2; x <= loc.getBlockX() + 2; x++)
			for(int z = loc.getBlockZ() - 2; z <= loc.getBlockZ() + 2; z++)
			{
				if(Math.abs(x - loc.getBlockX()) == 2 && Math.abs(z - loc.getBlockZ()) == 2)
					continue;
				
				for(int y = loc.getBlockY(); y >= 0; y--)
				{ 
					Block block = loc.getWorld().getBlockAt(x, y, z);
					
					if(block.isEmpty() || block.isLiquid() || block.getType() == Material.STANDING_BANNER || block.getType() == Material.WALL_BANNER || block.getType() == Material.BANNER)
						continue;
					
					game.addBlockToRegen(block.getLocation());
					
					if(block.getType().isOccluding())
					{
						block.setType(Material.DIRT);
						block.setData((byte) 2);
						if(y <= vehicle.getEntity().getLocation().getY())
							break;
					}
					else
						block.setType(Material.AIR);
				}
			}
		
		return ItemResult.DISCARD;
	}

	@Override
	public ItemStack getItemStack()
	{
		ItemStack item = new ItemStack(Material.DIRT, 1);
		item.setDurability((short) 2);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§r§6" + CourseMessage.ITEM_SPECIAL_PIG.toString());
		item.setItemMeta(itemMeta);
		return item;
	}

	@Override
	public void cancel()
	{
		
	}
	
}
