package sw_client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.swing.*;
import java.util.stream.IntStream;
import static sw_client.Image.origScrName;
import static sw_client.Image.resizedScrName;
import static sw_client.Image.rotatedScrName;

class Core {
    private static final Logger log = LogManager.getLogger("com.adbmanager.log4j2");

    private final static String unauth  = "Unauthorized ";
    private final static String errMsg  = "Error!!";
    private final static String noMsg   = "";
    private final static String START   = "01start";
    private final static String BOOT    = "02boot";
    private final static String STAGE3  = "03st";
    private final static String BOSS    = "07boss1";
    private final static String LOSE    = "10boss lose";
    private final static String VICTORY = "11victory1";
    private final static String REVIVE  = "11revive";
    private final static String VICTORY2= "12victory2";
    private final static String NORUNE  = "13other no rune";
    private final static String RUNE5   = "13rune 5";
    private final static String RUNE6   = "13rune 6";
    private final static String RUNE    = "13rune";
    private final static String REPLAY  = "15replay";
    private final static String SELL    = "14sell";
    private final static String NOENERGY    = "16no energy";
    private final static String BUYENERGY   = "18buy energy";
    private final static String CLICKENERGY = "17click energy";
    private final static String BUYENERGYOK = "19buy energy ok";
    private final static String ENERGYFULL  = "20energy full";

    private final static String GAME    = "game";
    private final static String NOGAME    = "nogame";

    private final static int tapTimeout  = 500;
    private static boolean supportFlag = false;
    
    private static void doReaction(String status) throws ServerException, InterruptedException {
        switch (status) {
            case unauth:
                JOptionPane.showMessageDialog(null, "Вам необходимо запросить кодовое слово '" + Configuration.KEY + "' через форму на сайте программы.");
                MForm.startStop(true);
                break;
            case noMsg:
                throw new ServerException("Данные от сервера не получены.");
            case errMsg:
                throw new ServerException("Сервер сообщил об ошибке.");
            case START:
                Actions.clickStartBtn();
                break;
            case BOOT:
                bootReaction();
                break;
            case STAGE3:
                bootReaction();
                break;
            case BOSS:
                bossReaction();
                break;
            case LOSE:
                loseOrVictoryReaction();
                break;
            case VICTORY:
                loseOrVictoryReaction();
                break;
            case REVIVE:
                reviveReaction();
                break;
            case VICTORY2:
                victory2Reaction();
                break;
            case NORUNE:
                noRuneReaction();
                break;
            case RUNE5:
                runeReaction();
                break;
            case RUNE6:
                runeReaction();
                break;
            case RUNE:
                runeReaction();
                break;
            case REPLAY:
                replayReaction();
                break;
            case SELL:
                sellReaction();
                break;
            case NOENERGY:
                energyReaction();
                break;
            case BUYENERGY:
                energyReaction();
                break;
            case CLICKENERGY:
                clickEnergyReaction();
                break;
            case BUYENERGYOK:
                buyEnergyOkReaction();
                break;
            case ENERGYFULL:
                energyFullReaction();
                break;
        }
    }

    static class ServerException extends Throwable {
        ServerException(String s) {
            log.error(s);
        }
    }

