package tfc.verticala.mixin;

import net.minecraft.core.block.Block;
import net.minecraft.core.enums.LightLayer;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.ChunkSection;
import net.minecraft.core.world.chunk.IChunkLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import tfc.verticala.impl.ChunkLoaderCubic;
import tfc.verticala.itf.ChunkModifications;
import tfc.verticala.itf.SectionModifications;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(value = Chunk.class, remap = false)
public abstract class ChunkMixin implements ChunkModifications {
	@Shadow
	@Final
	private ChunkSection[] sections;
	@Shadow
	public boolean isModified;

	@Shadow
	protected abstract void lightGaps(int x, int z);

	@Shadow
	@Final
	public int zPosition;
	@Shadow
	@Final
	public int xPosition;
	@Shadow
	public World world;

	@Shadow
	public abstract int getHeightValue(int x, int z);

	@Shadow
	public static boolean isLit;

	@Shadow
	public double[] temperature;
	@Shadow
	public double[] humidity;
	@Shadow
	public double[] variety;

	@Shadow
	public abstract void init();

	@Shadow
	protected abstract void setHeightValue(int x, int z, int y);

	@Shadow
	public int lowestY;
	@Shadow
	public int averageBlockHeight;
	@Shadow
	public short[] heightMap;

	private final Map<Integer, ChunkSection> sectionHashMap = new ConcurrentHashMap<>();

	public Map<Integer, ChunkSection> v_c$getSectionHashMap() {
		return sectionHashMap;
	}

	ChunkSection latest;

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public ChunkSection getSection(int index) {
		if (latest != null && latest.yPosition == index) return latest;
		if (!sectionHashMap.containsKey(index)) {
			IChunkLoader ldr = world.getSaveHandler().getChunkLoader(world.dimension);
			if (ldr instanceof ChunkLoaderCubic) {
				try {
					if (sectionHashMap.isEmpty()) {
						((ChunkLoaderCubic) ldr).loadChunk(world, (Chunk) (Object) this, xPosition, 0, zPosition);
						((ChunkLoaderCubic) ldr).loadChunk(world, (Chunk) (Object) this, xPosition, -1, zPosition);
						if (index != 0)
							((ChunkLoaderCubic) ldr).loadChunk(world, (Chunk) (Object) this, xPosition, index, zPosition);
					} else
						((ChunkLoaderCubic) ldr).loadChunk(world, (Chunk) (Object) this, xPosition, index, zPosition);
				} catch (Throwable err) {
					err.printStackTrace();
				}
			}
		}
		latest = sectionHashMap.computeIfAbsent(index, (k) -> new ChunkSection((Chunk) (Object) this, k));
		return latest;
	}

	public ChunkSection v_c$createSection(int index) {
		if (latest != null && latest.yPosition == index) return latest;
		latest = sectionHashMap.computeIfAbsent(index, (k) -> new ChunkSection((Chunk) (Object) this, k));
		return latest;
	}

