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
			Item layer = BeyCraftRegistry.ITEMSLAYER.get(random.nextInt(BeyCraftRegistry.ITEMSLAYER.size()));
			Item disc = BeyCraftRegistry.ITEMSDISCLIST.get(random.nextInt(BeyCraftRegistry.ITEMSDISCLIST.size()));
			Item driver = BeyCraftRegistry.ITEMSDRIVER.get(random.nextInt(BeyCraftRegistry.ITEMSDRIVER.size()));
			summonItem(worldIn,playerIn,layer);
			summonItem(worldIn,playerIn,disc);
			summonItem(worldIn,playerIn,driver);
			if(layer instanceof ItemBeyLayerGT || layer instanceof ItemBeyLayerGTNoWeight){
				Item chip = BeyCraftRegistry.ITEMSGTCHIP.get(random.nextInt(BeyCraftRegistry.ITEMSGTCHIP.size()));
				summonItem(worldIn,playerIn, chip);
				if (layer instanceof ItemBeyLayerGT){
					Item weight = BeyCraftRegistry.ITEMSGTWEIGHT.get(random.nextInt(BeyCraftRegistry.ITEMSGTWEIGHT.size()));
					summonItem(worldIn,playerIn, weight);
				}
			}
			if(layer instanceof ItemBeyLayerGod){
				Item chip = BeyCraftRegistry.ITEMSGODCHIP.get(random.nextInt(BeyCraftRegistry.ITEMSGODCHIP.size()));
				summonItem(worldIn,playerIn, chip);
			}
			if(disc instanceof ItemBeyDiscFrame){
				Item frame = BeyCraftRegistry.ITEMSFRAME.get(random.nextInt(BeyCraftRegistry.ITEMSFRAME.size()));
				summonItem(worldIn,playerIn, frame);
			}
			if(playerIn.getItemInHand(handIn).hasTag() && playerIn.getItemInHand(handIn).getTag().contains("launcher") && playerIn.getItemInHand(handIn).getTag().getBoolean("launcher")){
				if(layer instanceof ItemBeyLayerDual || layer instanceof ItemBeyLayerGTDual || layer instanceof ItemBeyLayerGTDualNoWeight){
					summonItem(worldIn, playerIn, BeyCraftRegistry.DUALLAUNCHER);
				} else {
					summonItem(worldIn, playerIn, ((ItemBeyLayer)layer).getRotationDirection() == 1? BeyCraftRegistry.LAUNCHER: BeyCraftRegistry.LEFTLAUNCHER);
				}
			}
			playerIn.getItemInHand(handIn).shrink(1);
		}
		return super.use(worldIn, playerIn, handIn);
	}

	private void summonItem(World worldIn, PlayerEntity playerIn, Item item){
		ItemEntity entity = new ItemEntity(worldIn, playerIn.getX(),
				playerIn.getY(), playerIn.getZ(),
				new ItemStack(item, 1));
		worldIn.addFreshEntity(entity);
	}

}
