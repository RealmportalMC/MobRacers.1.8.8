package me.winterguardian.mobracers.vehicle.types;

import java.util.ArrayList;
import java.util.Arrays;

import me.winterguardian.core.sorting.AntiRecursiveRandomSelector;
import me.winterguardian.core.sorting.RandomSelector;
import me.winterguardian.core.util.SoundEffect;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.item.Item;
import me.winterguardian.mobracers.item.ItemType;
import me.winterguardian.mobracers.item.types.SugarItem;
import me.winterguardian.mobracers.item.types.WallItem;
import me.winterguardian.mobracers.item.types.WallItem.WallBlock;
import me.winterguardian.mobracers.item.types.special.HorseSpecialItem;
import me.winterguardian.mobracers.vehicle.Vehicle;
import me.winterguardian.mobracers.vehicle.VehicleGUIItem;
import me.winterguardian.mobracers.vehicle.VehicleType;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HorseVehicle extends Vehicle
{
	private static RandomSelector<Style> horseStyle = new AntiRecursiveRandomSelector<Style>(Style.values());
	private static RandomSelector<Color> horseColor = new AntiRecursiveRandomSelector<Color>(Color.values());
	
	@Override
	public VehicleType getType()
	{
		return VehicleType.HORSE;
	}

	@Override
	public void spawn(Location loc, Player p)
	{
		super.spawn(loc, p);
		if(this.getEntity() != null && this.getEntity() instanceof Horse)
		{
			((Horse) this.getEntity()).setTamed(false);
			((Horse) this.getEntity()).setAdult();
			((Horse) this.getEntity()).setAgeLock(true);
			((Horse) this.getEntity()).setVariant(Variant.HORSE);
			((Horse) this.getEntity()).setStyle(horseStyle.next());
			((Horse) this.getEntity()).setColor(horseColor.next());
			((Horse) this.getEntity()).getInventory().setSaddle(new ItemStack(Material.SADDLE));
		}
	}
	
	@Override
	public boolean canChoose(Player p)
	{
		return true;
	}


	@Override
	public String getName()
	{
		return CourseMessage.VEHICLE_HORSE_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.VEHICLE_HORSE_DESC.toString();
	}

	
	@Override
	public Item getItem(ItemType type)
	{
		switch(type)
		{
		case SUGAR:
			return new SugarItem(new SoundEffect(Sound.ENTITY_HORSE_GALLOP , 1, 1f));
		case WALL:
			return new WallItem(Arrays.asList(new WallBlock(Material.HAY_BLOCK, (byte)0), new WallBlock(Material.HAY_BLOCK, (byte)4), new WallBlock(Material.HAY_BLOCK, (byte)8)), new SoundEffect(Sound.ENTITY_HORSE_AMBIENT, 1, 1f));
		case SPECIAL:
			return new HorseSpecialItem();
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
	public VehicleGUIItem getGUIItem()
	{
		return new VehicleGUIItem(getType(), 19, Material.MONSTER_EGG, 1, (short)100, "§e§l" + getName(), new ArrayList<String>());
	}
}
