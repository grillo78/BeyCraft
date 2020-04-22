package com.grillo78.beycraft.capabilities;

public class BladerLevel implements IBladerLevel{

    private int bladerLevel=1;
    private float experience=0;
    private float expForNexLevel;

    @Override
    public void setBladerLevel(int level) {
        bladerLevel = level;
        expForNexLevel = (float) (Math.log(bladerLevel)*2+2);
    }

    @Override
    public int getBladerLevel() {
        return bladerLevel;
    }

    @Override
    public void increaseExperience(float experience) {
        this.experience += experience;
        expForNexLevel -= experience;
        if(expForNexLevel<=0){
            bladerLevel++;
            expForNexLevel = (float) (Math.exp(bladerLevel/3)*100/5)-this.experience;
        }
    }

    @Override
    public void setExpForNextLevel(float expForNexLevel) {
        this.expForNexLevel = expForNexLevel;
    }

    @Override
    public float getExpForNextLevel() {
        return expForNexLevel;
    }

    @Override
    public void setExperience(float experience) {
        this.experience = experience;
        expForNexLevel = (float) (Math.log(bladerLevel)*2+2-experience);
    }

    @Override
    public float getExperience() {
        return experience;
    }

}