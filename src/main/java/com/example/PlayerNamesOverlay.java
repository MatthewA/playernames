package net.runelite.client.plugins.playernames;

import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import javax.inject.Inject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

public class PlayerNamesOverlay extends Overlay
{
    private final Client client;
    private final PlayerNamesConfig config;
    private final PlayerNamesPlugin plugin;

    @Inject
    private PlayerNamesOverlay(Client client, PlayerNamesConfig config, PlayerNamesPlugin plugin)
    {
        this.client = client;
        this.config = config;
        this.plugin = plugin;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!plugin.shouldShowPlayerInfo()) {
            return null;
        }

        Player localPlayer = client.getLocalPlayer();
        if (localPlayer == null) {
            return null;
        }

        for (Player player : client.getPlayers())
        {
            if (player == null || player == localPlayer || player.getName() == null) {
                continue;
            }

            int playerCombatLevel = player.getCombatLevel();
            Color nameColor = plugin.getPlayerColor(playerCombatLevel, localPlayer);

            // Render name and combat level above head
            Point textLocation = player.getCanvasTextLocation(graphics, player.getName() + " (level " + playerCombatLevel + ")", player.getLogicalHeight() + 20);
            if (textLocation != null) {
                OverlayUtil.renderTextLocation(graphics, textLocation, player.getName() + " (level " + playerCombatLevel + ")", nameColor);
            }
        }

        return null;
    }
}