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

package ru.futcamp.controller.subcontrollers.modules.meteo;

import ru.futcamp.IAppModule;
import ru.futcamp.controller.subcontrollers.modules.meteo.db.MeteoDB;
import ru.futcamp.controller.subcontrollers.modules.meteo.db.MeteoDBData;
import ru.futcamp.utils.TimeControl;
import ru.futcamp.utils.log.ILogger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Meteo station
 */
public class MeteoStation implements IMeteoStation, IAppModule {
    private Map<String, IMeteoDevice> devices = new HashMap<>();
    private String modName;

    private ILogger log;

    public MeteoStation(String name, IAppModule ...dep) {
        this.modName = name;
        this.log = (ILogger) dep[0];
    }

    /**
     * Save meteo data to ru.futcamp.db
     * @throws Exception If fail to save data
     */
    private void saveMeteoData(IMeteoDevice device) throws Exception {
        MeteoDBData data = new MeteoDBData();
        data.setTemp(device.getTemp());
        data.setHum(device.getHumidity());
        data.setPres(device.getPressure());
        data.setHour(TimeControl.getCurHour());
        data.setDate(TimeControl.getCurDate());

        int lastHour = MeteoDB.getLastTime(device.getName());
        int curHour = TimeControl.getCurHour();
        if (curHour != lastHour) {
            MeteoDB.saveMeteoData(device.getName(), data);
        }
    }

    /**
     * Get meteo info from one sensor
     * @param alias Alias of device
     * @return Info
     */
    public MeteoInfo getMeteoInfo(String alias) throws Exception {
        MeteoInfo info = new MeteoInfo();

        IMeteoDevice device = devices.get(alias);

        if (device == null) {
            throw new Exception("Meteo device \"" + alias + "\" not found");
        }

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

        List<MeteoDBData> data = MeteoDB.getDataByDate(device.getName(), date);
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
             * Save meteo data to ru.futcamp.db
             */
            try {
                saveMeteoData(device);
            } catch (Exception e) {
                log.error("Fail to save meteo data to ru.futcamp.db: " + e.getMessage(), "METEO");
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
