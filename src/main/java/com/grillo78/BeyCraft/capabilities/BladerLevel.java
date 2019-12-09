package com.grillo78.BeyCraft.capabilities;

public class BladerLevel implements IBladerLevel{

	private int bladerLevel=1;
	private float experience=0;
	
	@Override
	public void setBladerLevel(int level) {
		bladerLevel = level;
	}

	@Override
	public int getBladerLevel() {
		return bladerLevel;
	}

	@Override
	public void setExperience(float experience) {
		this.experience = experience;
	}

	@Override
	public float getExperience() {
		return experience;
	}

	@Override
	public float getMaxExperience() {
		return (float) Math.pow(10, bladerLevel);
	}

}
