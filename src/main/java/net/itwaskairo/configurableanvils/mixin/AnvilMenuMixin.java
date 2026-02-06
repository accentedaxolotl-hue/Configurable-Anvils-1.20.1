package net.itwaskairo.configurableanvils.mixin;

import net.itwaskairo.configurableanvils.ConfigurableAnvilsConfig;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(value = AnvilMenu.class, priority = 2000)
public abstract class AnvilMenuMixin extends AbstractContainerMenu {

    protected AnvilMenuMixin(MenuType<?> type, int id) {
        super(type, id);
    }

    @Shadow @Final private DataSlot cost;

    public int maxCost(){
        if (ConfigurableAnvilsConfig.SERVER.enableNoMaxCost.get()) {
            return 2147483647;
        }
        else {
            return ConfigurableAnvilsConfig.SERVER.customMaxCost.get();
        }
    }

    private static int maxLength(){
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
        return maxCost();
    }

    @ModifyConstant(
            method = "createResult",
            constant = @Constant(intValue = 39)
    )
    private int adjustResultingCost(int original) {
        return maxCost() - 1;
    }

    @ModifyConstant(
            method = "validateName",
            constant = @Constant(intValue = 50)
    )
    private static int modifyMaxNameLength(int original) {
        return maxLength();
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
            method = "isValidBlock",
            at = @At("TAIL"),
            cancellable = true
            )
    private void noAnvilMenu(BlockState blockState, CallbackInfoReturnable<Boolean> cir) {
        if (ConfigurableAnvilsConfig.SERVER.disableAnvils.get()) {
            cir.setReturnValue(false);
        }
        else {
            cir.setReturnValue(blockState.is(BlockTags.ANVIL));
        }
    }

    private int calculateModifier(int level, int maxLevel) {
        int costModifier = maxLevel + level - 1;
        return costModifier;
    }

    private int getBaseCost(Enchantment enchantment) {
        if (enchantment == Enchantments.SHARPNESS) {
            return ConfigurableAnvilsConfig.SERVER.customSharpnessCost.get();
        }
        if (enchantment == Enchantments.BLOCK_EFFICIENCY) {
            return ConfigurableAnvilsConfig.SERVER.customEfficiencyCost.get();
        }
        if (enchantment == Enchantments.MENDING) {
            return ConfigurableAnvilsConfig.SERVER.customMendingCost.get();
        }
        return 0;
    }

    @Inject(
            method = "createResult",
            at = @At("TAIL")
    )
    private void customEnchantCost(CallbackInfo ci) {
        if (ConfigurableAnvilsConfig.SERVER.enableCustomEnchantCosts.get()) {

            ItemStack right = this.getSlot(1).getItem();
            ItemStack left = this.getSlot(0).getItem();

            if (right.is(Items.ENCHANTED_BOOK) && !left.isEmpty()) {
                Map<Enchantment, Integer> enchants =
                        EnchantmentHelper.getEnchantments(right);

                if (enchants.isEmpty()) return;

                int totalCost = 0;

                for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                    Enchantment enchantment = entry.getKey();
                    int level = entry.getValue();

                    int baseCost = getBaseCost(enchantment);

                    int maxLevel = enchantment.getMaxLevel();
                    int modifier = calculateModifier(level, maxLevel) / 2;

                    if (baseCost <= 0) {
                        baseCost = this.cost.get();
                        modifier = 1;
                    }

                    totalCost += baseCost * modifier;
                }

                this.cost.set(totalCost);

            }
        }
    }

    @Inject(
            method = "createResult",
            at = @At("TAIL")
    )
    private void globalCostModifier(CallbackInfo ci) {
        int originalCost = this.cost.get();

        int rounded = Math.max((int) Math.round(originalCost * ConfigurableAnvilsConfig.SERVER.customCostGlobalModifier.get()), 0);

        this.cost.set(rounded);
    }

    @Inject(
            method = "createResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/AnvilMenu;broadcastChanges()V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void interceptRenameCost(CallbackInfo ci, ItemStack itemstack, int i, int j, int k) {
        if (k > 0 && i == k) {
            if (ConfigurableAnvilsConfig.SERVER.enableCustomRenamingCost.get()) {
                this.cost.set(ConfigurableAnvilsConfig.SERVER.customRenamingCost.get());
            }
        }
    }
}