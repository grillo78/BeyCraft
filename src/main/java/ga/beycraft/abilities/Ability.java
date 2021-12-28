package ga.beycraft.abilities;

import ga.beycraft.entity.EntityBey;
import net.minecraft.item.ItemStack;

import java.util.Random;

/**
 * @author a19guillermong
 *
 */
public abstract class Ability {

	protected Random random = new Random();
	
	public abstract void executeAbility(EntityBey entity);

	public abstract void executeAbility(ItemStack piece);
}
