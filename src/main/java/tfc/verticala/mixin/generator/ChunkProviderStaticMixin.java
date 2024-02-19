package tfc.verticala.mixin.generator;

import net.minecraft.client.world.chunk.provider.ChunkProviderStatic;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.ChunkSection;
import net.minecraft.core.world.chunk.IChunkLoader;
import net.minecraft.core.world.generate.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfc.verticala.generator.ChunkGeneratorCubic;
import tfc.verticala.generator.decorator.ChunkDecoratorCubic;
import tfc.verticala.impl.generator.VanillaCubicChunkGenerator;
import tfc.verticala.itf.ChunkProviderModifications;

@Mixin(value = ChunkProviderStatic.class, remap = false)
public class ChunkProviderStaticMixin implements ChunkProviderModifications {
	@Unique
	ChunkGeneratorCubic cubic;

	@Override
	public ChunkGeneratorCubic getCubicGenerator() {
		return cubic;
	}

	@Inject(at = @At("TAIL"), method = "<init>")
	public void postInit(World world, IChunkLoader ichunkloader, ChunkGenerator chunkGenerator, CallbackInfo ci) {
		cubic = new VanillaCubicChunkGenerator(world, chunkGenerator, (chunk, section) -> {
			// no-op
		});
	}

	@Redirect(method = "regenerateChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/generate/chunk/ChunkGenerator;generate(II)Lnet/minecraft/core/world/chunk/Chunk;"))
	public Chunk postGen(ChunkGenerator instance, int section, int sectionY) {
		return cubic.pregen(section, sectionY);
	}

	@Redirect(method = "populate", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/generate/chunk/ChunkGenerator;decorate(Lnet/minecraft/core/world/chunk/Chunk;)V"))
	public void postGen(ChunkGenerator instance, Chunk chunk) {
		cubic.preDecorate(chunk);
	}
}
