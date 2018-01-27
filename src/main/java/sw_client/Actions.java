package sw_client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static sw_client.Image.origScrName;
import static sw_client.Image.rotatedScrName;

class Actions {
    private static final Logger log = LogManager.getLogger("com.adbmanager.log4j2");

    static void clickAutoBtn(){
        do_adb_tap(20, 95);
    }

    static void clickStartBtn(){
        do_adb_tap(85, 70);
    }

    static void clickRightNoBtn(){
        do_adb_tap(65, 65);
    }

    static void clickCenter(){
        do_adb_tap(50, 50);
    }

    static void clickFreeSpace(){
        do_adb_tap(10, 12);
    }

    static void buyEnergyOkBtn() {
        do_adb_tap(50, 60);
    }


    static void do_adb_tap(int x, int y) {
        switch(Configuration.SCREEN_ROTATE){
            case 0:     do_adb_tap0(x,y);   break;
            case 90:    do_adb_tap90(x,y);  break;
            case 180:   do_adb_tap180(x,y); break;
            case 270:   do_adb_tap270(x,y); break;
            default:    do_adb_tap0(x,y);
        }
    }

    private static void do_adb_tap0(int x, int y) {
        MForm.toLog("*** Ожидание подключения к устройству");
        try{
            AdbController.runCmd("adb wait-for-device");
            String tap_x = ""+((Image.getWidth() * x / 100) + Configuration.SCREEN_MOVE_X);
            String tap_y = ""+((Image.getHeight() * y / 100) + Configuration.SCREEN_MOVE_Y);
            String shellCmdText = "adb shell input touchscreen tap " + tap_x + ' ' + tap_y + "";
            AdbController.runCmd(shellCmdText);
            MForm.toLog("*** Клик выполнен о координатам: ["+ tap_x + "," + tap_y + "]");
            if(Configuration.DEBUG)
                Image.drawPointOfTap(origScrName, Integer.parseInt(tap_x), Integer.parseInt(tap_y));
        }catch(Exception e){
            MForm.toLog("\n***ERR - do_adb_tap");
            log.error("ERR - do_adb_tap", e);
        }
    }

    private static void do_adb_tap90(int x, int y) {
        MForm.toLog("*** Ожидание подключения к устройству");
        try{
            AdbController.runCmd("adb wait-for-device");
            String tap_y = ""+(Image.getWidth() * x / 100);
            String tap_x = ""+(Image.getHeight() * (100-y) / 100);
            String shellCmdText = "adb shell input touchscreen tap " + tap_x + ' ' + tap_y + "";
            AdbController.runCmd(shellCmdText);
            MForm.toLog("*** Клик выполнен о координатам: ["+ tap_x + "," + tap_y + "]");
            if(Configuration.DEBUG)
                Image.drawPointOfTap(rotatedScrName, Integer.parseInt(tap_x), Integer.parseInt(tap_y));
        }catch(Exception e){
            MForm.toLog("\n***ERR - do_adb_tap");
            log.error("ERR - do_adb_tap", e);
        }
    }

    private static void do_adb_tap180(int x, int y) {
        MForm.toLog("*** Ожидание подключения к устройству");
        try{
            AdbController.runCmd("adb wait-for-device");
            String tap_x = ""+(Image.getWidth() * (100-x) / 100);
            String tap_y = ""+(Image.getHeight() * (100-y) / 100);
            String shellCmdText = "adb shell input touchscreen tap " + tap_x + ' ' + tap_y + "";
            AdbController.runCmd(shellCmdText);
            MForm.toLog("*** Клик выполнен о координатам: ["+ tap_x + "," + tap_y + "]");
            if(Configuration.DEBUG)
                Image.drawPointOfTap(rotatedScrName, Integer.parseInt(tap_x), Integer.parseInt(tap_y));
        }catch(Exception e){
            MForm.toLog("\n***ERR - do_adb_tap");
            log.error("ERR - do_adb_tap", e);
        }
    }

    private static void do_adb_tap270(int x, int y) {
        MForm.toLog("*** Ожидание подключения к устройству");
        try{
            AdbController.runCmd("adb wait-for-device");
            String tap_y = ""+(Image.getWidth() * (100-x) / 100);
            String tap_x = ""+(Image.getHeight() * y / 100);
            String shellCmdText = "adb shell input touchscreen tap " + tap_x + ' ' + tap_y + "";
            AdbController.runCmd(shellCmdText);
            MForm.toLog("*** Клик выполнен о координатам: ["+ tap_x + "," + tap_y + "]");
            if(Configuration.DEBUG)
                Image.drawPointOfTap(rotatedScrName, Integer.parseInt(tap_x), Integer.parseInt(tap_y));
        }catch(Exception e){
            MForm.toLog("\n***ERR - do_adb_tap");
            log.error("ERR - do_adb_tap", e);
        }
    }
}
