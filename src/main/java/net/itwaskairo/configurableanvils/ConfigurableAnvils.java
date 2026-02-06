package net.itwaskairo.configurableanvils;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ConfigurableAnvils.modID)
public final class ConfigurableAnvils {
    public static final String modID = "configurableanvils";

    public ConfigurableAnvils(FMLJavaModLoadingContext context) {
        context.registerConfig(
                ModConfig.Type.SERVER,
                ConfigurableAnvilsConfig.SERVER_SPEC
        );
    }
}