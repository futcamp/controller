package ru.futcamp.utils.configs.settings.meteo;

public class MeteoTimersSettings {
    private int sensors;
    private int db;
    private int lcd;

    public int getSensors() {
        return sensors;
    }

    public void setSensors(int sensors) {
        this.sensors = sensors;
    }

    public int getDb() {
        return db;
    }

    public void setDb(int db) {
        this.db = db;
    }

    public int getLcd() {
        return lcd;
    }

    public void setLcd(int lcd) {
        this.lcd = lcd;
    }
}
