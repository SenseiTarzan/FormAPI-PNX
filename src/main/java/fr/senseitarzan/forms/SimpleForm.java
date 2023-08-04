package fr.senseitarzan.forms;

import cn.nukkit.Player;
import cn.nukkit.form.element.*;
import fr.senseitarzan.forms.responses.SimpleFormResponse;
import fr.senseitarzan.forms.elements.ImageType;
import org.checkerframework.checker.interning.qual.InternMethod;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SimpleForm extends Form {


    private final String type = "form"; //This variable is used for JSON import operations. Do NOT delete :) -- @Snake1999
    private String content = "";
    private List<ElementButton> buttons;

    public SimpleForm() {
        this.buttons = new ArrayList<>();
    }
    public SimpleForm(SimpleFormResponse handler) {
        this.buttons = new ArrayList<>();
        this.setHandler(handler);
    }

    public SimpleForm(String title) {
        this.title = title;
        this.buttons = new ArrayList<>();
    }

    public SimpleForm(String title, SimpleFormResponse handler) {
        this.title = title;
        this.buttons = new ArrayList<>();
        this.setHandler(handler);
    }

    public SimpleForm(String title, String content, SimpleFormResponse handler) {
        this.title = title;
        this.content = content;
        this.buttons = new ArrayList<>();
        this.setHandler(handler);
    }

    public SimpleForm setTitle(String value) {
        this.title = value;
        return this;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ElementButton> getButtons() {
        return buttons;
    }

    public void addButton(ElementButton button) {
        this.buttons.add(button);
    }

    public void addButton(ElementButton button, String label) {
        labelsByData.put(this.buttons.size(), label);
        buttons.add(button);
    }

    public SimpleForm addContent(String value) {
        this.setContent(this.getContent() + value);
        return this;
    }

    public SimpleForm addContentLine(String value) {
        return addContent(value + "\n");
    }

    public SimpleForm addContentOnNextLine(String value) {
        return addContent("\n" + value);
    }

    public SimpleForm addButton(String text, String label) {
        this.addButton(new ElementButton(text), label);
        return this;
    }

    public SimpleForm addButton(String text) {
        this.addButton(new ElementButton(text));
        return this;
    }

    public SimpleForm addButton(String text, ElementButtonImageData image) {
        this.addButton(new ElementButton(text,image));
        return this;
    }

    public SimpleForm addButton(String text, ElementButtonImageData image, String label) {
        this.addButton(new ElementButton(text, image), label);
        return this;
    }

    public SimpleForm addButton(String text, ImageType type, String ico) {
        ElementButton button = new ElementButton(text);
        button.addImage(new ElementButtonImageData(type.getType(), ico));
        this.addButton(button);
        return this;
    }

    public SimpleForm addButton(String text, ImageType type, String ico, String label) {
        ElementButton button = new ElementButton(text);
        button.addImage(new ElementButtonImageData(type.getType(), ico));
        this.addButton(button, label);
        return this;
    }

    @Override
    public void preHandler(Player player, String data) {

        if (data.equals("null")) {
            this.closed = true;
            this.getHandler().handle(player, null);
            return;
        }
        int buttonID;
        try {
            buttonID = Integer.parseInt(data);
        } catch (Exception e) {
            return;
        }
        if (buttonID >= this.buttons.size()) {
            this.getHandler().handle(player, null);
            return;
        }
        if (!this.labelsByData.containsKey(buttonID)) {
            this.getHandler().handle(player, data);
            return;
        }
        this.getHandler().handle(player, this.labelsByData.get(buttonID));
    }
    public SimpleForm setHandler(SimpleFormResponse handler) {
        this.handler = handler;
        return this;
    }

    @Override
    public SimpleFormResponse getHandler() {
        return (SimpleFormResponse) super.getHandler();
    }
}