    private static void bootReaction() {
        int timeout = Configuration.LONG_STAGE_TIMEOUT;
        MForm.toLog(String.format("*** Прохождение - ожидание секунд: %s", timeout));
        IntStream.range(0, timeout).forEachOrdered(n -> {
            if(!Configuration.PAUSE)
            {
                if(n%5==0)
                    MForm.toLog(String.format("*** Прохождение -  ожидание секунд: [%03d/%03d]", n, timeout));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    MForm.toLog("ERR Ошибка ожидания - bootReaction");
                    log.error("ERR Ошибка ожидания", e);
                }
            }
        });
    }

    private static void bossReaction()  {
        int timeout = Configuration.BOSS_TIMEOUT;
        MForm.toLog(String.format("*** Босс - ожидание секунд: %s", timeout));
        IntStream.range(0, timeout).forEachOrdered(n -> {
            if(!Configuration.PAUSE)
            {
                if(n%5==0)
                    MForm.toLog(String.format("*** Босс - ожидание секунд: [%03d/%03d]", n, timeout));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    MForm.toLog("ERR Ошибка ожидания - bossReaction");
                    log.error("Ошибка ожидания", e);
                }
            }
        });
    }

    private static void reviveReaction() {
        MForm.toLog("Воскрешения за кристаллы - НЕ будет.");
        MForm.toLog("Нажатие кнопки 'НЕТ'.");
        Actions.clickRightNoBtn();
    }

    private static void loseOrVictoryReaction() throws InterruptedException {
        Actions.clickCenter();
        Actions.clickFreeSpace();
        Thread.sleep(tapTimeout);
        Actions.clickCenter();
    }

    private static void victory2Reaction() {
        Actions.clickCenter();
    }

    private static void noRuneReaction() {
        IntStream.range(0, 4).forEachOrdered(n -> Actions.do_adb_tap(50, 75+n*5));
    }

    private static void runeReaction() {
        if(Configuration.SELL_ALL_RUNES){
            MForm.toLog("Продажа всех рун ВКЛ");
            Actions.do_adb_tap(37, 78);
        } else {
            MForm.toLog("Продажа всех рун НЕ вкл");
            Actions.do_adb_tap(63, 78);
            Actions.do_adb_tap(63, 67);
        }
    }

    private static void replayReaction() {
        Actions.do_adb_tap(45, 50);
    }

    private static void sellReaction() {
        if(Configuration.SELL_ALL_RUNES)
            Actions.do_adb_tap(39, 60);
        else
            Actions.do_adb_tap(61, 60);
    }

    private static void energyFullReaction() {
        Actions.do_adb_tap(83, 17);
    }

    private static void buyEnergyOkReaction() {
        Actions.buyEnergyOkBtn();
    }

    private static void clickEnergyReaction() {
        if(supportFlag) Actions.do_adb_tap(30, 50);
        else Actions.do_adb_tap(83, 17);
        supportFlag = !supportFlag;
    }

    private static void energyReaction() {
        if(Configuration.BUY_ENERGY_AND_GO)
            Actions.do_adb_tap(40, 60);
        else {
            Actions.do_adb_tap(60, 60);
            Configuration.SLEEP_TIMEOUT_WITHOUT_ENERGY = Integer.valueOf(MForm.wEnTF.getText());
            int timeout = Configuration.SLEEP_TIMEOUT_WITHOUT_ENERGY * 60;
            MForm.toLog(String.format("*** Энергия закончилась - ожидание секунд: %s", timeout));
            IntStream.range(0, timeout).forEachOrdered(n -> {
                if (!Configuration.PAUSE) {
                    MForm.form.setStatus("Ожидание энергии: " + (((timeout-n)/3600)%60)+":"+(((timeout-n)/60)%60)+":"+((timeout-n)%60));
                    if (n % 5 == 0)
                        log.info(String.format("*** Энергия закончилась - ожидание секунд: [%05d/%05d]", n, timeout));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        MForm.toLog("ERR Ошибка ожидания - energyReaction");
                        log.error("Ошибка ожидания", e);
                    }
                }
            });
        }
    }

    static void mainFunction() throws Exception, Core.ServerException {

        //Image.delete();
        //AdbController.Image();
        while (Configuration.PAUSE) {
            Thread.sleep(500);
        }


        while (!AdbController.getScreenshotViaADB()) {
            MForm.toLog("ERR Не удалось получить изображение по ADB.");
            Thread.sleep(1000);
        }
        if (!Image.getReadFromFile()) return;
        Image.createCopy(origScrName);
//        if(Image.getWidth() <= Image.getHeight() && (Configuration.SCREEN_ROTATE == 0 || Configuration.SCREEN_ROTATE == 180)) {
//            JOptionPane.showMessageDialog(null, "Ваше устройство неправильно формирует изображение.\n" +
//                    "" + Image.getWidth() + " : " + Image.getHeight() +
//                    "C текущими настройками продолжение, скорее всего, невозможно.\n" +
//                    "Рекомендуется выставить в настройках SCREEN_ROTATE, равное 90 или 270.\n" +
//                    "Работа приостановлена!");
//            reportAdmin("rotated image");
//        }

        if (Image.getWidth() < Image.getHeight()) {
            Image.changeImageSize();
        }

        if (Configuration.SCREEN_ROTATE != 0) {
            Image.rotate(Configuration.SCREEN_ROTATE);
            Image.createCopy(rotatedScrName);
        }

        Image.resizeImage(299, 299);
        Image.createCopy(resizedScrName);

        MForm.toLog("*** Отправка изображения на сервер");
        String srvMsg = NetworkAdapter.sendImage(Image.getScrName());
        MForm.toLog("*** Получен ответ от сервера: " + srvMsg);
        Image.imageNameSuffix = srvMsg;

        if (Configuration.VIEW_ONLY_MODE) return;

        // Добавление результата в список.
        Utils.addResultToList(srvMsg);
        if (Utils.isSameResultsLastNTimes(15) && srvMsg.equalsIgnoreCase(Core.STAGE3)) {
            Actions.clickAutoBtn();
            MForm.toLog("--Click Autoplay button");
            Utils.resultList.clear();
        }
        if (Utils.isSameResultsLastNTimes(30)
                && !srvMsg.equalsIgnoreCase(Core.unauth)
                && !srvMsg.equalsIgnoreCase(Core.GAME)
                && !srvMsg.equalsIgnoreCase(Core.NOGAME)) {
            Utils.reportAdmin(Utils.transliterateStringIfNeeded("Too many Same Results in Line"));
            log.debug("*** Странное поведение программы - слишком много одинаковых оссбщений от сервера - Отправка BugReport");
            Utils.resultList.clear();
        }

        Core.doReaction(srvMsg);
    }
    
}
