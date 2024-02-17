package tfc.verticala.mixin.interaction;

import net.minecraft.core.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = Entity.class, remap = false)
public class EntityMixin {
	@ModifyConstant(method = "baseTick", constant = @Constant(doubleValue = -64.0))
	public double switchThreshold(double constant) {
		return -32000000;
	}
}
