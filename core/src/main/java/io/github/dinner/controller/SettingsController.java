package io.github.dinner.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Preferences;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SettingsController {
    private static final String SAVE_NAME = "settings";
    private static Preferences preferences;

    private static float musicVolume;
    private static float soundVolume;
    private static int screenWidth;
    private static int screenHeight;
    private static boolean fullscreen;
    private static boolean vsync;


    public static void init()
    {
        preferences = Gdx.app.getPreferences(SAVE_NAME);
        SettingsController.load();
        SettingsController.setAllSettings(); // Applica le impostazioni all'avvio
    }

    public static void load() {
        SettingsController.musicVolume = preferences.getFloat("musicVolume", 0.8f);
        SettingsController.soundVolume = preferences.getFloat("soundVolume", 0.8f);
        SettingsController.screenWidth = preferences.getInteger("screenWidth", 640);
        SettingsController.screenHeight = preferences.getInteger("screenHeight", 480);
        SettingsController.fullscreen = preferences.getBoolean("fullscreen", true);
        SettingsController.vsync = preferences.getBoolean("vsync", false);
    }

    public static void save() {
        preferences.putFloat("musicVolume", SettingsController.musicVolume);
        preferences.putFloat("soundVolume", SettingsController.soundVolume);
        preferences.putInteger("screenWidth", SettingsController.screenWidth);
        preferences.putInteger("screenHeight", SettingsController.screenHeight);
        preferences.putBoolean("fullscreen", SettingsController.fullscreen);
        preferences.putBoolean("vsync", SettingsController.vsync);
        preferences.flush();
    }

    public static void setAllSettings() {
        Gdx.graphics.setForegroundFPS(60);
        if (SettingsController.fullscreen) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        } else {
            Gdx.graphics.setWindowedMode(SettingsController.screenWidth, SettingsController.screenHeight);
        }
        Gdx.graphics.setVSync(SettingsController.vsync);
        AudioController.setMusicVolume(SettingsController.musicVolume);
        AudioController.setSoundVolume(SettingsController.soundVolume);
        SettingsController.save();
    }

    public static float getMusicVolume() {
        return SettingsController.musicVolume;
    }

    public static float getSoundVolume() {
        return SettingsController.soundVolume;
    }

    public static void saveMusicVolume(float musicVolume) {
        SettingsController.musicVolume = musicVolume;
        SettingsController.save();
    }

    public static void saveSoundVolume(float soundVolume) {
        SettingsController.soundVolume = soundVolume;
        SettingsController.save();
    }

    public static int getScreenWidth() {
        return SettingsController.screenWidth;
    }

    public static int getScreenHeight() {
        return SettingsController.screenHeight;
    }

    public static void saveScreenSize(int width, int height) {
        SettingsController.screenWidth = width;
        SettingsController.screenHeight = height;
        SettingsController.save();
    }

    public static boolean isFullscreen() {
        return SettingsController.fullscreen;
    }

    public static void saveFullscreen(boolean fullscreen) {
        SettingsController.fullscreen = fullscreen;
        SettingsController.save();
    }

    public static boolean isVsync() {
        return SettingsController.vsync;
    }

    public static void saveVsync(boolean vsync) {
        SettingsController.vsync = vsync;
        SettingsController.save();
    }

    public static String[] getResolutions()
    {
        // Elenco delle risoluzioni desiderate
        Set<String> resolutionSet = new HashSet<>(Arrays.asList(
            "854x480", //SD
            "1024x576",
            "1152x648",
            "1280x720",  // HD
            "1366x768",
            "1600x900",
            "1920x1080", // Full HD
            "2560x1440",
            "3840x2160"  // 4K UHD
        ));

        // OttienE la risoluzione massima supportata dal sistema
        Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
        int maxWidth = currentMode.width;
        int maxHeight = currentMode.height;

        // Filtra le risoluzioni desiderate in base alla risoluzione massima
        resolutionSet.removeIf(resolution -> {
            String[] parts = resolution.split("x");
            int width = Integer.parseInt(parts[0]);
            int height = Integer.parseInt(parts[1]);
            return width > maxWidth || height > maxHeight; // Rimuovi risoluzioni superiori a quelle massime
        });

        //QUA VORREI CHE VENISSERO FILTRATE FINO ALLA RISOLUZIONE MASSIMA, LASCIANDO IL
        //RESTO DEL CODICE INVARIATO

        // Converte il Set in un array e lo ordina
        String[] resolutions = resolutionSet.toArray(new String[resolutionSet.size()]);

        // Ordina le risoluzioni numericamente prima per larghezza (width), poi per altezza (height)
        Arrays.sort(resolutions, (a, b) -> {
            // Divide ogni risoluzione in larghezza e altezza
            String[] resA = a.split("x");
            String[] resB = b.split("x");

            // Confronta larghezza
            int widthCompare = Integer.compare(Integer.parseInt(resA[0]), Integer.parseInt(resB[0]));

            if (widthCompare != 0) {
                return widthCompare; // Se le larghezze sono diverse, ordina per larghezza
            }

            // Se le larghezze sono uguali, confronta altezza
            return Integer.compare(Integer.parseInt(resA[1]), Integer.parseInt(resB[1]));
        });

        return resolutions;
    }

    public static void setMusicVolume(){
        AudioController.setMusicVolume(SettingsController.musicVolume);
    }

    public static void setSoundVolume(){
        AudioController.setSoundVolume(SettingsController.soundVolume);
    }

    public static void setScreenSize() {
        if(!SettingsController.fullscreen){
            Gdx.graphics.setWindowedMode(SettingsController.screenWidth, SettingsController.screenHeight);
        }
    }

    public static void setFullScreen(){
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
    }

    public static void setVsync(){
        Gdx.graphics.setVSync(SettingsController.vsync);
    }

    public static void setPreferencesForTesting(Preferences testPreferences) {
        preferences = testPreferences;
    }
}
