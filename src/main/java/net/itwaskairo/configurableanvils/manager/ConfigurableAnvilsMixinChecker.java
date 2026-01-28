package net.itwaskairo.configurableanvils.manager;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigurableAnvilsMixinChecker implements IMixinConfigPlugin {

    private static final Set<String> configurableAnvilsPostAppliedMixins = new HashSet<>();
    private static final Set<String> configurableAnvilsPreAppliedMixins = new HashSet<>();

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins()
    {
        return null;
    }

    @Override
    public void preApply(
            String targetClassName,
            ClassNode targetClass,
            String mixinClassName,
            IMixinInfo mixinInfo
    ) {
        configurableAnvilsPreAppliedMixins.add(mixinClassName);
    }

    @Override
    public void postApply(
            String targetClassName,
            ClassNode targetClass,
            String mixinClassName,
            IMixinInfo mixinInfo
    ) {
        configurableAnvilsPostAppliedMixins.add(mixinClassName);
    }

    public static int getPostAppliedMixinCount() {
        return configurableAnvilsPostAppliedMixins.size();
    }

    public static int getPreAppliedMixinCount() {
        return configurableAnvilsPreAppliedMixins.size();
    }
}