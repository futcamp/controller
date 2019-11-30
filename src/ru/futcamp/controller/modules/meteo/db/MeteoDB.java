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

package ru.futcamp.controller.modules.meteo.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.util.List;

/**
 * Database management class
 */
public class MeteoDB {
    private static String path;

    /**
     * Get meteo data list by date
     * @param sensor Meteo sensor name
     * @param findDate Date
     * @return Meteo data list
     * @throws Exception If fail to get meteo data
     */
    public static List<MeteoDBData> getDataByDate(String sensor, String findDate) throws Exception {
        List<MeteoDBData> data = null;
        Dao<MeteoDBData, String> dao;

        synchronized (MeteoDB.class) {
            try (ConnectionSource source = new JdbcConnectionSource("jdbc:sqlite:" + path + sensor + ".db")) {
                dao = DaoManager.createDao(source, MeteoDBData.class);
                data = dao.queryForEq("date", findDate);
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        }

        return data;
    }

    /**
     * Get last record hour
     * @param sensor Sensor name
     * @return Hour
     * @throws Exception If fail to get hour
     */
    public static int getLastTime(String sensor) throws Exception {
        List<MeteoDBData> data = null;
        Dao<MeteoDBData, String> dao;

        synchronized (MeteoDB.class) {
            try (ConnectionSource source = new JdbcConnectionSource("jdbc:sqlite:" + path + sensor + ".db")) {
                dao = DaoManager.createDao(source, MeteoDBData.class);
                data = dao.queryForAll();
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        }

        return data.get(data.size() - 1).getHour();
    }

    /**
     * Save meteo data to db
     * @param sensor Meteo sensor name
     * @param data Meteo data
     * @throws Exception If fail to save data
     */
    public static void saveMeteoData(String sensor, MeteoDBData data) throws Exception {
        Dao<MeteoDBData, String> dao;

        synchronized (MeteoDB.class) {
            try (ConnectionSource source = new JdbcConnectionSource("jdbc:sqlite:" + path + sensor + ".db")) {
                dao = DaoManager.createDao(source, MeteoDBData.class);
                dao.create(data);
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        }
    }

    public static void setPath(String path) {
        MeteoDB.path = path;
    }
}
