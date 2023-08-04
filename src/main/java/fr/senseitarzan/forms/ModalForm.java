package fr.senseitarzan.forms;

import cn.nukkit.Player;
import fr.senseitarzan.forms.responses.ModalFormResponse;

public class ModalForm extends Form {

    private final String type = "modal"; //This variable is used for JSON import operations. Do NOT delete :) -- @Snake1999
    private String content = "";
    private String button1 = "";
    private String button2 = "";
    public ModalForm() {

    }

    public ModalForm(ModalFormResponse handler) {
        this.setHandler(handler);
    }

    public ModalForm(String title) {
        this.title = title;
    }

    public ModalForm(String title, ModalFormResponse handler) {
        this.title = title;
        this.setHandler(handler);
    }

    public ModalForm(String title, String content) {
        this.title = title;
        this.content = content;

    }

    public ModalForm(String title, String content, ModalFormResponse handler) {
        this.title = title;
        this.content = content;
        this.setHandler(handler);

    }

    public ModalForm(String title, String content, String trueButton) {
        this.title = title;
        this.content = content;
        this.button1 = trueButton;
    }

    public ModalForm(String title, String content, String trueButton, ModalFormResponse handler) {
        this.title = title;
        this.content = content;
        this.button1 = trueButton;
        this.setHandler(handler);
    }

    public ModalForm(String title, String content, String trueButton, String falseButton) {
        this.title = title;
        this.content = content;
        this.button1 = trueButton;
        this.button2 = falseButton;
    }

    public ModalForm(String title, String content, String trueButton, String falseButton, ModalFormResponse handler) {
        this.title = title;
        this.content = content;
        this.button1 = trueButton;
        this.button2 = falseButton;
        this.setHandler(handler);
    }

    public ModalForm setTitle(String value) {
        this.title = value;
        return this;
    }

    public ModalForm setContent(String value) {
        this.content = value;
        return this;
    }

    public ModalForm setButtonTrue(String value) {
        this.button1 = value;
        return this;
    }

    public ModalForm setButtonFalse(String value) {
        this.button2 = value;
        return this;
    }
    public ModalForm setHandler(ModalFormResponse handler) {
        this.handler = handler;
        return this;
    }
    @Override
    public ModalFormResponse getHandler() {
        return (ModalFormResponse) super.getHandler();
    }

    @Override
    public void preHandler(Player player, String data) {

        if (data.equals("null")) {
            closed = true;
            this.getHandler().handle(player, null);
            return;
        }
        this.getHandler().handle(player, data.equals("true"));
    }
}