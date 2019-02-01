package me.winterguardian.core.gameplay.listener;

import me.winterguardian.core.gameplay.GameplayManager;
import me.winterguardian.core.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class GameplayListener implements Listener
{
	private GameplayManager manager;

	public GameplayListener(GameplayManager manager)
	{
		this.manager = manager;
	}

	@EventHandler
	public void onPlayerUseUnbreaking10(final PlayerItemDamageEvent e)
	{
		if(e.getItem().containsEnchantment(Enchantment.DURABILITY))
			if(e.getItem().getEnchantmentLevel(Enchantment.DURABILITY) >= 10)
			{
				e.setCancelled(true);
				Bukkit.getScheduler().runTaskLater(manager.getPlugin(), new Runnable()
				{
					@Override
					public void run()
					{
						e.getPlayer().updateInventory();
					}
					
				}, 5);
			}
	}

	@EventHandler
	public void onDeadEntityDamage(EntityDamageByEntityEvent e)
	{
		if(e.getDamager() != null)
			if(e.getDamager().isDead())
				e.setCancelled(true);
	}

	@EventHandler
	public void onDeadEntityShoot(ProjectileLaunchEvent e)
	{
		if(e.getEntity() != null)
			if(e.getEntity().getShooter() != null)
				if(e.getEntity() instanceof LivingEntity)
					if(((LivingEntity)e.getEntity().getShooter()).isDead())
						e.setCancelled(true);
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event)
	{
		if(event.getCause() != EntityDamageEvent.DamageCause.FALL)
			return;

		Block block = event.getEntity().getLocation().getBlock().getRelative(BlockFace.DOWN);

		if(block.getType() == Material.HAY_BLOCK || block.getType() == Material.WOOL)
		{
			event.setCancelled(true);
			if(event.getEntity() instanceof Player)
				((Player)event.getEntity()).playSound(event.getEntity().getLocation(), Sound.ANVIL_USE ,10, (float) 0.75);
		}
	}

	//@EventHandler
	public void onPlayerJumpBreakGlitch(PlayerMoveEvent event)
	{
		if(PlayerUtil.isJump(event))
		{
			for(Block block : PlayerUtil.getBlocksHolding(event.getPlayer()))
			{
				if(block.getRelative(BlockFace.UP).getType() == Material.AIR
						|| block.getRelative(BlockFace.UP).getType() == Material.PISTON_MOVING_PIECE
						|| block.getRelative(BlockFace.UP).getType() == Material.BANNER
						|| block.getRelative(BlockFace.UP).getType() == Material.YELLOW_FLOWER
						|| block.getRelative(BlockFace.UP).getType() == Material.RED_ROSE
						|| block.getRelative(BlockFace.UP).getType() == Material.IRON_BARDING
						|| block.getRelative(BlockFace.UP).getType() == Material.CARROT
						|| block.getRelative(BlockFace.UP).getType() == Material.POTATO
						|| block.getRelative(BlockFace.UP).getType() == Material.CROPS
						|| block.getRelative(BlockFace.UP).getType() == Material.SUGAR_CANE_BLOCK
						|| block.getRelative(BlockFace.UP).getType() == Material.MELON_STEM
						|| block.getRelative(BlockFace.UP).getType() == Material.PUMPKIN_STEM
						|| block.getRelative(BlockFace.UP).getType() == Material.SAPLING
						|| block.getRelative(BlockFace.UP).getType() == Material.DEAD_BUSH
						|| block.getRelative(BlockFace.UP).getType() == Material.LONG_GRASS
						|| block.getRelative(BlockFace.UP).getType() == Material.THIN_GLASS
						|| block.getRelative(BlockFace.UP).getType() == Material.STRING
						|| block.getRelative(BlockFace.UP).getType() == Material.SIGN
						|| block.getRelative(BlockFace.UP).getType() == Material.SIGN_POST
						|| block.getRelative(BlockFace.UP).getType() == Material.WALL_SIGN
						|| block.getRelative(BlockFace.UP).getType() == Material.WALL_BANNER
						|| block.getRelative(BlockFace.UP).getType() == Material.WATER_LILY
						|| block.getRelative(BlockFace.UP).getType() == Material.FLOWER_POT
						|| block.getRelative(BlockFace.UP).getType() == Material.SKULL
						|| block.getRelative(BlockFace.UP).getType() == Material.DOUBLE_PLANT
						|| block.getRelative(BlockFace.UP).getType() == Material.TORCH
						|| block.getRelative(BlockFace.UP).getType() == Material.REDSTONE_TORCH_ON
						|| block.getRelative(BlockFace.UP).getType() == Material.REDSTONE_TORCH_OFF
						|| (block.getRelative(BlockFace.UP).getType() == Material.BREWING_STAND && block.getType() != Material.BREWING_STAND)
						|| (block.getRelative(BlockFace.UP).getType() == Material.CARPET && block.getType() != Material.CARPET)
						|| (block.getRelative(BlockFace.UP).getType() == Material.ACACIA_DOOR && block.getType() != Material.ACACIA_DOOR)
						|| (block.getRelative(BlockFace.UP).getType() == Material.DARK_OAK_DOOR && block.getType() != Material.DARK_OAK_DOOR)
						|| (block.getRelative(BlockFace.UP).getType() == Material.WOOD_DOOR && block.getType() != Material.WOOD_DOOR)
						|| (block.getRelative(BlockFace.UP).getType() == Material.IRON_DOOR && block.getType() != Material.IRON_DOOR)
						|| (block.getRelative(BlockFace.UP).getType() == Material.BIRCH_DOOR && block.getType() != Material.BIRCH_DOOR)
						|| (block.getRelative(BlockFace.UP).getType() == Material.JUNGLE_DOOR && block.getType() != Material.JUNGLE_DOOR)
						|| (block.getRelative(BlockFace.UP).getType() == Material.SPRUCE_DOOR && block.getType() != Material.SPRUCE_DOOR)
						|| (block.getRelative(BlockFace.UP).getType() == Material.BIRCH_DOOR && block.getType() != Material.BIRCH_DOOR)
						|| (block.getRelative(BlockFace.UP).getType() == Material.CAKE_BLOCK && block.getType() != Material.CAKE_BLOCK)
						|| (block.getRelative(BlockFace.UP).getType() == Material.ANVIL && block.getType() != Material.ANVIL && block.getType() != Material.CACTUS && block.getType() != Material.CHEST && block.getType() != Material.ENDER_CHEST && block.getType() != Material.TRAPPED_CHEST)
						|| (block.getRelative(BlockFace.UP).getType() == Material.CACTUS && block.getType() != Material.CACTUS)
						|| (block.getRelative(BlockFace.UP).getType() == Material.CHEST && block.getType() != Material.ANVIL && block.getType() != Material.CACTUS && block.getType() != Material.CHEST && block.getType() != Material.ENDER_CHEST && block.getType() != Material.TRAPPED_CHEST)
						|| (block.getRelative(BlockFace.UP).getType() == Material.ENDER_CHEST && block.getType() != Material.ANVIL && block.getType() != Material.CACTUS && block.getType() != Material.CHEST && block.getType() != Material.ENDER_CHEST && block.getType() != Material.TRAPPED_CHEST)
						|| (block.getRelative(BlockFace.UP).getType() == Material.TRAPPED_CHEST && block.getType() != Material.ANVIL && block.getType() != Material.CACTUS && block.getType() != Material.CHEST && block.getType() != Material.ENDER_CHEST && block.getType() != Material.TRAPPED_CHEST)
						|| block.getRelative(BlockFace.UP).getType() == Material.BROWN_MUSHROOM
						|| block.getRelative(BlockFace.UP).getType() == Material.REDSTONE_COMPARATOR
						|| block.getRelative(BlockFace.UP).getType() == Material.REDSTONE_COMPARATOR_OFF
						|| block.getRelative(BlockFace.UP).getType() == Material.REDSTONE_COMPARATOR_ON
						|| block.getRelative(BlockFace.UP).getType() == Material.REDSTONE_WIRE
						|| block.getRelative(BlockFace.UP).getType() == Material.DIODE_BLOCK_ON
						|| block.getRelative(BlockFace.UP).getType() == Material.DIODE_BLOCK_OFF
						|| block.getRelative(BlockFace.UP).getType() == Material.ACTIVATOR_RAIL
						|| block.getRelative(BlockFace.UP).getType() == Material.RAILS
						|| block.getRelative(BlockFace.UP).getType() == Material.POWERED_RAIL
						|| block.getRelative(BlockFace.UP).getType() == Material.DETECTOR_RAIL
						|| block.getRelative(BlockFace.UP).getType() == Material.ENDER_PORTAL
						|| block.getRelative(BlockFace.UP).getType() == Material.PORTAL
						|| block.getRelative(BlockFace.UP).getType() == Material.FENCE
						|| block.getRelative(BlockFace.UP).getType() == Material.FENCE_GATE
						|| block.getRelative(BlockFace.UP).getType() == Material.ACACIA_FENCE
						|| block.getRelative(BlockFace.UP).getType() == Material.ACACIA_FENCE_GATE
						|| block.getRelative(BlockFace.UP).getType() == Material.BIRCH_FENCE
						|| block.getRelative(BlockFace.UP).getType() == Material.BIRCH_FENCE_GATE
						|| block.getRelative(BlockFace.UP).getType() == Material.JUNGLE_FENCE
						|| block.getRelative(BlockFace.UP).getType() == Material.JUNGLE_FENCE_GATE
						|| block.getRelative(BlockFace.UP).getType() == Material.DARK_OAK_FENCE
						|| block.getRelative(BlockFace.UP).getType() == Material.DARK_OAK_FENCE_GATE
						|| block.getRelative(BlockFace.UP).getType() == Material.SPRUCE_FENCE
						|| block.getRelative(BlockFace.UP).getType() == Material.SPRUCE_FENCE_GATE
						|| block.getRelative(BlockFace.UP).getType() == Material.COBBLE_WALL
						|| block.getRelative(BlockFace.UP).getType() == Material.SNOW
						|| block.getRelative(BlockFace.UP).getType() == Material.WEB
						|| block.getRelative(BlockFace.UP).getType() == Material.IRON_TRAPDOOR
						|| block.getRelative(BlockFace.UP).getType() == Material.TRAP_DOOR
						|| block.getRelative(BlockFace.UP).getType() == Material.TRIPWIRE_HOOK
						|| block.getRelative(BlockFace.UP).getType() == Material.LADDER
						|| block.getRelative(BlockFace.UP).getType() == Material.VINE
						|| block.getRelative(BlockFace.UP).getType() == Material.STAINED_GLASS_PANE
						|| block.getRelative(BlockFace.UP).getType() == Material.STANDING_BANNER
						|| block.getRelative(BlockFace.UP).getType() == Material.STONE_PLATE
						|| block.getRelative(BlockFace.UP).getType() == Material.WOOD_PLATE
						|| block.getRelative(BlockFace.UP).getType() == Material.GOLD_PLATE
						|| block.getRelative(BlockFace.UP).getType() == Material.IRON_PLATE)
					return;
			}
			event.setCancelled(true);
			event.getPlayer().teleport(event.getPlayer().getLocation().add(0, -1, 0));
		}
	}
}
