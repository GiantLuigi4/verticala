package tfc.verticala.data;

import net.minecraft.core.world.chunk.ChunkSection;

public class SectionGroup {
	private static int getSize() {
		return 8;
	}

	public static final int SIZE = getSize();

	ChunkSection[] sections = new ChunkSection[SIZE];
	int crd;

	public SectionGroup(int crd) {
		this.crd = crd;
	}
}
