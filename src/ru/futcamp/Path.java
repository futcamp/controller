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

package ru.futcamp;

/**
 * Default path storage
 */
class Path {
    private static Path path;

    static synchronized Path getInstance() {
        if (path == null)
            path = new Path();
        return path;
    }

    /**
     * Get default path
     * @param path Path alias
     * @return Path string
     */
    String getPath(String path) {
        switch (path) {
            case "log":
                return "/var/log/futcamp/";

            case "tgbot":
                return "/etc/futcamp/tgbot.conf";

            case "http":
                return "/etc/futcamp/http.conf";

            case "meteo":
                return "/etc/futcamp/meteo.conf";

            case "secure":
                return "/etc/futcamp/secure.conf";

            case "ctrl":
                return "/etc/futcamp/controller.conf";

            case "therm":
                return "/etc/futcamp/therm.conf";

            case "light":
                return "/etc/futcamp/light.conf";

            case "vision":
                return "/etc/futcamp/vision.conf";
        }
        return null;
    }
}
