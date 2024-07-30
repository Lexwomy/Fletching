package lexwomy.fletchingtable;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;

public abstract class FletchingScreenHandler extends ScreenHandler {
    public FletchingScreenHandler(int syncId, PlayerInventory inventory, final ScreenHandlerContext context) {
        super(null, syncId);
    }
}
