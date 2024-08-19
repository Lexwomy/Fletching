package lexwomy.fletching.mixin;

import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PersistentProjectileEntity.class)
public interface PierceLevelAccessor {
    @Invoker("setPierceLevel")
    void invokeSetPierceLevel(byte level);
}
