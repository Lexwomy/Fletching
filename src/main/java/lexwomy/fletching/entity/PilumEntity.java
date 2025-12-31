package lexwomy.fletching.entity;

import lexwomy.fletching.component.FletchingComponents;
import lexwomy.fletching.item.FletchingItems;
import lexwomy.fletching.item.GreatbowItem;
import lexwomy.fletching.mixin.PierceLevelAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class PilumEntity extends AbstractArrow {
    public PilumEntity(EntityType<? extends PilumEntity> entityType, Level world) {
        super(entityType, world);
    }

    public PilumEntity(Level world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(FletchingEntities.PILUM, x, y, z, world, stack, shotFrom);
        this.setPilumAttributes(stack);
    }

    public PilumEntity(Level world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(FletchingEntities.PILUM, owner, world, stack, shotFrom);
        this.setPilumAttributes(stack);
    }

    private void setPilumAttributes(ItemStack stack) {
        int piercing = stack.getOrDefault(FletchingComponents.PIERCING, 1);
        ((PierceLevelAccessor)this).invokeSetPierceLevel((byte)piercing);
        this.setBaseDamage(GreatbowItem.BASE_DAMAGE);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(FletchingItems.FLINT_PILUM);
    }

    @Override
    protected void doPostHurtEffects(LivingEntity target) {
        super.doPostHurtEffects(target);


    }
}
