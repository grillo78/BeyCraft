package ga.beycraft.util;

import com.google.common.collect.Lists;
import friedrichlp.renderlib.RenderLibSettings;
import friedrichlp.renderlib.library.RenderEffect;
import friedrichlp.renderlib.model.ModelLoaderProperty;
import friedrichlp.renderlib.tracking.ModelInfo;
import friedrichlp.renderlib.tracking.ModelManager;
import ga.beycraft.BeyCraft;
import ga.beycraft.abilities.*;
import ga.beycraft.items.*;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class ItemCreator {

    public static HashMap<Item, ModelInfo> models = new HashMap<>();


    public static void getItemsFromFolder() {
        File itemsFolder = new File("BeyParts");
        File[] propertiesFiles = itemsFolder.listFiles((dir, name) -> name.endsWith(".properties"));
        BeyCraft.logger.info(propertiesFiles.length + " parts was found in BeyParts folder");

        try {
            for (File file : propertiesFiles) {
                if (file.getName().endsWith(".properties")) {
                    InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
                    Properties properties = new Properties();
                    properties.load(reader);
                    Item item = null;
                    switch ((String) properties.get("part")) {
                        case "layer":
                            item = new ItemBeyLayer(file.getName().replace(".properties", ""),
                                    new Float(properties.getProperty("rotationDirection")),
                                    new Float(properties.getProperty("attack")),
                                    new Float(properties.getProperty("defense")),
                                    new Float(properties.getProperty("weight")),
                                    new Float(properties.getProperty("burst")),
                                    getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
                                    getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
                                    properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null);
                            break;
                        case "layerDual":
                            item = new ItemBeyLayerDual(file.getName().replace(".properties", ""),
                                    new Float(properties.getProperty("attack")),
                                    new Float(properties.getProperty("defense")),
                                    new Float(properties.getProperty("weight")),
                                    new Float(properties.getProperty("burst")),
                                    getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
                                    getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
                                    properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null);
                            break;
                        case "disc":
                            item = new ItemBeyDisc(file.getName().replace(".properties", ""),
                                    new Float(properties.getProperty("attack")),
                                    new Float(properties.getProperty("defense")),
                                    new Float(properties.getProperty("weight")),
                                    getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
                                    getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
                                    properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null, new Item.Properties());
                            break;
                        case "frame":
                            item = new ItemBeyFrame(file.getName().replace(".properties", ""),
                                    new Float(properties.getProperty("attack")),
                                    new Float(properties.getProperty("defense")),
                                    properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null);
                            break;
                        case "framedisc":
                            item = new ItemBeyDiscFrame(file.getName().replace(".properties", ""),
                                    new Float(properties.getProperty("attack")),
                                    new Float(properties.getProperty("defense")),
                                    new Float(properties.getProperty("weight")),
                                    new Float(properties.getProperty("frameRotation")),
                                    getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
                                    getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
                                    properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null);
                            break;
                        case "driver":
                            item = new ItemBeyDriver(file.getName().replace(".properties", ""),
                                    new Float(properties.getProperty("friction")),
                                    new Float(properties.getProperty("radiusReduction")),
                                    getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
                                    getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
                                    properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null);
                            break;
//                        case "Godlayer":
//                            item = new ItemBeyLayerGod(file.getName().replace(".properties", ""),
//                                    new Float(properties.getProperty("rotationDirection")),
//                                    new Float(properties.getProperty("attack")),
//                                    new Float(properties.getProperty("defense")),
//                                    new Float(properties.getProperty("weight")),
//                                    getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
//                                    getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
//                                    properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null);
//                            break;
                        case "GTlayer":
                            item = new ItemBeyLayerGT(file.getName().replace(".properties", ""),
                                    new Float(properties.getProperty("rotationDirection")),
                                    new Float(properties.getProperty("attack")),
                                    new Float(properties.getProperty("defense")),
                                    new Float(properties.getProperty("weight")),
                                    getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
                                    getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
                                    properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null);
                            break;
                        case "GTlayerDual":
                            item = new ItemBeyLayerGTDual(file.getName().replace(".properties", ""),
                                    new Float(properties.getProperty("attack")),
                                    new Float(properties.getProperty("defense")),
                                    new Float(properties.getProperty("weight")),
                                    getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
                                    getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
                                    properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null);
                            break;

                        case "GTlayerNoWeight":
                            item = new ItemBeyLayerGTNoWeight(file.getName().replace(".properties", ""),
                                    new Float(properties.getProperty("rotationDirection")),
                                    new Float(properties.getProperty("attack")),
                                    new Float(properties.getProperty("defense")),
                                    new Float(properties.getProperty("weight")),
                                    getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
                                    getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
                                    properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null);
                            break;
                        case "GTlayerDualNoWeight":
                            item = new ItemBeyLayerGTDualNoWeight(file.getName().replace(".properties", ""),
                                    new Float(properties.getProperty("attack")),
                                    new Float(properties.getProperty("defense")),
                                    new Float(properties.getProperty("weight")),
                                    getFirstAbilityByName(properties.getProperty("firstAbilityName"), properties),
                                    getSecondAbilityByName(properties.getProperty("secondAbilityName"), properties),
                                    properties.containsKey("type") ? BeyTypes.getByName(properties.getProperty("type")) : null);
                            break;
                        case "GTchip":
                            item = new ItemBeyGTChip(file.getName().replace(".properties", ""),
                                    properties.containsKey("weight") ? new Float(properties.getProperty("weight")) : 0,
                                    properties.containsKey("burst") ? new Float(properties.getProperty("burst")) : 2);
                            break;
                        case "GTWeight":
                            item = new ItemBeyGTWeight(file.getName().replace(".properties", ""),
                                    Float.valueOf(properties.getProperty("weight")));
                            break;
                        case "GodChip":
                            item = new ItemBeyGodChip(file.getName().replace(".properties", ""),
                                    Float.valueOf(properties.getProperty("weight")));
                            break;
                    }
                    Item finalItem = item;
                    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ()->()->{
//                    if (item instanceof ItemBeyLayerDual || item instanceof ItemBeyLayerGTDual || item instanceof ItemBeyLayerGTDualNoWeight) {
//                        models.put(item, ModelManager.registerModel(new File("BeyParts\\models\\" + file.getName().replace(".properties", "_right.obj")), new ModelLoaderProperty(0.0f)));
//                        models.put(item, ModelManager.registerModel(new File("BeyParts\\models\\" + file.getName().replace(".properties", "_left.obj")), new ModelLoaderProperty(0.0f)));
//
//                    } else {
                        RenderLibSettings.General.MODEL_LOAD_LIMIT++;
                        models.put(finalItem, ModelManager.registerModel(new File("BeyParts\\models\\" + file.getName().replace(".properties", ".obj")), new ModelLoaderProperty(0.0f)));
//                    }
                        models.get(finalItem).addRenderEffect(RenderEffect.NORMAL_LIGHTING);
                        models.get(finalItem).addRenderEffect(RenderEffect.AMBIENT_OCCLUSION);
                    });
                }
            }
        } catch (IOException e) {

        }

        BeyCraft.logger.info("Items were registered");
    }

    private static Ability getFirstAbilityByName(String name, Properties properties) {
        switch (name) {
            case "Absorb":
                return new Absorb();
            case "SpringAttack":
                return new SpringAttack();
            case "SpeedReboot":
                return new SpeedReboot();
            case "MultiType":
                return new MultiType(getTypes(properties.getProperty("types").split(" ")));
            case "MultiMode":
                return new MultiMode(getModes(properties.getProperty("modes").split(" ")));
            case "CounterStrike":
                return new CounterStrike();
            default:
                return null;
        }
    }

    private static Ability getSecondAbilityByName(String name, Properties properties) {
        switch (name) {
            case "Absorb":
                return new Absorb();
            case "SpringAttack":
                return new SpringAttack();
            case "SpeedReboot":
                return new SpeedReboot();
            case "MultiType":
                return new MultiType(getTypes(properties.getProperty("types").split(" ")));
            case "MultiMode":
                return new MultiMode(getModes(properties.getProperty("modes").split(" ")));
            default:
                return null;
        }
    }

    private static List<MultiMode.Mode> getModes(String[] list) {
        List<MultiMode.Mode> modeList = Lists.newArrayList();

        for (String mode : list) {
            modeList.add(new MultiMode.Mode(mode));
        }

        return modeList;
    }

    private static List<Type> getTypes(String[] list) {
        List<Type> typeList = Lists.newArrayList();

        for (String type : list) {
            typeList.add(new Type(type));
        }

        return typeList;
    }
}
