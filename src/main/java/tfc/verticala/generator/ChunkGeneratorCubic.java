package tfc.verticala.generator;

import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.ChunkSection;
import tfc.verticala.generator.decorator.ChunkDecoratorCubic;
import tfc.verticala.generator.struct.SectionGeneratorResult;
import tfc.verticala.itf.ChunkModifications;

public abstract class ChunkGeneratorCubic {
	protected final World world;
	private final ChunkDecoratorCubic decorator;
	private final ChunkPopulationHandler populationHandler;

	public ChunkGeneratorCubic(World world, ChunkDecoratorCubic decorator) {
		this.world = world;
		this.decorator = decorator;
		populationHandler = new ChunkPopulationHandler(decorator);
	}

	public final void generate(Chunk chunk, int minSection, int maxSection) {
		int chunkX = chunk.xPosition;
		int chunkZ = chunk.zPosition;
		chunk.temperature = this.world.getBiomeProvider().getTemperatures((double[]) null, chunkX * 16, chunkZ * 16, 16, 16);
		chunk.humidity = this.world.getBiomeProvider().getHumidities((double[]) null, chunkX * 16, chunkZ * 16, 16, 16);
		chunk.variety = this.world.getBiomeProvider().getVarieties((double[]) null, chunkX * 16, chunkZ * 16, 16, 16);
		Biome[] biomes = new Biome[512];

		for (int sectionY = minSection; sectionY < maxSection; sectionY++) {
			ChunkSection section = ((ChunkModifications) chunk).v_c$createSection(sectionY);

			this.world.getBiomeProvider().getBiomes(biomes, chunk.temperature, chunk.humidity, chunk.variety, chunkX * 16, sectionY * 16, chunkZ * 16, 16, 2, 16);

			for (int i = 0; i < section.biome.length; i++) {
				section.biome[i] = (byte) Registries.BIOMES.getNumericIdOfItem(biomes[i]);
			}

			SectionGeneratorResult result = this.doBlockGeneration(chunk, section);
			if (result.hasBlocks()) section.blocks = result.getSectionBlocks();
		}
	}

	public final void decorate(Chunk chunk, ChunkSection section) {
//		this.decorator.decorate(chunk, section);
		populationHandler.populate(
			chunk.world, chunk,
			section.yPosition >> 3,
			chunk.xPosition, chunk.zPosition
		);
	}

	public Chunk pregen(int chunkX, int chunkZ) {
		return new Chunk(world, chunkX, chunkZ);
	}

	public void preDecorate(Chunk chunk) {
	}

	protected abstract SectionGeneratorResult doBlockGeneration(Chunk chunk, ChunkSection section);
}
