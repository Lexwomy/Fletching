package lexwomy.fletching.entity;

import lexwomy.fletching.item.FletchingItems;
import lexwomy.fletching.item.PilumItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PilumEntity extends PersistentProjectileEntity {
    public PilumEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public PilumEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(EntityType.ARROW, x, y, z, world, stack, shotFrom);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(FletchingItems.FLINT_PILUM);
    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);


    }
}
