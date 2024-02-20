package tfc.verticala.impl.generator;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.ChunkSection;
import net.minecraft.core.world.generate.chunk.ChunkDecorator;
import net.minecraft.core.world.generate.chunk.ChunkGenerator;
import tfc.verticala.generator.ChunkGeneratorCubic;
import tfc.verticala.generator.SectionGeneratorResult;
import tfc.verticala.generator.decorator.ChunkDecoratorCubic;

public class VanillaCubicChunkGenerator extends ChunkGeneratorCubic {
	SectionGeneratorResult voidRes = new SectionGeneratorResult();
	SectionGeneratorResult skyRes = new SectionGeneratorResult();

	ChunkGenerator vanilla;

	public VanillaCubicChunkGenerator(
		World world,
		ChunkGenerator vanilla,
		ChunkDecoratorCubic decorator
	) {
		super(world, decorator);

		this.vanilla = vanilla;

		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				for (int z = 0; z < 16; z++) {
					voidRes.setBlock(x, y, z, Block.stone.id);
					skyRes.setBlock(x, y, z, 0);
				}
			}
		}
	}

	@Override
	protected SectionGeneratorResult doBlockGeneration(Chunk chunk, ChunkSection section) {
		if ((section.yPosition) < 0) return new SectionGeneratorResult().copy(voidRes);
		return new SectionGeneratorResult().copy(skyRes);
	}

	@Override
	public Chunk pregen(int chunkX, int chunkZ) {
		return vanilla.generate(chunkX, chunkZ);
	}

	@Override
	public void preDecorate(Chunk chunk) {
		vanilla.decorate(chunk);
	}
}
