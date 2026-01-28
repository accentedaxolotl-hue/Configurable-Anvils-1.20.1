package net.itwaskairo.configurableanvils.mixin;

import net.itwaskairo.configurableanvils.ConfigurableAnvilsConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = AnvilMenu.class, priority = 1001)
public class AnvilMenuMixin {

    @Shadow private DataSlot cost;

    public int MaxCost(){
        if (ConfigurableAnvilsConfig.SERVER.enableNoMaxCost.get()) {
            return 2147483647;
        }
        else if (ConfigurableAnvilsConfig.SERVER.enableCustomMaxCost.get()){
            return ConfigurableAnvilsConfig.SERVER.customMaxCost.get();
        }
        return 40;
    }

    private static int MaxLength(){
        if (ConfigurableAnvilsConfig.SERVER.enableCustomMaxNameLength.get()) {
            return ConfigurableAnvilsConfig.SERVER.customNameLength.get();
        }
        return 50;
    }

    @ModifyConstant(
            method = "createResult",
            constant = @Constant(intValue = 40)
    )
    private int adjustMaxCost(int original) {
        return MaxCost();
    }

    @ModifyConstant(
            method = "createResult",
            constant = @Constant(intValue = 39)
    )
    private int adjustResultingCost(int original) {
        return MaxCost() - 1;
    }

    @ModifyConstant(
            method = "validateName",
            constant = @Constant(intValue = 50)
    )
    private static int modifyMaxNameLength(int original) {
        return MaxLength();
    }

    @Inject(
            method = "mayPickup",
            at = @At("TAIL"),
            cancellable = true
    )
    private void changedMayPickup(Player player, boolean bool, CallbackInfoReturnable<Boolean> cir) {
        if (ConfigurableAnvilsConfig.SERVER.enableCustomRenamingCost.get()) {
            cir.setReturnValue (player.getAbilities().instabuild || player.experienceLevel >= this.cost.get() && this.cost.get() > -1);
        }
        else {
            cir.setReturnValue(player.getAbilities().instabuild || player.experienceLevel >= this.cost.get() && this.cost.get() > 0);
        }
    }

    @Inject(
            method = "createResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/AnvilMenu;broadcastChanges()V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void interceptRenameCost(CallbackInfo ci,
                                     ItemStack itemstack,
                                     int i,
                                     int j,
                                     int k
    ) {
        if (k > 0 && i == k) {
            if (ConfigurableAnvilsConfig.SERVER.enableCustomRenamingCost.get()) {
                this.cost.set(ConfigurableAnvilsConfig.SERVER.customRenamingCost.get());
            }
        }
    }
}