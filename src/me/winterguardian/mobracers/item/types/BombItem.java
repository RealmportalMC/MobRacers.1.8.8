package me.winterguardian.mobracers.item.types;

import java.util.Random;

import me.winterguardian.core.particle.ParticleType;
import me.winterguardian.core.particle.ParticleUtil;
import me.winterguardian.core.util.SoundEffect;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.item.Item;
import me.winterguardian.mobracers.item.ItemResult;
import me.winterguardian.mobracers.item.ItemType;
import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.vehicle.Vehicle;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class BombItem extends Item implements Runnable
{
	private static final Random random = new Random();
	private static final int[][][] pattern = new int[][][]
			{{
				{0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 1, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0}
			},
			{
				{0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 1, 0, 0, 0},
				{0, 0, 1, 2, 1, 0, 0},
				{0, 1, 2, 2, 2, 1, 0},
				{0, 0, 1, 2, 1, 0, 0},
				{0, 0, 0, 1, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0}
			},
			{
				{0, 0, 0, 0, 0, 0, 0},
				{0, 0, 1, 2, 1, 0, 0},
				{0, 1, 2, 2, 2, 1, 0},
				{0, 2, 2, 2, 2, 2, 0},
				{0, 1, 2, 2, 2, 1, 0},
				{0, 0, 1, 2, 1, 0, 0},
				{0, 0, 0, 0, 0, 0, 0}
			},
			{
				{0, 0, 0, 1, 0, 0, 0},
				{0, 1, 2, 2, 2, 1, 0},
				{0, 2, 2, 2, 2, 2, 0},
				{1, 2, 2, 2, 2, 2, 1},
				{0, 2, 2, 2, 2, 2, 0},
				{0, 1, 2, 2, 2, 1, 0},
				{0, 0, 0, 1, 0, 0, 0}
			},
			{
				{0, 0, 0, 0, 0, 0, 0},
				{0, 0, 1, 2, 1, 0, 0},
				{0, 1, 2, 2, 2, 1, 0},
				{0, 2, 2, 2, 2, 2, 0},
				{0, 1, 2, 2, 2, 1, 0},
				{0, 0, 1, 2, 1, 0, 0},
				{0, 0, 0, 0, 0, 0, 0}
			},
			{
				{0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 1, 0, 0, 0},
				{0, 0, 1, 2, 1, 0, 0},
				{0, 1, 2, 2, 2, 1, 0},
				{0, 0, 1, 2, 1, 0, 0},
				{0, 0, 0, 1, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0}
			},
			{	
				{0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 1, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0}
			}};
	
	private GameState game;
	
	private int countdown, taskID;
	private Location location;
	
	public BombItem()
	{
		this.countdown = 2;
		this.taskID = -1;
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.ITEM_BOMB.toString();
	}

	@Override
	public ItemType getType()
	{
		return ItemType.BOMB;
	}

	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{
		this.game = game;
		this.location = vehicle.getEntity().getLocation();
		while(location.getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR)
			this.location.add(0, -1, 0);
		Block block = location.getBlock();
		game.addBlockToRegen(location);
		block.setType(Material.SKULL);
		if(!(block.getState() instanceof Skull))
			return ItemResult.DISCARD;

		Skull skull = (Skull) block.getState();
		skull.setSkullType(SkullType.PLAYER);
		skull.setOwner("MHF_TNT2");
		skull.update(true);
		
		this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobRacersPlugin.getPlugin(), this, 0, 20);
		
		return ItemResult.DISCARD;
	}

	@Override
	public ItemStack getItemStack() 
	{
		ItemStack bomb = new ItemStack(Material.TNT, 1);
		ItemMeta meta = bomb.getItemMeta();
		meta.setDisplayName("§8" + CourseMessage.ITEM_BOMB.toString());
		bomb.setItemMeta(meta);
		return bomb;
	}

	@Override
	public void cancel()
	{
		if(taskID == -1)
			return;
		Bukkit.getScheduler().cancelTask(taskID);
	}

	@Override
	public void run()
	{
		if(this.countdown == 0)
		{
			new SoundEffect(Sound.EXPLODE, 1f, 1f).play(MobRacersPlugin.getGame().getPlayers(), location);
			ParticleUtil.playSimpleParticles(location, ParticleType.EXPLOSION_LARGE, 1, 1, 1, 0, 15);
			
			for(int x = 0; x < 7; x++)
				for(int y = 0; y < 7; y++)
					for(int z = 0; z < 7; z++)
					{
						switch(pattern[x][y][z])
						{
						case 0:
							continue;
							
						case 1:
							if(random.nextBoolean())
								continue;
						case 2:
							Block block = location.clone().add(x - 3, y - 3, z - 3).getBlock();
							if(block.getType() == Material.BEDROCK || block.getType() == Material.OBSIDIAN || block.getType() == Material.COMMAND || block.getType() == Material.BARRIER)
								continue;
							game.addBlockToRegen(block.getLocation());
							block.setType(Material.AIR, false);
						}
					}
					
			for(Player p : MobRacersPlugin.getGame().getPlayers())
			{	
				if(game.getPlayerData(p) == null || game.getPlayerData(p).isFinished())
					continue;
				
				Entity v = game.getPlayerData(p).getVehicle().getEntity();
				
				if(v == null)
					continue;
				
				double distance = location.distanceSquared(v.getLocation());
				
				if(distance > 100) //10 * 10
					continue;
				
				if(ShieldItem.protect(p))
					continue;
				
				if(distance < 1)
					distance = 1;
				
				double reverseX = -Math.sin(Math.toRadians(-p.getLocation().getYaw()));
				double reverseZ = -Math.cos(Math.toRadians(-p.getLocation().getYaw()));
				
				double x = (v.getLocation().getX() - location.getX()) / distance * 3;
				double y = 1 / Math.pow(distance, 0.1);
				double z = (v.getLocation().getZ() - location.getZ()) / distance * 3;
				
				if(x * reverseX < 0)
					x = x + reverseX;
				
				if(z * reverseZ < 0)
					z = z + reverseZ;

				v.setVelocity(new Vector(x * 2, y * 2, z * 2));
			}	
			
			cancel();
			return;
		}
		
		new SoundEffect(Sound.ORB_PICKUP, 1f, 1f).play(MobRacersPlugin.getGame().getPlayers(), location);
		this.countdown--;
	}

}
