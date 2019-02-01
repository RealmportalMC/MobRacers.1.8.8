package me.winterguardian.mobracers.pluginsupport;

import me.winterguardian.core.game.Game;
import me.winterguardian.core.inventorygui.GUIItem;
import me.winterguardian.mobracers.MobRacersGame;
import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.state.lobby.ArenaSelectionState;
import me.winterguardian.mobracers.state.lobby.VehicleSelectionState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Alexander Winter on 2016-01-04.
 */
public class SekaiHubItem extends GUIItem
{
	private MobRacersGame game;

	public SekaiHubItem(MobRacersGame game)
	{
		super(14);
		this.game = game;
	}

	@Override
	public void click(Player player, ClickType click)
	{
		if(click.isLeftClick())
		{
			player.performCommand("mobracers join");
			return;
		}

		player.performCommand("mobracers stats");
	}

	@Override
	public ItemStack getItemStack(Player player)
	{
		ItemStack saddle = new ItemStack(Material.SADDLE, 1);
		ItemMeta meta = saddle.getItemMeta();
		meta.setDisplayName("§f§lMob§2§lRacers");
		List<String> lore = new ArrayList<>();

		if(game == null)
			return saddle;

		int players = game.getPlayers().size();
		lore.add("§e" + players + " joueur" + (players > 1 ? "s" : ""));

		if(game.getState() instanceof GameState)
		{
			lore.add("§fEn course sur");
			lore.add("§e" + ((GameState)game.getState()).getArena().getName());
		}
		else if(game.getState() instanceof VehicleSelectionState)
		{
			VehicleSelectionState vehicleSelectionState = (VehicleSelectionState)game.getState();

			lore.add("§fChoix du véhicule");
			lore.add("§e" + vehicleSelectionState.getArena().getName());
		}
		else if(game.getState() instanceof ArenaSelectionState)
		{
			lore.add("§fChoix de l'arène");
		}
		else
		{
			lore.add("§fEn attente");
		}

		lore.add(" ");
		lore.add("§aClic gauche pour jouer");
		lore.add("§eClic droit pour les stats");

		meta.setLore(lore);

		saddle.setItemMeta(meta);

		return saddle;
	}
}
