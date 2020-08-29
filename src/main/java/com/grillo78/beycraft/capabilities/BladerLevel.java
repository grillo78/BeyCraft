package com.grillo78.beycraft.capabilities;

import com.grillo78.beycraft.BeyCraft;
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
		expForNexLevel -= experience;
		if (expForNexLevel <= 0) {
			bladerLevel++;
			this.experience = 0;
			expForNexLevel = (float) (Math.exp(bladerLevel / 3) * 100 / 5);
		}
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			DiscordRichPresence rich = new DiscordRichPresence.Builder("\nExperience: " + experience).setBigImage("beycraft", "")
					.setDetails("Level: " + bladerLevel).setStartTimestamps(BeyCraft.TIME_STAMP)
					.build();
			DiscordRPC.discordUpdatePresence(rich);
		});
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
		expForNexLevel = (float) (Math.exp(bladerLevel / 3) * 100 / 5) - this.experience;
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