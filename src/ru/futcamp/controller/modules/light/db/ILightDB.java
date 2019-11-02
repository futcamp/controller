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

import java.sql.SQLException;
import java.util.List;

public interface ILightDB {
    void setFileName(String fileName);
    void connect() throws SQLException;
    List<LightDBData> loadLightStates() throws SQLException;
    void saveStates(LightDBData data) throws SQLException;
    void close() throws SQLException;
}
