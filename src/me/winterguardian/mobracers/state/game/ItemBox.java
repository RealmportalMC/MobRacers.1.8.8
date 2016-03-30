package me.winterguardian.mobracers.state.game;

import me.winterguardian.core.particle.ParticleType;
import me.winterguardian.core.particle.ParticleUtil;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.vehicle.Vehicle;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

public class ItemBox
{
	private boolean active;
	
	private int taskId;
	
	private int bannerRotation;
	private Location loc;
	
	private int delay;
	
	public ItemBox(Location loc, int delay)
	{
		this.loc = new Location(loc.getWorld(), loc.getBlockX() + 0.5, loc.getBlockY() + 0.5, loc.getBlockZ() + 0.5);
		this.bannerRotation = getBannerRotation(loc.getYaw());
		this.taskId = -1;
		this.delay = delay;
	}
	
	public boolean collide(Vehicle vehicle)
	{
		if(!active)
			return false;
		
		if(this.loc == null)
			return false;
		
		if(vehicle == null || vehicle.getEntity() == null)
			return false;
		
		if(loc.getWorld() != vehicle.getEntity().getWorld())
			return false;
		
		return vehicle.getEntity().getLocation().distance(this.loc) < 1;
	}
	
	@SuppressWarnings("deprecation")
	public void spawn()
	{
		this.active = true;

		if(this.loc.getBlock().getType() == Material.STANDING_BANNER)
			return;

		this.loc.getBlock().setTypeIdAndData(Material.STANDING_BANNER.getId(), (byte) this.bannerRotation, true);
		if(!(this.loc.getBlock().getState() instanceof Banner))
			return;

		Banner state = ((Banner)this.loc.getBlock().getState());
		state.setBaseColor(DyeColor.YELLOW);
		state.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP));
		state.addPattern(new Pattern(DyeColor.YELLOW, PatternType.RHOMBUS_MIDDLE));
		state.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_DOWNLEFT));
		state.addPattern(new Pattern(DyeColor.YELLOW, PatternType.HALF_HORIZONTAL_MIRROR));
		state.addPattern(new Pattern(DyeColor.BLACK, PatternType.TRIANGLE_BOTTOM));
		state.addPattern(new Pattern(DyeColor.YELLOW, PatternType.STRIPE_MIDDLE));
		state.addPattern(new Pattern(DyeColor.YELLOW, PatternType.STRIPE_BOTTOM));
		state.addPattern(new Pattern(DyeColor.YELLOW, PatternType.BORDER));
		state.addPattern(new Pattern(DyeColor.YELLOW, PatternType.CURLY_BORDER));
		state.addPattern(new Pattern(DyeColor.BROWN, PatternType.GRADIENT_UP));
		state.update(true);
		
		ParticleUtil.playBlockParticles(this.loc, ParticleType.BLOCK_CRACK, 0.5f, 0.5f, 0.5f, 0, 100, Material.WOOL.getId(), (int)DyeColor.YELLOW.getWoolData());
		ParticleUtil.playBlockParticles(this.loc, ParticleType.BLOCK_CRACK, 0.5f, 0.5f, 0.5f, 0, 100, Material.WOOL.getId(), (int)DyeColor.WHITE.getWoolData());
	}
	
	public void remove()
	{
		this.active = false;
		this.loc.getBlock().setType(Material.AIR, true);
		if(this.taskId != -1)
			Bukkit.getScheduler().cancelTask(this.taskId);
	}
	
	public void pickUp()
	{
		this.remove();
		this.taskId = Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				ItemBox.this.spawn();
				ItemBox.this.taskId = -1;
			}
			
		}, this.delay).getTaskId();
	}
	
	public static int getBannerRotation(float yaw)
	{
		int rotation = Math.round((yaw + 540) * 2 / 45);
		while(rotation >= 16)
			rotation -= 16;
		return rotation;
	}
}
