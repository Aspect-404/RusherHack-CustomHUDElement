package dev.aspect404.rusherplugin;

import dev.aspect404.rusherplugin.hud.CustomElement;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.plugin.Plugin;

public class init extends Plugin {
	@Override
	public void onLoad() {
		this.getLogger().info(this.getName() + " loaded!");
		RusherHackAPI.getHudManager().registerFeature(new CustomElement());
	}
	@Override
	public void onUnload() { this.getLogger().info(this.getName() + " unloaded!"); }
	@Override
	public String getName() { return "CustomHUDElement"; }
	@Override
	public String getVersion() { return "1.0"; }
	@Override
	public String getDescription() { return "Make a customizable HUD element."; }
	@Override
	public String[] getAuthors() { return new String[]{"Aspect404_"}; }
}