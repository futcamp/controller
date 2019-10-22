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

import ru.futcamp.controller.modules.meteo.db.IMeteoDB;
import ru.futcamp.controller.modules.meteo.db.MeteoDBData;
import ru.futcamp.utils.TimeControl;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Meteo station
 */
public class MeteoStation implements IMeteoStation {
    private List<IMeteoDevice> devices = new LinkedList<>();
    private IMeteoDB db;

    public MeteoStation(IMeteoDB db) {
        this.db = db;
    }

    /**
     * Set database file name
     * @param fileName Path to database
     */
    public void setDBFileName(String fileName) {
        db.setFileName(fileName);
    }

    /**
     * Save meteo data to db
     * @param device Meteo device
     * @throws Exception If fail to save meteo data
     */
    public void saveMeteoData(IMeteoDevice device) throws Exception {
        MeteoDBData data = new MeteoDBData();
        data.setTemp(device.getTemp());
        data.setHum(device.getHumidity());
        data.setPres(device.getPressure());
        data.setHour(TimeControl.getCurHour());
        data.setDate(TimeControl.getCurDate());

        try {
            db.connect();
            db.saveMeteoData(device.getName(), data);
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            db.close();
        }
    }

    /**
     * Get last hour from db
     * @param sensor Meteo sensor alias
     * @return Last hour
     * @throws Exception If fail to load last hour
     */
    public int getLastHour(String sensor) throws Exception {
        String hour;

        try {
            db.connect();
            hour = db.getLastTime(getDeviceByAlias(sensor).getName());
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            db.close();
        }

        if (hour.equals(""))
            hour = "-1";

        return Integer.parseInt(hour);
    }

    /**
     * Get meteo data from sensor by date
     * @param sensor Meteo sensor alias
     * @param date Date
     * @return List of meteo data
     * @throws Exception If fail to get data from db
     */
    public List<MeteoDBData> getDataByDate(String sensor, String date) throws Exception {
        List<MeteoDBData> data;

        try {
            db.connect();
            data = db.getDataByDate(getDeviceByAlias(sensor).getName(), date);
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            db.close();
        }

        return data;
    }

    /**
     * Add new device
     * @param device New device
     */
    public void addDevice(IMeteoDevice device) {
        devices.add(device);
    }

    /**
     * Get device by name
     * @param name Name of device
     * @return Found device
     */
    public IMeteoDevice getDevice(String name) {
        for (IMeteoDevice device : devices) {
            if (device.getName().equals(name))
                return device;
        }
        return null;
    }

    /**
     * Get all meteo devices list
     * @return Devices list
     */
    public List<IMeteoDevice> getDevices() {
        return devices;
    }

    /**
     * Get device by alias
     * @param alias Alias of device
     * @return Meteo device
     */
    private IMeteoDevice getDeviceByAlias(String alias) {
        for (IMeteoDevice device : devices) {
            if (device.getAlias().equals(alias))
                return device;
        }
        return null;
    }
}
