package lexwomy.fletching.screen;

import lexwomy.fletching.Fletching;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

//TO-DO Make a UI once types of arrows/bows fleshed out
public class FletchingScreenHandler extends AbstractContainerMenu {
    private final ContainerLevelAccess context;

    //Client constructor
    public FletchingScreenHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, ContainerLevelAccess.NULL);
    }

    //Server constructor
    public FletchingScreenHandler(int syncId, Inventory inventory, final ContainerLevelAccess context) {
        super(Fletching.FLETCHING, syncId);
        this.context = context;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }
}
