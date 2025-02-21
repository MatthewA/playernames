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

public class PlayerNamesMinimapOverlay extends Overlay
{
    private final Client client;
    private final PlayerNamesConfig config;
    private final PlayerNamesPlugin plugin;

    @Inject
    private PlayerNamesMinimapOverlay(Client client, PlayerNamesConfig config, PlayerNamesPlugin plugin)
    {
        this.client = client;
        this.config = config;
        this.plugin = plugin;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS); // Render above minimap widget
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!plugin.shouldShowPlayerInfo() || !config.showOnMinimap()) {
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
            Point minimapLocation = player.getMinimapLocation();
            if (minimapLocation != null) {
                Point adjustedMinimapLocation = new Point(minimapLocation.getX(), minimapLocation.getY() - 5);
                OverlayUtil.renderTextLocation(graphics, adjustedMinimapLocation, player.getName(), nameColor);
            }
        }

        return null;
    }
}