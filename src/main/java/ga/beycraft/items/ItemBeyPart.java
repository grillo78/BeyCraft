package ga.beycraft.items;

import com.google.common.collect.Lists;
import ga.beycraft.Reference;
import ga.beycraft.abilities.Ability;
import ga.beycraft.abilities.MultiMode;
import ga.beycraft.abilities.MultiType;
import ga.beycraft.util.BeyTypes;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ItemBeyPart extends Item {

    private final static ArrayList<String> PART_NAMES = Lists.newArrayList("chip", "weight", "disc", "frame", "driver");
    protected BeyTypes type;
    protected final String name;
    protected final Ability PRIMARYABILITY;
    protected final Ability SECUNDARYABILITY;

    public ItemBeyPart(String name, BeyTypes type, Ability primaryAbility, Ability secundaryAbility, ItemGroup tab,
                       Item.Properties properties) {
        super(properties.tab(tab).stacksTo(1));
        PRIMARYABILITY = primaryAbility;
        SECUNDARYABILITY = secundaryAbility;
        this.type = type;
        this.name = name;
        setRegistryName(new ResourceLocation(Reference.MOD_ID, name.replaceAll(" ", "").replace("+", "plus").toLowerCase()));
    }

    public Ability getPrimaryAbility() {
        return PRIMARYABILITY;
    }

    public Ability getSecundaryAbility() {
        return SECUNDARYABILITY;
    }

    public BeyTypes getType(ItemStack stack) {
        if (PRIMARYABILITY instanceof MultiType) {
            if (stack.hasTag() && stack.getTag().contains("Type")) {
                return ((MultiType) PRIMARYABILITY).getTypeHashMap().get(stack.getTag().getString("Type")).getType();
            }
            return ((MultiType) PRIMARYABILITY).getTypes().get(0).getType();
        }
        if (SECUNDARYABILITY instanceof MultiType) {
            if (stack.hasTag() && stack.getTag().contains("Type")) {
                return ((MultiType) SECUNDARYABILITY).getTypeHashMap().get(stack.getTag().getString("Type")).getType();
            }
            return ((MultiType) SECUNDARYABILITY).getTypes().get(0).getType();
        }
        return type;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand handIn) {
        if (player.isCrouching() && (PRIMARYABILITY instanceof MultiType || SECUNDARYABILITY instanceof MultiType)) {
            if (PRIMARYABILITY instanceof MultiType) {
                PRIMARYABILITY.executeAbility(player.getItemInHand(handIn));
            } else {
                SECUNDARYABILITY.executeAbility(player.getItemInHand(handIn));
            }
            return super.use(world, player, handIn);
        }
        return ActionResult.fail(player.getItemInHand(handIn));
    }

    @Override
    public ITextComponent getName(ItemStack stack) {
        AtomicReference<String> text = new AtomicReference<>(name);
        if (stack.hasTag()) {
            PART_NAMES.forEach(s -> {
                if (stack.getTag().contains(s)) {
                    ItemStack itemStack = ItemStack.of(stack.getTag().getCompound(s));
                    if(itemStack.getItem() instanceof ItemBeyPart)
                        text.set(text.get() + " " + itemStack.getHoverName().getString());
                }
            });
        }

        return new StringTextComponent(text.get());
    }

    @Override
    public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        BeyTypes type = getType(stack);
        if (type != null) tooltip.add(new TranslationTextComponent(type.getName()));
        if (getPrimaryAbility() instanceof MultiMode) {
            if (stack.hasTag() && stack.getTag().contains("mode")) {
                tooltip.add(new StringTextComponent(stack.getTag().getString("mode")));
            } else {
                tooltip.add(new StringTextComponent(((MultiMode) getPrimaryAbility()).getModes().get(0).getName()));
            }
        }
        if (getSecundaryAbility() instanceof MultiMode) {
            if (stack.hasTag() && stack.getTag().contains("mode")) {
                tooltip.add(new StringTextComponent(stack.getTag().getString("mode")));
            } else {
                tooltip.add(new StringTextComponent(((MultiMode) getSecundaryAbility()).getModes().get(0).getName()));
            }
        }
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
}
