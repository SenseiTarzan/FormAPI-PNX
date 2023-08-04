package fr.senseitarzan.forms.elements;

public enum ImageType {
    PATH("path"),
    URL("url");

    private final String type;

    private ImageType(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
