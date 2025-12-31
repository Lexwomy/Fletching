package lexwomy.fletching.item;

import lexwomy.fletching.entity.PilumEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class PilumItem extends Item implements ProjectileItem {
    public PilumItem(Item.Properties settings) {
        super(settings);
    }

    public AbstractArrow createPilum(Level world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
        return new PilumEntity(world, shooter, stack.copyWithCount(1), shotFrom);
    }

    @Override
    public Projectile asProjectile(Level world, Position pos, ItemStack stack, Direction direction) {
        PilumEntity pilumEntity = new PilumEntity(world, pos.x(), pos.y(), pos.z(), stack.copyWithCount(1), null);
        pilumEntity.pickup = AbstractArrow.Pickup.ALLOWED;
        return pilumEntity;
    }


}
