package fr.senseitarzan.events;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.player.PlayerServerSettingsRequestEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.network.protocol.ModalFormResponsePacket;
import cn.nukkit.network.protocol.ServerSettingsRequestPacket;
import cn.nukkit.network.protocol.ServerSettingsResponsePacket;
import cn.nukkit.scheduler.Task;
import fr.senseitarzan.forms.Form;
import fr.senseitarzan.forms.CustomForm;
import fr.senseitarzan.forms.ModalForm;
import fr.senseitarzan.forms.SimpleForm;
import fr.senseitarzan.forms.elements.ImageType;

import java.util.*;

public class EventListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDataReceive(DataPacketReceiveEvent event) {
        Player player = event.getPlayer();
        var packet = event.getPacket();

        if (packet instanceof ModalFormResponsePacket modalFormResponsePacket) {
            if (Form.playerForms.containsKey(player) && Form.playerForms.get(player).containsKey(modalFormResponsePacket.formId)) {
                event.setCancelled();
                Form temp = Form.playerForms.get(player).get(modalFormResponsePacket.formId);
                temp.preHandler(player, modalFormResponsePacket.data.trim());
                Form.playerForms.get(player).remove(modalFormResponsePacket.formId);
            } else if (Form.playerForms.containsKey(player) && Form.playerServerSettings.get(player).containsKey(modalFormResponsePacket.formId)) {
                event.setCancelled();
                Form temp = Form.playerServerSettings.get(player).get(modalFormResponsePacket.formId);
                temp.preHandler(player, modalFormResponsePacket.data.trim());

            }
        } else if (packet instanceof ServerSettingsRequestPacket) {
            event.setCancelled();
            Map<Integer, Form> settingsRequestMap = Form.getServerSettingByPlayer(player);
            Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                @Override
                public void onRun(int currentTick) {
                    settingsRequestMap.forEach((id, window) -> {
                        System.out.println(window.getJSONData());
                        ServerSettingsResponsePacket re = new ServerSettingsResponsePacket();
                        re.formId = id;
                        re.data = window.getJSONData();
                        player.dataPacket(re);
                    });
                }
            }, 20, true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onServerSettingResponse(PlayerServerSettingsRequestEvent event){
        event.setCancelled();
        event.setSettings(new HashMap<>());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        Form.formId.remove(event.getPlayer());
        Form.playerForms.remove(event.getPlayer());
        Form.playerServerSettings.remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        Form.formId.put(event.getPlayer(), 0);
        Form.playerForms.put(event.getPlayer(), new HashMap<>());
        Form.playerServerSettings.put(event.getPlayer(), new HashMap<>());
    }
}