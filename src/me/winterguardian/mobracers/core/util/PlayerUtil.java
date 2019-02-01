package me.winterguardian.core.util;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;

public class PlayerUtil
{
	public static final double gravityYVelocity = -0.0784000015258789;

	private PlayerUtil() {}
	
	public static void clearInventory(Player player)
	{
		player.setItemOnCursor(null);
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
	}
	
	@Deprecated
	public static void clearAll(Player player)
	{
		clearInventory(player);
	}
	
	public static void heal(Player player)
	{
		for(PotionEffect eff : player.getActivePotionEffects())
			player.removePotionEffect(eff.getType());
		player.setFireTicks(0);
		player.setHealth(player.getMaxHealth());
		player.setFoodLevel(20);
	}
	
	public static void clearBoard(Player player)
	{
		player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}
	
	public static void prepare(Player player)
	{
		if (player.getGameMode() != GameMode.ADVENTURE)
			player.setGameMode(GameMode.ADVENTURE);
		
		player.setFlying(false);
		player.setWalkSpeed(0.2F);
		player.setAllowFlight(false);
		player.setLevel(0);
		player.setExp(0);
		player.closeInventory();
		player.eject();
		player.leaveVehicle();
	}
	
	@Deprecated
	public static String getIp(Player player)
	{
		return player.getAddress().getAddress().getHostAddress();
	}
	
	public static boolean isHolding(Player player, Block block)
	{
		if(player.getLocation().getX() < block.getX() - 0.3)
			return false;
		if(player.getLocation().getX() > block.getX() + 1.3)
			return false;
		if(player.getLocation().getZ() < block.getZ() - 0.3)
			return false;
		if(player.getLocation().getZ() > block.getZ() + 1.3)
			return false;

		return !(player.getLocation().getY() != block.getY() || player.getLocation().getY() != block.getY() + 0.125 || player.getLocation().getY() != block.getY() + 0.25 || player.getLocation().getY() != block.getY() + 0.375 || player.getLocation().getY() != block.getY() + 0.5 || player.getLocation().getY() != block.getY() + 0.625 || player.getLocation().getY() != block.getY() + 0.75 || player.getLocation().getY() != block.getY() + 0.875 || player.getLocation().getY() != block.getY() + 0.1);

	}
	
	public static Set<Block> getBlocksHolding(Player player)
	{
		Set<Block> blocks = new HashSet<>();
		Set<Block> holdingBlocks = new HashSet<>();
		
		blocks.add(player.getLocation().getBlock().getRelative(1, -1, 1));
		blocks.add(player.getLocation().getBlock().getRelative(1, -1, 0));
		blocks.add(player.getLocation().getBlock().getRelative(1, -1, -1));
		
		blocks.add(player.getLocation().getBlock().getRelative(0, -1, 1));
		blocks.add(player.getLocation().getBlock().getRelative(0, -1, 0));
		blocks.add(player.getLocation().getBlock().getRelative(0, -1, -1));
		
		blocks.add(player.getLocation().getBlock().getRelative(-1, -1, 1));
		blocks.add(player.getLocation().getBlock().getRelative(-1, -1, 0));
		blocks.add(player.getLocation().getBlock().getRelative(-1, -1, -1));
		
		for(Block block : blocks)
			if(isHolding(player, block))
				holdingBlocks.add(block);
		
		return holdingBlocks;
	}
	
	public static boolean isJump(PlayerMoveEvent event)
	{
		return event.getTo().getY() - (int)(event.getTo().getY() * 1000D) / 1000D != 0 
				&& event.getTo().getY() > event.getFrom().getY()
				&& event.getFrom().getY() - (int)(event.getFrom().getY() * 1000D) / 1000D == 0
				&& event.getPlayer().isOnGround()
				&& event.getPlayer().getVelocity().getY() == gravityYVelocity;
	}
	
	public static Entity getFirstPointed(Player player, double radius, double lenght)
	{
		for(double i = 0; i < lenght; i += 0.1)
		{
			Location loc = player.getEyeLocation().clone().add(player.getLocation().getDirection().clone().multiply(i));
			for(Entity entity : player.getWorld().getEntities())
			{
				if(entity == player)
					continue;
				try
				{
					Object nmsEntity = ReflectionUtil.getHandle(entity);
					Object entityBox = Class.forName("net.minecraft.server." + ReflectionUtil.getVersion() + ".Entity").getDeclaredMethod("getBoundingBox").invoke(nmsEntity);
					
					Class<?> boxClass = Class.forName("net.minecraft.server." + ReflectionUtil.getVersion() + ".AxisAlignedBB");
					Object newBox = boxClass.getDeclaredConstructor(double.class, double.class, double.class, double.class, double.class, double.class).newInstance(loc.getX(), loc.getY(), loc.getZ(), radius / 2, radius / 2, radius / 2);
					
					if((boolean) boxClass.getDeclaredMethod("b", boxClass).invoke(entityBox, newBox))
						return entity;
				}
				catch(Exception e)
				{
					e.printStackTrace();
					return null;
				}
			}
		}
		
		return null;
		
	}
}
