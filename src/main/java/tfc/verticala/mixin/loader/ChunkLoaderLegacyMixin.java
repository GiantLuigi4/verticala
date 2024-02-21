package tfc.verticala.mixin.loader;

import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.ChunkLoaderLegacy;
import net.minecraft.core.world.chunk.ChunkSection;
import net.minecraft.core.world.chunk.reader.ChunkReader;
import net.minecraft.core.world.chunk.writer.ChunkWriter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfc.verticala.impl.ChunkLoaderCubic;

import java.util.Map;

@Mixin(value = ChunkLoaderLegacy.class, remap = false)
public class ChunkLoaderLegacyMixin {
	@Inject(at = @At("HEAD"), method = "storeChunkSectionInCompound", cancellable = true)
	private static void preStore(ChunkSection section, ChunkWriter writer, CallbackInfo ci) {
		if (ChunkLoaderCubic.cancelSections)
			ci.cancel();
	}

	@Inject(at = @At("HEAD"), method = "loadChunkSectionFromCompound", cancellable = true)
	private static void preRead(ChunkSection section, ChunkReader reader, Map<Integer, String> biomeRegistry, CallbackInfo ci) {
		if (ChunkLoaderCubic.cancelSections)
			ci.cancel();
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/chunk/Chunk;getSection(I)Lnet/minecraft/core/world/chunk/ChunkSection;"), method = "loadChunkIntoWorldFromCompound")
	private static ChunkSection preGetSec(Chunk instance, int index) {
		if (ChunkLoaderCubic.cancelSections)
			return null;
		return instance.getSection(index);
	}
}
