package com.pouffydev.oneblock.core.init;

import com.pouffydev.oneblock.objects.DebugWrenchItem;
import com.pouffydev.oneblock.objects.TheOneBlock;
import com.pouffydev.oneblock.util.ModUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class OneBlockObjects {
    public static final DeferredRegister.Items ITEMS = ModUtils.createRegister(DeferredRegister::createItems);
    public static final DeferredRegister.Blocks BLOCKS = ModUtils.createRegister(DeferredRegister::createBlocks);

    public static final DeferredItem<DebugWrenchItem> debugWrench = registerItem("debug_wrench", () -> new DebugWrenchItem(new Item.Properties()));

    public static final DeferredBlock<TheOneBlock> theOneBlock = registerBlock("the_one_block", () -> new TheOneBlock(Block.Properties.of()));


    private static <T extends Item> DeferredItem<T> registerItem(String id, Supplier<T> pIProp) {
        return ITEMS.register(id.toLowerCase(), pIProp);
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String id, Supplier<T> pBProp) {
        return BLOCKS.register(id.toLowerCase(), pBProp);
    }

    public static void staticInit() {
        ModUtils.logInfo("Registering Objects...");
    }
}
