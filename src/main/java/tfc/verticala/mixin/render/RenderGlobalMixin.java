package tfc.verticala.mixin.render;

import net.minecraft.client.render.ChunkRenderer;
import net.minecraft.client.render.RenderGlobal;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = RenderGlobal.class, remap = false)
public class RenderGlobalMixin {
	@Shadow
	private int minBlockX;
	@Shadow
	private int minBlockY;
	@Shadow
	private int minBlockZ;
	@Shadow
	private int maxBlockX;
	@Shadow
	private int maxBlockY;
	@Shadow
	private int maxBlockZ;
	@Shadow
	private int renderChunksWide;
	@Shadow
	private int renderChunksDeep;
	@Shadow
	private int renderChunksTall;
	@Shadow
	private ChunkRenderer[] chunkRenderers;
	@Shadow
	private List<ChunkRenderer> chunkRenderersToUpdate;

	@Redirect(method = "drawDebugChunkBorders", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;getHeightBlocks()I"))
	public int switchHeight0(World instance) {
		return 256;
	}

	@Redirect(method = "loadRenderers", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;getHeightBlocks()I"))
	public int switchHeight1(World instance) {
		return 256;
	}

	@Inject(method = "loadRenderers", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/RenderGlobal;renderChunksDeep:I"))
	public void modifyChunksTall(CallbackInfo ci) {
		renderChunksTall = renderChunksWide;
	}

	@Redirect(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;getHeightBlocks()I"))
	public int switchHeight2(World instance) {
		return 256;
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	private void markRenderersForNewPosition(int camX, int camY, int camZ) {
		camX -= 8;
		camY -= 8;
		camZ -= 8;
		this.minBlockX = Integer.MAX_VALUE;
		this.minBlockY = Integer.MAX_VALUE;
		this.minBlockZ = Integer.MAX_VALUE;
		this.maxBlockX = Integer.MIN_VALUE;
		this.maxBlockY = Integer.MIN_VALUE;
		this.maxBlockZ = Integer.MIN_VALUE;

		int xmod1 = this.renderChunksWide * 16;
		int xmod0 = xmod1 / 2;

		int zmod1 = this.renderChunksDeep * 16;
		int zmod0 = zmod1 / 2;

		int ymod1 = this.renderChunksTall * 16;
		int ymod0 = ymod1 / 2;

		for(int x0 = 0; x0 < this.renderChunksWide; ++x0) {
			int x1 = x0 * 16;
			int x2 = x1 + xmod0 - camX;
			if (x2 < 0) {
				x2 -= xmod1 - 1;
			}

			x2 /= xmod1;
			x1 -= x2 * xmod1;
			if (x1 < this.minBlockX) {
				this.minBlockX = x1;
			}

			if (x1 > this.maxBlockX) {
				this.maxBlockX = x1;
			}

			for(int z0 = 0; z0 < this.renderChunksDeep; ++z0) {
				int z1 = z0 * 16;
				int z2 = z1 + zmod0 - camZ;
				if (z2 < 0) {
					z2 -= zmod1 - 1;
				}

				z2 /= zmod1;
				z1 -= z2 * zmod1;
				if (z1 < this.minBlockZ) {
					this.minBlockZ = z1;
				}

				if (z1 > this.maxBlockZ) {
					this.maxBlockZ = z1;
				}

				for(int y0 = 0; y0 < this.renderChunksTall; ++y0) {
					int y1 = y0 * 16;
					int y2 = y1 + ymod0 - camY;
					if (y2 < 0) {
						y2 -= ymod1 - 1;
					}

					y2 /= ymod1;
					y1 -= y2 * ymod1;
					if (y1 < this.minBlockY) {
						this.minBlockY = y1;
					}

					if (y1 > this.maxBlockY) {
						this.maxBlockY = y1;
					}

					ChunkRenderer worldrenderer = this.chunkRenderers[(z0 * this.renderChunksTall + y0) * this.renderChunksWide + x0];
					boolean flag = worldrenderer.needsUpdate;
  					worldrenderer.setPosition(x1, y1, z1);
					if (!flag && worldrenderer.needsUpdate) {
						this.chunkRenderersToUpdate.add(worldrenderer);
					}
				}
			}
		}

	}
}
