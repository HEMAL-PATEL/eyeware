package com.google.android.gms.samples.vision.face.eyeware;

/**
 * Created by Casey Brito on 8/6/2016.
 */
public class Frames {
    private int pictureID;
    private String name;
    private String gender;
    private String color;
    private String shape;
    private int price;
    private int iconID;


    public Frames(int pictureID, String name, String gender, String color, String shape, int price, int iconID) {
        this.name = name;
        this.gender = gender;
        this.color = color;
        this.shape = shape;
        this.price = price;
        this.pictureID = pictureID;
        this.iconID = iconID;
    }

    public int getPictureID() {
        return pictureID;
    }

    public int getIconID() {
        return iconID;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getColor() {
        return color;
    }

    public String getShape() {
        return shape;
    }

    public int getPrice() {
        return price;
    }


}
