package tfc.verticala.mixin;

import net.minecraft.core.world.chunk.ChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfc.verticala.itf.SectionModifications;

@Mixin(value = ChunkSection.class, remap = false)
public class SectionMixin implements SectionModifications {
	@Shadow
	public int yPosition;
	@Unique
	private boolean populated = false;

	@Unique
	int[] heeightmappp;

	@Unique
	boolean modified = false;

	@Inject(at = @At("RETURN"), method = "setBlock")
	public void setModified0(int x, int y, int z, int id, CallbackInfo ci) {
		modified = true;
	}

	@Inject(at = @At("RETURN"), method = "setData")
	public void setModified1(int x, int y, int z, int id, CallbackInfo ci) {
		modified = true;
	}

	@Inject(at = @At("RETURN"), method = "setChunkSectionData")
	public void setModified2(byte[] data, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int startIndex, CallbackInfoReturnable<Integer> cir) {
		modified = true;
	}

	@Override
	public boolean modified() {
		return modified;
	}

	@Override
	public int[] v_c$getHeightmap() {
		if (heeightmappp == null)
			heeightmappp = new int[16 * 16];

		return heeightmappp;
	}

	@Override
	public void v_c$setModified(boolean b) {
		this.modified = b;
	}

	@Override
	public void v_c$setPopulated() {
		this.populated = true;
	}

	@Override
	public boolean v_c$isPopulated() {
		return populated;
	}
}
