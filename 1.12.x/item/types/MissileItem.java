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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class MissileItem extends Item
{
	private GameState game;
	private int taskId;
	private Player victim;
	
	private SoundEffect sound;
	private ParticleData flyingParticle, explosionParticle;
	
	public MissileItem()
	{
		this.taskId = -1;
		
		this.victim = null;
		this.sound = new SoundEffect(Sound.EXPLODE, 1, 1);
		this.flyingParticle = new ParticleData(ParticleType.FIREWORKS_SPARK, 0, 0, 0, 0, 1, (int[])null);
		this.explosionParticle = new ParticleData(ParticleType.EXPLOSION_LARGE, 1.5f, 1.5f, 1.5f, 0, 10, (int[])null);
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.ITEM_MISSILE.toString();
	}
	
	@Override
	public ItemType getType()
	{
		return ItemType.MISSILE;
	}

	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{
		this.game = game;
		if(player == game.getPlayer(1 + game.getFinishedPlayers()))
			return ItemResult.DISCARD;
		this.victim = game.getPlayer(1 + game.getFinishedPlayers());
		this.taskId = Bukkit.getScheduler().runTaskTimer(MobRacersPlugin.getPlugin(), new MissileTask(player.getEyeLocation()), 0, 1).getTaskId();
		return ItemResult.DISCARD;
	}

	@Override
	public ItemStack getItemStack()
	{
		ItemStack item = new ItemStack(Material.FIREWORK, 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§r§9" + CourseMessage.ITEM_MISSILE.toString());
		item.setItemMeta(itemMeta);
		return item;
	}
	
	private void missileTouch()
	{
		this.cancel();
		
		this.explosionParticle.apply(victim.getEyeLocation());
		sound.play(MobRacersPlugin.getGame().getPlayers(), victim.getEyeLocation());
		
		
		for(Player player : MobRacersPlugin.getGame().getPlayers())
		{
			if(player.getWorld() != this.victim.getWorld())
				continue;
		
			if(this.game.getPlayerData(player) == null || this.game.getPlayerData(player).isFinished())
				continue;
			
			if(player != this.victim && this.victim.getLocation().distance(player.getLocation()) > 5)
				continue;
			
			if(!this.game.getPlayerData(player).getVehicle().getEntity().isOnGround())
				continue;
			
			if(ShieldItem.protect(player))
				continue;
			
			this.game.getPlayerData(player).getVehicle().getEntity().setVelocity(new Vector(-Math.sin(Math.toRadians(-player.getLocation().getYaw())) * 2, (player == this.victim ? 1.25 : 0.75), -Math.cos(Math.toRadians(-player.getLocation().getYaw())) * 2));
		}
	}
	
	private class MissileTask implements Runnable
	{
		private Location projLocation;
		private int i;
		
		public MissileTask(Location loc)
		{
			this.i = 0;
			this.projLocation = loc;
		}
		
		@Override
		public void run()
		{
			
			if(this.projLocation == null || MissileItem.this.victim.getLocation() == null)
			{
				MissileItem.this.cancel();
				return;
			}
			
			if(this.projLocation.getWorld() != MissileItem.this.victim.getWorld())
			{
				MissileItem.this.cancel();
				return;
			}
				
			if(this.projLocation.distance(MissileItem.this.victim.getEyeLocation()) < 1)
			{
				MissileItem.this.missileTouch();
				return;
			}
			
			int speed = 4;
			
			if(this.projLocation.distance(MissileItem.this.victim.getEyeLocation()) < 40)
				speed = 2;
			
			if(this.projLocation.distance(MissileItem.this.victim.getEyeLocation()) < 10)
				speed = 1;
			
			for(int i = 0; i < speed; i++)
			{
				this.projLocation = this.projLocation.add(new Vector(
						(MissileItem.this.victim.getEyeLocation().getX() - this.projLocation.getX()) / (this.projLocation.distance(MissileItem.this.victim.getEyeLocation()) * speed), 
						(MissileItem.this.victim.getEyeLocation().getY() - this.projLocation.getY()) / (this.projLocation.distance(MissileItem.this.victim.getEyeLocation()) * speed), 
						(MissileItem.this.victim.getEyeLocation().getZ() - this.projLocation.getZ()) / (this.projLocation.distance(MissileItem.this.victim.getEyeLocation()) * speed)));
				MissileItem.this.flyingParticle.apply(this.projLocation);
			}
			
			if(i % (int)(this.projLocation.distance(MissileItem.this.victim.getEyeLocation()) / 10 + 1) == 0)
				new SoundEffect(Sound.ORB_PICKUP, 1, 1).play(MobRacersPlugin.getGame().getPlayers(), this.projLocation);
				
			this.i++;
		}
		
	}

	@Override
	public void cancel()
	{
		if(this.taskId == -1)
			return;
		
		Bukkit.getScheduler().cancelTask(this.taskId);
		this.taskId = -1;
	}
}
