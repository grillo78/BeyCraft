package grillo78.beycraft.common.item;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class PackageItem extends Item {
    private Random random = new Random();
    public PackageItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ActionResult<ItemStack> use(World pLevel, PlayerEntity pPlayer, Hand pHand) {
        ItemStack stack = pPlayer.getItemInHand(pHand);
        if(!pLevel.isClientSide){
            ItemStack partStack = new ItemStack(getRandomItem(ModItems.ItemCreator.LAYERS));
            ItemEntity itemEntity = new ItemEntity(pLevel, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),partStack);
            pLevel.addFreshEntity(itemEntity);
            if(partStack.getItem() instanceof ClearWheelItem) {
                partStack = new ItemStack(getRandomItem(ModItems.ItemCreator.FUSION_WHEELS));
                itemEntity = new ItemEntity(pLevel, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), partStack);
                pLevel.addFreshEntity(itemEntity);
            }
            partStack = new ItemStack(getRandomItem(ModItems.ItemCreator.DISCS));
            itemEntity = new ItemEntity(pLevel, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),partStack);
            pLevel.addFreshEntity(itemEntity);
            if(partStack.getItem() instanceof DiscFrameItem && random.nextBoolean()) {
                partStack = new ItemStack(getRandomItem(ModItems.ItemCreator.FRAMES));
                itemEntity = new ItemEntity(pLevel, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), partStack);
                pLevel.addFreshEntity(itemEntity);
            }
            partStack = new ItemStack(getRandomItem(ModItems.ItemCreator.DRIVERS));
            itemEntity = new ItemEntity(pLevel, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), partStack);
            pLevel.addFreshEntity(itemEntity);
            stack.shrink(1);
        }
        return ActionResult.success(stack);
    }

    private <T extends BeyPartItem> T getRandomItem(List<T> items) {
        return items.get(random.nextInt(items.size()));
    }
}
