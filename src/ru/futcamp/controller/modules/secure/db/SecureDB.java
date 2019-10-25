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

package ru.futcamp.controller.modules.secure.db;

import org.sqlite.JDBC;
import sun.awt.Mutex;

import java.sql.*;

/**
 * Database management class
 */
public class SecureDB implements ISecureDB {
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
     * Get status and alarm states from db
     * @return States
     * @throws SQLException If fail to read states
     */
    public SecureDBData loadSecureStates() throws SQLException {
        SecureDBData states = new SecureDBData();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT status, alarm FROM secure");

        while (resultSet.next()) {
            states.setStatus(resultSet.getBoolean("status"));
            states.setAlarm(resultSet.getBoolean("alarm"));
        }
        statement.close();
        resultSet.close();

        return states;
    }

    /**
     * Save states to db
     * @param data States data
     * @throws SQLException If fail to save states
     */
    public void saveStates(SecureDBData data) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("UPDATE secure SET status = ?, alarm = ?");
        statement.setObject(1, data.isStatus());
        statement.setObject(2, data.isAlarm());
        statement.execute();
        statement.close();
    }

    /**
     * Save Man In Home states to db
     * @param data MIH data
     * @throws SQLException If fail to save states
     */
    public void saveMIHStatus(MIHDBData data) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("UPDATE mih SET status = ?, tmon = ?, tmoff = ?");
        statement.setObject(1, data.isStatus());
        statement.setObject(2, data.getTimeOn());
        statement.setObject(3, data.getTimeOff());
        statement.execute();
        statement.close();
    }

    /**
     * Get Man In Home subsystem data from db
     * @return States
     * @throws SQLException If fail to read states
     */
    public MIHDBData loadMIHData() throws SQLException {
        MIHDBData data = new MIHDBData();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM mih");

        while (resultSet.next()) {
            data.setStatus(resultSet.getBoolean("status"));
            data.setTimeOn(resultSet.getInt("tmon"));
            data.setTimeOff(resultSet.getInt("tmoff"));
        }
        statement.close();
        resultSet.close();

        return data;
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
