package tfc.verticala.generator;

import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.ChunkSection;
import tfc.verticala.generator.decorator.ChunkDecoratorCubic;

public abstract class ChunkGeneratorCubic {
	protected final World world;
	private final ChunkDecoratorCubic decorator;

	public ChunkGeneratorCubic(World world, ChunkDecoratorCubic decorator) {
		this.world = world;
		this.decorator = decorator;
	}

	public final void generate(Chunk chunk, ChunkSection section) {
		int chunkX = chunk.xPosition;
		int chunkY = section.yPosition;
		int chunkZ = chunk.zPosition;
		chunk.temperature = this.world.getBiomeProvider().getTemperatures((double[]) null, chunkX * 16, chunkZ * 16, 16, 16);
		chunk.humidity = this.world.getBiomeProvider().getHumidities((double[]) null, chunkX * 16, chunkZ * 16, 16, 16);
		chunk.variety = this.world.getBiomeProvider().getVarieties((double[]) null, chunkX * 16, chunkZ * 16, 16, 16);
		Biome[] biomes = new Biome[512];

//		for (int sectionY = 0; sectionY < 16; ++sectionY) {
		this.world.getBiomeProvider().getBiomes(biomes, chunk.temperature, chunk.humidity, chunk.variety, chunkX * 16, chunkY * 16, chunkZ * 16, 16, 2, 16);
//			ChunkSection section = chunk.getSection(sectionY);
//
//			for (sectionY = 0; sectionY < biomes.length; ++sectionY) {
//				section.biome[sectionY] = (byte) Registries.BIOMES.getNumericIdOfItem(biomes[sectionY]);
//			}

		for (int i = 0; i < section.biome.length; i++) {
			section.biome[i] = (byte) Registries.BIOMES.getNumericIdOfItem(biomes[i]);
		}
		SectionGeneratorResult result = this.doBlockGeneration(chunk, section);
		if (result.hasBlocks()) section.blocks = result.getSectionBlocks();
//		}
//
//		return chunk;
	}

	public final void decorate(Chunk chunk, ChunkSection section) {
		this.decorator.decorate(chunk, section);
	}

	public Chunk pregen(int chunkX, int chunkZ) {
		return new Chunk(world, chunkX, chunkZ);
	}

	public void preDecorate(Chunk chunk) {
	}

	protected abstract SectionGeneratorResult doBlockGeneration(Chunk chunk, ChunkSection section);
}
