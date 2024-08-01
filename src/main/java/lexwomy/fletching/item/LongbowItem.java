package lexwomy.fletching.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.BowItem;
import org.jetbrains.annotations.Nullable;

public class LongbowItem extends BowItem {
    public static final int TICKS_PER_SECOND = 20;
    public static final int RANGE = 20;

    public LongbowItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getRange() {
        return RANGE;
    }

    @Override
    protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {

    }
}
