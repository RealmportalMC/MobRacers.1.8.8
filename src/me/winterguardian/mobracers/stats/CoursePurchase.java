package me.winterguardian.mobracers.stats;

import java.io.File;

import me.winterguardian.core.Core;
import me.winterguardian.core.json.JsonUtil;
import me.winterguardian.core.util.TextUtil;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersConfig;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.music.CourseRecord;
import me.winterguardian.mobracers.vehicle.VehicleType;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CoursePurchase
{
	public static void init(){}
	
	private static YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(MobRacersPlugin.getPlugin().getDataFolder(), "purchases.yml"));
	
	public static final CoursePurchase COW = new CoursePurchase(VehicleType.COW, config.getInt("vehicle.cow.price", 2_000));
	public static final CoursePurchase SLIME = new CoursePurchase(VehicleType.SLIME, config.getInt("vehicle.slime.price",2_000));
	public static final CoursePurchase RABBIT = new CoursePurchase(VehicleType.RABBIT, config.getInt("vehicle.rabbit.price", 2_000));
	public static final CoursePurchase SQUID = new CoursePurchase(VehicleType.SQUID, config.getInt("vehicle.squid.price", 2_000));
	public static final CoursePurchase UNDEAD_HORSE = new CoursePurchase(VehicleType.UNDEAD_HORSE, config.getInt("vehicle.undead_horse.price", 2_000));
	public static final CoursePurchase WOLF = new CoursePurchase(VehicleType.WOLF, config.getInt("vehicle.wolf.price", 2_000));
	public static final CoursePurchase CHICKEN = new CoursePurchase(VehicleType.CHICKEN, config.getInt("vehicle.chicken.price", 2_000));
	
	public static final CoursePurchase CHIRP = new CoursePurchase(CourseMessage.PURCHASE_CHIRP.toString(), CourseRecord.CHIRP, config.getInt("disc.chirp.price", 200));
	public static final CoursePurchase MALL = new CoursePurchase(CourseMessage.PURCHASE_MALL.toString(), CourseRecord.MALL, config.getInt("disc.mall.price", 200));
	public static final CoursePurchase STRAD = new CoursePurchase(CourseMessage.PURCHASE_STRAD.toString(), CourseRecord.STRAD, config.getInt("disc.strad.price", 200));
	
	private PurchaseType type;
	
	private String userDataId;
	private String name;
	private int price;
	
	public CoursePurchase(VehicleType vehicle, int price)
	{
		this.type = PurchaseType.VEHICLE;
		
		this.userDataId = "vehicle." + vehicle.name().toLowerCase().replaceAll("_", "-");
		this.name = vehicle.createNewVehicle().getName().replaceAll(" ", "-");
		this.price = price;
	}
	
	public CoursePurchase(String name, CourseRecord music, int price)
	{
		this.type = PurchaseType.MUSIC;
		
		this.userDataId = "record." + music.getItem().name().toLowerCase().replace("_", "-");
		this.name = name;
		this.price = price;
	}
	
	public void purchase(Player p)
	{
		CourseStats stats = new CourseStats(p, Core.getUserDatasManager().getUserData(p));
		
		if(this.hasPurchased(stats))
		{
			CourseMessage.PURCHASE_ALREADYPURCHASED.say(p);
			return;
		}
		
		if(stats.getPoints() < this.price)
		{
			CourseMessage.PURCHASE_NOTENOUGHTMONEY.say(p);
			return;
		}
		
		CourseMessage.SEPARATOR_PURCHASE.say(p);
		p.sendMessage(type.getPurchaseMessage(this.name.replaceAll("-", " ")));
		stats.purchase(this.userDataId);
		stats.removePoints(getPrice());
		CourseMessage.POINTS_REMOVE.say(p, "#", CourseMessage.toString(this.price), "<bal>", CourseMessage.toString(stats.getPoints()));
		CourseMessage.SEPARATOR_PURCHASE.say(p);
	}
	
	public boolean hasPurchased(Player p)
	{
		if(!((MobRacersConfig)MobRacersPlugin.getGame().getConfig()).enableStats())
			return true;
		
		return this.hasPurchased(new CourseStats(p, Core.getUserDatasManager().getUserData(p)));
	}
	
	private boolean hasPurchased(CourseStats stats)
	{
		return stats.isPurchased(this.userDataId);
	}

	public PurchaseType getType()
	{
		return type;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getPrice()
	{
		return this.price;
	}
	
	public String getPresentation(int points)
	{
		if(this.price <= points)
			return JsonUtil.toJson(this.getType().getName() + " §r" + this.getName() + " §r§l- §a§l" + CourseMessage.PURCHASE_PRICE.toString().replace("<price>", TextUtil.toString(this.getPrice())), "show_text", JsonUtil.toJson(CourseMessage.PURCHASE_CLICKTOBUY.toString()), "suggest_command", "\"/mobracers buy " + this.getName() + "\"");
		
		return JsonUtil.toJson(this.getType().getName() + " §r" + this.getName() + " §r§l- §c§l" + CourseMessage.PURCHASE_PRICE.toString().replace("<price>", TextUtil.toString(this.getPrice())), "show_text", JsonUtil.toJson(CourseMessage.PURCHASE_NOTENOUGHTTOBUY.toString()), null, null);
	}
	
	public static CoursePurchase[] values()
	{
		CoursePurchase[] purchases = new CoursePurchase[CoursePurchase.class.getFields().length];
		
		for(int i = 0; i < CoursePurchase.class.getFields().length; i++)
			try
			{
				purchases[i] = (CoursePurchase) CoursePurchase.class.getFields()[i].get(null);
			}
			catch (Exception e)
			{
				purchases[i] = null;
			}
		return purchases;
	}
	
	public static CoursePurchase getByName(String name)
	{
		for(CoursePurchase coursePurchase : values())
			if(coursePurchase.getName().equalsIgnoreCase(name))
				return coursePurchase;
		return null;
	}
	
	public static boolean hasPurchasedEverything(Player p)
	{
		for(CoursePurchase purchase : values())
			if(!purchase.hasPurchased(p))
				return false;
		
		return true;
	}
}
