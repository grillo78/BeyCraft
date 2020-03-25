package com.grillo78.beycraft.entity;

import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.blocks.StadiumBlock;
import com.grillo78.beycraft.items.ItemBeyDriver;

import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityGoalRotate extends Goal {

    private EntityBey bey;

    public EntityGoalRotate(EntityBey bey) {
        this.bey = bey;
    }

    @Override
    public boolean shouldExecute() {
        return true;
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (!bey.isStoped()) {
            if (bey.onGround) {
                if (bey.getRadius() != 0) {
                    if (!bey.isStoped() && bey.onGround) {
                        bey.rotationYaw += bey.getRotationSpeed() * bey.getRotationDirection() * 2
                                / (-bey.getMaxRotationSpeed() * 0.1);
                    }
                    bey.move(MoverType.SELF, new Vec3d(bey.getLookVec().x * bey.getRadius() / 2.1, 0,
                            bey.getLookVec().z * bey.getRadius() / 2.1));
                }
                if (bey.world.getBlockState(new BlockPos(bey.getPositionVector().x + 0.23,
                        bey.getPositionVector().y, bey.getPositionVector().z)).getBlock() != BeyRegistry.STADIUM) {
                    bey.rotationYaw = 90;
                }
                if (bey.world.getBlockState(new BlockPos(bey.getPositionVector().x - 0.23,
                        bey.getPositionVector().y, bey.getPositionVector().z)).getBlock() != BeyRegistry.STADIUM) {
                    bey.rotationYaw = -90;
                }
                if (bey.world.getBlockState(new BlockPos(bey.getPositionVector().x, bey.getPositionVector().y,
                        bey.getPositionVector().z + 0.23)).getBlock() != BeyRegistry.STADIUM) {
                    bey.rotationYaw = 180;
                }
                if (bey.world.getBlockState(new BlockPos(bey.getPositionVector().x, bey.getPositionVector().y,
                        bey.getPositionVector().z - 0.23)).getBlock() != BeyRegistry.STADIUM) {
                    bey.rotationYaw = 0;
                }
                if (!bey.isStoped() && bey.getRadius() == 0 && bey.world
                        .getBlockState(new BlockPos(bey.getPositionVector().x,
                                bey.getPositionVector().y + 1 - 0.0625 * 7, bey.getPositionVector().z))
                        .getBlock() instanceof StadiumBlock) {
                    switch (bey.world.getBlockState(bey.getPosition()).get(StadiumBlock.PART).getID()) {
                        case 0:
                            bey.move(MoverType.SELF, new Vec3d(0.1, 0, 0.1));
                            break;
                        case 1:
                            bey.move(MoverType.SELF, new Vec3d(-0.1, 0, 0));
                            break;
                        case 2:
                            bey.move(MoverType.SELF, new Vec3d(0.1, 0, -0.1));
                            break;
                        case 3:
                            bey.move(MoverType.SELF, new Vec3d(0, 0, 0.1));
                            break;
                        case 4:
                            bey.move(MoverType.SELF,
                                    new Vec3d((bey.getPosition().getX() + 0.5 - bey.getPositionVector().x) / 4, 0,
                                            (bey.getPosition().getZ() + 0.5 - bey.getPositionVector().z) / 4));
                            break;
                        case 5:
                            bey.move(MoverType.SELF, new Vec3d(0, 0, -0.1));
                            break;
                        case 6:
                            bey.move(MoverType.SELF, new Vec3d(-0.1, 0, 0.1));
                            break;
                        case 7:
                            bey.move(MoverType.SELF, new Vec3d(0.1, 0, 0));
                            break;
                        case 8:
                            bey.move(MoverType.SELF, new Vec3d(-0.1, 0, -0.1));
                            break;
                    }
                }
                if (bey.getRadius() > 0 && !bey.isIncreaseRadius()) {
                    bey.setRadius(bey.getRadius()
                            - (0.004f * bey.getMaxRotationSpeed() / 10) * ((ItemBeyDriver) bey.getInventory().getStackInSlot(2).getItem()).getRadiusReduction()
                            * bey.getRotationSpeed() / (bey.getMaxRotationSpeed()));
                } else {
                    if (bey.isIncreaseRadius()) {
                        bey.setRadius(bey.getRadius() + 0.01f
                                * ((ItemBeyDriver) bey.getInventory().getStackInSlot(2).getItem()).getRadiusReduction()
                                * bey.getRotationSpeed() / (bey.getMaxRotationSpeed()));
                        if (bey.getRadius() >= 1.6F * bey.getRotationSpeed() / 10) {
                            bey.setIncreaseRadius(false);
                        }
                    } else {
                        bey.setRadius(0);
                    }
                }
            }
        }
        return !bey.isStoped();
    }

}
