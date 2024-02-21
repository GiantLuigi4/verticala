package tfc.verticala.generator;

import net.minecraft.client.render.model.Cube;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;

public class ChunkPopulationHandler {
	protected boolean isLayerPopulated(World world, int yIdx, int x, int z, boolean top) {
		Chunk[] cache = new Chunk[9];
		for (int x1 = -1; x1 <= 0; x1++) {
			for (int z1 = -1; z1 <= 0; z1++) {
				cache[(x1 + 1) + (z1 + 1) * 3] = world.isChunkLoaded(
					x1 + x,
					z1 + z
				) ? world.getChunkFromChunkCoords(
					x1 + x,
					z1 + z
				) : null;
			}
		}

		for (int x1 = -1; x1 <= 0; x1++) {
			for (int z1 = -1; z1 <= 0; z1++) {
				Chunk toPop = cache[(x1 + 1) + (z1 + 1) * 3];
				Chunk ref0 = cache[(x1 + 2) + (z1 + 1) * 3];
				Chunk ref1 = cache[(x1 + 2) + (z1 + 2) * 3];
				Chunk ref2 = cache[(x1 + 1) + (z1 + 2) * 3];
				if (toPop != null && ref0 != null && ref1 != null && ref2 != null) {
					// TODO:
				}
			}
		}

		return false;
	}

	public void populate(World world, int sectionGroup, int x, int z) {
		int yIdx = sectionGroup << 3;

//		if (!this.chunks[i1].isTerrainPopulated && this.isChunkLoaded(chunkX + 1, chunkZ + 1) && this.isChunkLoaded(chunkX, chunkZ + 1) && this.isChunkLoaded(chunkX + 1, chunkZ)) {
//			this.populate(this, chunkX, chunkZ);
//		}
//
//		if (this.isChunkLoaded(chunkX - 1, chunkZ) && !this.provideChunk(chunkX - 1, chunkZ).isTerrainPopulated && this.isChunkLoaded(chunkX - 1, chunkZ + 1) && this.isChunkLoaded(chunkX, chunkZ + 1) && this.isChunkLoaded(chunkX - 1, chunkZ)) {
//			this.populate(this, chunkX - 1, chunkZ);
//		}
//
//		if (this.isChunkLoaded(chunkX, chunkZ - 1) && !this.provideChunk(chunkX, chunkZ - 1).isTerrainPopulated && this.isChunkLoaded(chunkX + 1, chunkZ - 1) && this.isChunkLoaded(chunkX, chunkZ - 1) && this.isChunkLoaded(chunkX + 1, chunkZ)) {
//			this.populate(this, chunkX, chunkZ - 1);
//		}
//
//		if (this.isChunkLoaded(chunkX - 1, chunkZ - 1) && !this.provideChunk(chunkX - 1, chunkZ - 1).isTerrainPopulated && this.isChunkLoaded(chunkX - 1, chunkZ - 1) && this.isChunkLoaded(chunkX, chunkZ - 1) && this.isChunkLoaded(chunkX - 1, chunkZ)) {
//			this.populate(this, chunkX - 1, chunkZ - 1);
//		}
	}
}
