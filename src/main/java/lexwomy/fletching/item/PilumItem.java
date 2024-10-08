package lexwomy.fletching.item;

import lexwomy.fletching.entity.PilumEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PilumItem extends Item implements ProjectileItem {
    public PilumItem(Item.Settings settings) {
        super(settings);
    }

    public PersistentProjectileEntity createPilum(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
        return new PilumEntity(world, shooter, stack.copyWithCount(1), shotFrom);
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        PilumEntity pilumEntity = new PilumEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1), null);
        pilumEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        return pilumEntity;
    }


}
