package ga.beycraft.items;

import ga.beycraft.BeyCraft;
import ga.beycraft.BeyRegistry;
import ga.beycraft.Reference;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Random;

public class ItemBeyPackage extends Item {
	public ItemBeyPackage(String name) {
		super(new Item.Properties().tab(BeyCraft.BEYCRAFTTAB).stacksTo(1));
		this.setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
		BeyRegistry.ITEMS.put(name,this);
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (!worldIn.isClientSide) {
			Random random = new Random();
			int randomNumber = random.nextInt(BeyRegistry.ITEMSLAYER.size());
			if (randomNumber != 0) {
				ItemEntity itemLayer = new ItemEntity(worldIn, playerIn.getX(),
						playerIn.getY(), playerIn.getZ(),
						new ItemStack(BeyRegistry.ITEMSLAYER.get(randomNumber - 1), 1));
				worldIn.addFreshEntity(itemLayer);
				if(BeyRegistry.ITEMSLAYER.get(randomNumber-1) instanceof ItemBeyLayerGT){
					randomNumber = random.nextInt(BeyRegistry.ITEMSGTWEIGHT.size());
					if(randomNumber != 0){
						ItemEntity itemChip = new ItemEntity(worldIn, playerIn.getX(),
								playerIn.getY(), playerIn.getZ(),
								new ItemStack(BeyRegistry.ITEMSGTWEIGHT.get(randomNumber - 1), 1));
						worldIn.addFreshEntity(itemChip);
					} else{
						ItemEntity itemChip = new ItemEntity(worldIn, playerIn.getX(),
								playerIn.getY(), playerIn.getZ(),
								new ItemStack(BeyRegistry.ITEMSGTWEIGHT.get(randomNumber), 1));
						worldIn.addFreshEntity(itemChip);
					}
					randomNumber = random.nextInt(BeyRegistry.ITEMSGTCHIP.size());
					if(randomNumber != 0){
						ItemEntity itemChip = new ItemEntity(worldIn, playerIn.getX(),
								playerIn.getY(), playerIn.getZ(),
								new ItemStack(BeyRegistry.ITEMSGTCHIP.get(randomNumber - 1), 1));
						worldIn.addFreshEntity(itemChip);
					} else{
						ItemEntity itemChip = new ItemEntity(worldIn, playerIn.getX(),
								playerIn.getY(), playerIn.getZ(),
								new ItemStack(BeyRegistry.ITEMSGTCHIP.get(randomNumber), 1));
						worldIn.addFreshEntity(itemChip);
					}
				}
			} else {
				ItemEntity itemLayer = new ItemEntity(worldIn, playerIn.getX(),
						playerIn.getY(), playerIn.getZ(),
						new ItemStack(BeyRegistry.ITEMSLAYER.get(randomNumber), 1));
				worldIn.addFreshEntity(itemLayer);
				if(BeyRegistry.ITEMSLAYER.get(randomNumber) instanceof ItemBeyLayerGT){
					randomNumber = random.nextInt(BeyRegistry.ITEMSGTCHIP.size());
					if(randomNumber != 0){
						ItemEntity itemChip = new ItemEntity(worldIn, playerIn.getX(),
								playerIn.getY(), playerIn.getZ(),
								new ItemStack(BeyRegistry.ITEMSGTCHIP.get(randomNumber - 1), 1));
						worldIn.addFreshEntity(itemChip);
					} else{
						ItemEntity itemChip = new ItemEntity(worldIn, playerIn.getX(),
								playerIn.getY(), playerIn.getZ(),
								new ItemStack(BeyRegistry.ITEMSGTCHIP.get(randomNumber), 1));
						worldIn.addFreshEntity(itemChip);
					}
				}
			}
			randomNumber = random.nextInt(BeyRegistry.ITEMSDISCLIST.size());
			if (randomNumber != 0) {
				ItemEntity itemDisk = new ItemEntity(worldIn, playerIn.getX(),
						playerIn.getY(), playerIn.getZ(),
						new ItemStack(BeyRegistry.ITEMSDISCLIST.get(randomNumber - 1), 1));
				worldIn.addFreshEntity(itemDisk);
				if(BeyRegistry.ITEMSDISCLIST.get(randomNumber-1) instanceof ItemBeyDiscFrame && random.nextInt(2) == 1){
					randomNumber = random.nextInt(BeyRegistry.ITEMSFRAME.size());
					if(randomNumber != 0) {
						ItemEntity itemFrame = new ItemEntity(worldIn, playerIn.getX(),
								playerIn.getY(), playerIn.getZ(),
								new ItemStack(BeyRegistry.ITEMSFRAME.get(randomNumber - 1), 1));
						worldIn.addFreshEntity(itemFrame);
					}else{
						ItemEntity itemFrame = new ItemEntity(worldIn, playerIn.getX(),
								playerIn.getY(), playerIn.getZ(),
								new ItemStack(BeyRegistry.ITEMSFRAME.get(randomNumber), 1));
						worldIn.addFreshEntity(itemFrame);
					}
				}
			} else {
				ItemEntity itemDisk = new ItemEntity(worldIn, playerIn.getX(),
						playerIn.getY(), playerIn.getZ(),
						new ItemStack(BeyRegistry.ITEMSDISCLIST.get(randomNumber), 1));
				worldIn.addFreshEntity(itemDisk);
				if(BeyRegistry.ITEMSDISCLIST.get(randomNumber) instanceof ItemBeyDiscFrame && random.nextInt(2) == 1){
					randomNumber = random.nextInt(BeyRegistry.ITEMSFRAME.size());
					if(randomNumber != 0) {
						ItemEntity itemFrame = new ItemEntity(worldIn, playerIn.getX(),
								playerIn.getY(), playerIn.getZ(),
								new ItemStack(BeyRegistry.ITEMSFRAME.get(randomNumber - 1), 1));
						worldIn.addFreshEntity(itemFrame);
					}else{
						ItemEntity itemFrame = new ItemEntity(worldIn, playerIn.getX(),
								playerIn.getY(), playerIn.getZ(),
								new ItemStack(BeyRegistry.ITEMSFRAME.get(randomNumber), 1));
						worldIn.addFreshEntity(itemFrame);
					}
				}
			}
			randomNumber = random.nextInt(BeyRegistry.ITEMSDRIVER.size());
			if (randomNumber != 0) {
				ItemEntity itemDriver = new ItemEntity(worldIn, playerIn.getX(),
						playerIn.getY(), playerIn.getZ(),
						new ItemStack(BeyRegistry.ITEMSDRIVER.get(randomNumber - 1), 1));
				worldIn.addFreshEntity(itemDriver);
			} else {
				ItemEntity itemDriver = new ItemEntity(worldIn, playerIn.getX(),
						playerIn.getY(), playerIn.getZ(),
						new ItemStack(BeyRegistry.ITEMSDRIVER.get(randomNumber), 1));
				worldIn.addFreshEntity(itemDriver);
			}
			playerIn.getItemInHand(handIn).shrink(1);
		}
		return super.use(worldIn, playerIn, handIn);
	}


}
