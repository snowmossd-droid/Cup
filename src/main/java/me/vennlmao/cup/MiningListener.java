package me.vennlmao.cup;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class MiningListener implements Listener {

    private final CupPlugin plugin;

    public MiningListener(CupPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;

        Block block = event.getBlock();

        Set<Material> ores = plugin.getConfig().getStringList("ores")
                .stream()
                .map(Material::matchMaterial)
                .filter(m -> m != null)
                .collect(Collectors.toSet());

        if (!ores.contains(block.getType())) return;

        ItemStack hand = event.getPlayer().getInventory().getItemInMainHand();

        if (hand.containsEnchantment(Enchantment.FORTUNE)) return;
        if (hand.containsEnchantment(Enchantment.SILK_TOUCH)) return;

        int level = plugin.getConfig().getInt("fortune-level", 3);

        ItemStack fakeTool = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta = fakeTool.getItemMeta();
        if (meta != null) {
            meta.addEnchant(Enchantment.FORTUNE, level, true);
            fakeTool.setItemMeta(meta);
        }

        event.setDropItems(false);

        Collection<ItemStack> drops = block.getDrops(fakeTool);
        for (ItemStack drop : drops) {
            block.getWorld().dropItemNaturally(block.getLocation(), drop);
        }
    }
            }
