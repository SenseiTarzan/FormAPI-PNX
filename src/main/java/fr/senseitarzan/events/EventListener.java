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

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(PlayerChatEvent event) {
        System.out.println("Chat");
        if (Objects.equals(event.getMessage(), "test")) {
            SimpleForm simpleForm = new SimpleForm((targetPlayer, data) -> {
                targetPlayer.sendMessage(data == null ? "null" : data.toString()); // return "diamond" if click in button Hello because has label "diamond" else null for exit form
            });
            simpleForm.setTitle("From with label");
            simpleForm.setContent("i'am a SimpleForm");
            simpleForm.addButton("Hello", ImageType.PATH, "textures/items/diamond", "diamond");
            simpleForm.send(event.getPlayer());
        } else if (Objects.equals(event.getMessage(), "model-test")) {
            ModalForm form = new ModalForm((targetPlayer, data) -> {
                if(data == null) return;
                targetPlayer.sendMessage(data.toString()); // return boolean
            });
            form.setTitle("It`s a title");
            form.setContent("Sample text");
            form.setButtonTrue("Positive button");
            form.setButtonFalse("Negative button");
            form.send(event.getPlayer());
        } else if (Objects.equals(event.getMessage(), "custom-test")) {
            CustomForm customForm = new CustomForm((player, data) -> {
                player.sendMessage("test: " + data.get(0).toString()); // return label element
                player.sendMessage("input: " + data.get("input1")); //return input send by client
            });
            customForm.setTitle("It`s a title");
            customForm.addLabel("label element");
            customForm.addInput(new ElementInput("Input button"), "input1");
            customForm.send(event.getPlayer());
        }else if (Objects.equals(event.getMessage(), "setting-test")) {
            CustomForm customForm = new CustomForm((player, data) -> {
                player.sendMessage("test: " + data.get(0).toString()); // return Hello, i'am form server setting
                player.sendMessage("input: " + data.get("test")); //return input send by client
            });
            customForm.setTitle("Test Setting");
            customForm.addLabel("Hello, i'am form server setting");
            customForm.setIcon(ImageType.PATH,"textures/items/apple");
            customForm.addInput(new ElementInput("test"), "test");
            customForm.setFormSetting(event.getPlayer());
        }
    }
}