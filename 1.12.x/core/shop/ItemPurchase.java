package me.winterguardian.core.shop;

import me.winterguardian.core.util.SoundEffect;
import me.winterguardian.core.util.TextUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * Created by 1541869 on 2015-11-24.
 */
public abstract class ItemPurchase implements PurchaseType
{
    private String creationHeader, header, line2;

    public ItemPurchase(String creationHeader, String header, String line2)
    {
        this.creationHeader = creationHeader;
        this.header = header;
        this.line2 = line2;
    }

    @Override
    public boolean canGive(Player player)
    {
	    return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void give(String[] sign, Player player)
    {
        int id, amount;
        short data = 0;

        String item = TextUtil.removeColorCodes(sign[2], '§');

        amount = Integer.parseInt(item.split("[xX][ ]?")[0]);

        if(sign[2].contains(":"))
        {
            id = Integer.parseInt(item.split("[xX][ ]?")[1].split(":")[0]);
            data = Short.parseShort(item.split("[xX][ ]?")[1].split(":")[1]);
        }
        else
            id = Integer.parseInt(item.split("[xX][ ]?")[1]);

        player.getInventory().addItem(new ItemStack(Material.getMaterial(id), amount, data));
	    new SoundEffect(Sound.BLOCK_ANVIL_LAND, 1f, 1f).play(player);
    }

    @Override
    public boolean match(String[] sign)
    {
        if(sign.length != 4)
            return false;

        if(!TextUtil.removeColorCodes(sign[0], '§').equalsIgnoreCase(TextUtil.removeColorCodes(header, '§')))
            return false;

        if(!TextUtil.removeColorCodes(line2, '§').equalsIgnoreCase(TextUtil.removeColorCodes(sign[1], '§')))
            return false;

        try
        {
            String item = TextUtil.removeColorCodes(sign[2], '§');

            Integer.parseInt(item.split("[xX][ ]?")[0]);

            Integer.parseInt(item.split("[xX][ ]?")[1].split(":")[0]);
            if(item.contains(":"))
                Short.parseShort(item.split("[xX][ ]?")[1].split(":")[1]);

            Integer.parseInt(TextUtil.removeColorCodes(sign[3], '§'));
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    @Override
    public boolean canCreate(String[] sign)
    {
        if(sign.length != 4)
            return false;

        if(!creationHeader.equalsIgnoreCase(sign[0]))
            return false;

        if(!"item".equalsIgnoreCase(sign[1]))
            return false;

        try
        {
            Integer.parseInt(sign[2].split("[xX][ ]?")[0]);

            Integer.parseInt(sign[2].split("[xX][ ]?")[1].split(":")[0]);
            if(sign[2].contains(":"))
                Integer.parseInt(sign[2].split("[xX][ ]?")[1].split(":")[1]);

            Integer.parseInt(sign[3]);
        }
        catch(Exception e)
        {
            return false;
        }

        return true;
    }

    @Override
    public String[] create(String[] sign)
    {
        return new String[]{header, line2, "§f" + sign[2], "§c" + sign[3]};
    }

    @Override
    public int getPrice(String[] sign)
    {
        return Integer.parseInt(TextUtil.removeColorCodes(sign[3], '§'));
    }
}
