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

package ru.futcamp.controller.modules.secure;

import ru.futcamp.controller.modules.secure.db.ISecureDB;
import ru.futcamp.controller.modules.secure.db.MIHDBData;
import ru.futcamp.controller.modules.secure.mod.SecureModule;

import java.sql.SQLException;

/**
 * Main In Home security system
 */
public class ManInHome extends MIHData implements IMainInHome {
    private ISecureDB db;
    private String ip;

    public ManInHome(ISecureDB db) {
        this.db = db;
    }

    public void setDBFileName(String fileName) {
        this.db.setFileName(fileName);
    }

    /**
     * Sync states with device
     */
    public void syncStates() throws Exception {
        SecureModule hdk = new SecureModule(ip);
        hdk.setMIHStates(isRadio(), isLamp());
    }

    /**
     * Save status to database
     * @throws Exception If fail to save status
     */
    public void saveData() throws Exception {
        try {
            db.connect();
            db.saveMIHStatus(new MIHDBData(isStatus(), getTimeOn(), getTimeOff()));
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            db.close();
        }
    }

    /**
     * Loading MIH data from db
     * @throws Exception If fail to load
     */
    public void loadDataFromDb() throws Exception {
        MIHDBData data;

        try {
            db.connect();
            data = db.loadMIHData();
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            db.close();
        }

        super.setStatus(data.isStatus());
        super.setTimeOn(data.getTimeOn());
        super.setTimeOff(data.getTimeOff());
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
