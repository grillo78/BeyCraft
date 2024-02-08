package com.beycraft.common.item;

import com.beycraft.BeyTypes;
import com.beycraft.Beycraft;
import com.beycraft.common.ability.Ability;
import com.beycraft.common.ability.AbilityType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.List;

public abstract class BeyPartItem extends Item {

    private static final List<BeyPartItem> PARTS = new ArrayList<>();

    
    private Ability primaryAbility;
    private Ability secondaryAbility;
    private BeyTypes type;
    private String name;
    private String displayName;

    public static List<BeyPartItem> getParts() {
        return PARTS;
    }

    public BeyPartItem(String name, String displayName, BeyTypes type, AbilityType primaryAbility, AbilityType secondaryAbility, ItemGroup tab, Properties properties) {
        super(properties.tab(tab).stacksTo(1));
        this.primaryAbility = primaryAbility.getAbility();
        this.secondaryAbility = secondaryAbility.getAbility();
        this.type = type;
        this.name = name;
        this.displayName = displayName;
        PARTS.add(this);
    }

    public String getName() {
        return name;
    }

    public Ability getPrimaryAbility() {
        return primaryAbility;
    }

    public Ability getSecondaryAbility() {
        return secondaryAbility;
    }

    @Override
    public StringTextComponent getName(ItemStack stack) {
        return new StringTextComponent(displayName);
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean canDropOnBurst() {
        return true;
    }

    public boolean canBeCrafted(Item item) {
        return item == ModItems.PLASTIC;
    }

    public ResourceLocation getBookCategory() {
        return new ResourceLocation(Beycraft.MOD_ID, "layers");
    }

    public JsonElement getEntryJson() {

        JsonObject jsonObject = new JsonObject();

        jsonObject.add("name", new JsonPrimitive(name));
        jsonObject.add("icon", new JsonPrimitive(getRegistryName().toString()));
        jsonObject.add("category", new JsonPrimitive(getBookCategory().toString()));

        JsonArray pages = new JsonArray();

        JsonObject itemPage = new JsonObject();
        itemPage.add("type", new JsonPrimitive(Beycraft.MOD_ID + ":big_item"));
        itemPage.add("item", new JsonPrimitive(getRegistryName().toString()));
        itemPage.add("title", new JsonPrimitive(name));
        pages.add(itemPage);

        JsonObject secondPage = getSecondPage();

        if (secondPage != null) pages.add(secondPage);

        jsonObject.add("pages", pages);

        return jsonObject;
    }

    protected JsonObject getSecondPage() {
        JsonObject itemPage = new JsonObject();
        itemPage.add("type", new JsonPrimitive(Beycraft.MOD_ID + ":part_info"));
        itemPage.add("type_image", new JsonPrimitive(Beycraft.MOD_ID + ":textures/gui/book/types/" + (type != null? type.getName() : "not_set") + ".png"));
        itemPage.add("test_text", new JsonPrimitive("test"));
        return itemPage;
    }
}
