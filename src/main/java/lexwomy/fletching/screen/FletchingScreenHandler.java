package lexwomy.fletching.screen;

import lexwomy.fletching.FletchingTableInitializer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;

//TO-DO Make a UI once types of arrows/bows fleshed out
public class FletchingScreenHandler extends ScreenHandler {
    private final ScreenHandlerContext context;

    //Client constructor
    public FletchingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    //Server constructor
    public FletchingScreenHandler(int syncId, PlayerInventory inventory, final ScreenHandlerContext context) {
        super(FletchingTableInitializer.FLETCHING, syncId);
        this.context = context;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return false;
    }
}
