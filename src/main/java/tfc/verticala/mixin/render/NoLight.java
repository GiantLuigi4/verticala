package tfc.verticala.mixin.render;

import net.minecraft.core.block.Block;
import net.minecraft.core.enums.LightLayer;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.LightUpdate;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LightUpdate.class, remap = false)
public class NoLight {
	@Shadow
	public int maxX;
	@Shadow
	public int minX;
	@Shadow
	public int maxY;
	@Shadow
	public int minY;
	@Shadow
	public int maxZ;
	@Shadow
	public int minZ;
	@Shadow
	@Final
	public LightLayer layer;

	protected int calc(
		World world,
		int x, int y, int z,
		int newV
	) {
		int id = world.getBlockId(x, y, z);
		int lb = Block.lightBlock[id];

		int x0 = world.getSavedLightValue(layer, x - 1, y, z);
		int x1 = world.getSavedLightValue(layer, x + 1, y, z);
		int z0 = world.getSavedLightValue(layer, x, y, z - 1);
		int z1 = world.getSavedLightValue(layer, x, y, z + 1);
		int y0 = world.getSavedLightValue(layer, x, y - 1, z);
		int y1 = world.getSavedLightValue(layer, x, y + 1, z);

		return Math.max(
			0, Math.max(
				Math.max(x0, x1),
				Math.max(
					Math.max(y0, y1),
					Math.max(z0, z1)
				)
			)
		) - lb - 1;
	}

	protected void apply(
		World world,
		int x, int y, int z,
		int x0, int y0, int z0,
		int emm
	) {
		int lv = emm - (Math.abs(x0) + Math.abs(y0) + Math.abs(z0));

		for (int x1 = -1; x1 <= 1; x1 += 2) {
			for (int y1 = -1; y1 <= 1; y1 += 2) {
				for (int z1 = -1; z1 <= 1; z1 += 2) {
					world.setLightValue(
						layer,
						x + x0 * x1, y + y0 * y1, z + z0 * z1,
						Math.max(
							world.getSavedLightValue(layer, x + x0 * x1, y + y0 * y1, z + z0 * z1),
							calc(
								world,
								x + x0 * x1, y + y0 * y1, z + z0 * z1,
								lv
							)
						)
					);
				}
			}
		}
	}

	protected void propagate(World world, int x, int y, int z) {
		int id = world.getBlockId(x, y, z);
		int emm = Block.lightEmission[id];
		if (emm == 0) return;

		for (int x1 = 0; x1 <= emm; x1++) {
			for (int y1 = 0; y1 <= emm; y1++) {
				for (int z1 = 0; z1 <= emm; z1++) {
					// center of lighting update is the value
					if (x1 == 0 && y1 == 0 && z1 == 0) {
						world.setLightValue(
							layer,
							x, y, z,
							emm
						);
						continue;
					}

					apply(
						world,
						x, y, z,
						x1, y1, z1,
						emm
					);
				}
			}
		}

//		for (int x1 = -emm; x1 <= emm; x1++) {
//			for (int y1 = -emm; y1 <= emm; y1++) {
//				for (int z1 = -emm; z1 <= emm; z1++) {
//					world.setLightValue(
//						layer, x, y, z,
//						Math.max(
//							world.getSavedLightValue(layer, x, y, z),
//							Math.abs(x1) + Math.abs(y1) + Math.abs(z1)
//						)
//					);
//				}
//			}
//		}
	}

	@Inject(at = @At("HEAD"), method = "performLightUpdate", cancellable = true)
	public void performLightUpdate(World world, CallbackInfo ci) {
		if (layer != LightLayer.Sky) {
			// TODO: light removal
			for (int x = minX; x <= maxX; x++) {
				for (int y = minY; y <= maxY; y++) {
					for (int z = minZ; z <= maxZ; z++) {
						propagate(
							world, x, y, z
						);
					}
				}
			}
			ci.cancel(); // TODO: fix block light
		}
	}

	@Redirect(method = "performLightUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;getHeightBlocks()I"))
	public int swapBlocks(World instance) {
		return instance.getHeightBlocks() / 256;
	}
}
