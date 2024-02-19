package tfc.verticala.impl;

import net.minecraft.core.world.save.mcregion.RegionFile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CubicFileCache {
	private static final Map<File, Reference<RegionFile>> cache = new HashMap();

	public static synchronized RegionFile loadRegionFileFromCoords(File worldDir, int x, int y, int z) {
		File regionDir = new File(worldDir, "region");
		File regionFile = new File(regionDir, "r." + (x >> 5) + "." + (y >> 3) + "." + (z >> 5) + ".mcr");
		Reference<RegionFile> reference = cache.get(regionFile);
		RegionFile loadedRegion;
		if (reference != null) {
			loadedRegion = (RegionFile) reference.get();
			if (loadedRegion != null) {
				return loadedRegion;
			}
		}

		if (!regionDir.exists()) {
			regionDir.mkdirs();
		}

		if (cache.size() >= 256) {
			flushCache();
		}

		loadedRegion = new RegionFile(regionFile);
		cache.put(regionFile, new SoftReference(loadedRegion));
		return loadedRegion;
	}

	public static synchronized void flushCache() {
		Iterator var0 = cache.values().iterator();

		while (var0.hasNext()) {
			Reference<RegionFile> reference = (Reference) var0.next();

			try {
				RegionFile regionfile = reference.get();
				if (regionfile != null) {
					regionfile.close();
				}
			} catch (IOException var3) {
				var3.printStackTrace();
			}
		}

		cache.clear();
	}

	public static DataInputStream getChunkInputStream(File worldDir, int x, int y, int z) {
		RegionFile regionFile = loadRegionFileFromCoords(worldDir, x, y, z);
		return regionFile.getChunkDataInputStream(x & 31, z & 31);
	}

	public static DataOutputStream getChunkOutputStream(File worldDir, int x, int y, int z) {
		RegionFile regionfile = loadRegionFileFromCoords(worldDir, x, y, z);
		return regionfile.getChunkDataOutputStream(x & 31, z & 31);
	}

	public static int getSizeDelta(File worldDir, int x, int y, int z) {
		RegionFile regionfile = loadRegionFileFromCoords(worldDir, x, y, z);
		return regionfile.getSizeDeltaBytes();
	}
}
