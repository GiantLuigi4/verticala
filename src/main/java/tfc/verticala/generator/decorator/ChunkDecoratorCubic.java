package tfc.verticala.generator.decorator;

import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.ChunkSection;
import tfc.verticala.generator.struct.GenerationGroup;

public interface ChunkDecoratorCubic {
	void decorate(Chunk chunk, GenerationGroup group);
}
