package ga.beycraft.capabilities;

public interface IBladerLevel {

    public int getBladerLevel();

    public void increaseExperience(float experience);

    public void setExperience(float experience);

    public float getExperience();

    public void setExpForNextLevel(float expForNexLevel);

    public float getExpForNextLevel();

    boolean isInResonance();

    void setInResonance(boolean inResonance);

    void increaseResonanceCount();

    void tick();
}
