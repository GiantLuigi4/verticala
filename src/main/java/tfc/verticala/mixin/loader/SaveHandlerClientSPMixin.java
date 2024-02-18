package tfc.verticala.mixin.loader;

import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.chunk.ChunkLoaderRegionAsync;
import net.minecraft.core.world.chunk.IChunkLoader;
import net.minecraft.core.world.save.ISaveFormat;
import net.minecraft.core.world.save.SaveHandlerClientSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfc.verticala.impl.ChunkLoaderCubic;

import java.io.File;

@Mixin(value = SaveHandlerClientSP.class, remap = false)
public class SaveHandlerClientSPMixin {
	@Unique ISaveFormat saveFormat;
	@Unique String worldDirName;

	@Inject(at = @At("RETURN"), method = "<init>")
	public void postInit(ISaveFormat saveFormat, File savesDir, String worldDirName, boolean isMultiplayer, CallbackInfo ci) {
		this.saveFormat = saveFormat;
		this.worldDirName = worldDirName;
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public IChunkLoader getChunkLoader(Dimension dimension) {
		File dimDir = this.saveFormat.getDimensionRootDir(this.worldDirName, dimension);
		dimDir.mkdirs();
		return new ChunkLoaderCubic(dimDir);
	}
}
