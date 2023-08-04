package fr.senseitarzan.forms;

import cn.nukkit.Player;
import cn.nukkit.form.element.*;
import com.google.gson.reflect.TypeToken;
import fr.senseitarzan.forms.elements.ImageType;
import fr.senseitarzan.forms.responses.CustomFormResponse;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomForm extends Form {

    public final String type = "custom_form"; //This variable is used for JSON import operations. Do NOT delete :) -- @Snake1999
    private ElementButtonImageData icon;
    private List<Element> content;
    public CustomForm() {
        this.content = new ArrayList<>();
    }
    public CustomForm(CustomFormResponse handler) {
        this.setHandler(handler);
        this.content = new ArrayList<>();
    }

    public CustomForm(String title) {
        this.title = title;
        this.content = new ArrayList<>();
    }

    public CustomForm setTitle(String value) {
        this.title = value;
        return this;
    }

    public CustomForm addLabel(String value) {
        this.addElement(new ElementLabel(value));
        return this;
    }

    public CustomForm addLabel(String value, String label) {
        this.addElement(new ElementLabel(value), label);
        return this;
    }


    public CustomForm setIcon(ElementButtonImageData value) {
        this.icon = value;
        return this;
    }


    public CustomForm setIcon(ImageType type, String path) {
        this.icon = new ElementButtonImageData(type.getType(), path);
        return this;
    }

    public List<Element> getElements() {
        return content;
    }

    public void addElement(Element element) {
        content.add(element);
    }

    public void addElement(Element element, String label) {
        labelsByData.put(content.size(), label);
        content.add(element);
    }

    public CustomForm addInput() {
        ElementInput element = new ElementInput("");
        this.addElement(element);
        return this;
    }

    public CustomForm addInput(String name) {
        ElementInput element = new ElementInput(name);
        this.addElement(element);
        return this;
    }

    public CustomForm addInput(String name, String placeholder) {
        ElementInput element = new ElementInput(name, placeholder);
        this.addElement(element);
        return this;
    }

    public CustomForm addInput(String name, String placeholder, String defaultText) {
        ElementInput element = new ElementInput(name, placeholder, defaultText);
        this.addElement(element);
        return this;
    }

    public CustomForm addInput(ElementInput elementInput, String label) {
        this.addElement(elementInput, label);
        return this;
    }

    public CustomForm addToggle() {
        ElementToggle element = new ElementToggle("");
        this.addElement(element);
        return this;
    }

    public CustomForm addToggle(String name) {
        ElementToggle element = new ElementToggle(name);
        this.addElement(element);
        return this;
    }

    public CustomForm addToggle(String name, boolean defaultValue) {
        ElementToggle element = new ElementToggle(name, defaultValue);
        this.addElement(element);
        return this;
    }

    public CustomForm addToggle(ElementToggle elementToggle, String label) {
        this.addElement(elementToggle, label);
        return this;
    }

    public CustomForm addDropDown(String name, List<String> list) {
        ElementDropdown element = new ElementDropdown(name, list);
        this.addElement(element);
        return this;
    }


    public CustomForm addDropDown(String name, List<String> list, int defaultValue) {
        ElementDropdown element = new ElementDropdown(name, list, defaultValue);
        this.addElement(element);
        return this;
    }

    public CustomForm addToggle(ElementDropdown elementDropdown, String label) {
        this.addElement(elementDropdown, label);
        return this;
    }

    public CustomForm addSlider(String name, int min, int max) {
        ElementSlider element = new ElementSlider(name, min, max);
        this.addElement(element);
        return this;
    }

    public CustomForm addSlider(String name, int min, int max, int step) {
        ElementSlider element = new ElementSlider(name, min, max, step, 3);
        this.addElement(element);
        return this;
    }

    public CustomForm addSlider(String name, int min, int max, int step, int defaultValue, String label) {
        return this.addSlider(new ElementSlider(name, min, max, step, defaultValue), label);
    }

    public CustomForm addSlider(String name, int min, int max, int step, int defaultValue) {
        this.addElement(new ElementSlider(name, min, max, step, defaultValue));
        return this;
    }

    public CustomForm addSlider(ElementSlider elementSlider, String label) {
        this.addElement(elementSlider,label);
        return this;
    }

    public CustomForm addStepSlider(String name, List<String> list) {
        ElementStepSlider element = new ElementStepSlider(name, list);
        this.addElement(element);
        return this;
    }

    public CustomForm addStepSlider(String name, List<String> list, int defaultStep) {
        ElementStepSlider element = new ElementStepSlider(name, list, defaultStep);
        this.addElement(element);
        return this;
    }

    public CustomForm addStepSlider(String name, List<String> list, int defaultStep, String label) {
        this.addElement(new ElementStepSlider(name, list, defaultStep), label);
        return this;
    }

    public CustomForm addStepSlider(ElementStepSlider elementStepSlider, String label) {
        this.addElement(elementStepSlider, label);
        return this;
    }

    public void setHandler(CustomFormResponse handler) {
        this.handler = handler;
    }

    @Override
    public void preHandler(Player player, String data) {
        if (data.equals("null")) {
            this.closed = true;
            this.getHandler().handle(player, null);
            return;
        }

        List<String> elementResponses = GSON.fromJson(data, new TypeToken<List<String>>() {
        }.getType());
        //elementResponses.remove(elementResponses.size() - 1); //submit button //maybe mojang removed that?

        Integer i = 0;

        HashMap<Object, Object> responses = new HashMap<>();
        boolean corromped = false;
        for (String elementData : elementResponses) {
            if (i >= content.size()) {
                break;
            }
            Object id = labelsByData.containsKey(i) ? labelsByData.get(i) : i;
            Element e = content.get(i);
            if (e == null) {
                corromped = true;
                break;
            }

            if (e instanceof ElementLabel elementLabel) {
                responses.put(id, elementLabel.getText());
            } else if (e instanceof ElementDropdown elementDropdown) {
                responses.put(id, elementDropdown.getOptions().get(Integer.parseInt(elementData)));
            } else if (e instanceof ElementInput) {
                responses.put(id, elementData);
            } else if (e instanceof ElementSlider) {
                responses.put(id, Float.parseFloat(elementData));
            } else if (e instanceof ElementStepSlider elementStepSlider) {
                responses.put(id, elementStepSlider.getSteps().get(Integer.parseInt(elementData)));
            } else if (e instanceof ElementToggle) {
                responses.put(id, Boolean.parseBoolean(elementData));
            }
            i++;
            id = null;
        }
        i = 0;
        if (corromped){
            this.getHandler().handle(player, null);
            return;
        }
        this.getHandler().handle(player, responses);

        if (this.isSetting){
            for (String elementData : elementResponses) {
                if (i >= content.size()) {
                    break;
                }
                Element e = content.get(i);
                if (e == null) break;
                if (e instanceof ElementDropdown elementDropdown) {
                    elementDropdown.setDefaultOptionIndex(Integer.parseInt(elementData));
                } else if (e instanceof ElementInput elementInput) {
                        elementInput.setDefaultText(elementData);
                } else if (e instanceof ElementSlider elementSlider) {
                    elementSlider.setDefaultValue(Float.parseFloat(elementData));
                } else if (e instanceof ElementStepSlider elementStepSlider) {
                    elementStepSlider.setDefaultOptionIndex(Integer.parseInt(elementData));
                } else if (e instanceof ElementToggle) {
                    ((ElementToggle) e).setDefaultValue(Boolean.parseBoolean(elementData));
                }
                i++;
            }
        }
        i = null;
    }

    @Override
    public CustomFormResponse getHandler() {
        return (CustomFormResponse) super.getHandler();
    }
    @Nullable
    public Integer setFormSetting(Player player){
        Form.clearFormsSetting(player);
        Integer id = formId.put(player, formId.get(player) + 1);
        Map<Integer, Form> formsPlayer = playerServerSettings.get(player);
        this.isSetting = true;
        formsPlayer.put(id, this);
        return id;
    }


}
