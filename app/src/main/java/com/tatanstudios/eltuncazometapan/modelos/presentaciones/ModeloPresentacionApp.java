package com.tatanstudios.eltuncazometapan.modelos.presentaciones;

public class ModeloPresentacionApp {

    String Title,Description;
    int ScreenImg;

    public ModeloPresentacionApp(String title, String description, int screenImg) {
        Title = title;
        Description = description;
        ScreenImg = screenImg;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public int getScreenImg() {
        return ScreenImg;
    }
}
