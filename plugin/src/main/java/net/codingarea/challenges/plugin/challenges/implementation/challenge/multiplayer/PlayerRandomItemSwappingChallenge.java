package net.codingarea.challenges.plugin.challenges.implementation.challenge.multiplayer;

import net.anweisen.utilities.common.annotations.Since;
import net.codingarea.challenges.plugin.challenges.type.abstraction.PlayerRandomizerSetting;
import net.codingarea.challenges.plugin.challenges.type.abstraction.TimedChallenge;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.menu.generator.categorised.SettingCategory;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Since("2.3.4")
public class PlayerRandomItemSwappingChallenge extends TimedChallenge {

    private long activationCounter;

    public PlayerRandomItemSwappingChallenge() {
        super(MenuType.CHALLENGES, 1, 60, 5);
        setCategory(SettingCategory.MULTIPLAYER_RANDOMIZER);
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        activationCounter = 0;
    }

    @Nonnull
    @Override
    public ItemBuilder createDisplayItem() {
        return new ItemBuilder(Material.HOPPER, Message.forName("item-player-random-swapping-challenge"));
    }

    @Nullable
    @Override
    protected String[] getSettingsDescription() {
        return Message.forName("item-time-seconds-description").asArray(getValue());
    }

    @Override
    public void playValueChangeTitle() {
        ChallengeHelper.playChallengeSecondsValueChangeTitle(this, getValue());
    }

    @Override
    protected int getSecondsUntilNextActivation() {
        return getValue();
    }

    @Override
    protected void onTimeActivation() {
        restartTimer();
        activationCounter++;

        Bukkit.getScheduler().runTask(plugin, () ->
                broadcastFiltered(player -> swapDeterministicRandomItems(player, activationCounter))
        );
    }

    private void swapDeterministicRandomItems(@Nonnull Player player, long contextSeed) {
        Inventory inventory = player.getInventory();

        List<Integer> fullSlots = getFullSlots(inventory);
        if (fullSlots.isEmpty()) return;

        Random random = PlayerRandomizerSetting.createRandomFor(player, contextSeed);

        int slot1 = fullSlots.get(random.nextInt(fullSlots.size()));
        int slot2 = random.nextInt(inventory.getSize());

        if (slot1 == slot2) return;

        ItemStack item1 = inventory.getItem(slot1);
        ItemStack item2 = inventory.getItem(slot2);

        inventory.setItem(slot1, item2);
        inventory.setItem(slot2, item1);
    }

    private List<Integer> getFullSlots(@Nonnull Inventory inventory) {
        List<Integer> slots = new ArrayList<>();

        for (int slot = 0; slot < inventory.getSize(); slot++) {
            ItemStack item = inventory.getItem(slot);

            if (item != null && item.getType() != Material.AIR) {
                slots.add(slot);
            }
        }

        return slots;
    }
}
