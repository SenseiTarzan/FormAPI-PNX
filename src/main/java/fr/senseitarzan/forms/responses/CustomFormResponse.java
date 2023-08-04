package fr.senseitarzan.forms.responses;

import cn.nukkit.Player;

import java.util.Map;

public interface CustomFormResponse extends FormResponse {


    void handle(Player targetPlayer, Map<Object, Object> data);
}
