package me.winterguardian.mobracers.item.types.special;

import java.util.ArrayList;
import java.util.List;

import me.winterguardian.core.sorting.AntiRecursiveRandomSelector;
import me.winterguardian.core.sorting.OrderedSelector;
import me.winterguardian.core.sorting.Selector;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.item.ItemResult;
import me.winterguardian.mobracers.item.types.ShieldItem;
import me.winterguardian.mobracers.item.types.SpecialItem;
import me.winterguardian.mobracers.state.game.GamePlayerData;
import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.vehicle.Vehicle;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SuperSheepSpecialItem extends SpecialItem implements Runnable, Listener
{
	private GamePlayerData playerData;
	private Vehicle vehicle;
	private GameState state;
	
	private Selector<Byte> woolColor;
	private Selector<Float> musicPitch;
	
	private List<Location> locs;
	
	private int count;
	private int taskId;
	
	public SuperSheepSpecialItem()
	{
		this.locs = new ArrayList<>();
		
		this.count = 0;
		this.woolColor = new AntiRecursiveRandomSelector<>(new Byte[]{1, 3, 4, 5, 10, 11, 14});
		this.musicPitch = new OrderedSelector<>(new Float[]{1.2f, 1f, 0.9f, 1f, 1.2f, 1.4f, 1.5f, 1.2f});
		this.taskId = -1;
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.ITEM_SPECIAL_SUPERSHEEP.toString();
	}

	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{
		this.playerData = game.getPlayerData(player);
		this.vehicle = vehicle;
		this.state = game;
		
		vehicle.setSpeed(vehicle.getSpeed() + 0.3f);
		
		Bukkit.getPluginManager().registerEvents(this, MobRacersPlugin.getPlugin());
		this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobRacersPlugin.getPlugin(), this, 0, 1);
		
		return ItemResult.DISCARD;
	}

	@Override
	public ItemStack getItemStack()
	{
		ItemStack item = new ItemStack(Material.GOLDEN_APPLE, 1);
		item.setDurability((short) 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§r§d" + CourseMessage.ITEM_SPECIAL_SUPERSHEEP.toString());
		item.setItemMeta(itemMeta);
		return item;
	}

	@Override
	public void cancel()
	{
		if(state.isCourseFinished())
		{	
			HandlerList.unregisterAll(this);
		}
		
		if(this.taskId == -1)
			return;
		
		Bukkit.getScheduler().cancelTask(taskId);
		this.taskId = -1;
		
		vehicle.setSpeed(vehicle.getSpeed() - 0.3f);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run()
	{
		if(playerData.getVehicle().getEntity() == null)
		{
			cancel();
			return;
		}
		
		Location loc = playerData.getPlayer().getEyeLocation();
		
		if(count % 3 == 0)
		{
			float pitch = this.musicPitch.next();
			for(Player p : MobRacersPlugin.getGame().getPlayers())
				p.playSound(loc, Sound.NOTE_PIANO, 1, pitch);
		}
		
		
		for(int x = loc.getBlockX() - 1; x <= loc.getBlockX() + 1; x++)
			for(int z = loc.getBlockZ() - 1; z <= loc.getBlockZ() + 1; z++)
			{
				if(Math.abs(x - loc.getBlockX()) == 2 && Math.abs(z - loc.getBlockZ()) == 2)
					continue;
				
				for(int y = loc.getBlockY(); y >= 0; y--)
				{ 
					Block block = loc.getWorld().getBlockAt(x, y, z);
					
					if(block.isEmpty() || block.isLiquid() || block.getType() == Material.STANDING_BANNER || block.getType() == Material.WALL_BANNER || block.getType() == Material.BANNER)
						continue;
					
					state.addBlockToRegen(block.getLocation());
					
					if(block.getType().isOccluding())
					{
						block.setType(Material.WOOL, false);
						block.setData(this.woolColor.next(), false);
						locs.add(block.getLocation());
						if(y <= playerData.getVehicle().getEntity().getLocation().getY())
							break;
					}
					else
						block.setType(Material.AIR);
				}
			}
		count++;
		
		if(count >= 120)
			cancel();
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		if(!MobRacersPlugin.getGame().getPlayers().contains(event.getPlayer()))
			return;
		
		GamePlayerData data;
		if((data = this.state.getPlayerData(event.getPlayer())) == null)
			return;
		
		if(this.playerData == data)
			return;
		
		if(data.isFinished())
			return;
		
		if(data.getVehicle().getEntity() == null)
			return;
		
		Location loc = data.getVehicle().getEntity().getLocation();
		
		for(Location wool : this.locs)
			if(wool.getBlockX() == loc.getBlockX() && wool.getBlockZ() == loc.getBlockZ())
			{
				if(ShieldItem.protect(event.getPlayer()))
					return;
				
				if(!event.getPlayer().hasPotionEffect(PotionEffectType.CONFUSION))
					event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 300, 0, false, false), true);
				break;
			}
	}
}
