package me.winterguardian.core.scoreboard;

import java.util.HashMap;

import me.winterguardian.core.util.PlayerUtil;

import org.bukkit.entity.Player;

public class StaticSidebarBoard extends Board
{
	private SidebarBoardType type;
	private Object data;
	
	public StaticSidebarBoard(String... elements)
	{
		this.data = elements;
		this.type = SidebarBoardType.UNRANKED;
	}
	
	public StaticSidebarBoard(String title, HashMap<String, Integer> elements)
	{
		this.data = new Object[]{title, elements};
		this.type = SidebarBoardType.UNRANKED;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void startDisplay(Player p)
	{
		switch(this.type)
		{
		case RANKED:
			ScoreboardUtil.rankedSidebarDisplay(p, (String)((Object[])data)[0], (HashMap<String, Integer>)((Object[])data)[1]);
			return;
		case UNRANKED:
			ScoreboardUtil.unrankedSidebarDisplay(p, (String[])data);
			return;
		}
		
	}

	@Override
	public void stopDisplay(Player p)
	{
		PlayerUtil.clearBoard(p);
	}
	
	public enum SidebarBoardType
	{
		RANKED(),
		UNRANKED()
	}

}
