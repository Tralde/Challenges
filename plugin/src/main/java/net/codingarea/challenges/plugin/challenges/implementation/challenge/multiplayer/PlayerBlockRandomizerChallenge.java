package net.codingarea.challenges.plugin.challenges.implementation.challenge.multiplayer;

import net.anweisen.utilities.bukkit.utils.misc.BukkitReflectionUtils;
import net.anweisen.utilities.common.annotations.Since;
import net.codingarea.challenges.plugin.challenges.type.abstraction.PlayerRandomizerSetting;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.item.ItemUtils;
import net.codingarea.challenges.plugin.utils.misc.ExperimentalUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Since("2.3.4")
public class PlayerBlockRandomizerChallenge extends PlayerRandomizerSetting {

    private final List<Material> drops = new ArrayList<>();
    private long randomizationSeed;

    public PlayerBlockRandomizerChallenge() {
        super(MenuType.CHALLENGES);
    }

    @Override
    protected void onEnable() {
        reloadDrops();
        randomizationSeed = System.currentTimeMillis();
    }

    @Override
    protected void onDisable() {
        drops.clear();
        randomizationSeed = 0;
    }

    @Nonnull
    @Override
    public ItemBuilder createDisplayItem() {
        return new ItemBuilder(Material.MINECART, Message.forName("item-player-block-randomizer-challenge"));
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!shouldExecuteEffect()) return;

        Player player = event.getPlayer();
        if (ignorePlayer(player)) return;
        if (player.getGameMode() == GameMode.CREATIVE) return;

        Block block = event.getBlock();
        Material drop = getDropFor(player, block.getType());

        if (drop == null) return;

        event.setDropItems(false);
        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(drop, 1));
    }

    private Material getDropFor(@Nonnull Player player, @Nonnull Material blockType) {
        if (drops.isEmpty()) reloadDrops();
        if (drops.isEmpty()) return null;

        long contextSeed = randomizationSeed * 31 + blockType.ordinal();
        return deterministicPick(player, contextSeed, drops);
    }

    private void reloadDrops() {
        drops.clear();
        drops.addAll(Arrays.asList(ExperimentalUtils.getMaterials()));
        drops.removeIf(material -> !material.isItem()
                || !ItemUtils.isObtainableInSurvival(material)
                || BukkitReflectionUtils.isAir(material));
    }
}
