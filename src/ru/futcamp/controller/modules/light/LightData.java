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

package ru.futcamp.controller.modules.light;

import sun.awt.Mutex;

public class LightData {
    private boolean status;
    private Mutex statMtx = new Mutex();

    public boolean isStatus() {
        boolean stat;

        statMtx.lock();
        stat = status;
        statMtx.unlock();

        return stat;
    }

    public void setStatus(boolean status) {
        statMtx.lock();
        this.status = status;
        statMtx.unlock();
    }
}
