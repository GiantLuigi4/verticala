package tfc.verticala.mixin;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.enums.LightLayer;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.ChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import tfc.verticala.itf.ChunkModifications;

import java.util.Map;

@Mixin(value = World.class, remap = false)
public abstract class WorldMixin {
	@Shadow
	public abstract Chunk getChunkFromBlockCoords(int x, int z);

	@Shadow
	public abstract Chunk getChunkFromChunkCoords(int x, int z);

	@Shadow
	public abstract boolean checkIfAABBIsClear(AABB axisalignedbb);

	@Shadow
	public abstract boolean isChunkLoaded(int x, int z);

	@Overwrite
	public int getHeightBlocks() {
		return 32000000;
	}

	protected int top(Chunk chunk) {
		Map<Integer, ChunkSection> sectionMap = ((ChunkModifications) chunk).v_c$getSectionHashMap();
		int mv = Integer.MIN_VALUE;
		for (Integer i : sectionMap.keySet())
			if (i > mv) mv = i;
		return sectionMap.get(mv).yPosition << 4 + 16;
	}

	@Overwrite
	public void scheduleLightingUpdate(LightLayer layer, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public int findTopSolidBlock(int x, int z) {
		Chunk chunk = this.getChunkFromBlockCoords(x, z);
		int y = 256 - 1;
		x &= 15;

		for (z &= 15; y > 0; --y) {
			int id = chunk.getBlockID(x, y, z);
			Material material = id != 0 ? Block.blocksList[id].blockMaterial : Material.air;
			if (material.blocksMotion() || material.isLiquid()) {
				return y + 1;
			}
		}

		return -1;
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public int findTopSolidNonLiquidBlock(int i, int j) {
		Chunk chunk = this.getChunkFromBlockCoords(i, j);
		int k = 256 - 1;
		i &= 15;

		for (j &= 15; k > 0; --k) {
			int l = chunk.getBlockID(i, k, j);
			Material material = l != 0 ? Block.blocksList[l].blockMaterial : Material.air;
			if (material.blocksMotion()) {
				return k + 1;
			}
		}

		return -1;
	}


	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public int getBlockId(int x, int y, int z) {
		if (x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
			if (y >= -32000000 && y <= getHeightBlocks()) {
				ChunkSection section =
					((ChunkModifications) this.getChunkFromChunkCoords(Math.floorDiv(x, 16), Math.floorDiv(z, 16))).v_c$getSectionNullable(
						y >> 4
					);
				if (section == null)
					return 0;
				return section.getBlock(x & 15, y & 15, z & 15);
			}
		}
		return 0;
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public int getBlockMetadata(int x, int y, int z) {
		if (x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
			if (y >= -32000000 && y <= getHeightBlocks()) {
				ChunkSection section =
					((ChunkModifications) this.getChunkFromChunkCoords(Math.floorDiv(x, 16), Math.floorDiv(z, 16))).v_c$getSectionNullable(
						y >> 4
					);
				if (section == null)
					return 0;
				return section.getData(x & 15, y & 15, z & 15);
			}
		}
		return 0;
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public boolean setBlock(int x, int y, int z, int id) {
		if (x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
			if (y >= -32000000 && y <= getHeightBlocks()) {
				return this.getChunkFromChunkCoords(Math.floorDiv(x, 16), Math.floorDiv(z, 16)).setBlockID(x & 15, y, z & 15, id);
			}
		}
		return false;
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public boolean setBlockMetadata(int x, int y, int z, int id) {
		if (x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
			if (y >= -32000000 && y <= getHeightBlocks()) {
				this.getChunkFromChunkCoords(Math.floorDiv(x, 16), Math.floorDiv(z, 16)).setBlockMetadata(x & 15, y, z & 15, id);
				return true;
			}
		}
		return false;
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public boolean setBlockAndMetadata(int x, int y, int z, int id, int meta) {
		if (x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
			if (y >= -32000000 && y <= getHeightBlocks()) {
				Chunk section = this.getChunkFromChunkCoords(Math.floorDiv(x, 16), Math.floorDiv(z, 16));
				return section.setBlockIDWithMetadata(x & 15, y, z & 15, id, meta);
			}
		}
		return false;
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public boolean canBlockBePlacedAt(int blockId, int x, int y, int z, boolean flag, Side side) {
		if (y >= -32000000 && y < this.getHeightBlocks()) {
			int j1 = this.getBlockId(x, y, z);
			Block block = Block.blocksList[j1];
			Block block1 = Block.blocksList[blockId];
			AABB axisalignedbb = block1.getCollisionBoundingBoxFromPool((World) (Object) this, x, y, z);
			if (flag) {
				axisalignedbb = null;
			}

			if (axisalignedbb != null && !this.checkIfAABBIsClear(axisalignedbb)) {
				return false;
			} else {
				if (block != null && block.hasTag(BlockTags.PLACE_OVERWRITES)) {
					block = null;
				}

				return blockId > 0 && block == null && block1.canPlaceBlockOnSide((World) (Object) this, x, y, z, side.getId());
			}
		} else {
			return false;
		}
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public boolean isBlockLoaded(int x, int y, int z) {
		return y >= -32000000 && y < this.getHeightBlocks() && this.isChunkLoaded(Math.floorDiv(x, 16), Math.floorDiv(z, 16));
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public boolean areBlocksLoaded(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		if (maxY >= -32000000 && minY < this.getHeightBlocks()) {
			minX >>= 4;
			minZ >>= 4;
			maxX >>= 4;
			maxZ >>= 4;

			for(int chunkX = minX; chunkX <= maxX; ++chunkX) {
				for(int chunkZ = minZ; chunkZ <= maxZ; ++chunkZ) {
					if (!this.isChunkLoaded(chunkX, chunkZ)) {
						return false;
					}
				}
			}

			return true;
		} else {
			return false;
		}
	}
}
