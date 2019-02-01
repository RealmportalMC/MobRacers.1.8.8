package me.winterguardian.mobracers;

import java.io.File;
import java.util.HashMap;

import me.winterguardian.core.game.state.StateGameSetup;
import me.winterguardian.core.world.SerializableLocation;
import me.winterguardian.mobracers.vehicle.VehicleType;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class MobRacersSetup extends StateGameSetup
{
	private HashMap<VehicleType, SerializableLocation> vehicleLocations;

	private ItemStack leaveItem, arenaItem, vehicleItem, spectatorItem;
	
	public MobRacersSetup(File file)
	{
		super(file);
		this.vehicleLocations = new HashMap<>();

		this.leaveItem = new ItemStack(Material.REDSTONE_BLOCK);
		this.arenaItem = new ItemStack(Material.ACTIVATOR_RAIL);
		this.vehicleItem = new ItemStack(Material.SADDLE);
		this.spectatorItem = new ItemStack(Material.COMPASS);
	}

	@Override
	protected void load(YamlConfiguration config)
	{
		super.load(config);
		for(VehicleType type : VehicleType.values())
			if(config.isString("vehicle-location." + type.name()))
				this.vehicleLocations.put(type, SerializableLocation.fromString(config.getString("vehicle-location." + type.name())));

		this.leaveItem = config.getItemStack("leave-item", this.leaveItem);
		this.arenaItem = config.getItemStack("arena-item", this.arenaItem);
		this.vehicleItem = config.getItemStack("vehicle-item", this.vehicleItem);
		this.spectatorItem = config.getItemStack("spectator-item", this.spectatorItem);
	}

	@Override
	protected void save(YamlConfiguration config)
	{
		super.save(config);
		for(VehicleType type : VehicleType.values())
			if(this.vehicleLocations.get(type) != null)
				config.set("vehicle-location." + type.name(), "" + this.vehicleLocations.get(type));
			else
				config.set("vehicle-location." + type.name(), null);

		config.set("leave-item", this.leaveItem);
		config.set("arena-item", this.arenaItem);
		config.set("vehicle-item", this.vehicleItem);
		config.set("spectator-item", this.spectatorItem);
	}
	
	public HashMap<VehicleType, SerializableLocation> getVehicleLocations()
	{
		return this.vehicleLocations;
	}

	public boolean isBadRegion()
	{
		if(getRegion() == null)
			return false;
		
		if(getLobby() != null && !getRegion().contains(getLobby()))
			return true;
		
		if(getExit() != null && getRegion().contains(getExit()))
			return true;
		
		for(SerializableLocation loc : this.vehicleLocations.values())
			if(loc != null && loc.getLocation() != null && !getRegion().contains(loc.getLocation()))
				return true;
		
		return false;
	}

	public ItemStack getLeaveItem()
	{
		return leaveItem;
	}

	public void setLeaveItem(ItemStack leaveItem)
	{
		this.leaveItem = leaveItem;
	}

	public ItemStack getArenaItem()
	{
		return arenaItem;
	}

	public void setArenaItem(ItemStack arenaItem)
	{
		this.arenaItem = arenaItem;
	}

	public ItemStack getVehicleItem()
	{
		return vehicleItem;
	}

	public void setVehicleItem(ItemStack vehicleItem)
	{
		this.vehicleItem = vehicleItem;
	}

	public ItemStack getSpectatorItem()
	{
		return spectatorItem;
	}

	public void setSpectatorItem(ItemStack spectatorItem)
	{
		this.spectatorItem = spectatorItem;
	}
}
