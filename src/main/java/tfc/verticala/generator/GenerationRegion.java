package tfc.verticala.generator;

import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.ChunkSection;
import tfc.verticala.itf.ChunkModifications;

public class GenerationRegion {
	public final Chunk myChunk;

	public final int chunkX, chunkZ;
	public final int minSection, maxSection;

	ChunkSection[] sections;

	public GenerationRegion(Chunk myChunk, int chunkX, int chunkZ, int minSection, int maxSection) {
		this.myChunk = myChunk;
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		this.minSection = minSection;
		this.maxSection = maxSection;

		sections = new ChunkSection[maxSection - minSection];
		for (int i = 0; i < sections.length; i++) {
			sections[i] = ((ChunkModifications) myChunk).v_c$createSection(minSection + i);
		}
	}

	public void setBlock(int x, int y, int z, int id) {
		int sec = y << 4;
		sections[sec].setBlock(x, y & 15, z, id);
	}

	public void setBlockMeta(int x, int y, int z, int meta) {
		int sec = y << 4;
		sections[sec].setData(x, y & 15, z, meta);
	}

	public void setBlockAndMeta(int x, int y, int z, int id, int meta) {
		int sec = y << 4;
		sections[sec].setBlock(x, y & 15, z, id);
		sections[sec].setData(x, y & 15, z, meta);
	}
}
