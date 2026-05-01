public abstract class PlayerRandomizerSetting extends Setting {

    public PlayerRandomizerSetting(MenuType menuType) {
        super(menuType);
        setCategory(SettingCategory.MULTIPLAYER_RANDOMIZER);
    }

    protected long createPlayerSeed(Player player, long contextSeed) {
        UUID uuid = player.getUniqueId();

        long seed = contextSeed;
        seed = seed * 31 + uuid.getMostSignificantBits();
        seed = seed * 31 + uuid.getLeastSignificantBits();

        return mix(seed);
    }

    protected long createPlayerChunkSeed(Player player, Chunk chunk, long worldSeed) {
        long seed = worldSeed;
        seed = seed * 31 + chunk.getWorld().getEnvironment().ordinal();
        seed = seed * 31 + chunk.getX();
        seed = seed * 31 + chunk.getZ();

        return createPlayerSeed(player, seed);
    }

    protected int deterministicIndex(Player player, long contextSeed, int size) {
        if (size <= 0) return 0;

        Random random = new Random(createPlayerSeed(player, contextSeed));
        return random.nextInt(size);
    }

    private long mix(long value) {
        value ^= (value >>> 33);
        value *= 0xff51afd7ed558ccdL;
        value ^= (value >>> 33);
        value *= 0xc4ceb9fe1a85ec53L;
        value ^= (value >>> 33);
        return value;
    }
}