	public ChunkSection v_c$getSectionNullable(int index) {
		if (latest != null && latest.yPosition == index) return latest;
		latest = sectionHashMap.get(index);
		return latest;
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public int getBlockID(int x, int y, int z) {
		if (x >= 0 && x < 16 && z >= 0 && z < 16) {
			ChunkSection section = v_c$getSectionNullable(y >> 4);
			if (section != null) return section.getBlock(x, y & 15, z);
		}
		return 0;
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public boolean setBlockIDWithMetadata(int x, int y, int z, int id, int data) {
		if (x >= 0 && x < 16 && z >= 0 && z < 16) {
			ChunkSection section = this.getSection(y >> 4);
			int heightValue = this.getHeightValue(x, z);
			int currentId = section.getBlock(x, y & 15, z);
			int currentData = section.getData(x, y & 15, z);
			if (currentId == id && currentData == data) {
				return false;
			} else {
				section.setBlock(x, y & 15, z, id);
				section.setData(x, y & 15, z, data);
				int worldX = this.xPosition * 16 + x;
				int worldZ = this.zPosition * 16 + z;
				if (currentId != 0 && !this.world.isClientSide) {
					Block.blocksList[currentId].onBlockRemoved(this.world, worldX, y, worldZ, currentData);
				}

				if (!this.world.worldType.hasCeiling()) {
					if (Block.lightBlock[id & 16383] != 0) {
						if (y >= heightValue + 1) {
							this.recalcHeight(x, y, z);
						}
					} else if (y == heightValue) {
						this.recalcHeight(x, y, z);
					}

					this.world.scheduleLightingUpdate(LightLayer.Sky, worldX, y, worldZ, worldX, y, worldZ);
				}

				this.world.scheduleLightingUpdate(LightLayer.Block, worldX, y, worldZ, worldX, y, worldZ);
				this.lightGaps(x, z);
				if (Block.getBlock(id) != null) {
					Block.blocksList[id].onBlockAdded(this.world, worldX, y, worldZ);
				}

				this.isModified = true;
				return true;
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
	public boolean setBlockID(int x, int y, int z, int id) {
		return this.setBlockIDWithMetadata(x, y, z, id, 0);
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public boolean setBlockIDRaw(int x, int y, int z, int id) {
		if (x >= 0 && x < 16 && z >= 0 && z < 16) {
			ChunkSection section = this.getSection(y >> 4);
			int currentId = section.getBlock(x, y & 15, z);
			if (currentId == id) {
				return false;
			} else {
				section.setBlock(x, y & 15, z, id);
				section.setData(x, y & 15, z, id);
				int worldX = this.xPosition * 16 + x;
				int worldZ = this.zPosition * 16 + z;
				this.world.scheduleLightingUpdate(LightLayer.Block, worldX, y, worldZ, worldX, y, worldZ);
				this.lightGaps(x, z);
				this.isModified = true;
				return true;
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
	public int getBlockMetadata(int x, int y, int z) {
		if (x >= 0 && x < 16 && z >= 0 && z < 16) {
			ChunkSection section = v_c$getSectionNullable(y >> 4);
			if (section != null) return section.getData(x, y & 15, z);
		}
		return 0;
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public void setBlockMetadata(int x, int y, int z, int value) {
		if (x >= 0 && x < 16 && z >= 0 && z < 16) {
			this.getSection(y >> 4).setData(x, y & 15, z, value);
			this.isModified = true;
		}
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public Biome getBlockBiome(int x, int y, int z) {
		if (x >= 0 && x < 16 && z >= 0 && z < 16) {
			ChunkSection section = v_c$getSectionNullable(y >> 4);
			if (section != null) return section.getBiome(x, y & 15, z);
		}
		return null;
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public boolean setBlockBiome(int x, int y, int z, Biome biome) {
		if (x >= 0 && x < 16 && z >= 0 && z < 16) {
			this.getSection(y >> 4).setBiome(x, y & 15, z, biome);
			this.isModified = true;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public int getBrightness(LightLayer layer, int x, int y, int z) {
		if (y > getHeightValue(x, z)) return 15;

		if (x >= 0 && x < 16 && z >= 0 && z < 16) {
			ChunkSection section = v_c$getSectionNullable(y / 16);
			if (section != null) return section.getBrightness(layer, x, y & 15, z);
		}
		return 0;
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public void setBrightness(LightLayer layer, int x, int y, int z, int value) {
		if (x >= 0 && x < 16 && z >= 0 && z < 16) {
			ChunkSection section = this.v_c$getSectionNullable(y / 16);
			if (section != null) {
				section.setBrightness(layer, x, y & 15, z, value);
				this.isModified = true;
			}
		}
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public int getRawBrightness(int x, int y, int z, int skySubtract) {
		if (y > getHeightValue(x, z)) return 15;

		if (x >= 0 && x < 16 && z >= 0 && z < 16) {
			ChunkSection section = v_c$getSectionNullable(y / 16);
			if (section == null)
				return 0;

			int lightValue = section.getRawBrightness(x, y & 15, z, skySubtract);
			if (lightValue > 0) {
				isLit = true;
			}

			return lightValue;
		} else {
			return 0;
		}
	}

	/**
	 * @author GiantLuigi4
	 * @reason per-section heightmap
	 */
	@Overwrite
	private void recalcHeight(int x, int y, int z) {
		ChunkSection sec = getSection(y >> 4);

		int yv = 15;
		boolean hit = false;

		for (; yv >= 0; yv--)
			if (Block.lightBlock[sec.getBlock(x, yv, z)] != 0) {
				hit = true;
				((SectionModifications) sec).v_c$getHeightmap()[x + z * 16] = yv;
				break;
			}

		if (!hit)
			((SectionModifications) sec).v_c$getHeightmap()[x + z * 16] = -1;

		recalcHeightmapOnly();

		this.isModified = true;
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public void recalcHeightmap() {
		this.recalcHeightmapOnly();
		int x;
		int z;
		if (!this.world.worldType.hasCeiling()) {
			for (ChunkSection value : sectionHashMap.values()) {
				for (x = 0; x < 16; ++x) {
					for (z = 0; z < 16; ++z) {
						int hv = getHeightValue(x, z);
						hv -= value.yPosition << 4;
						if (hv > 16) hv = 16;
						else if (hv < 0) hv = 0;
						for (int y = 0; y < hv; y++) {
							setBrightness(
								LightLayer.Sky, x, y + (value.yPosition << 4), z, 0
							);
						}
						if (hv != 16) {
							for (int y = hv; y < 15; y++) {
								setBrightness(
									LightLayer.Sky, x, y + (value.yPosition << 4), z, 15
								);
							}
						}
					}
				}

				this.world.scheduleLightingUpdate(
					LightLayer.Sky,
					(xPosition << 4) - 1,
					(value.yPosition << 4) - 1,
					(zPosition << 4) - 1,
					(xPosition << 4) + 16,
					(value.yPosition << 4) + 16,
					(zPosition << 4) + 16
				);
			}
		}
	}

	/**
	 * @author GiantLuigi4
	 * @reason per-section heightmap
	 */
	@Overwrite
	public void recalcHeightmapOnly() {
		Arrays.fill(heightMap, Short.MIN_VALUE);

		for (ChunkSection value : sectionHashMap.values()) {
			if (((SectionModifications) value).modified()) {
				SectionModifications motif = ((SectionModifications) value);

				motif.v_c$setModified(false);
				for (int x = 0; x < 16; x++) {
					for (int z = 0; z < 16; z++) {
						int y = 15;

						boolean hit = false;
						for (; y >= 0; y--)
							if (Block.lightBlock[value.getBlock(x, y, z)] != 0) {
								hit = true;
								motif.v_c$getHeightmap()[x + z * 16] = y;
								break;
							}

						if (!hit)
							motif.v_c$getHeightmap()[x + z * 16] = -1;
					}
				}
			}
		}

		for (ChunkSection value : sectionHashMap.values()) {
			for (int i = 0; i < ((SectionModifications) value).v_c$getHeightmap().length; i++) {
				int hv = ((SectionModifications) value).v_c$getHeightmap()[i];
				if (hv != -1)
					this.heightMap[i] = (short) Math.max(this.heightMap[i], (value.yPosition << 4) + hv);
			}
		}
	}

	// TODO: do these especially better

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public int setChunkData(byte[] data, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int startIndex) {
		int minSectionY = minY / 16;
		int maxSectionY = (int) Math.ceil((double) maxY / 16.0);

		int x;
		int z;
		for (x = minSectionY; x <= maxSectionY; ++x) {
			if (x >= 0 && x < this.sections.length) {
				z = minY - x * 16;
				int maxYSection = maxY - x * 16;
				if (z < 0) {
					z = 0;
				}

				if (maxYSection > 16) {
					maxYSection = 16;
				}

				startIndex = this.getSection(x).setChunkSectionData(data, minX, z, minZ, maxX, maxYSection, maxZ, startIndex);
			}
		}

		this.recalcHeightmapOnly();

		for (x = minX; x < maxX; ++x) {
			for (z = minZ; z < maxZ; ++z) {
				this.temperature[x * 16 + z] = (double) (data[startIndex++] & 255) / 255.0;
			}
		}

		for (x = minX; x < maxX; ++x) {
			for (z = minZ; z < maxZ; ++z) {
				this.humidity[x * 16 + z] = (double) (data[startIndex++] & 255) / 255.0;
			}
		}

		for (x = minX; x < maxX; ++x) {
			for (z = minZ; z < maxZ; ++z) {
				this.variety[x * 16 + z] = (double) (data[startIndex++] & 255) / 255.0;
			}
		}

		return startIndex;
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public int getChunkData(byte[] data, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int startIndex) {
		int minSectionY = minY / 16;
		int maxSectionY = (int) Math.ceil((double) maxY / 16.0);

		int x;
		int z;
		for (x = minSectionY; x <= maxSectionY; ++x) {
			if (x >= 0 && x < this.sections.length) {
				z = minY - x * 16;
				int maxYSection = maxY - x * 16;
				if (z < 0) {
					z = 0;
				}

				if (maxYSection > 16) {
					maxYSection = 16;
				}

				startIndex = this.getSection(x).getChunkSectionData(data, minX, z, minZ, maxX, maxYSection, maxZ, startIndex);
			}
		}

		for (x = minX; x < maxX; ++x) {
			for (z = minZ; z < maxZ; ++z) {
				data[startIndex++] = (byte) ((int) (this.temperature[x * 16 + z] * 255.0));
			}
		}

		for (x = minX; x < maxX; ++x) {
			for (z = minZ; z < maxZ; ++z) {
				data[startIndex++] = (byte) ((int) (this.humidity[x * 16 + z] * 255.0));
			}
		}

		for (x = minX; x < maxX; ++x) {
			for (z = minZ; z < maxZ; ++z) {
				data[startIndex++] = (byte) ((int) (this.variety[x * 16 + z] * 255.0));
			}
		}

		return startIndex;
	}
}
