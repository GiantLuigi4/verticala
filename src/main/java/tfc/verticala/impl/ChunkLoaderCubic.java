package tfc.verticala.impl;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.NbtIo;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.ChunkLoaderLegacy;
import net.minecraft.core.world.chunk.ChunkSection;
import net.minecraft.core.world.chunk.IChunkLoader;
import net.minecraft.core.world.chunk.reader.ChunkReaderVersion2;
import net.minecraft.core.world.chunk.writer.ChunkWriter;
import net.minecraft.core.world.save.LevelData;
import net.minecraft.core.world.save.mcregion.RegionFileCache;
import tfc.verticala.generator.ChunkGeneratorCubic;
import tfc.verticala.itf.ChunkModifications;
import tfc.verticala.itf.ChunkProviderModifications;
import tfc.verticala.itf.SectionModifications;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class ChunkLoaderCubic implements IChunkLoader {
	public static boolean cancelSections;
	private final File worldDir;

	public ChunkLoaderCubic(File worldDir) {
		this.worldDir = worldDir;
	}

	int cm(int y) {
		return ((y + (y < 0 ? 1 : 0)) / 8) * 8 - (y < 0 ? 8 : 0);
	}

	public void loadChunk(World world, Chunk chnk, int x, int y, int z) throws IOException {
		boolean dskip = cancelSections;
		cancelSections = false;
		DataInputStream regionStream = CubicFileCache.getChunkInputStream(this.worldDir, x, y, z);

		ChunkGeneratorCubic cubic = ((ChunkProviderModifications) world.getChunkProvider()).getCubicGenerator();
		if (regionStream != null) {
			CompoundTag tag = NbtIo.read(regionStream);
			ChunkReaderVersion2 rdr = new ChunkReaderVersion2(world, tag);
			Map<Integer, String> biomeRegistry = rdr.getBiomeRegistry();
			int cm = cm(y);
			for (int i = 0; i < 8; i++) {
				ChunkSection sec = ((ChunkModifications) chnk).v_c$createSection(cm + i);
				ChunkLoaderLegacy.loadChunkSectionFromCompound(
					sec, rdr, biomeRegistry
				);
				((SectionModifications) sec).v_c$setModified(true);
				if (
					tag.containsKey("PopulatedL") &&
						tag.getBoolean(i < 4 ? "PopulatedL" : "PopulatedU")
				)
					((SectionModifications) sec).v_c$setPopulated();
			}

			ChunkSection first = ((ChunkModifications) chnk).v_c$createSection(cm);
			cubic.decorate(chnk, first);
		} else {
			int cm = cm(y);
			cubic.generate(
				chnk,
				cm,
				cm + 8
			);

			for (int i = 0; i < 8; i++) {
				ChunkSection sec = ((ChunkModifications) chnk).v_c$createSection(cm + i);
				((SectionModifications) sec).v_c$setModified(true);
			}

			ChunkSection sec = ((ChunkModifications) chnk).v_c$createSection(cm);
			cubic.decorate(chnk, sec);
			chnk.setChunkModified();
		}

		chnk.world.markBlocksDirty(
			chnk.xPosition << 4,
			y << 4,
			chnk.zPosition << 4,
			(chnk.xPosition << 4) + 16,
			(y + 7) << 4,
			(chnk.zPosition << 4) + 16
		);

		chnk.recalcHeightmap();
		cancelSections = dskip;
	}

	public Chunk loadChunk(World world, int x, int z) throws IOException {
		cancelSections = true;
		DataInputStream regionStream = RegionFileCache.getChunkInputStream(this.worldDir, x, z);
		if (regionStream != null) {
			CompoundTag tag = NbtIo.read(regionStream);
			Chunk chunk = ChunkLoaderLegacy.loadChunkIntoWorldFromCompound(world, tag.getCompound("Level"));

			if (!chunk.isAtLocation(x, z)) {
				System.out.println("Chunk file at " + x + "," + z + " is in the wrong location; relocating. (Expected " + x + ", " + z + ", got " + chunk.xPosition + ", " + chunk.zPosition + ")");
				tag.putInt("xPos", x);
				tag.putInt("zPos", z);
				chunk = ChunkLoaderLegacy.loadChunkIntoWorldFromCompound(world, tag.getCompound("Level"));
			}

			chunk.fixMissingBlocks();
			return chunk;
		} else {
			cancelSections = false;
			return null;
		}
	}

	public void saveChunk(World world, Chunk chunk) throws IOException {
		cancelSections = true;
		world.checkSessionLock();

		try {
			{
				DataOutputStream regionStream = RegionFileCache.getChunkOutputStream(this.worldDir, chunk.xPosition, chunk.zPosition);
				CompoundTag levelTag = new CompoundTag();
				CompoundTag chunkDataTag = new CompoundTag();
				levelTag.put("Level", chunkDataTag);
				ChunkLoaderLegacy.storeChunkInCompound(chunk, world, chunkDataTag);
				NbtIo.write(levelTag, regionStream);
				regionStream.close();
				LevelData levelData = world.getLevelData();
				levelData.setSizeOnDisk(levelData.getSizeOnDisk() + (long) RegionFileCache.getSizeDelta(this.worldDir, chunk.xPosition, chunk.zPosition));
			}

			Map<Integer, ChunkSection> map = ((ChunkModifications) chunk).v_c$getSectionHashMap();
			Set<Integer> ints = map.keySet();
			ArrayList<Integer> sort = new ArrayList<>(ints);
			sort.sort(Integer::compareTo);

			cancelSections = false;

			// write cubic sections
			DataOutputStream cubicReegionStream = null;
			CompoundTag sectionsTag = null;

			int cd = Integer.MAX_VALUE;
			int idx = 0;
			for (Integer i : sort) {
				idx = i;
				ChunkSection section = map.get(i);

				int crd = i >> 3;
				if (cd != crd) {
					if (cubicReegionStream != null) {
						NbtIo.write(sectionsTag, cubicReegionStream);
						cubicReegionStream.close();
						LevelData levelData = world.getLevelData();
						levelData.setSizeOnDisk(levelData.getSizeOnDisk() + (long) CubicFileCache.getSizeDelta(this.worldDir, chunk.xPosition, i, chunk.zPosition));
					}

					cubicReegionStream = CubicFileCache.getChunkOutputStream(this.worldDir, chunk.xPosition, i, chunk.zPosition);
					sectionsTag = new CompoundTag();
					try {
						sectionsTag.putBoolean("PopulatedL", ((SectionModifications) map.get(crd << 3)).v_c$isPopulated());
						sectionsTag.putBoolean("PopulatedU", ((SectionModifications) map.get((crd << 3) + 7)).v_c$isPopulated());
					} catch (Throwable err) {
					}
					new ChunkWriter(world, sectionsTag).putBiomeRegistry();
					cd = crd;
				}

				ChunkLoaderLegacy.storeChunkSectionInCompound(
					section,
					new ChunkWriter(world, sectionsTag)
				);
			}

			if (cubicReegionStream != null) {
				NbtIo.write(sectionsTag, cubicReegionStream);
				cubicReegionStream.close();
				LevelData levelData = world.getLevelData();
				levelData.setSizeOnDisk(levelData.getSizeOnDisk() + (long) CubicFileCache.getSizeDelta(this.worldDir, chunk.xPosition, idx, chunk.zPosition));
			}
		} catch (Exception var7) {
			var7.printStackTrace();
		}
		cancelSections = false;
	}
}
