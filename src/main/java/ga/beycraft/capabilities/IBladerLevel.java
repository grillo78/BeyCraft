package ga.beycraft.capabilities;

public interface IBladerLevel {

    public void setBladerLevel(int level);

    public int getBladerLevel();

    public void increaseExperience(float experience);

    public void setExperience(float experience);

    public float getExperience();

    public void setExpForNextLevel(float expForNexLevel);

    public float getExpForNextLevel();
}
