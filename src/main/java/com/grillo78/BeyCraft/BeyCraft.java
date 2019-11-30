package com.grillo78.BeyCraft;

import java.util.Random;

import org.apache.logging.log4j.Logger;

import com.grillo78.BeyCraft.network.BladerLevelMessage;
import com.grillo78.BeyCraft.programs.BeyRanking;
import com.grillo78.BeyCraft.proxy.CommonProxy;
import com.grillo78.BeyCraft.tab.BeyCraftTab;
import com.grillo78.BeyCraft.tileentity.ExpositoryTileEntity;
import com.grillo78.BeyCraft.util.DatabaseConnection;
import com.grillo78.BeyCraft.util.SoundHandler;
import com.mrcrayfish.device.api.ApplicationManager;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraft.entity.passive.EntityVillager.PriceInfo;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class BeyCraft
{

	public static VillagerProfession BEYBLADE_SELLER;
	public static VillagerCareer BEY_SELLER;
	
    public static Logger logger;
    public static DatabaseConnection dbConn;
    public static final CreativeTabs beyCraftTab = new BeyCraftTab("BeyCraft");
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID);

    @Instance(Reference.MODID)
    public static BeyCraft instance;
    
    @SidedProxy(clientSide = "com.grillo78.BeyCraft.proxy.ClientProxy", serverSide = "com.grillo78.BeyCraft.proxy.CommonProxy")
    public static CommonProxy proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        dbConn = new DatabaseConnection();
        GameRegistry.registerTileEntity(ExpositoryTileEntity.class, new ResourceLocation(Reference.MODID,"tileEntity3DPrinter"));
		proxy.onPreInit();
        proxy.registerRenders();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	
    	BEYBLADE_SELLER= new VillagerRegistry.VillagerProfession(Reference.MODID+":bey_seller",
                Reference.MODID+":textures/entity/villager/bey_seller.png", 
                Reference.MODID+":textures/entity/villager/bey_seller.png"
                );
      	IForgeRegistry<VillagerRegistry.VillagerProfession> villagerProfessions = ForgeRegistries.VILLAGER_PROFESSIONS;
  		villagerProfessions.register(BEYBLADE_SELLER);
  		BEY_SELLER = (new VillagerRegistry.VillagerCareer(BEYBLADE_SELLER, "cloud_enchanter"));
  		BEY_SELLER.addTrade(1, new TradeEmeraldsForPackage());
  		BEY_SELLER.addTrade(1, new TradeEmeraldsForHandle());
  		BEY_SELLER.addTrade(1, new TradeEmeraldsForRedLauncher());
    	SoundHandler.init();
    	logger.info(isDeviceModInstalled());
    	if (isDeviceModInstalled()) {
    		registerApplication();
    	}
        INSTANCE.registerMessage(BladerLevelMessage.class, BladerLevelMessage.class, 0, Side.CLIENT);
    }
    
    
    
    public boolean isDeviceModInstalled() {
    	try {
    		Class.forName("com.mrcrayfish.device.MrCrayfishDeviceMod");
    		return true;
    	} catch (Exception e) {
			return false;
		}
    }
    
    public void registerApplication(){
    	ApplicationManager.registerApplication(new ResourceLocation(Reference.MODID, "beyranking"), BeyRanking.class);
    }
    public static class TradeEmeraldsForPackage implements ITradeList{
    	
    	public ItemStack stack;
    	public EntityVillager.PriceInfo priceInfo;
    	
    	public TradeEmeraldsForPackage() {
			stack = new ItemStack(BeyRegistry.BEYPACKAGE);
			priceInfo = new PriceInfo(10, 30);
		}
    	
    	@Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
        {
            int actualPrice = 1;

            if (priceInfo != null)
            {
                actualPrice = priceInfo.getPrice(random);
            }

            ItemStack stackToPay = new ItemStack(Items.EMERALD, actualPrice, 0);
            recipeList.add(new MerchantRecipe(stackToPay, stack));
            
            // DEBUG
            System.out.println("Merchant recipe list = "+recipeList.getRecipiesAsTags());
        }
    	
    }
public static class TradeEmeraldsForHandle implements ITradeList{
    	
    	public ItemStack stack;
    	public EntityVillager.PriceInfo priceInfo;
    	
    	public TradeEmeraldsForHandle() {
			stack = new ItemStack(BeyRegistry.LAUNCHERHANDLE);
			priceInfo = new PriceInfo(10, 30);
		}
    	
    	@Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
        {
            int actualPrice = 1;

            if (priceInfo != null)
            {
                actualPrice = priceInfo.getPrice(random);
            }

            ItemStack stackToPay = new ItemStack(Items.EMERALD, actualPrice, 0);
            recipeList.add(new MerchantRecipe(stackToPay, stack));
            
            // DEBUG
            System.out.println("Merchant recipe list = "+recipeList.getRecipiesAsTags());
        }
    	
    }
public static class TradeEmeraldsForRedLauncher implements ITradeList{
	
	public ItemStack stack;
	public EntityVillager.PriceInfo priceInfo;
	
	public TradeEmeraldsForRedLauncher() {
		stack = new ItemStack(BeyRegistry.REDLAUNCHER);
		priceInfo = new PriceInfo(10, 30);
	}
	
	@Override
    public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
    {
        int actualPrice = 1;

        if (priceInfo != null)
        {
            actualPrice = priceInfo.getPrice(random);
        }

        ItemStack stackToPay = new ItemStack(Items.EMERALD, actualPrice, 0);
        recipeList.add(new MerchantRecipe(stackToPay, stack));
        
        // DEBUG
        System.out.println("Merchant recipe list = "+recipeList.getRecipiesAsTags());
    }
	
}
}