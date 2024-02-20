package tfc.verticala.impl.generator;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.ChunkSection;
import net.minecraft.core.world.type.WorldType;
import tfc.verticala.generator.decorator.ChunkDecoratorCubic;

import java.util.ArrayList;
import java.util.Random;

public class OverworldChunkDecoratorCubic implements ChunkDecoratorCubic {
	@Override
	public void decorate(Chunk chunk, ChunkSection section) {
		if (section.yPosition == -1) {
			Random rand = chunk.world.rand;

			WorldType type = chunk.world.getWorldType();

			ArrayList<Integer> xcs = new ArrayList<>();
			ArrayList<Integer> zcs = new ArrayList<>();
			ArrayList<Integer> szs = new ArrayList<>();
			ArrayList<Double> ags = new ArrayList<>();
			ArrayList<Double> vrs = new ArrayList<>();
			ArrayList<Double> vas = new ArrayList<>();
			for (int x1 = -1; x1 <= 1; x1++) {
				for (int z1 = -1; z1 <= 1; z1++) {
					rand.setSeed((long) (chunk.xPosition + x1) * 341873128712L + (long) (chunk.zPosition + z1) * 132897987541L);
					if (rand.nextInt(20) < 3) {
						xcs.add((chunk.xPosition + x1) * 16 + rand.nextInt(16));
						zcs.add((chunk.zPosition + z1) * 16 + rand.nextInt(16));
						szs.add(rand.nextInt(8) + 2);
						ags.add(rand.nextDouble() * Math.PI * 2);
						vrs.add(rand.nextDouble() * 3);
						vas.add(rand.nextDouble() * 4);
					}
				}
			}

			rand.setSeed((long) chunk.xPosition * 341873128712L + (long) chunk.zPosition * 132897987541L);

			for (int x = 0; x < 16; x++) {

				lz:
				for (int z = 0; z < 16; z++) {
					for (int i = 0; i < xcs.size(); i++) {
						// gap
						int xc = xcs.get(i); // xcoord
						int zc = zcs.get(i); // zcoord
						int sz = szs.get(i); // size
						double ag = ags.get(i); // angle
						double vr = vrs.get(i); // variation rate
						double va = vas.get(i); // variation amount

						// offsets
						int xo = (xc - (x + (chunk.xPosition * 16)));
						int zo = (zc - (z + (chunk.zPosition * 16)));
						// distance
						double d = Math.sqrt(
							xo * xo +
								zo * zo
						);
						double angle = ag +
							Math.atan2(xo, zo) * vr;
						d -= Math.sin(angle) * va - Math.cos(angle) * va;

						if (d < sz) {
							// TODO: ease chance out?
							continue lz;
						}
					}

					for (int y = -5; y < 0; y++) {
						if (y >= type.getMinY() - rand.nextInt(5)) {
							section.setBlock(x, y + 16, z, Block.bedrock.id);
						}
					}
				}
			}
		}
	}
}
