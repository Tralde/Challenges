package net.codingarea.challenges.plugin.challenges.implementation.challenge.multiplayer;

import net.anweisen.utilities.common.annotations.Since;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.abstraction.PlayerRandomizerSetting;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.scheduler.task.ScheduledTask;
import net.codingarea.challenges.plugin.spigot.events.PlayerIgnoreStatusChangeEvent;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Since("2.3.4")
public class PlayerChunkRandomEffectChallenge extends PlayerRandomizerSetting {

    private Map<UUID, PotionEffect> currentPotionEffects = new HashMap<>();
    private long worldSeed;

    public PlayerChunkRandomEffectChallenge() {
        super(MenuType.CHALLENGES);
    }

    @Nonnull
    @Override
    public ItemBuilder createDisplayItem() {
        return new ItemBuilder(
                Material.POTION,
                Message.forName("item-player-chunk-effect-challenge")
        );
    }

    @Override
    protected void onEnable() {
        currentPotionEffects = new HashMap<>();
        worldSeed = ChallengeAPI.getGameWorld(World.Environment.NORMAL).getSeed();

        broadcastFiltered(player -> addEffect(player, player.getLocation()));
    }

    @Override
    protected void onDisable() {
        if (currentPotionEffects != null) {
            broadcastFiltered(this::removeCurrentEffect);
            currentPotionEffects.clear();
        }

        worldSeed = 0;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        if (!shouldExecuteEffect()) return;
        if (event.getTo() == null) return;
        if (ignorePlayer(event.getPlayer())) return;
        if (isSameChunk(event.getFrom(), event.getTo())) return;

        addEffect(event.getPlayer(), event.getTo());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        if (!shouldExecuteEffect()) return;
        if (event.getTo() == null) return;
        if (ignorePlayer(event.getPlayer())) return;
        if (isSameChunk(event.getFrom(), event.getTo())) return;

        addEffect(event.getPlayer(), event.getTo());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onGameModeChange(PlayerIgnoreStatusChangeEvent event) {
        if (!shouldExecuteEffect()) return;

        if (event.isIgnored()) {
            removeCurrentEffect(event.getPlayer());
        } else {
            addEffect(event.getPlayer(), event.getPlayer().getLocation());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        currentPotionEffects.remove(event.getPlayer().getUniqueId());
    }

    @ScheduledTask(ticks = 20, async = false)
    public void onSecond() {
        if (!shouldExecuteEffect()) return;

        broadcastFiltered(player -> addEffect(player, player.getLocation()));
    }

    private boolean isSameChunk(@Nonnull Location from, @Nonnull Location to) {
        if (from.getWorld() == null || to.getWorld() == null) return false;
        if (!from.getWorld().equals(to.getWorld())) return false;

        return (from.getBlockX() >> 4) == (to.getBlockX() >> 4)
                && (from.getBlockZ() >> 4) == (to.getBlockZ() >> 4);
    }

    private void addEffect(@Nonnull Player player, @Nonnull Location location) {
        Chunk chunk = location.getChunk();
        PotionEffect newEffect = getEffect(player, chunk);
        PotionEffect currentEffect = currentPotionEffects.get(player.getUniqueId());

        if (currentEffect != null) {
            if (currentEffect.getType().equals(newEffect.getType())
                    && currentEffect.getAmplifier() == newEffect.getAmplifier()) {
                return;
            }

            player.removePotionEffect(currentEffect.getType());
        }

        player.addPotionEffect(newEffect);
        currentPotionEffects.put(player.getUniqueId(), newEffect);
    }

    private void removeCurrentEffect(@Nonnull Player player) {
        PotionEffect currentEffect = currentPotionEffects.remove(player.getUniqueId());

        if (currentEffect != null) {
            player.removePotionEffect(currentEffect.getType());
        }
    }

    private PotionEffect getEffect(@Nonnull Player player, @Nonnull Chunk chunk) {
        Random random = new Random(createPlayerChunkSeed(player, chunk, worldSeed));
        PotionEffectType[] types = PotionEffectType.values();
        PotionEffectType type = types[random.nextInt(types.length)];

        return type.createEffect(Integer.MAX_VALUE, random.nextInt(type.isInstant() ? 1 : 4));
    }
}
