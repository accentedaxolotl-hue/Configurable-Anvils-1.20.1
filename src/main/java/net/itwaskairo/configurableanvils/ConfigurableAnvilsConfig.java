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

        public final ForgeConfigSpec.BooleanValue enableCustomMaxCost;
        public final ForgeConfigSpec.BooleanValue enableNoMaxCost;
        public final ForgeConfigSpec.BooleanValue enableCustomMaxNameLength;
        public final ForgeConfigSpec.BooleanValue enableCustomRenamingCost;

        Server(ForgeConfigSpec.Builder builder) {
            builder.push("Configurable Anvils Values");

            customMaxCost = builder
                    .comment("The custom max cost for anvil use that will be implemented if toggled.")
                    .defineInRange("Custom Max Cost Value", 50, 0, 1000);

            customRenamingCost = builder
                    .comment("A custom renaming cost, separate from any other vanilla modifier.")
                    .defineInRange("Custom Max Renaming Value", 0, 0, 1000);

            customNameLength = builder
                    .comment("A custom name max-length that will be used when renaming in an anvil.")
                    .defineInRange("Custom Max Name Length", 50, 0, 1000);

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

            builder.pop();
        }
    }
}