package com.grillo78.BeyCraft;

import org.apache.logging.log4j.Logger;

import com.grillo78.BeyCraft.proxy.CommonProxy;
import com.grillo78.BeyCraft.tab.BeyCraftTab;
import com.grillo78.BeyCraft.util.SoundHandler;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class BeyCraft
{

    public static Logger logger;
    public static final CreativeTabs beyCraftTab = new BeyCraftTab("BeyCraft");

    @Instance(Reference.MODID)
    public static BeyCraft instance;
    
    @SidedProxy(clientSide = "com.grillo78.BeyCraft.proxy.ClientProxy", serverSide = "com.grillo78.BeyCraft.proxy.CommonProxy")
    public static CommonProxy proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        proxy.onPreInit();
        proxy.registerRenders();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	SoundHandler.init();
    }
}
