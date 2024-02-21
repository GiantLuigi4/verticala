package tfc.verticala.generator.struct;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.chunk.ChunkSection;

public class SectionGeneratorResult {
	private final short[] sectionBlocksArray;
	private int numBlocksInSectionArray = 0;

	public SectionGeneratorResult() {
		this.sectionBlocksArray = new short[4096];
	}

	public void setBlock(int x, int y, int z, int id) {
		if (x >= 0 && x < 16 && y >= 0 && y < 16 && z >= 0 && z < 16) {
			Block newBlock = Block.getBlock(id);

			Block currentBlock = Block.getBlock(this.getBlock(x, y, z));
			if (newBlock == null && currentBlock != null) {
				this.numBlocksInSectionArray--;
			} else if (newBlock != null && currentBlock == null) {
				this.numBlocksInSectionArray++;
			}

			this.sectionBlocksArray[ChunkSection.makeBlockIndex(x, y, z)] = (short) id;
		}
	}

	public int getBlock(int x, int y, int z) {
		if (x >= 0 && x < 16 && y >= 0 && y < 16 && z >= 0 && z < 16) {
			return sectionBlocksArray[ChunkSection.makeBlockIndex(x, y, z)];
		} else {
			return 0;
		}
	}

	public short[] getSectionBlocks() {
		return this.sectionBlocksArray;
	}

	public boolean hasBlocks() {
		return numBlocksInSectionArray != 0;
	}

	public SectionGeneratorResult copy(SectionGeneratorResult template) {
		System.arraycopy(template.sectionBlocksArray, 0, sectionBlocksArray, 0, this.sectionBlocksArray.length);
		this.numBlocksInSectionArray = template.numBlocksInSectionArray;
		return this;
	}
}
