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

package ru.futcamp.db;

import redis.clients.jedis.Jedis;

/**
 * Redis database client
 */
public class RedisClient {
    private Jedis client;

    /**
     * constructor: connect to Redis server and authorization
     * @param host Ip address of database
     * @param table Redis table
     */
    public RedisClient(String host, int table) throws Exception {
        client = new Jedis(host);
        client.connect();
        String code = client.select(table);
        if (!code.equals("OK"))
            throw new Exception("Table not found");
    }

    /**
     * Set string value to redis table
     * @param key Existing key
     * @param value New value
     */
    public void setValue(String key, String value) {
        client.set(key, value);
    }

    /**
     * Set boolean value to redis table
     * @param key Existing key
     * @param value New value
     */
    public void setValue(String key, boolean value) {
        client.set(key, String.valueOf(value));
    }

    /**
     * Set integer value to redis table
     * @param key Existing key
     * @param value New value
     */
    public void setValue(String key, int value) {
        client.set(key, String.valueOf(value));
    }

    /**
     * Get redis value
     * @param key Existing key
     * @return String value
     */
    public String getStrValue(String key) {
        return client.get(key);
    }

    /**
     * Get redis value
     * @param key Existing key
     * @return Integer value
     */
    public Integer getIntValue(String key) {
        return Integer.parseInt(client.get(key));
    }

    /**
     * Get redis value
     * @param key Existing key
     * @return Boolean value
     */
    public Boolean getBoolValue(String key) {
        return Boolean.parseBoolean(client.get(key));
    }

    /**
     * close connection
     */
    public void close() {
        if (client.isConnected()) {
            client.disconnect();
        }
    }
}
