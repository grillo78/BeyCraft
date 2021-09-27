package ga.beycraft.tileentity;

import ga.beycraft.BeyRegistry;
import ga.beycraft.entity.EntityBey;
import ga.beycraft.ranking.Battle;
import ga.beycraft.util.PlayerUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.server.ServerWorld;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StadiumTileEntity extends TileEntity implements ITickableTileEntity {

    private Battle battle;
    private boolean allowBattle = true;
    private int pointsToWin = 3;

    public StadiumTileEntity() {
        super(BeyRegistry.STADIUMTILEENTITYTYPE);
    }

    @Override
    public void tick() {
        if (!level.isClientSide) {
            List<EntityBey> beys = level.getEntitiesOfClass(EntityBey.class,
                    new AxisAlignedBB(getBlockPos().north().east(2), getBlockPos().south(2).west().above()));
            if (allowBattle) {
                int beysSpinning = 0;
                for (EntityBey bey : beys) {
                    if (!bey.isStoped()) {
                        beysSpinning++;
                    }
                }
                List<PlayerEntity> bladers = new ArrayList<>();
                boolean isValidBattle = true;
                for (EntityBey bey : beys) {
                    PlayerEntity player = PlayerUtils.getPlayerByName(bey.getPlayerName(), (ServerWorld) level);
                    if (player != null) {
                        if (!bladers.contains(player)) {
                            bladers.add(player);
                        } else {
                            isValidBattle = false;
                        }
                    } else {
                            isValidBattle = false;
                        }
                }
                if (isValidBattle) {
                    if (battle == null && beysSpinning > 1) {
                        battle = new Battle((ServerWorld) level, this);
                    }
                } else {
                    battle = null;
                }
                if (battle != null) {
                    battle.tick(beys);
                }
            } else {
                allowBattle = beys.size() == 0;
            }
        }
//        if (battle != null) {
//            if (beysSpinning == 1 && !level.isClientSide) {
//                for (ServerPlayerEntity player : ((ServerWorld) level).getServer().getPlayerList().getPlayers()) {
//                    for (EntityBey bey : beys) {
//                        if (player.getName().getString().equals(bey.getPlayerName())) {
//                            if (bey.hasBeylogger()) {
//                                player.getCapability(BladerCapProvider.BLADERCURRENCY_CAP).ifPresent(h -> {
//                                    Random rand = new Random();
//                                    h.increaseCurrency(round(rand.nextInt(100) + rand.nextFloat(), 2));
//                                });
//                            }
//                            player.getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(h -> {
//                                Random rand = new Random();
//                                if (beys.size() != beysInBattle) {
//                                    h.increaseExperience(round(
//                                            rand.nextInt(100 * (beysInBattle - beys.size())) + rand.nextFloat(), 2));
//                                } else {
//                                    h.increaseExperience(round(
//                                            rand.nextInt(50 * (beysInBattle - 1)) * beys.get(0).getBladerLevel() + rand.nextFloat(), 2));
//                                }
//
//                                PacketHandler.instance.sendTo(new MessageSyncBladerLevel(h.getBladerLevel(), h.getExperience()), player.connection.getConnection(),
//                                        NetworkDirection.PLAY_TO_CLIENT);
//                            });
//                        }
//                    }
//                }
//
//            }
//            if (beysSpinning < 2) {
//                inBattle = false;
//                beysInBattle = 0;
//            }
//        } else {
//            inBattle = beysSpinning > 1;
//        }
    }

    public int getPointsToWin() {
        return pointsToWin;
    }

    public void invalidateBattle() {
        battle = null;
        allowBattle = false;
    }

    public void setAllowBattle(boolean allowBattle) {
        this.allowBattle = allowBattle;
    }
}
