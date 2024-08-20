package lexwomy.fletching.entity;

import lexwomy.fletching.component.FletchingComponents;
import lexwomy.fletching.item.FletchingItems;
import lexwomy.fletching.item.GreatbowItem;
import lexwomy.fletching.mixin.PierceLevelAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PilumEntity extends PersistentProjectileEntity {
    public PilumEntity(EntityType<? extends PilumEntity> entityType, World world) {
        super(entityType, world);
    }

    public PilumEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(FletchingEntities.PILUM, x, y, z, world, stack, shotFrom);
        int piercing = stack.getOrDefault(FletchingComponents.PIERCING, 1);
        ((PierceLevelAccessor)this).invokeSetPierceLevel((byte)piercing);
        this.setDamage(GreatbowItem.BASE_DAMAGE);
    }

    public PilumEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(FletchingEntities.PILUM, owner, world, stack, shotFrom);
        int piercing = stack.getOrDefault(FletchingComponents.PIERCING, 1);
        ((PierceLevelAccessor)this).invokeSetPierceLevel((byte)piercing);
        this.setDamage(GreatbowItem.BASE_DAMAGE);
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
