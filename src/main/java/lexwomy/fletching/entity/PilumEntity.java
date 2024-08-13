package lexwomy.fletching.entity;

import lexwomy.fletching.item.FletchingItems;
import lexwomy.fletching.item.PilumItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PilumEntity extends PersistentProjectileEntity {
    public PilumEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(FletchingItems.FLINT_PILUM);
    }


}
