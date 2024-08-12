package lexwomy.fletching.mixin;

import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PersistentProjectileEntity.class)
public interface ExposeArrowDamageMixin {
    @Accessor
    double getDamage();

    @Accessor("damage")
    void setDamage(double damage);
}
