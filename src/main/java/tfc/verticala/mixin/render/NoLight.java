package tfc.verticala.mixin.render;

import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.LightUpdate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = LightUpdate.class, remap = false)
public class NoLight {
	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public void performLightUpdate(World world) {
	}
}
