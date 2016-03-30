package me.winterguardian.mobracers.item.types.special;

import java.util.ArrayList;
import java.util.List;

import me.winterguardian.core.particle.ParticleType;
import me.winterguardian.core.particle.ParticleUtil;
import me.winterguardian.core.util.SoundEffect;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.item.ItemResult;
import me.winterguardian.mobracers.item.types.ShieldItem;
import me.winterguardian.mobracers.item.types.SpecialItem;
import me.winterguardian.mobracers.state.game.GamePlayerData;
import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.vehicle.Vehicle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GuardianSpecialItem extends SpecialItem
{
	private GameState game;
	
	private List<Player> victims;
	private List<Vehicle> vehicleVictims;
	private List<Float> pitchs;	
	private int taskId;
	
	public GuardianSpecialItem()
	{
		victims = new ArrayList<Player>();
		vehicleVictims = new ArrayList<Vehicle>();
		this.taskId = -1;
		this.pitchs = new ArrayList<Float>();
		this.pitchs.add(1f);
		this.pitchs.add(1.4f);
		this.pitchs.add(1.3f);
		this.pitchs.add(1.1f);
		this.pitchs.add(0.8f);
		this.pitchs.add(0.5f);
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.ITEM_SPECIAL_GUARDIAN.toString();
	}
	
	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{
		this.game = game;
		
		victims.clear();
		vehicleVictims.clear();
		
		for(GamePlayerData data : game.getPlayerDatas())
		{
			if(data.getPlayer() == player)
				continue;
			
			
			if(!data.isFinished())
			{
				if(ShieldItem.protect(data.getPlayer()))
					continue;
				
				data.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * this.pitchs.size(), 255, false, false));
				this.vehicleVictims.add(data.getVehicle());
				victims.add(data.getPlayer());
			}
		}
		
		this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobRacersPlugin.getPlugin(), new ScarePlayers(), 0, 20);
		for(Vehicle v : this.vehicleVictims)
			v.setSpeed(v.getSpeed() - 0.3f);
	
		return ItemResult.DISCARD;
	}

	@Override
	public ItemStack getItemStack()
	{
		ItemStack item = new ItemStack(Material.PRISMARINE_SHARD, 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§r§3" + CourseMessage.ITEM_SPECIAL_GUARDIAN.toString());
		item.setItemMeta(itemMeta);
		return item;
	}

	@Override
	public void cancel()
	{
		if(this.taskId != -1)
		{
			Bukkit.getScheduler().cancelTask(this.taskId);
			this.taskId = -1;
			for(Vehicle v : this.vehicleVictims)
				v.setSpeed(v.getSpeed() + 0.3f);
		}
	}
	
	private class ScarePlayers implements Runnable
	{
		private int i;
		
		public ScarePlayers()
		{
			this.i = pitchs.size();
		}

		@Override
		public void run()
		{
			for(Player p : victims)
			{
				if(!game.getPlayerData(p).isFinished())
				{
					ParticleUtil.playSimpleParticles(p, p.getEyeLocation(), ParticleType.MOB_APPEARANCE, 0, 0, 0, 0, 15);
					new SoundEffect("mob.guardian.curse", 1, pitchs.get(pitchs.size() - i)).play(p, p.getEyeLocation());
				}
			}
			
			this.i--;
			if(this.i <= 0)
				cancel();
		}
		
	}
}
