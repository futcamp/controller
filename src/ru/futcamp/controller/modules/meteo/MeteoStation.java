///////////////////////////////////////////////////////////////////////
//
// Future Camp Project
//
// Copyright(C) 2019 Sergey Denisov.
//
// Written by Sergey Denisov aka LittleBuster(DenisovS21@gmail.com)
// Github:  https://github.com/LittleBuster
//          https://github.com/futcamp
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public Licence 3
// as published by the Free Software Foundation; either version 3
// of the Licence, or(at your option) any later version.
//
///////////////////////////////////////////////////////////////////////

package ru.futcamp.controller.modules.meteo;

import ru.futcamp.IAppModule;
import ru.futcamp.controller.modules.meteo.db.IMeteoDB;
import ru.futcamp.controller.modules.meteo.db.MeteoDBData;
import ru.futcamp.utils.TimeControl;
import ru.futcamp.utils.log.ILogger;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Meteo station
 */
public class MeteoStation implements IMeteoStation, IAppModule {
    private Map<String, IMeteoDevice> devices = new HashMap<>();

    private IMeteoDB db;
    private ILogger log;

    private String modName;

    public MeteoStation(String name, IAppModule ...dep) {
        this.modName = name;
        this.db = (IMeteoDB) dep[0];
        this.log = (ILogger) dep[1];
    }

    /**
     * Save meteo data to db
     * @throws Exception If fail to save data
     */
    private void saveMeteoData(IMeteoDevice device) throws Exception {
        MeteoDBData data = new MeteoDBData();
        data.setTemp(device.getTemp());
        data.setHum(device.getHumidity());
        data.setPres(device.getPressure());
        data.setHour(TimeControl.getCurHour());
        data.setDate(TimeControl.getCurDate());

        try {
            db.connect();
            int lastHour = Integer.parseInt(db.getLastTime(device.getName()));
            int curHour = TimeControl.getCurHour();
            if (curHour != lastHour) {
                db.saveMeteoData(device.getName(), data);
            }
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            db.close();
        }
    }

    /**
     * Get meteo data from sensor by date
     * @param device Meteo device
     * @param date Date
     * @return List of meteo data
     * @throws Exception If fail to get data from db
     */
    private List<MeteoDBData> getDataByDate(IMeteoDevice device, String date) throws Exception {
        List<MeteoDBData> data;

        try {
            db.connect();
            data = db.getDataByDate(device.getName(), date);
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            db.close();
        }

        return data;
    }

    /**
     * Get meteo info from one sensor
     * @param alias Alias of device
     * @return Info
     */
    public MeteoInfo getMeteoInfo(String alias) {
        MeteoInfo info = new MeteoInfo();

        IMeteoDevice device = devices.get(alias);
        info.setHum(device.getHumidity());
        info.setPres(device.getPressure());
        info.setTemp(device.getTemp());
        info.setName(device.getName());

        return info;
    }

    /**
     * Get meteo info from all sensors
     * @return Meteo info list
     */
    public List<MeteoInfo> getMeteoInfo() {
        List<MeteoInfo> infoList = new LinkedList<>();

        for (Map.Entry<String, IMeteoDevice> entry : devices.entrySet()) {
            IMeteoDevice device = entry.getValue();

            MeteoInfo info = new MeteoInfo();
            info.setHum(device.getHumidity());
            info.setPres(device.getPressure());
            info.setTemp(device.getTemp());
            info.setName(device.getName());
            info.setAlias(device.getAlias());

            infoList.add(info);
        }

        return infoList;
    }

    /**
     * Get meteo info by concrete ate
     * @param alias Alias of device
     * @param date Date
     * @return Meteo info list
     * @throws Exception If fail to get info
     */
    public List<MeteoInfo> getMeteoInfoByDate(String alias, String date) throws Exception {
        List<MeteoInfo> infoList = new LinkedList<>();
        IMeteoDevice device = devices.get(alias);

        List<MeteoDBData> data = getDataByDate(device, date);
        for (MeteoDBData datum : data) {
            MeteoInfo info = new MeteoInfo();

            info.setName(device.getName());
            info.setTemp(datum.getTemp());
            info.setHum(datum.getHum());
            info.setPres(datum.getPres());
            info.setHour(datum.getHour());

            infoList.add(info);
        }

        return infoList;
    }

    /**
     * Set database file name
     * @param fileName Path to database
     */
    public void setDBFileName(String fileName) {
        db.setFileName(fileName);
    }

    /**
     * Update meteo data from sensors
     */
    public void update() {
        for(Map.Entry<String, IMeteoDevice> entry : devices.entrySet()) {
            IMeteoDevice device = entry.getValue();
            try {
                device.syncMeteoData();
            } catch (Exception e) {
                log.error("Fail to sync meteo data with \"" + device.getName() + "\": " + e.getMessage(), "METEO");
            }

            /*
             * Save meteo data to db
             */
            try {
                saveMeteoData(device);
            } catch (Exception e) {
                log.error("Fail to save meteo data to db: " + e.getMessage(), "METEO");
            }
        }
    }

    /**
     * Add new device
     * @param device New device
     */
    public void addDevice(IMeteoDevice device) {
        devices.put(device.getAlias(), device);
    }

    public String getModName() {
        return modName;
    }
}
