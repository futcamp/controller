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

import org.sqlite.JDBC;
import sun.awt.Mutex;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Database management class
 */
public class MeteoDB implements IMeteoDB {
    private String fileName;
    private Connection conn;
    private Mutex mtx = new Mutex();

    /**
     * Set path to database file
     * @param fileName Name of database file
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
        try {
            DriverManager.registerDriver(new JDBC());
        } catch (Exception ignored) {}
    }

    /**
     * Connect to database
     * @throws SQLException If fail to connect
     */
    public void connect() throws SQLException {
        mtx.lock();
        this.conn = DriverManager.getConnection("jdbc:sqlite:" + fileName);
    }

    /**
     * Get last hour of data
     * @param sensor Meteo sensor name
     * @return Last hour
     * @throws SQLException
     */
    public String getLastTime(String sensor) throws SQLException {
        String time = "";
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT hour FROM " + sensor);

        while (resultSet.next()) {
            time = resultSet.getString("hour");
        }
        statement.close();
        resultSet.close();

        return time;
    }

    /**
     * Get meteo data list by date
     * @param sensor Meteo sensor name
     * @param findDate Date
     * @return Meteo data list
     * @throws SQLException If fail to get meteo data
     */
    public List<MeteoDBData> getDataByDate(String sensor, String findDate) throws SQLException {
        List<MeteoDBData> data = new LinkedList<>();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + sensor + " WHERE date = '" + findDate + "'");

        while (resultSet.next()) {
            MeteoDBData datum = new MeteoDBData();
            datum.setTemp(resultSet.getInt("temp"));
            datum.setHum(resultSet.getInt("hum"));
            datum.setPres(resultSet.getInt("pres"));
            datum.setHour(resultSet.getInt("hour"));
            datum.setDate(resultSet.getString("date"));
            data.add(datum);
        }
        statement.close();
        resultSet.close();

        return data;
    }

    /**
     * Save meteo data to db
     * @param sensor Meteo sensor name
     * @param data Meteo data
     * @throws SQLException If fail to save data
     */
    public void saveMeteoData(String sensor, MeteoDBData data) throws SQLException {
        PreparedStatement statement =
                conn.prepareStatement("INSERT INTO " + sensor +
                        " (temp, hum, pres, hour, date) VALUES (?, ?, ?, ?, ?)");
        statement.setObject(1, data.getTemp());
        statement.setObject(2, data.getHum());
        statement.setObject(3, data.getPres());
        statement.setObject(4, data.getHour());
        statement.setObject(5, data.getDate());
        statement.execute();
        statement.close();
    }

    /**
     * Close db connection
     * @throws SQLException If fail to close
     */
    public void close() throws SQLException {
        this.conn.close();
        mtx.unlock();
    }
}
