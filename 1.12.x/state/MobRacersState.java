package me.winterguardian.mobracers.state;

import me.winterguardian.core.game.state.State;
import me.winterguardian.core.world.Region;

public interface MobRacersState extends State
{
	Region getRegion();
}
