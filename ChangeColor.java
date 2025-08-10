package ru.netology.graphics.image;

public class ChangeColor implements TextColorSchema{

    private static final String CHANGE_CHAR = "#$@%*+- ";

    @Override
    public char convert(int color) {
        return CHANGE_CHAR.charAt(color*(CHANGE_CHAR.length()-1)/255);
    }
}
