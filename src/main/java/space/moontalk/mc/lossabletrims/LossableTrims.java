package space.moontalk.mc.lossabletrims;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.plugin.java.JavaPlugin;

import org.jetbrains.annotations.NotNull;

public class LossableTrims extends    JavaPlugin
                           implements Listener {
    @EventHandler
    public void onPrepareAnvil(@NotNull PrepareAnvilEvent event) {
        final var inventory = event.getInventory();
        final var first     = inventory.getFirstItem();

        if (first == null || !isTemplate(first.getType()))
            return;

        final var second = inventory.getSecondItem();

        if (second == null || second.getType() != Material.ENCHANTED_BOOK)
            return;

        final var meta = (EnchantmentStorageMeta) second.getItemMeta();

        if (!meta.hasStoredEnchant(Enchantment.VANISHING_CURSE))
            return;

        final var result = first.clone();

        result.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
        inventory.setRepairCost(4);
        
        event.setResult(result);
    }

    @EventHandler
    public void onPrepareItemCraft(@NotNull PrepareItemCraftEvent event) {
        final var recipe = event.getRecipe();

        if (recipe == null)
            return;

        final var result = recipe.getResult();

        if (!isTemplate(result.getType()))
            return;

        final var inventory = event.getInventory();

        for (final var item : inventory)
            if (isTemplate(item.getType()) && item.containsEnchantment(Enchantment.VANISHING_CURSE))
                inventory.setResult(null);
    }

    private static boolean isTemplate(@NotNull Material matrial) {
        return switch (matrial) {
            case COAST_ARMOR_TRIM_SMITHING_TEMPLATE,
                 DUNE_ARMOR_TRIM_SMITHING_TEMPLATE,
                 EYE_ARMOR_TRIM_SMITHING_TEMPLATE,
                 HOST_ARMOR_TRIM_SMITHING_TEMPLATE,
                 RAISER_ARMOR_TRIM_SMITHING_TEMPLATE,
                 RIB_ARMOR_TRIM_SMITHING_TEMPLATE,
                 SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE,
                 SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE,
                 SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE,
                 SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE,
                 SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE,
                 TIDE_ARMOR_TRIM_SMITHING_TEMPLATE,
                 VEX_ARMOR_TRIM_SMITHING_TEMPLATE,
                 WARD_ARMOR_TRIM_SMITHING_TEMPLATE,
                 WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE,
                 WILD_ARMOR_TRIM_SMITHING_TEMPLATE,
                 NETHERITE_UPGRADE_SMITHING_TEMPLATE -> true;

            default -> false;
        };
    }

    @Override
    public void onEnable() {
        final var server = getServer();
        final var manager = server.getPluginManager();
    
        manager.registerEvents(this, this);
    }
}
