package tfc.verticala.mixin.render;

import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.ChunkCache;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ChunkCache.class, remap = false)
public class ChunkCacheMixin {
	@Shadow
	@Final
	private World worldObj;
	@Shadow
	@Final
	private int chunkX;
	@Shadow
	@Final
	private int chunkZ;
	@Shadow
	@Final
	private Chunk[][] chunkArray;

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public int getBlockId(int x, int y, int z) {
		if (y < -32000000) {
			return 0;
		} else if (y >= this.worldObj.getHeightBlocks()) {
			return 0;
		} else {
			int l = Math.floorDiv(x, 16) - this.chunkX;
			int i1 = Math.floorDiv(z, 16) - this.chunkZ;
			if (l >= 0 && l < this.chunkArray.length && i1 >= 0 && i1 < this.chunkArray[l].length) {
				Chunk chunk = this.chunkArray[l][i1];
				return chunk == null ? 0 : chunk.getBlockID(x & 15, y, z & 15);
			} else {
				return 0;
			}
		}
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public int getBlockMetadata(int x, int y, int z) {
		if (y < -32000000) {
			return 0;
		} else if (y >= this.worldObj.getHeightBlocks()) {
			return 0;
		} else {
			int chunkX = Math.floorDiv(x, 16) - this.chunkX;
			int chunkZ = Math.floorDiv(z, 16) - this.chunkZ;
			return this.chunkArray[chunkX][chunkZ].getBlockMetadata(x & 15, y, z & 15);
		}
	}
}
