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

package ru.futcamp.controller.subcontrollers.modules.hum.db;

import com.alibaba.fastjson.JSON;
import ru.futcamp.db.RedisClient;

/**
 * Database management class
 */
public class HumDB extends RedisClient implements IHumDB {

    /**
     * constructor: connect to Redis server and authorization
     *
     * @param host  Ip address of database
     * @param table Redis table
     */
    public HumDB(String host, int table) throws Exception {
        super(host, table);
    }

    /**
     * Save therm data to database
     * @param name Name of device
     * @param data Therm data
     */
    public void saveData(String name, HumDBData data) {
        setValue(name, JSON.toJSONString(data));
    }

    /**
     * Load therm data from ru.futcamp.db
     * @param name Name of device
     * @return Therm data
     */
    public HumDBData loadData(String name) {
        return JSON.parseObject(getStrValue(name), HumDBData.class);
    }
}
