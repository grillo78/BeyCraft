package com.beycraft.common.block_entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.HashMap;
import java.util.UUID;

public class BattleInformerTileEntity extends TileEntity {
    private HashMap<PlayerEntity, Integer> points = new HashMap<>();

    public BattleInformerTileEntity() {
        super(ModTileEntities.BATTLE_INFORMER);
    }

    public HashMap<PlayerEntity, Integer> getPoints() {
        return points;
    }

    public void setPoints(HashMap<PlayerEntity, Integer> points) {
        this.points = points;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getBlockPos().getX(),getBlockPos().getY(),getBlockPos().getZ(),getBlockPos().getX()+1.1,getBlockPos().getY()+10,getBlockPos().getZ()+1.1);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 1, this.writeNetwork());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.readNetwork(pkt.getTag());
    }

    public CompoundNBT serializePoints() {
        CompoundNBT points = new CompoundNBT();
        this.points.forEach((playerEntity, pointsValue) -> points.putInt(String.valueOf(playerEntity.getUUID()), pointsValue));
        return points;
    }

    public CompoundNBT writeNetwork() {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.put("points", serializePoints());
        return compoundNBT;
    }

    public void readNetwork(CompoundNBT nbt) {
        this.points.clear();
        CompoundNBT points = nbt.getCompound("points");
        for (String id : points.getAllKeys()) {
            this.points.put(level.getPlayerByUUID(UUID.fromString(id)),points.getInt(id));
        }
    }
}
