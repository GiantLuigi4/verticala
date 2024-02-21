package tfc.verticala.generator;

import net.minecraft.client.render.model.Cube;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.ChunkSection;
import tfc.verticala.generator.decorator.ChunkDecoratorCubic;
import tfc.verticala.generator.struct.GenerationGroup;
import tfc.verticala.itf.ChunkModifications;
import tfc.verticala.itf.SectionModifications;

public class ChunkPopulationHandler {
	ChunkDecoratorCubic decoratorCubic;

	public ChunkPopulationHandler(ChunkDecoratorCubic decoratorCubic) {
		this.decoratorCubic = decoratorCubic;
	}

	protected boolean needsPopulation(Chunk chunk, int sec) {
		ChunkSection section = ((ChunkModifications) chunk).v_c$getSectionNullable(sec);
		if (section == null) return false;
		if (!((SectionModifications) section).v_c$isPopulated())
			return true;
		return false;
	}

	protected boolean sectionLoaded(Chunk chunk, int yIdx) {
		ChunkSection section = ((ChunkModifications) chunk).v_c$getSectionNullable(yIdx);
		return section != null;
	}

	protected void doPopulate(Chunk centre, World world, int yIdx, int x, int z, boolean top) {
		{
			// if it's already populated, it shouldn't be re-populated
			// also if it's null, it can't be populated
			if (centre == null) return;
			ChunkSection section = centre.getSection(yIdx + (top ? 7 : 0));
			if (section == null) return;
			if (((SectionModifications) section).v_c$isPopulated()) return;
		}

		Chunk[] cache = new Chunk[25];
		for (int x1 = -2; x1 <= 2; x1++) {
			for (int z1 = -2; z1 <= 2; z1++) {
				if (x1 == 0 && z1 == 0) {
					cache[(x1 + 2) + (z1 + 2) * 5] = centre;
					continue;
				}
				cache[(x1 + 2) + (z1 + 2) * 5] = world.isChunkLoaded(
					x1 + x,
					z1 + z
				) ? world.getChunkFromChunkCoords(
					x1 + x,
					z1 + z
				) : null;
			}
		}

		for (int x1 = -1; x1 <= 1; x1++) {
			for (int z1 = -1; z1 <= 1; z1++) {
				Chunk[] mCache = new Chunk[]{
					cache[(x1 + 1) + (z1 + 3) * 5], cache[(x1 + 2) + (z1 + 3) * 5], cache[(x1 + 3) + (z1 + 3) * 5],
					cache[(x1 + 1) + (z1 + 2) * 5], cache[(x1 + 2) + (z1 + 2) * 5], cache[(x1 + 3) + (z1 + 2) * 5],
					cache[(x1 + 1) + (z1 + 1) * 5], cache[(x1 + 2) + (z1 + 1) * 5], cache[(x1 + 3) + (z1 + 1) * 5],
				};

				boolean clear = true;
				for (Chunk chunk : mCache) {
					if (chunk == null) {
						clear = false;
						break;
					}

					if (!sectionLoaded(
						chunk, yIdx + (top ? 8 : -1)
					)) {
						clear = false;
						break;
					}

					if (!sectionLoaded(
						chunk, yIdx + (top ? 7 : 0)
					)) {
						clear = false;
						break;
					}
				}
				if (!clear) continue;

				populate(mCache, yIdx, top);
			}
		}
	}

	protected void populate(Chunk[] chunks, int yIdx, boolean top) {
		GenerationGroup group = new GenerationGroup(
			chunks,
			chunks[4].xPosition,
			yIdx + (top ? 4 : 0),
			chunks[4].zPosition,
			yIdx + (top ? 0 : -8),
			yIdx + (top ? 7 + 8 : 0),
			top ? 4 : -8
		);
		decoratorCubic.decorate(chunks[4], group);
		((SectionModifications) chunks[4].getSection(yIdx + (top ? 7 : 0))).v_c$setPopulated();
		chunks[4].setChunkModified();
	}

	public void populate(World world, Chunk chunk, int sectionGroup, int x, int z) {
		int yIdx = sectionGroup << 3;
		doPopulate(chunk, world, yIdx, x, z, false);
		doPopulate(chunk, world, yIdx, x, z, true);
	}
}
