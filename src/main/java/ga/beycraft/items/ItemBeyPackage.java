package ga.beycraft.items;

import ga.beycraft.BeyCraft;
import ga.beycraft.BeyCraftRegistry;
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
		BeyCraftRegistry.ITEMS.put(name,this);
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (!worldIn.isClientSide) {
			Random random = new Random();
			int randomNumber = random.nextInt(BeyCraftRegistry.ITEMSLAYER.size());
			if (randomNumber != 0) {
				ItemEntity itemLayer = new ItemEntity(worldIn, playerIn.getX(),
						playerIn.getY(), playerIn.getZ(),
						new ItemStack(BeyCraftRegistry.ITEMSLAYER.get(randomNumber - 1), 1));
				worldIn.addFreshEntity(itemLayer);
				if(BeyCraftRegistry.ITEMSLAYER.get(randomNumber-1) instanceof ItemBeyLayerGT){
					randomNumber = random.nextInt(BeyCraftRegistry.ITEMSGTWEIGHT.size());
					if(randomNumber != 0){
						ItemEntity itemChip = new ItemEntity(worldIn, playerIn.getX(),
								playerIn.getY(), playerIn.getZ(),
								new ItemStack(BeyCraftRegistry.ITEMSGTWEIGHT.get(randomNumber - 1), 1));
						worldIn.addFreshEntity(itemChip);
					} else{
						ItemEntity itemChip = new ItemEntity(worldIn, playerIn.getX(),
								playerIn.getY(), playerIn.getZ(),
								new ItemStack(BeyCraftRegistry.ITEMSGTWEIGHT.get(randomNumber), 1));
						worldIn.addFreshEntity(itemChip);
					}
					randomNumber = random.nextInt(BeyCraftRegistry.ITEMSGTCHIP.size());
					if(randomNumber != 0){
						ItemEntity itemChip = new ItemEntity(worldIn, playerIn.getX(),
								playerIn.getY(), playerIn.getZ(),
								new ItemStack(BeyCraftRegistry.ITEMSGTCHIP.get(randomNumber - 1), 1));
						worldIn.addFreshEntity(itemChip);
					} else{
						ItemEntity itemChip = new ItemEntity(worldIn, playerIn.getX(),
								playerIn.getY(), playerIn.getZ(),
								new ItemStack(BeyCraftRegistry.ITEMSGTCHIP.get(randomNumber), 1));
						worldIn.addFreshEntity(itemChip);
					}
				}
			} else {
				ItemEntity itemLayer = new ItemEntity(worldIn, playerIn.getX(),
						playerIn.getY(), playerIn.getZ(),
						new ItemStack(BeyCraftRegistry.ITEMSLAYER.get(randomNumber), 1));
				worldIn.addFreshEntity(itemLayer);
				if(BeyCraftRegistry.ITEMSLAYER.get(randomNumber) instanceof ItemBeyLayerGT){
					randomNumber = random.nextInt(BeyCraftRegistry.ITEMSGTCHIP.size());
					if(randomNumber != 0){
						ItemEntity itemChip = new ItemEntity(worldIn, playerIn.getX(),
								playerIn.getY(), playerIn.getZ(),
								new ItemStack(BeyCraftRegistry.ITEMSGTCHIP.get(randomNumber - 1), 1));
						worldIn.addFreshEntity(itemChip);
					} else{
						ItemEntity itemChip = new ItemEntity(worldIn, playerIn.getX(),
								playerIn.getY(), playerIn.getZ(),
								new ItemStack(BeyCraftRegistry.ITEMSGTCHIP.get(randomNumber), 1));
						worldIn.addFreshEntity(itemChip);
					}
				}
			}
			randomNumber = random.nextInt(BeyCraftRegistry.ITEMSDISCLIST.size());
			if (randomNumber != 0) {
				ItemEntity itemDisk = new ItemEntity(worldIn, playerIn.getX(),
						playerIn.getY(), playerIn.getZ(),
						new ItemStack(BeyCraftRegistry.ITEMSDISCLIST.get(randomNumber - 1), 1));
				worldIn.addFreshEntity(itemDisk);
				if(BeyCraftRegistry.ITEMSDISCLIST.get(randomNumber-1) instanceof ItemBeyDiscFrame && random.nextInt(2) == 1){
					randomNumber = random.nextInt(BeyCraftRegistry.ITEMSFRAME.size());
					if(randomNumber != 0) {
						ItemEntity itemFrame = new ItemEntity(worldIn, playerIn.getX(),
								playerIn.getY(), playerIn.getZ(),
								new ItemStack(BeyCraftRegistry.ITEMSFRAME.get(randomNumber - 1), 1));
						worldIn.addFreshEntity(itemFrame);
					}else{
						ItemEntity itemFrame = new ItemEntity(worldIn, playerIn.getX(),
								playerIn.getY(), playerIn.getZ(),
								new ItemStack(BeyCraftRegistry.ITEMSFRAME.get(randomNumber), 1));
						worldIn.addFreshEntity(itemFrame);
					}
				}
			} else {
				ItemEntity itemDisk = new ItemEntity(worldIn, playerIn.getX(),
						playerIn.getY(), playerIn.getZ(),
						new ItemStack(BeyCraftRegistry.ITEMSDISCLIST.get(randomNumber), 1));
				worldIn.addFreshEntity(itemDisk);
				if(BeyCraftRegistry.ITEMSDISCLIST.get(randomNumber) instanceof ItemBeyDiscFrame && random.nextInt(2) == 1){
					randomNumber = random.nextInt(BeyCraftRegistry.ITEMSFRAME.size());
					if(randomNumber != 0) {
						ItemEntity itemFrame = new ItemEntity(worldIn, playerIn.getX(),
								playerIn.getY(), playerIn.getZ(),
								new ItemStack(BeyCraftRegistry.ITEMSFRAME.get(randomNumber - 1), 1));
						worldIn.addFreshEntity(itemFrame);
					}else{
						ItemEntity itemFrame = new ItemEntity(worldIn, playerIn.getX(),
								playerIn.getY(), playerIn.getZ(),
								new ItemStack(BeyCraftRegistry.ITEMSFRAME.get(randomNumber), 1));
						worldIn.addFreshEntity(itemFrame);
					}
				}
			}
			randomNumber = random.nextInt(BeyCraftRegistry.ITEMSDRIVER.size());
			if (randomNumber != 0) {
				ItemEntity itemDriver = new ItemEntity(worldIn, playerIn.getX(),
						playerIn.getY(), playerIn.getZ(),
						new ItemStack(BeyCraftRegistry.ITEMSDRIVER.get(randomNumber - 1), 1));
				worldIn.addFreshEntity(itemDriver);
			} else {
				ItemEntity itemDriver = new ItemEntity(worldIn, playerIn.getX(),
						playerIn.getY(), playerIn.getZ(),
						new ItemStack(BeyCraftRegistry.ITEMSDRIVER.get(randomNumber), 1));
				worldIn.addFreshEntity(itemDriver);
			}
			playerIn.getItemInHand(handIn).shrink(1);
		}
		return super.use(worldIn, playerIn, handIn);
	}


}
