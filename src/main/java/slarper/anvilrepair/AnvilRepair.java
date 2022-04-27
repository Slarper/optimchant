package slarper.anvilrepair;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class AnvilRepair implements ModInitializer, UseBlockCallback {
    @Override
    public void onInitialize() {
        UseBlockCallback.EVENT.register(this);
    }

    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.getItem() == Items.IRON_INGOT) {
            BlockPos pos = hitResult.getBlockPos();
            BlockState anvil = world.getBlockState(pos);
            if (canRepair(anvil.getBlock())) {
                world.setBlockState(pos, getRepair(anvil.getBlock()).getDefaultState().with(AnvilBlock.FACING, anvil.get(AnvilBlock.FACING)));
                stack.decrement(1);
                world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    private static final Map<Block, Block> repairMap = new HashMap<>();

    static {
        repairMap.put(Blocks.CHIPPED_ANVIL,Blocks.ANVIL);
        repairMap.put(Blocks.DAMAGED_ANVIL,Blocks.CHIPPED_ANVIL);
    }

    public static boolean canRepair(Block block) {
        return repairMap.containsKey(block);
    }

    public static Block getRepair(Block block) {
        return repairMap.get(block);
    }
}
