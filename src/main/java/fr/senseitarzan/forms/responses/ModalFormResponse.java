package fr.senseitarzan.forms.responses;

import cn.nukkit.Player;

import javax.annotation.Nullable;

public interface ModalFormResponse extends FormResponse {


    void handle(Player targetPlayer, @Nullable Boolean data);
}
