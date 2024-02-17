package tfc.verticala.mixin.interaction;

import net.minecraft.core.block.Block;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = ItemBlock.class, remap = false)
public class ItemBlockMixin {
	@Shadow
	protected int blockID;

	/**
	 * @author
	 * @reason mixin is stupid
	 * 		   TODO: mixin plugin
	 */
	@Overwrite
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
		if (stack.stackSize <= 0) {
			return false;
		} else {
			if (!world.canPlaceInsideBlock(blockX, blockY, blockZ)) {
				blockX += side.getOffsetX();
				blockY += side.getOffsetY();
				blockZ += side.getOffsetZ();
			}

			if (blockY >= -32000000 && blockY < world.getHeightBlocks()) {
				if (world.canBlockBePlacedAt(this.blockID, blockX, blockY, blockZ, false, side) && stack.consumeItem(player)) {
					Block block = Block.blocksList[this.blockID];
					if (world.setBlockAndMetadataWithNotify(blockX, blockY, blockZ, this.blockID, ((Item) (Object) this).getPlacedBlockMetadata(stack.getMetadata()))) {
						block.onBlockPlaced(world, blockX, blockY, blockZ, side, player, yPlaced);
						world.playBlockSoundEffect((double) ((float) blockX + 0.5F), (double) ((float) blockY + 0.5F), (double) ((float) blockZ + 0.5F), block, EnumBlockSoundEffectType.PLACE);
					}

					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}
}
