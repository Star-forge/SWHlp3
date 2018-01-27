package sw_client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.stream.IntStream;

public class Controller {
//
//    AdbController adbCtrl;
//
//    private final Logger log = LogManager.getLogger("com.adbmanager.log4j2");
//
//    private void mainSleep() throws InterruptedException {
//        log.info(String.format("*** ЗАДЕРЖКА ОТПРАВКИ СЕКУНД: %s ***", Configuration.MAIN_TIMEOUT));
//        Thread.sleep(Configuration.MAIN_TIMEOUT * 1000);
//    }
//
//    private final static String errMsg  = "Error!!";
//    private final static String noMsg   = "";
//    private final static String START   = "01start";
//    private final static String BOOT    = "02boot";
//    private final static String STAGE3  = "03st";
//    private final static String BOSS    = "07boss1";
//    private final static String LOSE    = "10boss lose";
//    private final static String VICTORY = "11victory1";
//    private final static String REVIVE  = "11revive";
//    private final static String VICTORY2= "12victory2";
//    private final static String NORUNE  = "13other no rune";
//    private final static String RUNE6   = "13rune 6";
//    private final static String RUNE    = "13rune";
//    private final static String REPLAY  = "15replay";
//    private final static String SELL    = "14sell";
//    private final static String NOENERGY    = "16no energy";
//    private final static String BUYENERGY   = "18buy energy";
//    private final static String CLICKENERGY = "17click energy";
//    private final static String BUYENERGYOK = "19buy energy ok";
//    private final static String ENERGYFULL  = "20energy full";
//
//    private final static int tapTimeout  = 500;
//    private static boolean supportFlag = false;
//
//    private void mainFunction() throws Exception, Core.ServerException {
//        if(AdbController.adbClients.size() == 0)
//            return;
//
//        //Image.delete();
//        //AdbController.Image();
//
//        while(!getScreenshotViaADB()) {
//            log.info("Не удалось получить изображение по ADB.");
//            Thread.sleep(1000);
//        }
//        if(!Image.getReadFromFile()) return;
//        //Image.createCopy();
//        Image.resizeImage(299, 299);
//
//        log.info("*** ОТПРАВКА ИЗОБРАЖЕНИЯ НА СЕРВЕР ***");
//        String srvMsg = NetworkAdapter.sendImage(Image.getScrName());
//
//        if(Configuration.VIEW_ONLY_MODE) return;
//
//        switch(srvMsg){
//            case noMsg: throw new MForm.ServerException("Данные от сервера не получены.");
//            case errMsg: throw new MForm.ServerException("Сервер сообщил об ошибке.");
//            case START:         startReaction(); break;
//            case BOOT:          bootReaction(); break;
//            case STAGE3:        bootReaction(); break;
//            case BOSS:          bossReaction(); break;
//            case LOSE:          loseOrVictoryReaction(); break;
//            case VICTORY:       loseOrVictoryReaction(); break;
//            case REVIVE:        reviveReaction(); break;
//            case VICTORY2:      victory2Reaction(); break;
//            case NORUNE:        noRuneReaction(); break;
//            case RUNE6:         runeReaction(); break;
//            case RUNE:          runeReaction(); break;
//            case REPLAY:        replayReaction(); break;
//            case SELL:          sellReaction();  break;
//            case NOENERGY:      energyReaction(); break;
//            case BUYENERGY:     energyReaction(); break;
//            case CLICKENERGY:   clickEnergyReaction(); break;
//            case BUYENERGYOK:   buyEnergyOkReaction(); break;
//            case ENERGYFULL:    energyFullReaction(); break;
//        }
//    }
//
//    private void startReaction() {
//        do_adb_tap(85, 70);
//    }
//
//    private void bootReaction() {
//        int timeout = Configuration.LONG_STAGE_TIMEOUT;
//        log.info(String.format("*** ПРОХОЖДЕНИЕ - ОЖИДАНИЕ СЕКУНД: %s ***", timeout));
//        IntStream.range(0, timeout).forEachOrdered(n -> {
//            if(Configuration.PAUSE) return;
//            else {
//                if(n%5==0) log.info(String.format("*** ПРОХОЖДЕНИЕ -  ОЖИДАНИЕ СЕКУНД: [%03d/%03d] ***", n, timeout));
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    log.error("Ошибка ожидания", e);
//                }
//            }
//        });
//    }
//
//    private void bossReaction()  {
//        int timeout = Configuration.BOSS_TIMEOUT;
//        log.info(String.format("*** БОСС - ОЖИДАНИЕ СЕКУНД: %s ***", timeout));
//        IntStream.range(0, timeout).forEachOrdered(n -> {
//            if(Configuration.PAUSE) return;
//            else {
//                if(n%5==0) log.info(String.format("*** БОСС - ОЖИДАНИЕ СЕКУНД: [%03d/%03d] ***", n, timeout));
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    log.error("Ошибка ожидания", e);
//                }
//            }
//        });
//    }
//
//    private void reviveReaction() {
//        do_adb_tap(65, 65);
//    }
//
//    private void loseOrVictoryReaction() throws InterruptedException {
//        do_adb_tap(50, 50);
//        do_adb_tap(10, 12);
//        Thread.sleep(tapTimeout);
//        do_adb_tap(50, 50);
//    }
//
//    private void victory2Reaction() {
//        do_adb_tap(50, 50);
//    }
//
//    private void noRuneReaction() {
//        IntStream.range(0, 4).forEachOrdered(n -> do_adb_tap(50, 75+n*5));
//    }
//
//    private void runeReaction() {
//        if(Configuration.SELL_ALL_RUNES){
//            log.info("Продажа всех рун ВКЛ");
//            do_adb_tap(37, 78);
//        } else {
//            log.debug("Продажа всех рун НЕ вкл");
//            do_adb_tap(63, 78);
//            do_adb_tap(63, 67);
//        }
//    }
//
//    private void replayReaction() {
//        do_adb_tap(45, 50);
//    }
//
//    private void sellReaction() {
//        do_adb_tap(37, 60);
//    }
//
//    private void energyFullReaction() {
//        do_adb_tap(83, 17);
//    }
//
//    private void buyEnergyOkReaction() {
//        do_adb_tap(50, 60);
//    }
//
//    private void clickEnergyReaction() {
//        if(supportFlag) do_adb_tap(30, 50);
//        else do_adb_tap(83, 17);
//        supportFlag = !supportFlag;
//    }
//
//    private void energyReaction() {
//        if(Configuration.BUY_ENERGY_AND_GO)
//            do_adb_tap(40, 60);
//        else
//            do_adb_tap(60, 60);
//
//        int timeout = Configuration.SLEEP_TIMEOUT_WITHOUT_ENERGY*60;
//        log.info(String.format("*** ЭНЕРГИЯ ЗАКОНЧИЛАСЬ - ОЖИДАНИЕ СЕКУНД: %s ***", timeout));
//        IntStream.range(0, timeout).forEachOrdered(n -> {
//            if(Configuration.PAUSE) return;
//            else {
//                if(n%5==0) log.info(String.format("*** ЭНЕРГИЯ ЗАКОНЧИЛАСЬ - ОЖИДАНИЕ СЕКУНД: [%05d/%05d]  ***", n, timeout));
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    log.error("Ошибка ожидания", e);
//                }
//            }
//        });
////    }
//
//    private void do_adb_tap(int x, int y) {
//        log.info("***ОЖИДАНИЕ ПОДКЛЮЧЕНИЯ К УСТРОЙСТВУ***");
//        try{
//            AdbController.runCmd("adb wait-for-device");
//            String tap_x = ""+(Image.getWidth() * x / 100);
//            String tap_y = ""+(Image.getHeight() * y / 100);
//            String shellCmdText = "adb shell input touchscreen tap " + tap_x + ' ' + tap_y + "";
//            AdbController.runCmd(shellCmdText);
//            log.info("***КЛИК ВЫПОЛНЕН***");
//        }catch(Exception e){
//            log.info("***ERROR***", e);
//        }
//    }
//
//    public boolean getScreenshotViaADB() {
//        log.info("***ОЖИДАНИЕ ПОДКЛЮЧЕНИЯ К УСТРОЙСТВУ***");
//        try{
//            adbCtrl.runCommand("adb wait-for-device");
//            adbCtrl.runCommand("adb shell screencap -p /data/local/tmp/tmp.png");
//            adbCtrl.runCommand("adb pull /data/local/tmp/tmp.png screenshot.jpg");
//            adbCtrl.runCommand("adb shell rm /data/local/tmp/tmp.png");
//            log.info("***ИЗОБРАЖЕНИЕ ПОЛУЧЕНО***");
//            return true;
//        }catch(Exception e){
//            log.info("***ERROR***");
//            return false;
//        }
//    }
//
//    public void doAdbTap(int[] imageAndScreenSize, int x, int y) throws Exception {
//        if(imageAndScreenSize[0] > 0){
//            int tapX = imageAndScreenSize[0] * x / 100;
//            int tapY = imageAndScreenSize[1] * y / 100;
//            String shellCommandStr = String.format("input tap %s %s\nexit\n", tapX, tapY);
//            adbCtrl.runCommand(shellCommandStr);
//            log.debug("run shell command {} via ADB", shellCommandStr);
//        }else{
//            log.debug("unable to do adb click via ADB because imageAndScreenSize[0] not greather 0");
//        }
//    }
}
