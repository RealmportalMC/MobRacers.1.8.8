package me.winterguardian.mobracers.item.types;

import java.util.ArrayList;
import java.util.List;

import me.winterguardian.core.particle.ParticleType;
import me.winterguardian.core.particle.ParticleUtil;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.item.Item;
import me.winterguardian.mobracers.item.ItemResult;
import me.winterguardian.mobracers.item.ItemType;
import me.winterguardian.mobracers.state.game.GamePlayerData;
import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.state.game.ItemBox;
import me.winterguardian.mobracers.vehicle.Vehicle;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class FakeItem extends Item implements Listener
{
	private long godToUser;
	private Player player;
	
	private GameState game;
	private Location loc;

	public FakeItem()
	{
		this.godToUser = 0;
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.ITEM_FAKEITEM.toString();
	}

	@Override
	public ItemType getType()
	{
		return ItemType.FAKE_ITEM;
	}

	@SuppressWarnings("deprecation")
	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{
		if(vehicle.getEntity().getLocation().getBlock().getType() == Material.STANDING_BANNER)
			return ItemResult.KEEP;
		
		this.player = player;
		this.game = game;
		this.loc = new Location(vehicle.getEntity().getLocation().getWorld(), 
				vehicle.getEntity().getLocation().getBlockX() + 0.5, 
				vehicle.getEntity().getLocation().getBlockY() + 0.5, 
				vehicle.getEntity().getLocation().getBlockZ() + 0.5);
		
		game.addBlockToRegen(this.loc);
		this.loc.getBlock().setTypeIdAndData(Material.STANDING_BANNER.getId(), (byte) ItemBox.getBannerRotation(player.getLocation().getYaw()), false);
		Banner banner = (Banner) vehicle.getEntity().getLocation().getBlock().getState();
		for(int i = 0; i < banner.numberOfPatterns(); i++)
			banner.removePattern(i);
		banner.setBaseColor(DyeColor.YELLOW);
		banner.setPatterns(getPatterns());
		banner.update(true);
		
		ParticleUtil.playBlockParticles(this.loc, ParticleType.BLOCK_CRACK, 0.5f, 0.5f, 0.5f, 0, 100, Material.WOOL.getId(), (int)DyeColor.ORANGE.getWoolData());
		ParticleUtil.playBlockParticles(this.loc, ParticleType.BLOCK_CRACK, 0.5f, 0.5f, 0.5f, 0, 100, Material.WOOL.getId(), (int)DyeColor.BLACK.getWoolData());
		
		this.godToUser = System.nanoTime() + 5_000_000_000l;
		
		Bukkit.getPluginManager().registerEvents(this, MobRacersPlugin.getPlugin());
		
		return ItemResult.DISCARD;
	}

	@Override
	public ItemStack getItemStack()
	{
		ItemStack item = new ItemStack(Material.BANNER, 1);
		
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§r§c" + CourseMessage.ITEM_FAKEITEM.toString());
		if(itemMeta instanceof BannerMeta)
		{
			((BannerMeta) itemMeta).setBaseColor(DyeColor.YELLOW);
			((BannerMeta) itemMeta).setPatterns(getPatterns());
		}
		item.setItemMeta(itemMeta);
		return item;
	}

	@Override
	public void cancel()
	{
		if(this.game == null)
			return;
		
		HandlerList.unregisterAll(this);
		if(!this.game.isCourseFinished())
			this.loc.getBlock().setType(Material.AIR);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		if(!MobRacersPlugin.getGame().contains(event.getPlayer()))
			return;
		
		if(MobRacersPlugin.getGame().getState() != this.game)
			return;
		
		GamePlayerData data = null;
		if((data = this.game.getPlayerData(event.getPlayer())) == null || data.getVehicle() == null || data.getVehicle().getEntity() == null)
			return;
		
		if(data.getVehicle().getEntity().getWorld() != this.loc.getWorld())
			return;
		
		if(event.getPlayer() == this.player && this.godToUser > System.nanoTime())
			return;
		
		
		if(data.getVehicle().getEntity().getLocation().distance(this.loc) < 1)
		{
			if(!ShieldItem.protect(event.getPlayer()))
			{
				data.getVehicle().getEntity().setVelocity(new Vector(-Math.sin(Math.toRadians(-event.getTo().getYaw())) * 1.5, 0.5, -Math.cos(Math.toRadians(-event.getTo().getYaw())) * 1.5));
				event.setCancelled(true);
			}
			
			ParticleUtil.playBlockParticles(this.loc, ParticleType.BLOCK_CRACK, 0.5f, 0.5f, 0.5f, 0, 100, Material.REDSTONE_BLOCK.getId(), 0);
			this.cancel();
		}
	}
	
	public List<Pattern> getPatterns()
	{
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP));
		patterns.add(new Pattern(DyeColor.YELLOW, PatternType.RHOMBUS_MIDDLE));
		patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_DOWNLEFT));
		patterns.add(new Pattern(DyeColor.YELLOW, PatternType.HALF_HORIZONTAL_MIRROR));
		patterns.add(new Pattern(DyeColor.BLACK, PatternType.TRIANGLE_BOTTOM));
		patterns.add(new Pattern(DyeColor.YELLOW, PatternType.STRIPE_MIDDLE));
		patterns.add(new Pattern(DyeColor.YELLOW, PatternType.STRIPE_BOTTOM));
		patterns.add(new Pattern(DyeColor.YELLOW, PatternType.BORDER));
		patterns.add(new Pattern(DyeColor.YELLOW, PatternType.CURLY_BORDER));
		patterns.add(new Pattern(DyeColor.ORANGE, PatternType.GRADIENT_UP));
		return patterns;
	}
}
