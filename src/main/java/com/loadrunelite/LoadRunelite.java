package com.loadrunelite;

import com.playernames.PlayerNamesPlugin;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class LoadRunelite {
    public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(PlayerNamesPlugin.class);
		RuneLite.main(args);
	}
}