package fr.senseitarzan.forms.responses;

import cn.nukkit.Player;

import javax.annotation.Nullable;

public interface SimpleFormResponse extends FormResponse {
    void handle(Player player, @Nullable Object data);
}
