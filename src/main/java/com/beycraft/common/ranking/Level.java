package com.beycraft.common.ranking;

import net.minecraft.nbt.CompoundNBT;

public class Level {

    private int level = 1;
    private float experience = 0;
    private float expForNextLevel = (float) (Math.exp(level / 50) * 20) - 20;

    public static float calcExpForNextLevel(float level) {
        return (float) (Math.exp(level / 50) * 20) - 20;
    }

    public CompoundNBT writeNBT() {
        CompoundNBT compound = new CompoundNBT();
        compound.putInt("level", level);
        compound.putFloat("experience", experience);
        return compound;
    }

    public static Level readFromNBT(CompoundNBT compound) {
        Level level = new Level();
        level.level = compound.getInt("level");
        level.experience = compound.getFloat("experience");
        level.calcExpForNextLevel();
        return level;
    }

    public void setCustomLevel(int level) {
        setExperience(calcExpForNextLevel(level));
    }

    public int getLevel() {
        return level;
    }

    public void increaseExperience(float experience) {
        this.experience += experience;
        expForNextLevel -= experience;
        if (expForNextLevel <= 0) {
            level++;
            this.experience = 0;
            calcExpForNextLevel();
        }
    }

    public void calcExpForNextLevel() {
        expForNextLevel = calcExpForNextLevel(level+1);
    }

    public float getExpForNextLevel() {
        return expForNextLevel;
    }

    public void setExperience(float experience) {
        this.experience = experience;
        level = 1;
        while ((expForNextLevel = calcExpForNextLevel(level)) < this.experience) {
            level++;
        }
        calcExpForNextLevel();
    }

    public float getExperience() {
        return experience;
    }
}
