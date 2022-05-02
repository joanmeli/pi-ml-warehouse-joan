package br.com.group9.pimlwarehouse.enums;

public enum CategoryENUM {
    FS("Fresco"),
    RF("Refrigerado"),
    FF("Congelado");

    private final String categoryDescription;

    CategoryENUM(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public static CategoryENUM convertToEnum(String categoryDescription) {
        CategoryENUM convertedEnum;
        if(categoryDescription.equals("Fresco")) {
            convertedEnum = FS;
        } else if (categoryDescription.equals("Refrigerado")) {
            convertedEnum = RF;
        } else {
            convertedEnum = FF;
        }
        return convertedEnum;
    }
}
