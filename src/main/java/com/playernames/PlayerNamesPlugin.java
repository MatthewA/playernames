package com.playernames;

import com.google.inject.Provides;
import net.runelite.api.Varbits;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.WorldType;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import javax.inject.Inject;
import java.awt.Color;

@PluginDescriptor(
        name = "Player Names",
        description = "Shows player names and combat levels with color-coded combat brackets",
        tags = {"pvp", "wilderness", "combat", "overlay"}
)
public class PlayerNamesPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private PlayerNamesConfig config;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private PlayerNamesOverlay overlay;

    @Inject
    private PlayerNamesMinimapOverlay minimapOverlay;

    private static final int PVP_WORLD_LEVEL_RANGE = 15;

    @Override
    protected void startUp() throws Exception
    {
        overlayManager.add(overlay);
        overlayManager.add(minimapOverlay);
    }

    @Override
    protected void shutDown() throws Exception
    {
        overlayManager.remove(overlay);
        overlayManager.remove(minimapOverlay);
    }

    @Provides
    PlayerNamesConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(PlayerNamesConfig.class);
    }

    @Subscribe
    public void onGameTick(GameTick event)
    {
        // Empty, kept for potential future use
    }

    public boolean shouldShowPlayerInfo()
    {
        return !config.onlyShowInPvpZones() || (client.getVarbitValue(Varbits.IN_WILDERNESS) == 1 || WorldType.isPvpWorld(client.getWorldType()));
    }

    public int getWildernessLevel()
    {
        if (!shouldShowPlayerInfo()) {
            return 0;
        }

        Widget wildernessWidget = client.getWidget(WidgetInfo.PVP_WILDERNESS_LEVEL);
        if (wildernessWidget != null && !wildernessWidget.isHidden()) {
            String text = wildernessWidget.getText(); // e.g., "Level: 21<br>6-48"
            try {
                String levelPart = text.split("<br>")[0].replace("Level: ", "").trim();
                return Integer.parseInt(levelPart);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                // Silently fail
            }
        }
        return 0; // Fallback
    }

    public Color getPlayerColor(int targetCombatLevel, Player localPlayer)
    {
        int localCombatLevel = localPlayer.getCombatLevel();
        int wildernessLevel = getWildernessLevel();
        boolean inWilderness = client.getVarbitValue(Varbits.IN_WILDERNESS) == 1;
        boolean inPvpWorld = WorldType.isPvpWorld(client.getWorldType());

        int minAttackable;
        int maxAttackable;

        if (inWilderness) {
            minAttackable = localCombatLevel - wildernessLevel;
            maxAttackable = localCombatLevel + wildernessLevel;
        } else if (inPvpWorld) {
            minAttackable = localCombatLevel - PVP_WORLD_LEVEL_RANGE;
            maxAttackable = localCombatLevel + PVP_WORLD_LEVEL_RANGE;
        } else {
            return config.safeColor();
        }

        if (targetCombatLevel >= minAttackable && targetCombatLevel <= maxAttackable) {
            return config.dangerousColor();
        }

        int levelDiff = Math.abs(localCombatLevel - targetCombatLevel);
        if (levelDiff <= 5 && (targetCombatLevel < minAttackable || targetCombatLevel > maxAttackable)) {
            return config.potentialThreatColor();
        }

        return config.safeColor();
    }
}