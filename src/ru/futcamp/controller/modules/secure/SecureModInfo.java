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

public class SecureModInfo {
    private String alias;
    private boolean state;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
