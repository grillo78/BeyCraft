package com.grillo78.BeyCraft.capabilities;

public class BladerLevel implements IBladerLevel{

	private int bladerLevel=0;
	
	@Override
	public void setBladerLevel(int level) {
		bladerLevel = level;
	}

	@Override
	public int getBladerLevel() {
		return bladerLevel;
	}

}
