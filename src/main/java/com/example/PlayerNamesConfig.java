package net.runelite.client.plugins.playernames;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import java.awt.Color;

@ConfigGroup("playernames")
public interface PlayerNamesConfig extends Config
{
    @ConfigItem(
            keyName = "onlyShowInPvpZones",
            name = "Only show in PvP zones",
            description = "Toggle to show player info only in PvP zones (Wilderness, PvP worlds) or everywhere",
            position = 0
    )
    default boolean onlyShowInPvpZones()
    {
        return true; // Enabled by default
    }

    @ConfigItem(
            keyName = "showOnMinimap",
            name = "Show on Minimap",
            description = "Toggle to show player names on the minimap overlay",
            position = 1
    )
    default boolean showOnMinimap()
    {
        return true; // Enabled by default
    }

    @ConfigItem(
            keyName = "dangerousColor",
            name = "Dangerous Color",
            description = "Color for players within your combat bracket",
            position = 2
    )
    default Color dangerousColor()
    {
        return Color.RED;
    }

    @ConfigItem(
            keyName = "potentialThreatColor",
            name = "Potential Threat Color",
            description = "Color for players just outside your combat bracket by < 5 levels",
            position = 3
    )
    default Color potentialThreatColor()
    {
        return Color.ORANGE;
    }

    @ConfigItem(
            keyName = "safeColor",
            name = "Safe Color",
            description = "Color for players outside your combat bracket",
            position = 4
    )
    default Color safeColor()
    {
        return Color.GREEN;
    }
}