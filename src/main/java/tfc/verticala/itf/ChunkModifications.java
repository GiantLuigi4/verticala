package tfc.verticala.itf;

import net.minecraft.core.world.chunk.ChunkSection;
import tfc.verticala.data.SectionGroup;

import java.util.Map;

public interface ChunkModifications {
	Map<Integer, SectionGroup> v_c$getSectionHashMap();
	ChunkSection v_c$getSectionNullable(int index);
	ChunkSection v_c$createSection(int index);
}
