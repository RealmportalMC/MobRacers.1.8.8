package me.winterguardian.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

public class PlayerState
{
	private UUID playerUUID;
	private Location location;
	private ItemStack[] inventory;
	private ItemStack[] armor;
	private ItemStack itemOnCursor;
	private float exp;
	private int level;
	private float flySpeed, walkingSpeed;
	private GameMode gamemode;
	private List<PotionEffect> potionEffects; 
	private double health;
	private int foodLevel;
	private float saturation;
	private float exhaustion;
	private int fireTicks;
	private int remainingAir;
	private float fallDistance;
	private boolean allowFlight, flying;
	private Vector velocity;
	
	public PlayerState(Player player)
	{
		this.playerUUID = player.getUniqueId();
		this.location = player.getLocation();
		this.inventory = player.getInventory().getContents();
		this.armor = player.getInventory().getArmorContents();
		this.itemOnCursor = player.getItemOnCursor();
		this.exp = player.getExp();
		this.level = player.getLevel();
		this.flySpeed = player.getFlySpeed();
		this.walkingSpeed = player.getWalkSpeed();
		this.gamemode = player.getGameMode();
		this.potionEffects = new ArrayList<PotionEffect>(player.getActivePotionEffects());
		this.health = player.getHealth();
		this.foodLevel = player.getFoodLevel();
		this.saturation = player.getSaturation();
		this.exhaustion = player.getExhaustion();
		this.fireTicks = player.getFireTicks();
		this.remainingAir = player.getRemainingAir();
		this.fallDistance = player.getFallDistance();
		this.allowFlight = player.getAllowFlight();
		this.flying = player.isFlying();
		this.velocity = player.getVelocity();
	}
	
	public UUID getUniqueId()
	{
		return this.playerUUID;
	}
	
	public void apply()
	{
		Player player = Bukkit.getPlayer(playerUUID);
		
		if(player == null || !player.isOnline())
			return;
		
		player.teleport(this.location);
		player.setVelocity(this.velocity);
		player.setFallDistance(this.fallDistance);
		player.setGameMode(this.gamemode);
		player.setAllowFlight(this.allowFlight);
		player.setFlying(this.flying);
		player.setWalkSpeed(this.walkingSpeed);
		player.setFlySpeed(this.flySpeed);
		player.setExp(this.exp);
		player.setLevel(this.level);
		player.setHealth(this.health);
		player.setFoodLevel(this.foodLevel);
		player.setSaturation(this.saturation);
		player.setExhaustion(this.exhaustion);
		for(PotionEffect effect : new ArrayList<PotionEffect>(player.getActivePotionEffects()))
			player.removePotionEffect(effect.getType());
		player.addPotionEffects(this.potionEffects);
		player.setFireTicks(this.fireTicks);
		player.setRemainingAir(this.remainingAir);
		player.getInventory().setContents(this.inventory);
		player.getInventory().setArmorContents(this.armor);
		player.setItemOnCursor(this.itemOnCursor);
	}
}
