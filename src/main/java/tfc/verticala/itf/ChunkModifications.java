package tfc.verticala.itf;

import net.minecraft.core.world.chunk.ChunkSection;

import java.util.Map;

public interface ChunkModifications {
	Map<Integer, ChunkSection> v_c$getSectionHashMap();
	ChunkSection v_c$getSectionNullable(int index);
	ChunkSection v_c$createSection(int index);
	void v_c$setLowerHalfPopulated();
	void v_c$setUpperHalfPopulated();
	boolean v_c$isLowerHalfPopulated();
	boolean v_c$isUpperHalfPopulated();
}
