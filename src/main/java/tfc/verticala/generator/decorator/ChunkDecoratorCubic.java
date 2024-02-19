package tfc.verticala.generator.decorator;

import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.ChunkSection;

public interface ChunkDecoratorCubic {
	void decorate(Chunk chunk, ChunkSection section);
}
