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

    /*                  TO-DO
    1. Add optionally max cost clamping
    2. Add optionally no limit cost
    3. Add optionally separate costs for each tier of gear/tools
    4. Add optionally free renaming
    5. Add Anvil cost modifiers (Increasing + Decreasing)
    6. more to come
    ...
    */
}