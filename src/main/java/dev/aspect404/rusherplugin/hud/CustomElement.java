package dev.aspect404.rusherplugin.hud;

import org.rusherhack.client.api.feature.hud.HudElement;
import org.rusherhack.client.api.render.RenderContext;
import org.rusherhack.client.api.setting.ColorSetting;
import org.rusherhack.core.setting.BooleanSetting;
import org.rusherhack.core.setting.EnumSetting;
import org.rusherhack.core.setting.StringSetting;

import java.awt.*;

public class CustomElement extends HudElement {
    private enum separators { None, Comma, Colon, Parenthesis, Braces, Brackets }
    private enum align { Left, Center, Right }
    private final BooleanSetting label = new BooleanSetting("Label", "Show label.", true);
    private final BooleanSetting text = new BooleanSetting("Text", "Show text.", true);
    private final StringSetting labelText = new StringSetting("Text", "The text to display.", "RusherHack+");
    private final StringSetting textText = new StringSetting("Text", "The text to display.", "Ultimate Edition");
    private final ColorSetting labelColor = new ColorSetting("Color", "The color of the label.", new Color(255, 255, 255, 255)).setAlphaAllowed(true);
    private final ColorSetting textColor = new ColorSetting("Color", "The color of the text.", new Color(150, 150, 150, 255)).setAlphaAllowed(true).setRainbow(true);
    private final EnumSetting<?> separator = new EnumSetting<>("Separator", "Separate the label and the text.", separators.Braces);
    private final BooleanSetting vertical = new BooleanSetting("Vertical", "Show label and text vertically.", false);
    private final EnumSetting<?> alignment = new EnumSetting<>("Align", "Separate the label and the text.", align.Center);

    public CustomElement() {
        super("CustomHUDElement");
        this.getSettings().clear();
        this.label.addSubSettings(this.labelText, this.labelColor);
        this.text.addSubSettings(this.textText, this.textColor);
        this.vertical.addSubSettings(this.alignment);
        this.text.onChange((T) -> this.separator.setHidden(!this.text.getValue() || this.vertical.getValue()));
        this.vertical.onChange((T) -> this.separator.setHidden(!this.text.getValue() || this.vertical.getValue()));
        this.registerSettings(this.label, this.text, this.vertical, this.separator);
    }

    private String textDisplay = "";
    private double position = 1;

    @Override
    public void renderContent(RenderContext context, int mouseX, int mouseY) {
        if (this.label.getValue()) this.position = this.getFontRenderer().drawString(this.labelText.getValue(), 1, 1, this.labelColor.getValueRGB());
        if (!this.text.getValue()) return;
        if (this.vertical.getValue()) {
            this.position = switch (this.alignment.getDisplayValue()){
                case "Center" -> (this.getFontRenderer().getStringWidth(this.labelText.getValue()) - this.getFontRenderer().getStringWidth(this.textText.getValue())) / 2;
                case "Right" -> this.getFontRenderer().getStringWidth(this.labelText.getValue()) - this.getFontRenderer().getStringWidth(this.textText.getValue()) + 1;
                default -> 1;
            };
            this.getFontRenderer().drawString(this.textText.getValue(), this.position, this.getFontRenderer().getFontHeight() + 1, this.textColor.getValueRGB());
        } else {
            this.textDisplay = switch (this.separator.getDisplayValue()) {
                case "Comma" -> ", " + this.textText.getValue();
                case "Colon" -> ": " + this.textText.getValue();
                case "Parenthesis" -> " (" + this.textText.getValue() + ")";
                case "Braces" -> " [" + this.textText.getValue() + "]";
                case "Brackets" -> " {" + this.textText.getValue() + "}";
                default -> " " + this.textText.getValue();
            };
            this.getFontRenderer().drawString(this.textDisplay, this.position, 1, this.textColor.getValueRGB());
        }
    }

    @Override
    public double getWidth() {
        double labelWidth = this.label.getValue() ? this.getFontRenderer().getStringWidth(this.labelText.getValue()) : 0;
        double textWidth = this.text.getValue() ? this.getFontRenderer().getStringWidth(this.textDisplay) : 0;
        return this.vertical.getValue() ? Math.max(labelWidth, textWidth) + 2 : labelWidth + textWidth + 2;
    }
    @Override
    public double getHeight() {
        return this.vertical.getValue() ? this.getFontRenderer().getFontHeight() * 2 + 2 : this.getFontRenderer().getFontHeight() + 1;
    }
}