package com.grillo78.beycraft.tileentity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.capabilities.BladerCapProvider;
import com.grillo78.beycraft.entity.EntityBey;

import com.grillo78.beycraft.network.PacketHandler;
import com.grillo78.beycraft.network.message.MessageSyncBladerLevel;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.NetworkDirection;

public class StadiumTileEntity extends TileEntity implements ITickableTileEntity {

	private boolean inBattle = false;
	private int beysInBattle = 0;

	public StadiumTileEntity() {
		super(BeyRegistry.STADIUMTILEENTITYTYPE);
	}

	@Override
	public void tick() {
		List<EntityBey> beys = world.getEntitiesWithinAABB(EntityBey.class,
				new AxisAlignedBB(getPos().north().east(2), getPos().south(2).west().up()));
		int beysSpinning = 0;
		if (beysInBattle < beys.size()) {
			beysInBattle = beys.size();
		}
		for (EntityBey bey : beys) {
			if (!bey.isStoped()) {
				beysSpinning++;
			}
		}
		if (inBattle) {
			if (beysSpinning == 1 && !world.isRemote) {
				for (ServerPlayerEntity player : ((ServerWorld) world).getServer().getPlayerList().getPlayers()) {
					if (player.getName().getString().equals(beys.get(0).getPlayerName())) {
						player.getCapability(BladerCapProvider.BLADERCURRENCY_CAP).ifPresent(h -> {
							Random rand = new Random();
							h.increaseCurrency(round(rand.nextInt(100) + rand.nextFloat(), 2));
						});
						player.getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(h -> {
							Random rand = new Random();
							if (beys.size() != beysInBattle) {
								h.increaseExperience(round(
										rand.nextInt(100 * (beysInBattle - beys.size())) + rand.nextFloat(), 2));
							} else {
								h.increaseExperience(round(
										rand.nextInt(50 * (beysInBattle-1))*beys.get(0).getBladerLevel() + rand.nextFloat(), 2));
							}

							PacketHandler.instance.sendTo(new MessageSyncBladerLevel(h.getBladerLevel(),h.getExperience()),player.connection.getNetworkManager(),
									NetworkDirection.PLAY_TO_CLIENT);
						});
					}
				}

			}
			if (beysSpinning < 2) {
				inBattle = false;
				beysInBattle = 0;
			}
		} else {
			inBattle = beysSpinning > 1;
		}
	}

	public float round(float d, int decimalPlace) {
		return BigDecimal.valueOf(d).setScale(decimalPlace, BigDecimal.ROUND_HALF_UP).floatValue();
	}
}