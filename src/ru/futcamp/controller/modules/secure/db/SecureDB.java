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

import db.RedisClient;

/**
 * Database management class
 */
public class SecureDB extends RedisClient implements ISecureDB {
    /**
     * constructor: connect to Redis server and authorization
     *
     * @param host  Ip address of database
     * @param table Redis table
     */
    public SecureDB(String host, int table) throws Exception {
        super(host, table);
    }

    public void saveStatus(boolean status) {
        setValue("status", status);
    }

    public void saveAlarm(boolean alarm) {
        setValue("alarm", alarm);
    }

    public void saveMIHStatus(boolean status) {
        setValue("mih-status", status);
    }

    public void saveMIHTimeOn(int hour) {
        setValue("mih-on", hour);
    }

    public void saveMIHTimeOff(int hour) {
        setValue("mih-off", hour);
    }

    public Boolean getStatus() {
        return getBoolValue("status");
    }

    public Boolean getAlarm() {
        return getBoolValue("alarm");
    }

    public Boolean getMIHStatus() {
        return getBoolValue("mih-status");
    }

    public Integer getMIHTimeOn() {
        return getIntValue("mih-on");
    }

    public Integer getMIHTimeOff() {
        return getIntValue("mih-off");
    }
}
