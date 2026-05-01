package net.codingarea.challenges.plugin.challenges.type.abstraction;

import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.menu.generator.categorised.SettingCategory;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Base/helper for player scoped randomizer challenges.
 *
 * Important:
 * Timed player-randomizer challenges cannot extend this class directly because
 * they already have to extend TimedChallenge. Therefore the seed helper methods
 * are static and can also be reused from TimedChallenge implementations.
 */
public abstract class PlayerRandomizerSetting extends Setting {

    public PlayerRandomizerSetting(@Nonnull MenuType menu) {
        super(menu);
        setCategory(SettingCategory.MULTIPLAYER_RANDOMIZER);
    }

    public PlayerRandomizerSetting(@Nonnull MenuType menu, boolean enabledByDefault) {
        super(menu, enabledByDefault);
        setCategory(SettingCategory.MULTIPLAYER_RANDOMIZER);
    }

    protected long createPlayerSeed(@Nonnull Player player, long contextSeed) {
        return createPlayerSeedFor(player, contextSeed);
    }

    protected long createPlayerChunkSeed(@Nonnull Player player, @Nonnull Chunk chunk, long worldSeed) {
        return createPlayerChunkSeedFor(player, chunk, worldSeed);
    }

    protected int deterministicIndex(@Nonnull Player player, long contextSeed, int size) {
        return deterministicIndexFor(player, contextSeed, size);
    }

    protected <T> T deterministicPick(@Nonnull Player player, long contextSeed, @Nonnull List<T> values) {
        return deterministicPickFor(player, contextSeed, values);
    }

    public static long createPlayerSeedFor(@Nonnull Player player, long contextSeed) {
        UUID uuid = player.getUniqueId();

        long seed = contextSeed;
        seed = seed * 31 + uuid.getMostSignificantBits();
        seed = seed * 31 + uuid.getLeastSignificantBits();

        return mix(seed);
    }

    public static long createPlayerChunkSeedFor(@Nonnull Player player, @Nonnull Chunk chunk, long worldSeed) {
        long seed = worldSeed;
        seed = seed * 31 + chunk.getWorld().getEnvironment().ordinal();
        seed = seed * 31 + chunk.getX();
        seed = seed * 31 + chunk.getZ();

        return createPlayerSeedFor(player, seed);
    }

    public static long createPlayerMaterialSeedFor(@Nonnull Player player, @Nonnull Material material, long contextSeed) {
        long seed = contextSeed;
        seed = seed * 31 + material.ordinal();

        return createPlayerSeedFor(player, seed);
    }

    public static int deterministicIndexFor(@Nonnull Player player, long contextSeed, int size) {
        if (size <= 0) return 0;

        Random random = new Random(createPlayerSeedFor(player, contextSeed));
        return random.nextInt(size);
    }

    public static <T> T deterministicPickFor(@Nonnull Player player, long contextSeed, @Nonnull List<T> values) {
        if (values.isEmpty()) {
            throw new IllegalArgumentException("values must not be empty");
        }

        return values.get(deterministicIndexFor(player, contextSeed, values.size()));
    }

    public static Random createRandomFor(@Nonnull Player player, long contextSeed) {
        return new Random(createPlayerSeedFor(player, contextSeed));
    }

    private static long mix(long value) {
        value ^= value >>> 33;
        value *= 0xff51afd7ed558ccdL;
        value ^= value >>> 33;
        value *= 0xc4ceb9fe1a85ec53L;
        value ^= value >>> 33;
        return value;
    }
}
