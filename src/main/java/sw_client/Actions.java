package sw_client;

import static sw_client.MForm.do_adb_tap;

public class Actions {

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
}
