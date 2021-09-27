package ga.beycraft.capabilities;

import ga.beycraft.BeyCraft;
import ga.beycraft.util.RankingUtil;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class BladerLevel implements IBladerLevel {

	private int bladerLevel = 1;
	private float experience = 0;
	private float expForNexLevel = (float) (Math.exp(bladerLevel / 3) * 100 / 5);

	@Override
	public void setBladerLevel(int level) {
		bladerLevel = level;
		expForNexLevel = (float) (Math.exp(bladerLevel / 3) * 100 / 5);
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			DiscordRichPresence rich = new DiscordRichPresence.Builder("\nExperience: " + experience).setBigImage("beycraft", "")
					.setDetails("Level: " + bladerLevel).setStartTimestamps(BeyCraft.TIME_STAMP)
					.build();
			DiscordRPC.discordUpdatePresence(rich);
		});
	}

	@Override
	public int getBladerLevel() {
		return bladerLevel;
	}

	@Override
	public void increaseExperience(float experience) {
		this.experience += experience;
		setExperience(this.experience + experience);
	}

	@Override
	public void setExpForNextLevel(float expForNexLevel) {
		this.expForNexLevel = expForNexLevel;
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			DiscordRichPresence rich = new DiscordRichPresence.Builder("\nExperience: " + experience).setBigImage("beycraft", "")
					.setDetails("Level: " + bladerLevel).setStartTimestamps(BeyCraft.TIME_STAMP)
					.build();
			DiscordRPC.discordUpdatePresence(rich);
		});
	}

	@Override
	public float getExpForNextLevel() {
		return expForNexLevel;
	}

	@Override
	public void setExperience(float experience) {
		this.experience = experience;
		while((expForNexLevel = (float) (Math.exp(bladerLevel / 3) * 100 / 5)) < this.experience){
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