package me.winterguardian.mobracers.item.types;

import me.winterguardian.core.particle.ParticleData;
import me.winterguardian.core.particle.ParticleType;
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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class GroundPoundItem extends Item implements Listener
{
	private SoundEffect sound;
	private ParticleData particle;
	
	private Player player;
	private Vehicle vehicle;
	private GameState game;
	
	private int taskId;
	private boolean active;
	
	public GroundPoundItem()
	{
		this(new SoundEffect(Sound.DIG_GRASS, 1, 1), new ParticleData(ParticleType.SMOKE_LARGE, 2, 2, 2, 0, 100, new int[0]));
	}
	
	public GroundPoundItem(SoundEffect sound, ParticleData particle)
	{
		this.active = false;
		this.sound = sound;
		this.particle = particle;
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.ITEM_GROUNDPOUND.toString();
	}

	@Override
	public ItemType getType()
	{
		return ItemType.GROUND_POUND;
	}

	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{
		this.player = player;
		this.vehicle = vehicle;
		this.game = game;
		
		vehicle.getEntity().setVelocity(new Vector(1.75 * Math.sin(Math.toRadians(-player.getLocation().getYaw())), 1, 1.75 * Math.cos(Math.toRadians(-player.getLocation().getYaw()))));
		this.taskId = Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				if(GroundPoundItem.this.vehicle.getEntity() == null)
					return;
				
				GroundPoundItem.this.vehicle.getEntity().setVelocity(new Vector(0, -10, 0));
				active = true;
				taskId = -1;
				Bukkit.getPluginManager().registerEvents(GroundPoundItem.this, MobRacersPlugin.getPlugin());
			}
		}, 20).getTaskId();
		
		return ItemResult.DISCARD;
	}

	@Override
	public ItemStack getItemStack()
	{
		ItemStack item = new ItemStack(Material.WOOD_SPADE, 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§r§e" + CourseMessage.ITEM_GROUNDPOUND.toString());
		item.setItemMeta(itemMeta);
		return item;
	}

	@Override
	public void cancel()
	{
		this.active = false;
		
		HandlerList.unregisterAll(this);
		
		if(this.taskId != -1)
			Bukkit.getScheduler().cancelTask(taskId);
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) 
	{
		if(active && event.getPlayer() == this.player)
		{
			Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
			{
				@Override
				public void run()
				{
					if(game.getPlayerData(player).isFinished())
					{
						cancel();
						return;
					}
					else if(game.getPlayerData(player).getVehicle().getEntity().isOnGround())
					{
						cancel();
						sound.play(MobRacersPlugin.getGame().getPlayers(), player.getLocation());
						particle.apply(player.getLocation());
						
						
						
						for(Player p : MobRacersPlugin.getGame().getPlayers())
						{
							if(p == player)
								continue;
							
							
							if(game.getPlayerData(p) == null || game.getPlayerData(p).isFinished())
								continue;
							
							Entity v = game.getPlayerData(p).getVehicle().getEntity();
							double distance = vehicle.getEntity().getLocation().distanceSquared(v.getLocation());
							
							if(!v.isOnGround())
								continue;
							
							if(distance > 100) //10 * 10
								continue;
							
							if(ShieldItem.protect(p))
								continue;
							
							if(distance < 1)
								distance = 1;
							
							double reverseX = -Math.sin(Math.toRadians(-p.getLocation().getYaw()));
							double reverseZ = -Math.cos(Math.toRadians(-p.getLocation().getYaw()));
							
							double x = (v.getLocation().getX() - vehicle.getEntity().getLocation().getX()) / distance * 3;
							double y = 1 / Math.pow(distance, 0.1);
							double z = (v.getLocation().getZ() - vehicle.getEntity().getLocation().getZ()) / distance * 3;
							
							if(x * reverseX < 0)
								x = x + reverseX;
							
							if(z * reverseZ < 0)
								z = z + reverseZ;

							v.setVelocity(new Vector(x, y, z));
						}	
					}
				}
			}, 0);
		}
	}

}
