package ga.beycraft.common.item;

import friedrichlp.renderlib.RenderLibSettings;
import friedrichlp.renderlib.library.RenderEffect;
import friedrichlp.renderlib.model.ModelLoaderProperty;
import friedrichlp.renderlib.tracking.ModelInfo;
import friedrichlp.renderlib.tracking.ModelManager;
import ga.beycraft.BeyTypes;
import ga.beycraft.Beycraft;
import ga.beycraft.ability.AbilityType;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Properties;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Beycraft.MOD_ID);
    public static final Item LAYERICON = register("layericon", new Item(new Item.Properties()));
    public static final Item DISCICON = register("discicon", new Item(new Item.Properties()));
    public static final Item DRIVERICON = register("drivericon", new Item(new Item.Properties()));

    static {
        ItemCreator.getItemsFromFolder();
    }

    private static <T extends Item> T register(String name, T item) {
        ITEMS.register(name, () -> item);
        return item;
    }

    public static class ItemCreator {
        public static HashMap<Item, ModelInfo> models = new HashMap();


        public static void getItemsFromFolder() {
            File itemsFolder = new File("BeyParts");
            File[] propertiesFiles = itemsFolder.listFiles((dir, name) -> name.endsWith(".properties"));
            Beycraft.LOGGER.info(propertiesFiles.length + " parts was found in BeyParts folder");

            try {
                for (File file : propertiesFiles) {
                    if (file.getName().endsWith(".properties")) {
                        InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
                        Properties properties = new Properties();
                        properties.load(reader);
                        BeyPartItem item = null;
                        float[] rgb = {1, 1, 1};
                        if (properties.containsKey("resonanceColor")) {
                            String[] rgbStr = properties.getProperty("resonanceColor").split(",");
                            for (int i = 0; i < rgb.length; i++) {
                                rgb[i] = Float.parseFloat(rgbStr[i]);
                            }
                        }
                        float[] rgb2 = {1, 1, 1};
                        if (properties.containsKey("secondResonanceColor")) {
                            String[] rgbStr = properties.getProperty("secondResonanceColor").split(",");
                            for (int i = 0; i < rgb.length; i++) {
                                rgb2[i] = Float.parseFloat(rgbStr[i]);
                            }
                        }
                        float[] rgb3 = {1, 1, 1};
                        if (properties.containsKey("thirdResonanceColor")) {
                            String[] rgbStr = properties.getProperty("thirdResonanceColor").split(",");
                            for (int i = 0; i < rgb.length; i++) {
                                rgb3[i] = Float.parseFloat(rgbStr[i]);
                            }
                        }
                        switch ((String) properties.get("part")) {
                            case "layer":
                                item = new LayerItem(file.getName().replace(".properties", ""), properties.getProperty("name"),
                                        Integer.parseInt(properties.getProperty("rotationDirection")),
                                        Float.parseFloat(properties.getProperty("attack")),
                                        Float.parseFloat(properties.getProperty("defense")),
                                        Float.parseFloat(properties.getProperty("weight")),
                                        Float.parseFloat(properties.getProperty("burst")),
                                        getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
                                        getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
                                        properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null,
                                        new LayerItem.Color(rgb[0], rgb[1], rgb[2]),
                                        new LayerItem.Color(rgb2[0], rgb2[1], rgb2[2]),
                                        new LayerItem.Color(rgb3[0], rgb3[1], rgb3[2]));
                                break;
                            case "layerDual":
                                item = new DualLayerItem(file.getName().replace(".properties", ""), properties.getProperty("name"),
                                        Float.parseFloat(properties.getProperty("attack")),
                                        Float.parseFloat(properties.getProperty("defense")),
                                        Float.parseFloat(properties.getProperty("weight")),
                                        Float.parseFloat(properties.getProperty("burst")),
                                        getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
                                        getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
                                        properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null,
                                        new LayerItem.Color(rgb[0], rgb[1], rgb[2]),
                                        new LayerItem.Color(rgb2[0], rgb2[1], rgb2[2]),
                                        new LayerItem.Color(rgb3[0], rgb3[1], rgb3[2]));
                                break;
                            case "disc":
                                item = new DiscItem(file.getName().replace(".properties", ""), properties.getProperty("name"),
                                        Float.parseFloat(properties.getProperty("attack")),
                                        Float.parseFloat(properties.getProperty("defense")),
                                        Float.parseFloat(properties.getProperty("weight")),
                                        getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
                                        getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
                                        properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null);
                                break;
                            case "frame":
                                item = new FrameItem(file.getName().replace(".properties", ""), properties.getProperty("name"),
                                        Float.parseFloat(properties.getProperty("attack")),
                                        Float.parseFloat(properties.getProperty("defense")),
                                        properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null);
                                break;
                            case "framedisc":
                                item = new DiscFrameItem(file.getName().replace(".properties", ""), properties.getProperty("name"),
                                        Float.parseFloat(properties.getProperty("attack")),
                                        Float.parseFloat(properties.getProperty("defense")),
                                        Float.parseFloat(properties.getProperty("weight")),
                                        Float.parseFloat(properties.getProperty("frameRotation")),
                                        getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
                                        getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
                                        properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null);
                                break;
                            case "driver":
                                item = new DriverItem(file.getName().replace(".properties", ""), properties.getProperty("name"),
                                        Float.parseFloat(properties.getProperty("friction")),
                                        Float.parseFloat(properties.getProperty("radiusReduction")),
                                        getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
                                        getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
                                        properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null);
                                break;
//                            case "GTlayer":
//                                item = new LayerGTItem(file.getName().replace(".properties", ""), properties.getProperty("name"),
//                                        Integer.parseInt(properties.getProperty("rotationDirection")),
//                                        Float.parseFloat(properties.getProperty("attack")),
//                                        Float.parseFloat(properties.getProperty("defense")),
//                                        Float.parseFloat(properties.getProperty("weight")),
//                                        getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
//                                        getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
//                                        properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null);
//                                break;
//                            case "GTlayerDual":
//                                item = new LayerGTDualItem(file.getName().replace(".properties", ""), properties.getProperty("name"),
//                                        Float.parseFloat(properties.getProperty("attack")),
//                                        Float.parseFloat(properties.getProperty("defense")),
//                                        Float.parseFloat(properties.getProperty("weight")),
//                                        getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
//                                        getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
//                                        properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null);
//                                break;
//
//                            case "GTlayerNoWeight":
//                                item = new LayerGTNoWeightItem(file.getName().replace(".properties", ""), properties.getProperty("name"),
//                                        Integer.parseInt(properties.getProperty("rotationDirection")),
//                                        Float.parseFloat(properties.getProperty("attack")),
//                                        Float.parseFloat(properties.getProperty("defense")),
//                                        Float.parseFloat(properties.getProperty("weight")),
//                                        getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
//                                        getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
//                                        properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null);
//                                break;
//                            case "GTlayerDualNoWeight":
//                                item = new LayerItemGTDualNoWeight(file.getName().replace(".properties", ""), properties.getProperty("name"),
//                                        Float.parseFloat(properties.getProperty("attack")),
//                                        Float.parseFloat(properties.getProperty("defense")),
//                                        Float.parseFloat(properties.getProperty("weight")),
//                                        getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
//                                        getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
//                                        properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null);
//                                break;
//                            case "GTchip":
//                                item = new ItemBeyGTChip(file.getName().replace(".properties", ""), properties.getProperty("name"),
//                                        properties.containsKey("weight") ? Float.parseFloat(properties.getProperty("weight")) : 0,
//                                        properties.containsKey("burst") ? Float.parseFloat(properties.getProperty("burst")) : 2, new LayerItem.Color(rgb[0], rgb[1], rgb[2]),
//                                        new LayerItem.Color(rgb2[0], rgb2[1], rgb2[2]), new LayerItem.Color(rgb3[0], rgb3[1], rgb3[2]));
//                                break;
//                            case "GTchipWeight":
//                                item = new ItemBeyGTChipWeight(file.getName().replace(".properties", ""), properties.getProperty("name"),
//                                        properties.containsKey("weight") ? Float.parseFloat(properties.getProperty("weight")) : 0,
//                                        properties.containsKey("burst") ? Float.parseFloat(properties.getProperty("burst")) : 2, new LayerItem.Color(rgb[0], rgb[1], rgb[2]),
//                                        new LayerItem.Color(rgb2[0], rgb2[1], rgb2[2]), new LayerItem.Color(rgb3[0], rgb3[1], rgb3[2]));
//                                break;
//                            case "GTWeight":
//                                item = new ItemBeyGTWeight(file.getName().replace(".properties", ""), properties.getProperty("name"),
//                                        Float.valueOf(properties.getProperty("weight")));
//                                break;
                            case "FusionWheel":
                                item = new FusionWheelItem(file.getName().replace(".properties", ""), properties.getProperty("name"),
                                        Float.parseFloat(properties.getProperty("attack")),
                                        Float.parseFloat(properties.getProperty("defense")),
                                        Float.parseFloat(properties.getProperty("weight")),
                                        getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
                                        getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
                                        properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null);
                                break;
                            case "ClearWheel":
                                item = new ClearWheelItem(file.getName().replace(".properties", ""), properties.getProperty("name"),
                                        Integer.parseInt(properties.getProperty("rotationDirection")),
                                        Float.parseFloat(properties.getProperty("weight")),
                                        Float.parseFloat(properties.getProperty("burst")),
                                        getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
                                        getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
                                        properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null, new LayerItem.Color(rgb[0], rgb[1], rgb[2]),
                                        new LayerItem.Color(rgb2[0], rgb2[1], rgb2[2]), new LayerItem.Color(rgb3[0], rgb3[1], rgb3[2]));
                                break;
//                        case "GodChip":
//                            item = new ItemBeyGodChip(file.getName().replace(".properties", ""),properties.getProperty("name"),
//                                    Float.valueOf(properties.getProperty("weight")));
//                            break;
                        }
                        BeyPartItem finalItem = item;
                        register(finalItem.getName().replaceAll(" ", "").replace("+", "plus").toLowerCase(), finalItem);
                        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
//                    if (item instanceof LayerItemDual || item instanceof LayerItemGTDual || item instanceof LayerItemGTDualNoWeight) {
//                        models.put(item, ModelManager.registerModel(new File("BeyParts/models/" + file.getName().replace(".properties", "_right.obj")), new ModelLoaderProperty(0.0f)));
//                        models.put(item, ModelManager.registerModel(new File("BeyParts/models/" + file.getName().replace(".properties", "_left.obj")), new ModelLoaderProperty(0.0f)));
//
//                    } else {
                            RenderLibSettings.General.MODEL_LOAD_LIMIT++;
                            models.put(finalItem, ModelManager.registerModel(new File("BeyParts/models/" + file.getName().replace(".properties", ".obj")), new ModelLoaderProperty(0.0f)));
//                    }
                            models.get(finalItem).addRenderEffect(RenderEffect.NORMAL_LIGHTING);
                            models.get(finalItem).addRenderEffect(RenderEffect.AMBIENT_OCCLUSION);
                        });
                    }
                }
            } catch (IOException e) {

            }

            Beycraft.LOGGER.info("Items were registered");
        }

        private static AbilityType getFirstAbilityByName(String firstAbilityName, Properties properties) {
            return null;
        }

        private static AbilityType getSecondAbilityByName(String firstAbilityName, Properties properties) {
            return null;
        }
    }
}
