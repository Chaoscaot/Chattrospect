package de.chaoscaot.chattrospect.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DrawContext.class)
public class ChattrospectScreenMixin {

    private static final String[] MESSAGES = new String[] {
            "§9Open URL: §l%s",
            "§9Open File: §l%s",
            "§9Run Command: §l%s",
            "§9Suggest Command: §l%s",
            "§9Change Page: §l%s",
            "§9Copy to Clipboard: §l%s"
    };

    @Redirect(method = "drawHoverEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Style;getHoverEvent()Lnet/minecraft/text/HoverEvent;"))
    private HoverEvent hoverEventGetHoverEventRedirect(Style instance) {
        HoverEvent hoverEvent = instance.getHoverEvent();
        boolean n = false;
        if (hoverEvent == null) {
            n = true;
            hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(""));
        }
        Text text = hoverEvent.getValue(HoverEvent.Action.SHOW_TEXT);

        if(text != null && instance.getClickEvent() != null) {
            ClickEvent clickEvent = instance.getClickEvent();
            MutableText mutableText = text.copy();
            if(!n) {
                mutableText.append("\n");
            }
            mutableText.append(Text.literal(String.format(MESSAGES[clickEvent.getAction().ordinal()], clickEvent.getValue())));
            return new HoverEvent(HoverEvent.Action.SHOW_TEXT, mutableText);
        } else {
            return hoverEvent;
        }
    }
}
