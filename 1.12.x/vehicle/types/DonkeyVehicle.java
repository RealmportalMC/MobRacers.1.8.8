package me.winterguardian.mobracers.vehicle.types;

import java.util.ArrayList;
import java.util.Arrays;

import me.winterguardian.core.util.SoundEffect;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.item.Item;
import me.winterguardian.mobracers.item.ItemType;
import me.winterguardian.mobracers.item.types.SugarItem;
import me.winterguardian.mobracers.item.types.WallItem;
import me.winterguardian.mobracers.item.types.WallItem.WallBlock;
import me.winterguardian.mobracers.item.types.special.DonkeySpecialItem;
import me.winterguardian.mobracers.stats.CourseAchievement;
import me.winterguardian.mobracers.vehicle.VehicleGUIItem;
import me.winterguardian.mobracers.vehicle.VehicleType;
import me.winterguardian.mobracers.vehicle.WinnableVehicle;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DonkeyVehicle extends WinnableVehicle
{
	@Override
	protected EntityType getEntityType()
	{
		return EntityType.HORSE;
	}

	@Override
	public VehicleType getType()
	{
		return VehicleType.DONKEY;
	}

	@Override
	public void spawn(Location loc, Player p)
	{
		super.spawn(loc, p);
		if(this.getEntity() != null && this.getEntity() instanceof Horse)
		{
			((Horse) this.getEntity()).setAdult();
			((Horse) this.getEntity()).setAgeLock(true);
			((Horse) this.getEntity()).getInventory().setSaddle(new ItemStack(Material.SADDLE));
			((Horse) this.getEntity()).setVariant(Horse.Variant.DONKEY);
			((Horse) this.getEntity()).setCarryingChest(true);
		}
	}
	
	@Override
	public Item getItem(ItemType type)
	{
		switch(type)
		{
		case SUGAR:
			return new SugarItem(new SoundEffect(Sound.ENTITY_DONKEY_AMBIENT, 1, 1f));
		case WALL:
			return new WallItem(Arrays.asList(new WallBlock(Material.HAY_BLOCK, (byte)0), new WallBlock(Material.HAY_BLOCK, (byte)4), new WallBlock(Material.HAY_BLOCK, (byte)8), new WallBlock(Material.WOOD, (byte)1)), new SoundEffect(Sound.ENTITY_DONKEY_ANGRY, 1, 1f));
		case SPECIAL:
			return new DonkeySpecialItem();
		default:
			return type.getDefault();
		
		}
	}

	@Override
	public boolean canChoose(Player p)
	{
		return getWinAchievement().isComplete(p); 
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.VEHICLE_DONKEY_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.VEHICLE_DONKEY_DESC.toString();
	}

	@Override
	public CourseAchievement getWinAchievement()
	{
		return CourseAchievement.HORSE_PASSINGS;
	}

	@Override
	public VehicleGUIItem getGUIItem()
	{
		return new VehicleGUIItem(getType(), 20, Material.MONSTER_EGG, 1, (short)100, "§3§l" + getName(), new ArrayList<String>());
	}
}
