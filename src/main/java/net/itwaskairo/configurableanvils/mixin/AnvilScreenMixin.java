package net.itwaskairo.configurableanvils.mixin;

import net.itwaskairo.configurableanvils.ConfigurableAnvilsConfig;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(value = AnvilScreen.class, priority = 2000)
public class AnvilScreenMixin {

    public int MaxCost() {
        if (ConfigurableAnvilsConfig.SERVER.enableNoMaxCost.get()) {
            return 2147483647;
        }
        else {
                return ConfigurableAnvilsConfig.SERVER.customMaxCost.get();
        }
    }

    public int MaxLength() {
        if (ConfigurableAnvilsConfig.SERVER.enableCustomMaxNameLength.get()) {
            return ConfigurableAnvilsConfig.SERVER.customNameLength.get();
        }
        return 50;
    }

    @ModifyConstant(
            method = "renderLabels",
            constant = @Constant(intValue = 40)
    )
    private int adjustMaxCost(int original) {
        return MaxCost();
    }

    @ModifyConstant(
            method = "subInit",
            constant = @Constant(intValue = 50)
    )
    private int adjustNameLength(int original) {
        return MaxLength();
    }
}