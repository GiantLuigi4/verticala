package tfc.verticala.mixin.generator;

import net.minecraft.client.Minecraft;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.chunk.ChunkGenerator;
import net.minecraft.core.world.type.WorldType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tfc.verticala.ClampedWorld;

@Mixin(value = Minecraft.class, remap = false)
public class MinecraftMixin {
	@Redirect(method = "createChunkProvider", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/type/WorldType;createChunkGenerator(Lnet/minecraft/core/world/World;)Lnet/minecraft/core/world/generate/chunk/ChunkGenerator;"))
	public ChunkGenerator wrapGen(WorldType instance, World world) {
		ChunkGenerator generator = instance.createChunkGenerator(new ClampedWorld(
			world, world.dimension
		));
		return generator;
	}
}
