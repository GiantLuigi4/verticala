package tfc.verticala.data;

import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.ChunkSection;

public class SectionGroup {
	private static int getShift() {return 3;}
	private static int getSize() {return 1 << getShift();}

	public static final int SHIFT = 3;
	public static final int SIZE = 8;

	ChunkSection[] sections = new ChunkSection[SIZE];
	final Chunk chunk;
	public final int crd;
	final int ycrd;

	public SectionGroup(Chunk chunk, int crd) {
		this.chunk = chunk;
		this.crd = crd;
		ycrd = crd << SHIFT;
	}

	public ChunkSection get(int sec) {
		if (sections[sec] == null) return sections[sec] = new ChunkSection(
			chunk,
			ycrd + sec
		);
		return sections[sec];
	}
}
