package com.grillo78.BeyCraft.capabilities;

public class BladerLevel implements IBladerLevel{

	private float bladerLevel=1;
	
	@Override
	public void setBladerLevel(float level) {
		bladerLevel = level;
	}

	@Override
	public float getBladerLevel() {
		return bladerLevel;
	}

}
