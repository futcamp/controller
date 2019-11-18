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

package ru.futcamp.controller.modules.therm.db;

import org.sqlite.JDBC;
import ru.futcamp.IAppModule;
import ru.futcamp.controller.modules.therm.ThermDevice;
import sun.awt.Mutex;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Database management class
 */
public class ThermDB implements IThermDB, IAppModule {
    private String fileName;
    private Connection conn;
    private Mutex mtx = new Mutex();

    private String modName;

    public ThermDB(String name, IAppModule ...dep) {
        this.modName = name;
    }

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
     * Get status and alarm states from db
     * @return States All devices states
     * @throws SQLException If fail to read states
     */
    public List<ThermDBData> loadThermData() throws SQLException {
        List<ThermDBData> data = new LinkedList<>();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM therm");

        while (resultSet.next()) {
            ThermDBData states = new ThermDBData();
            states.setName(resultSet.getString("name"));
            states.setStatus(resultSet.getBoolean("status"));
            states.setThreshold(resultSet.getInt("threshold"));
            data.add(states);
        }
        statement.close();
        resultSet.close();

        return data;
    }

    /**
     * Save states to db
     * @param data States data
     * @throws SQLException If fail to save states
     */
    public void saveStates(ThermDBData data) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("UPDATE therm SET status = ?, threshold = ? WHERE name = '" +
                                                            data.getName() + "'");
        statement.setObject(1, data.isStatus());
        statement.setObject(2, data.getThreshold());
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

    public String getModName() {
        return modName;
    }
}
