package ga.beycraft.capabilities;

import ga.beycraft.BeyCraft;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class BladerLevel implements IBladerLevel {

    private int bladerLevel = 1;
    private float experience = 0;
    private float expForNextLevel = calcExpForNextLevel(bladerLevel);
    private boolean inResonance = false;
    private int resonanceCount = 0;
    private int resonanceTimeoutCount = 0;

    @Override
    public void increaseResonanceCount() {
        resonanceCount++;
        if (resonanceCount == 400) {
            resonanceCount = 0;
            inResonance = false;
            resonanceTimeoutCount = 300;
            increaseExperience(0.05F);
        }
    }

    @Override
    public int getBladerLevel() {
        return bladerLevel;
    }

    @Override
    public void increaseExperience(float experience) {
        setExperience(this.experience + experience);
    }

    @Override
    public boolean isInResonance() {
        return inResonance;
    }

    @Override
    public void setInResonance(boolean inResonance) {
        if (resonanceTimeoutCount == 0) {
            this.inResonance = inResonance;
        }
    }

    @Override
    public void tick() {
        if (resonanceTimeoutCount != 0)
            resonanceTimeoutCount--;
    }

    public static float calcExpForNextLevel(float bladerLevel) {
        return (float) (Math.exp(bladerLevel / 50) * 20) - 20;
    }

    @Override
    public void setExpForNextLevel(float expForNextLevel) {
        this.expForNextLevel = expForNextLevel;
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            DiscordRichPresence rich = new DiscordRichPresence.Builder("\nExperience: " + experience).setBigImage("beycraft", "")
                    .setDetails("Level: " + bladerLevel).setStartTimestamps(BeyCraft.TIME_STAMP)
                    .build();
            DiscordRPC.discordUpdatePresence(rich);
        });
    }

    @Override
    public float getExpForNextLevel() {
        return expForNextLevel;
    }

    @Override
    public void setExperience(float experience) {
        this.experience = experience;
        bladerLevel = 1;
        while ((expForNextLevel = calcExpForNextLevel(bladerLevel)) < this.experience) {
            bladerLevel++;
        }
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            DiscordRichPresence rich = new DiscordRichPresence.Builder("\nExperience: " + experience).setBigImage("beycraft", "")
                    .setDetails("Level: " + bladerLevel).setStartTimestamps(BeyCraft.TIME_STAMP)
                    .build();
            DiscordRPC.discordUpdatePresence(rich);
        });
    }

    @Override
    public float getExperience() {
        return experience;
    }

}