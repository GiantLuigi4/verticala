package tfc.verticala.mixin;

import net.minecraft.core.world.chunk.ChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import tfc.verticala.itf.SectionModifications;

@Mixin(ChunkSection.class)
public class SectionMixin implements SectionModifications {
	@Unique
	private boolean populated = false;

	@Override
	public void v_c$setPopulated() {
		this.populated = true;
	}

	@Override
	public boolean v_c$isPopulated() {
		return populated;
	}
}
