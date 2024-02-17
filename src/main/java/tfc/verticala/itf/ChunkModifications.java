package tfc.verticala.itf;

import net.minecraft.core.world.chunk.ChunkSection;

import java.util.HashMap;

public interface ChunkModifications {
	HashMap<Integer, ChunkSection> v_c$getSectionHashMap();
	ChunkSection v_c$getSectionNullable(int index);
}
