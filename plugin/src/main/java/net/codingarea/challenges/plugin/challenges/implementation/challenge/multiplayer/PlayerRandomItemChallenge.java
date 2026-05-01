package net.codingarea.challenges.plugin.challenges.implementation.challenge.multiplayer;

import net.anweisen.utilities.common.annotations.Since;
import net.codingarea.challenges.plugin.challenges.type.abstraction.PlayerRandomizerSetting;
import net.codingarea.challenges.plugin.challenges.type.abstraction.TimedChallenge;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.menu.generator.categorised.SettingCategory;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.item.ItemUtils;
import net.codingarea.challenges.plugin.utils.misc.ExperimentalUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Since("2.3.4")
public class PlayerRandomItemChallenge extends TimedChallenge {

    private final List<Material> materials = new ArrayList<>();
    private long activationCounter;

    public PlayerRandomItemChallenge() {
        super(MenuType.CHALLENGES, 1, 60, 30, false);
        setCategory(SettingCategory.MULTIPLAYER_RANDOMIZER);
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        activationCounter = 0;
        reloadMaterials();
    }

    @Nonnull
    @Override
    public ItemBuilder createDisplayItem() {
        return new ItemBuilder(Material.BEACON, Message.forName("item-player-random-item-challenge"));
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

        broadcastFiltered(player -> giveDeterministicRandomItem(player, activationCounter));
    }

    private void giveDeterministicRandomItem(@Nonnull Player player, long contextSeed) {
        if (materials.isEmpty()) reloadMaterials();
        if (materials.isEmpty()) return;

        Material material = PlayerRandomizerSetting.deterministicPickFor(player, contextSeed, materials);
        player.getInventory().addItem(new ItemStack(material, 1));
    }

    private void reloadMaterials() {
        materials.clear();
        materials.addAll(Arrays.asList(ExperimentalUtils.getMaterials()));
        materials.removeIf(material -> !material.isItem() || !ItemUtils.isObtainableInSurvival(material));
    }
}
