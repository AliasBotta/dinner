package io.github.dinner.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import java.util.*;

public class AudioController {

    public static Map<String, Music> musicMap = new HashMap<>();
    public static Map<String, Music> soundMap = new HashMap<>();

    private static Music currentMusic; //musica attualmente in riproduzione
    private static Music currentSound; //suono attualmente in riproduzione

    public static void playMusic(String trackId, boolean loop) { // Metodo per riprodurre una musica in base al suo trackId
        // Ottieni la traccia dalla playlist usando il trackId
        AudioController.currentMusic = AudioController.musicMap.get(trackId);

        if (AudioController.currentMusic != null) { // Verifica che la traccia esista
            AudioController.currentMusic.setLooping(loop);
            AudioController.currentMusic.play();
        } else {
            System.out.println("Track with ID '" + trackId + "' not found in the playlist.");
        }
    }

    public static void playSound(String trackId, boolean loop) { // Metodo per riprodurre un suono in base al suo trackId
        // Ottieni il suono dalla soundPlaylist usando il trackId
        AudioController.currentSound = AudioController.soundMap.get(trackId);

        if (AudioController.currentSound != null) { // Verifica che il suono esista
            AudioController.currentSound.setLooping(loop);
            AudioController.currentSound.play();
        } else {
            System.out.println("Sound with ID '" + trackId + "' not found in the soundPlaylist.");
        }
    }

    public static Music getCurrentSound(){
        return currentSound;
    }

    public static void setMusicVolume(float volume) {
        // Imposta il volume per tutti gli elementi nella playlist
        for (Map.Entry<String, Music> entry : musicMap.entrySet()) {
            entry.getValue().setVolume(volume);
        }
    }

    public static void setSoundVolume(float volume) {
        // Imposta il volume per tutti gli elementi nella soundPlaylist
        for (Map.Entry<String, Music> entry : soundMap.entrySet()) {
            entry.getValue().setVolume(volume);
        }
    }

    public static void playMusicOfLevel(String level){
        if (level.equals("gardenSFLevel") || level.equals("gardenSFNoCorpseLevel")){
            playMusic("garden_ambience", true);
        } else {
            playMusic("home", true);
        }
    }

    public static void pauseMusic(){ //metodo per mettere in pausa la musica
        AudioController.currentMusic.pause();
    }

    public static void stopMusic(){
        AudioController.currentMusic.stop();
    }

    public static void stopAllSounds(){
        AudioController.currentSound.stop();
    }

    public static void stopSound(String sound){
        AudioController.soundMap.get(sound).stop();
    }

    public static void initializeMusic(){
        AudioController.musicMap.put("MainMenu", Gdx.audio.newMusic(Gdx.files.internal("music/MainMenu.wav")));
        AudioController.musicMap.put("home", Gdx.audio.newMusic(Gdx.files.internal("music/home.wav")));
        AudioController.musicMap.put("start_boom", Gdx.audio.newMusic(Gdx.files.internal("music/start_boom.mp3")));
        AudioController.musicMap.put("garden_ambience", Gdx.audio.newMusic(Gdx.files.internal("music/garden_ambience.wav")));
    }

    public static void initializeSound(){
        AudioController.soundMap.put("woodFS1", Gdx.audio.newMusic(Gdx.files.internal("sound/woodFS1.mp3")));
        AudioController.soundMap.put("woodFS2", Gdx.audio.newMusic(Gdx.files.internal("sound/woodFS2.mp3")));
        AudioController.soundMap.put("grassFS1_0", Gdx.audio.newMusic(Gdx.files.internal("sound/grassFS1.mp3")));
        AudioController.soundMap.put("grassFS1_1", Gdx.audio.newMusic(Gdx.files.internal("sound/grassFS1.mp3")));
        AudioController.soundMap.put("grassFS2_0", Gdx.audio.newMusic(Gdx.files.internal("sound/grassFS2.mp3")));
        AudioController.soundMap.put("grassFS2_1", Gdx.audio.newMusic(Gdx.files.internal("sound/grassFS2.mp3")));
        AudioController.soundMap.put("check", Gdx.audio.newMusic(Gdx.files.internal("sound/check.mp3")));
        AudioController.soundMap.put("click_down", Gdx.audio.newMusic(Gdx.files.internal("sound/click_down.mp3")));
        AudioController.soundMap.put("click_up", Gdx.audio.newMusic(Gdx.files.internal("sound/click_up.mp3")));
        AudioController.soundMap.put("flush", Gdx.audio.newMusic(Gdx.files.internal("sound/flush.mp3")));
        AudioController.soundMap.put("flush2", Gdx.audio.newMusic(Gdx.files.internal("sound/flush2.mp3")));
        AudioController.soundMap.put("bump", Gdx.audio.newMusic(Gdx.files.internal("sound/bump.mp3")));
        AudioController.soundMap.put("type", Gdx.audio.newMusic(Gdx.files.internal("sound/type.mp3")));
        AudioController.soundMap.put("collected", Gdx.audio.newMusic(Gdx.files.internal("sound/collected.mp3")));
        AudioController.soundMap.put("transition", Gdx.audio.newMusic(Gdx.files.internal("sound/transition.mp3")));
        AudioController.soundMap.put("pc_noise_1", Gdx.audio.newMusic(Gdx.files.internal("sound/pc_noise_1.mp3")));
        AudioController.soundMap.put("pc_noise_2", Gdx.audio.newMusic(Gdx.files.internal("sound/pc_noise_2.mp3")));
        AudioController.soundMap.put("pc_text_printing", Gdx.audio.newMusic(Gdx.files.internal("sound/pc_text_printing.mp3")));
        AudioController.soundMap.put("pc_turn_off", Gdx.audio.newMusic(Gdx.files.internal("sound/pc_turn_off.mp3")));
        AudioController.soundMap.put("pc_click_1", Gdx.audio.newMusic(Gdx.files.internal("sound/pc_click_1.mp3")));
        AudioController.soundMap.put("pc_click_2", Gdx.audio.newMusic(Gdx.files.internal("sound/pc_click_2.mp3")));
        AudioController.soundMap.put("door_entrance_1", Gdx.audio.newMusic(Gdx.files.internal("sound/door_entrance_1.mp3")));
        AudioController.soundMap.put("door_entrance_2", Gdx.audio.newMusic(Gdx.files.internal("sound/door_entrance_2.mp3")));
        AudioController.soundMap.put("stairs_up", Gdx.audio.newMusic(Gdx.files.internal("sound/stairs_up.mp3")));
        AudioController.soundMap.put("stairs_down", Gdx.audio.newMusic(Gdx.files.internal("sound/stairs_down.mp3")));
        AudioController.soundMap.put("old_scream", Gdx.audio.newMusic(Gdx.files.internal("sound/old_scream.mp3")));
        AudioController.soundMap.put("bush_rustling", Gdx.audio.newMusic(Gdx.files.internal("sound/bush_rustling.mp3")));
        AudioController.soundMap.put("door_closed", Gdx.audio.newMusic(Gdx.files.internal("sound/door_closed.mp3")));
    }
}
