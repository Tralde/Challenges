public class PlayerChunkRandomEffectChallenge extends PlayerRandomizerSetting {

    private Map<UUID, PotionEffect> currentPotionEffects = new HashMap<>();
    private long worldSeed;

    public PlayerChunkRandomEffectChallenge() {
        super(MenuType.CHALLENGES);
    }

    @NotNull
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

    private PotionEffect getEffect(Player player, Chunk chunk) {
        PotionEffectType[] types = PotionEffectType.values();

        Random random = new Random(createPlayerChunkSeed(player, chunk, worldSeed));
        PotionEffectType type = types[random.nextInt(types.length)];

        return type.createEffect(Integer.MAX_VALUE, random.nextInt(type.isInstant() ? 1 : 4));
    }
}
