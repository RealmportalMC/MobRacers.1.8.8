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
import me.winterguardian.mobracers.item.types.special.UndeadHorseSpecialItem;
import me.winterguardian.mobracers.stats.CoursePurchase;
import me.winterguardian.mobracers.vehicle.PurchasableVehicle;
import me.winterguardian.mobracers.vehicle.VehicleGUIItem;
import me.winterguardian.mobracers.vehicle.VehicleType;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class UndeadHorseVehicle extends PurchasableVehicle
{
	@Override
	public VehicleType getType()
	{
		return VehicleType.UNDEAD_HORSE;
	}

	@Override
	public void spawn(Location loc, Player p)
	{
		super.spawn(loc, p);
		if(this.getEntity() != null && this.getEntity() instanceof Horse)
		{
			((Horse) this.getEntity()).setAdult();
			((Horse) this.getEntity()).setAgeLock(true);
			((Horse) this.getEntity()).setVariant(Variant.UNDEAD_HORSE);
			((Horse) this.getEntity()).getInventory().setSaddle(new ItemStack(Material.SADDLE));
		}
	}


	@Override
	public String getName()
	{
		return CourseMessage.VEHICLE_UNDEADHORSE_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.VEHICLE_UNDEADHORSE_DESC.toString();
	}

	
	@Override
	public Item getItem(ItemType type)
	{
		switch(type)
		{
		case SUGAR:
			return new SugarItem(new SoundEffect(Sound.ZOMBIE_IDLE, 1, 1.1f));
		case WALL:
			return new WallItem(Arrays.asList(new WallBlock(Material.STAINED_CLAY, (byte)13)), new SoundEffect(Sound.ZOMBIE_PIG_ANGRY, 1, 1f));
		case SPECIAL:
			return new UndeadHorseSpecialItem();
		default:
			return type.getDefault();
		
		}
	}

	@Override
	protected EntityType getEntityType()
	{
		return EntityType.HORSE;
	}

	@Override
	public CoursePurchase getPurchase()
	{
		return CoursePurchase.UNDEAD_HORSE;
	}

	@Override
	public VehicleGUIItem getGUIItem()
	{
		return new VehicleGUIItem(getType(), 29, Material.MONSTER_EGG, 1, (short)54, "§2§l" + getName(), new ArrayList<String>());
	}
}
