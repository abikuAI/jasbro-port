/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util;

import jasbro.gui.pictures.ImageTag;
import jasbro.util.Settings;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigHandler {
    private static final Properties settings = new Properties();

    public static void loadConfig() {
        try {
            settings.load(new FileInputStream("config.ini"));
        }
        catch (IOException iOException) {
            // empty catch block
        }
        ImageTag.LOLI.setExcludeTag(!ConfigHandler.isShowLoliImages());
        ImageTag.FUTAONMALE.setExcludeTag(!ConfigHandler.isShowFutaOnMaleImages());
        ImageTag.BLACKANDWHITE.setExcludeTag(!ConfigHandler.isShowBlackAndWhiteImages());
        ImageTag.THREE_D.setExcludeTag(!ConfigHandler.isShow3dImages());
    }

    public static boolean changeResolution(Settings key, String resolution) throws FileNotFoundException, IOException {
        if (resolution != null && !resolution.equalsIgnoreCase(settings.getProperty(key.name()))) {
            settings.setProperty(key.name(), resolution);
            settings.store(new FileOutputStream("config.ini"), null);
            return true;
        }
        return false;
    }

    public static int getResolution(Settings key) {
        String setting = settings.getProperty(key.name());
        if (setting != null) {
            try {
                int defaultValue = Integer.valueOf(setting);
                return defaultValue;
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        if (key == Settings.RESOLUTIONHEIGHT) {
            return 720;
        }
        return 1280;
    }

    public static boolean changeSetting(Settings key, String value) throws FileNotFoundException, IOException {
        if (!value.equalsIgnoreCase(settings.getProperty(key.name()))) {
            settings.setProperty(key.name(), value);
            settings.store(new FileOutputStream("config.ini"), null);
            return true;
        }
        return false;
    }

    public static int getSetting(Settings key, int defaultValue) {
        String setting = settings.getProperty(key.name());
        if (setting != null) {
            try {
                int parsedSetting = Integer.valueOf(setting);
                return parsedSetting;
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        return defaultValue;
    }

    public static boolean getSetting(Settings key, boolean defaultValue) {
        String setting = settings.getProperty(key.name());
        if (setting != null) {
            if (setting.equalsIgnoreCase("true")) {
                return true;
            }
            if (setting.equalsIgnoreCase("false")) {
                return false;
            }
        }
        return defaultValue;
    }

    public static boolean isCheat() {
        return ConfigHandler.getSetting(Settings.CHEAT_MODE, false);
    }

    public static void setCheat(boolean cheat) {
        try {
            ConfigHandler.changeSetting(Settings.CHEAT_MODE, String.valueOf(cheat));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isShowTimeTaken() {
        return ConfigHandler.getSetting(Settings.SHOWTIMETAKEN, false);
    }

    public static void setShowTimeTaken(boolean timeTaken) {
        try {
            ConfigHandler.changeSetting(Settings.SHOWTIMETAKEN, String.valueOf(timeTaken));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isShowLoliImages() {
        return ConfigHandler.getSetting(Settings.LOLI_IMAGES, false);
    }

    public static void setShowLoliImages(boolean showLoliImages) {
        try {
            ConfigHandler.changeSetting(Settings.LOLI_IMAGES, String.valueOf(showLoliImages));
            ImageTag.LOLI.setExcludeTag(!ConfigHandler.isShowLoliImages());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isShowBlackAndWhiteImages() {
        return ConfigHandler.getSetting(Settings.BLACK_AND_WHITE_IMAGES, true);
    }

    public static void setShowBlackAndWhiteImages(boolean showBlackAndWhiteImages) {
        try {
            ConfigHandler.changeSetting(Settings.BLACK_AND_WHITE_IMAGES, String.valueOf(showBlackAndWhiteImages));
            ImageTag.BLACKANDWHITE.setExcludeTag(!ConfigHandler.isShowBlackAndWhiteImages());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isTownScreenNew() {
        return ConfigHandler.getSetting(Settings.TOWNSCREENNEW, true);
    }

    public static void setTownScreenNew(boolean townscreenNew) {
        try {
            ConfigHandler.changeSetting(Settings.TOWNSCREENNEW, String.valueOf(townscreenNew));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isShowFutaOnMaleImages() {
        return ConfigHandler.getSetting(Settings.FUTA_ON_MALE_IMAGES, false);
    }

    public static boolean isShow3dImages() {
        return ConfigHandler.getSetting(Settings.THREE_D_IMAGES, true);
    }

    public static void setShowFutaOnMaleImages(boolean showFutaOnMaleImages) {
        try {
            ConfigHandler.changeSetting(Settings.FUTA_ON_MALE_IMAGES, String.valueOf(showFutaOnMaleImages));
            ImageTag.FUTAONMALE.setExcludeTag(!ConfigHandler.isShowFutaOnMaleImages());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setShow3DImages(boolean show3dImages) {
        try {
            ConfigHandler.changeSetting(Settings.THREE_D_IMAGES, String.valueOf(show3dImages));
            ImageTag.THREE_D.setExcludeTag(!ConfigHandler.isShow3dImages());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isShowUnavailableSchool() {
        return ConfigHandler.getSetting(Settings.SHOW_UNAVAILABLE_SCHOOL, true);
    }

    public static void setShowUnavailableSchool(boolean showUnavailableSchool) {
        try {
            ConfigHandler.changeSetting(Settings.SHOW_UNAVAILABLE_SCHOOL, String.valueOf(showUnavailableSchool));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isShowCosplay() {
        return ConfigHandler.getSetting(Settings.COSPLAY, true);
    }

    public static void setShowCosplay(boolean showCosplay) {
        try {
            ConfigHandler.changeSetting(Settings.COSPLAY, String.valueOf(showCosplay));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isShowSleep() {
        return ConfigHandler.getSetting(Settings.SLEEP, true);
    }

    public static void setShowSleep(boolean showSleep) {
        try {
            ConfigHandler.changeSetting(Settings.SLEEP, String.valueOf(showSleep));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setHideArrowKeys(boolean selected) {
        try {
            ConfigHandler.changeSetting(Settings.HIDEARROWKEYS, String.valueOf(selected));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isHideArrowKeys() {
        return ConfigHandler.getSetting(Settings.HIDEARROWKEYS, false);
    }

    public static void setProtagonistIsPlayer(boolean selected) {
        try {
            ConfigHandler.changeSetting(Settings.PROTAGONISTISPLAYER, String.valueOf(selected));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isProtagonistPlayer() {
        return ConfigHandler.getSetting(Settings.PROTAGONISTISPLAYER, false);
    }

    public static void setUseSystemLookAndFeel(boolean selected) {
        try {
            ConfigHandler.changeSetting(Settings.USE_SYSTEM_LF, String.valueOf(selected));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isUseSystemLookAndFeel() {
        return ConfigHandler.getSetting(Settings.USE_SYSTEM_LF, false);
    }

    public static void setShowNumbersOnBars(boolean selected) {
        try {
            ConfigHandler.changeSetting(Settings.SHOW_NUMBERS_ON_BARS, String.valueOf(selected));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isShowNumbersOnBars() {
        return ConfigHandler.getSetting(Settings.SHOW_NUMBERS_ON_BARS, true);
    }
}

