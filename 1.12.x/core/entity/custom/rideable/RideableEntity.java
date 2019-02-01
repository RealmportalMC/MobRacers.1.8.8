package me.winterguardian.core.entity.custom.rideable;

public interface RideableEntity
{
	float getClimbHeight();
	void setClimbHeight(float climbHeight);

	float getJumpHeight();
	void setJumpHeight(float jumpHeight);
	
	float getJumpThrust();
	void setJumpThrust(float jumpThrust);

	float getSpeed();
	void setSpeed(float speed);

	float getBackwardSpeed();
	void setBackwardSpeed(float backwardSpeed);

	float getSidewaySpeed();
	void setSidewaySpeed(float sidewaySpeed);
}
