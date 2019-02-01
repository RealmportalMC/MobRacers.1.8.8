package me.winterguardian.core.game;

import org.bukkit.configuration.file.YamlConfiguration;

public abstract class VotableArena extends Arena
{
    private int votes, totalVotes;
    
    public VotableArena(String name)
	{
		super(name);
        this.votes = 0;
        this.totalVotes = 0;
	}

    protected void load(YamlConfiguration config)
	{
        super.load(config);
		this.votes = config.getInt("votes");
		this.totalVotes = config.getInt("total-votes");
	}
	
	protected void save(YamlConfiguration config)
	{
        super.save(config);
		config.set("votes", this.votes);
		config.set("total-votes", this.totalVotes);
	}
    
    public void reportVoteSummary(int votes, int totalVotes)
    {
        this.votes += votes;
        this.totalVotes += totalVotes;
    }
    
    public double getVotePercentage()
    {
        return (double)this.votes / (double)this.totalVotes * 100d;
    }
}