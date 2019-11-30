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

package ru.futcamp.controller.modules.light.db;

import ru.futcamp.db.RedisClient;

/**
 * Database management class
 */
public class LightDB extends RedisClient implements ILightDB {
    /**
     * constructor: connect to Redis server and authorization
     *
     * @param host  Ip address of database
     * @param table Redis table
     */
    public LightDB(String host, int table) throws Exception {
        super(host, table);
    }

    public Boolean getStatus(String alias) {
        return getBoolValue(alias);
    }

    public void saveStatus(String alias, boolean status) {
        setValue(alias, status);
    }
}
