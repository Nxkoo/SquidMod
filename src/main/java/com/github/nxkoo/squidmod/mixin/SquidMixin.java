package com.github.nxkoo.squidmod.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SquidEntity.class)
public class SquidMixin {

    @Inject(method = "aiStep", at = @At("HEAD"))
    public void aiStep(CallbackInfo ci) {
        SquidEntity squid = ((SquidEntity) (Object) this);
        LivingEntity target = squid.getTarget();
        if (target == null) {
            target = squid.level.getNearestPlayer(squid.getX(),
                    squid.getY(), squid.getZ(), 5.0D, true);
        }
        if (target != null) {
            BlockPos targetPos = new BlockPos(target.getX(), target.getY(), target.getZ());
            if (!squid.level.getBlockState(targetPos).getMaterial().isSolid()) {

                squid.moveTo(target.getX(), target.getY(), target.getZ(), 1.0F, 1.0F);

                double distance = squid.distanceTo(target);
                if (distance < 2.0D) {
                    target.hurt(DamageSource.mobAttack(squid), 5.0f);
                }
            }
        }
    }
}
