package net.itwaskairo.configurableanvils;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ConfigurableAnvilsConfig {
    public static final ForgeConfigSpec SERVER_SPEC;
    public static final Server SERVER;

    static {
        Pair<Server, ForgeConfigSpec> serverPair =
                new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER = serverPair.getLeft();
        SERVER_SPEC = serverPair.getRight();
    }

    public static class Server {
        public final ForgeConfigSpec.IntValue customMaxCost;
        public final ForgeConfigSpec.IntValue customRenamingCost;
        public final ForgeConfigSpec.IntValue customNameLength;
        public final ForgeConfigSpec.IntValue customSharpnessCost;
        public final ForgeConfigSpec.IntValue customEfficiencyCost;
        public final ForgeConfigSpec.ConfigValue<Double> customCostGlobalModifier;

        public final ForgeConfigSpec.BooleanValue enableCustomEnchantCosts;
        public final ForgeConfigSpec.BooleanValue enableCustomMaxCost;
        public final ForgeConfigSpec.BooleanValue enableNoMaxCost;
        public final ForgeConfigSpec.BooleanValue enableCustomMaxNameLength;
        public final ForgeConfigSpec.BooleanValue enableCustomRenamingCost;
        public final ForgeConfigSpec.BooleanValue disableAnvils;

        Server(ForgeConfigSpec.Builder builder) {
            builder.push("Configurable Anvils Values");

            customMaxCost = builder
                    .comment("The custom max cost for anvil use that will be implemented if toggled.")
                    .defineInRange("Custom Max Cost", 50, 0, 1000);

            customRenamingCost = builder
                    .comment("A custom renaming cost, separate from any other vanilla modifier.")
                    .defineInRange("Custom Max Renaming Cost", 0, 0, 1000);

            customNameLength = builder
                    .comment("A custom name max-length that will be used when renaming in an anvil.")
                    .defineInRange("Custom Max Name Length", 50, 0, 1000);

            customCostGlobalModifier = builder
                    .comment("A custom global modifier for every anvil use.")
                    .comment("NOTE: This is multiplied with the base cost and a custom rename cost is exempt from this modifier.")
                    .defineInRange("Custom Global Modifier", 1.0, 0, 1000);

            customSharpnessCost = builder
                    .comment("A custom cost that will be used (with scaling) when enchanting an item with sharpness in an anvil.")
                    .defineInRange("Custom Sharpness Cost", 6, 0, 1000);

            customEfficiencyCost = builder
                    .comment("A custom cost that will be used (with scaling) when enchanting an item with efficiency in an anvil.")
                    .defineInRange("Custom Efficiency Cost", 4, 0, 1000);

            builder.pop();

            builder.push("Configurable Anvils Toggles");

            enableCustomMaxCost = builder
                    .comment("Enable a custom max cost for anvils")
                    .define("Custom Max Cost Toggle", false);

            enableNoMaxCost = builder
                    .comment("Enable anvils to not have any max cost")
                    .define("No Max Cost Toggle", false);

            enableCustomMaxNameLength = builder
                    .comment("Enable anvils to have a specified max name length instead of the vanilla 50")
                    .define("No Max Length Toggle", false);

            enableCustomRenamingCost = builder
                    .comment("Enable anvils to have a fixed custom renaming cost")
                    .define("Custom Renaming Cost Toggle", true);

            enableCustomEnchantCosts = builder
                    .comment("Enable anvils to have a fixed cost for specific popular and widely used enchants")
                    .define("Custom Enchant Costs Toggle", false);

            disableAnvils = builder
                    .comment("Disable anvils altogether")
                    .define("Disable Anvils Toggle", false);

            builder.pop();
        }
    }
}