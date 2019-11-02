package ru.futcamp.utils.configs.settings;

public class TelegramMenuSettings {
    private String[][] main;
    private String[][] meteostat;
    private String[][] therm;
    private String[][] light;

    public String[][] getMain() {
        return main;
    }

    public void setMain(String[][] main) {
        this.main = main;
    }

    public String[][] getMeteostat() {
        return meteostat;
    }

    public void setMeteostat(String[][] meteostat) {
        this.meteostat = meteostat;
    }

    public String[][] getTherm() {
        return therm;
    }

    public void setTherm(String[][] therm) {
        this.therm = therm;
    }

    public String[][] getLight() {
        return light;
    }

    public void setLight(String[][] light) {
        this.light = light;
    }
}
