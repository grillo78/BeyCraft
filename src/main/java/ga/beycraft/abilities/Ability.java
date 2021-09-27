package ga.beycraft.abilities;

import ga.beycraft.entity.EntityBey;
import net.minecraft.item.ItemStack;

/**
 * @author a19guillermong
 *
 */
public abstract class Ability {

	
	public abstract void executeAbility(EntityBey entity);

	public abstract void executeAbility(ItemStack piece);
}
