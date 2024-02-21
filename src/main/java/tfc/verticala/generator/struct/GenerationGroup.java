package tfc.verticala.generator.struct;

import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.ChunkSection;
import tfc.verticala.itf.ChunkModifications;

public class GenerationGroup {
	Chunk[] chunks;
	public final int xPosition, yPosition, zPosition;
	public final int minY, maxY;
	protected final int yIdxShift;

	public GenerationGroup(Chunk[] chunks, int xPosition, int yPosition, int zPosition, int minY, int maxY, int yIdxShift) {
		this.chunks = chunks;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.zPosition = zPosition;
		this.minY = minY;
		this.maxY = maxY;
		this.yIdxShift = yIdxShift;
	}

	public Chunk getChunk(int x, int z) {
		if (x < this.xPosition - 1) return null;
		if (z < this.zPosition - 1) return null;
		if (x > (this.xPosition + 1)) return null;
		if (z > (this.zPosition + 1)) return null;
		return chunks[(x - this.xPosition + 1) + (z - this.zPosition + 1) * 3];
	}

	public ChunkSection getSection(int x, int y, int z) {
		if (y < minY - 7) return null;
		if (y > maxY + 8) return null;
		Chunk chnk = getChunk(x, z);
		if (chnk == null) return null;
		return ((ChunkModifications) chnk).v_c$getSectionNullable(y);
	}

	public void setBlock(int x, int y, int z, int id) {
		ChunkSection sec = getSection(
			(x >> 4) + this.xPosition,
			(y >> 4) - yIdxShift + this.yPosition,
			(z >> 4) + this.zPosition
		);
		if (sec == null) {
			System.err.println("A chunk decorator attempted to place a block at " + x + ", " + y + ", " + z + " in chunk " + xPosition + ", " + yPosition + ", " + zPosition + ".");
		} else {
			sec.setBlock(x & 15, y & 15, z & 15, id);
		}
	}
}
