package com.beycraft.common.launch;

import com.beycraft.common.entity.BeybladeEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class HandSpinningLaunch extends Launch {

    @Override
    public BeybladeEntity launchBeyblade(ItemStack beyblade, World level, ServerPlayerEntity player, Hand hand) {
        BeybladeEntity beybladeEntity = super.launchBeyblade(beyblade, level, player, hand);
        beybladeEntity.setEnergy(beybladeEntity.getEnergy()/10);
        return beybladeEntity;
    }
}
