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

import sun.awt.Mutex;

public class SecureData {
    private boolean state;
    private Mutex mtx = new Mutex();

    public boolean isState() {
        boolean s;

        mtx.lock();
        s = state;
        mtx.unlock();

        return s;
    }

    public void setState(boolean state) {
        mtx.lock();
        this.state = state;
        mtx.unlock();
    }
}
