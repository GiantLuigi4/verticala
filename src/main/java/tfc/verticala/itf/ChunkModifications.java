package tfc.verticala.itf;

import net.minecraft.core.world.chunk.ChunkSection;

import java.util.Map;

public interface ChunkModifications {
	Map<Integer, ChunkSection> v_c$getSectionHashMap();
	ChunkSection v_c$getSectionNullable(int index);
	ChunkSection v_c$createSection(int index);
}
