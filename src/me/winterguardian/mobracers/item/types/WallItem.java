package me.winterguardian.mobracers.item.types;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.winterguardian.core.particle.ParticleType;
import me.winterguardian.core.particle.ParticleUtil;
import me.winterguardian.core.sorting.AntiRecursiveRandomSelector;
import me.winterguardian.core.sorting.RandomSelector;
import me.winterguardian.core.util.SoundEffect;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.item.Item;
import me.winterguardian.mobracers.item.ItemResult;
import me.winterguardian.mobracers.item.ItemType;
import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.vehicle.Vehicle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WallItem extends Item
{	
	private List<BlockState> backup;
	
	private List<WallBlock> blocks;
	private SoundEffect sound;
	
	public WallItem()
	{
		this.backup = new ArrayList<>();
		this.blocks = new ArrayList<>();
		this.blocks.add(new WallBlock(Material.WOOD, (byte) 0));
		this.blocks.add(new WallBlock(Material.WOOD, (byte) 1));
		this.blocks.add(new WallBlock(Material.WOOD, (byte) 2));
		this.blocks.add(new WallBlock(Material.WOOD, (byte) 3));
		this.blocks.add(new WallBlock(Material.WOOD, (byte) 4));
		this.blocks.add(new WallBlock(Material.WOOD, (byte) 5));
		this.sound = new SoundEffect(Sound.BLOCK_WOODEN_DOOR_CLOSE, 1, 1);
	}
	
	public WallItem(List<WallBlock> blocks, SoundEffect sound)
	{
		this.backup = new ArrayList<>();
		
		this.blocks = new ArrayList<>();
		if(blocks != null)
			this.blocks.addAll(blocks);
		else
			this.blocks.add(new WallBlock(Material.WOOD, (byte) 0));
		this.sound = sound;
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.ITEM_WALL.toString();
	}

	@Override
	public ItemType getType()
	{
		return ItemType.WALL;
	}

	@SuppressWarnings("deprecation")
	@Override
	public ItemResult onUse(Player player, final Vehicle vehicle, final GameState game)
	{
		final Set<Block> locs = new HashSet<Block>();
		
		locs.add(vehicle.getEntity().getLocation().add(0, 0, 0).getBlock());
		locs.add(vehicle.getEntity().getLocation().add(0, 1, 0).getBlock());
		locs.add(vehicle.getEntity().getLocation().add(0, 2, 0).getBlock());
		
		double deltaX = 1 * Math.cos(Math.toRadians(player.getLocation().getYaw() + 180));
		double deltaZ = 1 * Math.sin(Math.toRadians(player.getLocation().getYaw() + 180));
		
		locs.add(vehicle.getEntity().getLocation().add(deltaX, 0, deltaZ).getBlock());
		locs.add(vehicle.getEntity().getLocation().add(deltaX, 1, deltaZ).getBlock());
		locs.add(vehicle.getEntity().getLocation().add(deltaX, 2, deltaZ).getBlock());
		
		locs.add(vehicle.getEntity().getLocation().add(-deltaX, 0, -deltaZ).getBlock());
		locs.add(vehicle.getEntity().getLocation().add(-deltaX, 1, -deltaZ).getBlock());
		locs.add(vehicle.getEntity().getLocation().add(-deltaX, 2, -deltaZ).getBlock());
		
		locs.add(vehicle.getEntity().getLocation().add(deltaX * 1.5, 0, deltaZ * 1.5).getBlock());
		locs.add(vehicle.getEntity().getLocation().add(deltaX * 1.5, 1, deltaZ * 1.5).getBlock());
		locs.add(vehicle.getEntity().getLocation().add(deltaX * 1.5, 2, deltaZ * 1.5).getBlock());
		
		locs.add(vehicle.getEntity().getLocation().add(-deltaX * 1.5, 0, -deltaZ * 1.5).getBlock());
		locs.add(vehicle.getEntity().getLocation().add(-deltaX * 1.5, 1, -deltaZ * 1.5).getBlock());
		locs.add(vehicle.getEntity().getLocation().add(-deltaX * 1.5, 2, -deltaZ * 1.5).getBlock());
		
		this.backup.clear();
		
		for(Block block : locs)
			ParticleUtil.playSimpleParticles(block.getLocation().add(0.5, 0.5, 0.5), ParticleType.VILLAGER_ANGRY, 0, 0, 0, 0, 1);
		
		Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				RandomSelector<WallBlock> random = new AntiRecursiveRandomSelector<WallBlock>(blocks);
				
				for(Block block : locs)
				{
					if(block.getType() == Material.STANDING_BANNER || block.getType() == Material.WALL_BANNER || block.getType() == Material.BANNER)
						continue;
					backup.add(block.getState());
					game.addBlockToRegen(block.getLocation());
					WallBlock wall = random.next();
					block.setType(wall.type, false);
					block.setData(wall.data, false);
				}
			}
		}, 5);

		Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				WallItem.this.cancel();
			}
			
		}, 300);
		
		sound.play(MobRacersPlugin.getGame().getPlayers(), player.getLocation());
		
		return ItemResult.DISCARD;
	}

	@Override
	public ItemStack getItemStack()
	{
		ItemStack item = new ItemStack(Material.CLAY_BRICK, 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§r§6" + CourseMessage.ITEM_WALL.toString());
		item.setItemMeta(itemMeta);
		return item;
	}
	
	@Override
	public void cancel()
	{
		for(BlockState state : this.backup)
			state.update(true, false);
	}

	public static class WallBlock
	{
		public Material type = null;
		public byte data = 0;
		
		public WallBlock(Material type, byte data)
		{
			this.type = type;
			this.data = data;
		}
	}
}
