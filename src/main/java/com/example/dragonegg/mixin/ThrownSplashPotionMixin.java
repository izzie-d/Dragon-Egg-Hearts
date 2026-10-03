package com.example.dragonegg.mixin;

import com.example.dragonegg.DragonEggHeartsMod;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownSplashPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownSplashPotion.class)
public class ThrownSplashPotionMixin {
    @Inject(
            method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At("TAIL")
    )
    private void dragonegghearts$scalePotionDurationForEggCarrier(
            Level level,
            LivingEntity shooter,
            ItemStack stack,
            CallbackInfo ci
    ) {
        if (level.isClientSide() || !(shooter instanceof ServerPlayer serverPlayer)) {
            return;
        }

        float scale = DragonEggHeartsMod.getSplashPotionDurationScale(serverPlayer);
        if (scale <= 1.0f) {
            return;
        }

        ThrownSplashPotion self = (ThrownSplashPotion) (Object) this;
        ItemStack thrownStack = self.getItem();
        if (thrownStack.isEmpty()) {
            thrownStack = stack;
        }
        if (thrownStack.isEmpty()) {
            return;
        }

        thrownStack.set(DataComponents.POTION_DURATION_SCALE, scale);
    }
}