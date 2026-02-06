package net.itwaskairo.configurableanvils.mixin;

import net.itwaskairo.configurableanvils.manager.ConfigurableAnvilsMixinChecker;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mixin(value = Minecraft.class, priority = 100000)
public class MinecraftStartupMixin {
    private static final Logger configurableAnvilsLogger = LogManager.getLogger("ConfigurableAnvils");

    @Inject(method = "run", at = @At("HEAD"))
    private void onStartup (CallbackInfo ci){
                configurableAnvilsLogger.info("Startup Successful");
                configurableAnvilsLogger.info("Unique Mixins loaded: {}", ConfigurableAnvilsMixinChecker.getPreAppliedMixinCount());
                configurableAnvilsLogger.info("Unique Mixins applied: {}", ConfigurableAnvilsMixinChecker.getPostAppliedMixinCount());
    }
}