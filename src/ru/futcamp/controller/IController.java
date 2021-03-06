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

package ru.futcamp.controller;

import ru.futcamp.controller.subcontrollers.ILightController;
import ru.futcamp.controller.subcontrollers.IMeteoController;
import ru.futcamp.controller.subcontrollers.IMiscController;
import ru.futcamp.controller.subcontrollers.ISecureController;

public interface IController {
    IMeteoController getMeteo();
    ISecureController getSecure();
    ILightController getLight();
    IMiscController getMisc();
    boolean startAll();
}
