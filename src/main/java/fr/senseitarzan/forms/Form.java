package fr.senseitarzan.forms;

import cn.nukkit.Player;
import cn.nukkit.network.protocol.ModalFormRequestPacket;
import com.google.gson.Gson;
import fr.senseitarzan.forms.responses.FormResponse;
import org.checkerframework.checker.interning.qual.InternMethod;
import org.checkerframework.checker.interning.qual.Interned;

import javax.annotation.Nullable;
import java.util.*;

public abstract class Form {

    protected static final Gson GSON = new Gson();
    protected transient boolean closed = false;
    protected transient boolean isSetting = false;
    public static WeakHashMap<Player, Integer> formId = new WeakHashMap<>();

    public static WeakHashMap<Player, Map<Integer, Form>> playerForms = new WeakHashMap<>();
    public static WeakHashMap<Player, Map<Integer, Form>> playerServerSettings = new WeakHashMap<>();


    public String title = "";
    public transient Map<Integer, String> labelsByData = new WeakHashMap<>();
    public transient FormResponse handler;

    public void send(Player player){
        this.showFormWindow(player, formId.put(player, formId.get(player) + 1));
    }

    protected int showFormWindow(Player player, Integer id) {
        Map<Integer, Form> formsPlayer = playerForms.get(player);
        if (formsPlayer.size() > 100) {
            player.kick("Possible DoS vulnerability: More Than 10 FormWindow sent to client already.");
            return id;
        }
        ModalFormRequestPacket packet = new ModalFormRequestPacket();
        packet.formId = id;
        packet.data = this.getJSONData();
        formsPlayer.put(packet.formId, this);

        player.dataPacket(packet);
        return id;
    }

    public static Map<Integer, Form> getServerSettingByPlayer(Player player) {
        return playerServerSettings.get(player);
    }
    public static void removeFormSetting(Player player, int id) {
        playerServerSettings.get(player).remove(id);
    }

    public static void clearFormsSetting(Player player) {
         playerServerSettings.get(player).clear();
    }

    public String getJSONData() {
        return Form.GSON.toJson(this);
    }

    public abstract void preHandler(Player player, String data);

    public FormResponse getHandler() {
        return handler;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            this.title = null;
            this.handler = null;
            this.labelsByData = null;
        } finally {
            super.finalize();
        }
    }
}