package net.itwaskairo.configurableanvils.mixin;

import net.itwaskairo.configurableanvils.ConfigurableAnvilsConfig;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AnvilMenu.class, priority = 1001)
public class AnvilMenuMixin {

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

    //@Shadow
    //private boolean onlyRenaming;

    @Shadow
    private DataSlot cost;

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

    @Inject(method = "mayPickup", at = @At("TAIL"), cancellable = true)
    private void changedMayPickup(CallbackInfoReturnable<Boolean> cir) {
        if (ConfigurableAnvilsConfig.SERVER.enableCustomRenamingCost.get()) {
            cir.setReturnValue(this.cost.get() > -1);
        }
    }

    /*
    @Inject(
            method = "createResult",
            at = @At("TAIL")
    )
    private void fixRenameCost(CallbackInfo ci) {
        if (!ConfigurableAnvilsConfig.SERVER.enableCustomRenamingCost.get()) return;

        if (this.onlyRenaming) {
            this.cost.set(ConfigurableAnvilsConfig.SERVER.customRenamingCost.get());
        }
    }
    */
}