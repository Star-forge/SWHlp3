package sw_client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.stream.IntStream;
import static sw_client.Image.*;

/**
 * Created by Starforge on 31.12.2017.
 */
public class MForm {
    private static final Logger log = LogManager.getLogger("com.adbmanager.log4j2");

    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JTextField ipTextField;
    private JButton adbDisconnectButton;
    private JButton adbDevicesButton;
    private JButton adbConnectIPButton;
    private JButton adbTcpip5555Button;
    private JButton adbUsbButton;
    private JCheckBox adbReconnectCheckBox;
    private JCheckBox buyEnergyCheckBox;
    private JCheckBox sellRunesCheckBox;
    private JTextField waitEnergyTextField;
    private JTextArea tlog;
    private JLabel status;
    private JLabel lbwait;
    private JCheckBox viewOnlyCheckBox;
    private JButton start_stopButton;
    private JScrollPane scrPane;
    private JButton report;
    static JTextArea stlog;
    static JTextField wEnTF;
    static MForm form;

    private static void startStop(Boolean stop){
        if(stop == null)
            Configuration.PAUSE = !Configuration.PAUSE;
        else
            Configuration.PAUSE = stop;
        form.start_stopButton.setText(Configuration.PAUSE?"СТАРТ":"СТОП");
        form.status.setText("*** " + (Configuration.PAUSE?"Бот приостановлен!":"Бот запущен!"));
        toLog("*** " + (Configuration.PAUSE?"Бот приостановлен!":"Бот запущен!"));
    }

    public MForm() {
        log.debug("mform");
        stlog = this.tlog;
        wEnTF = this.waitEnergyTextField;
        form = this;
        log.debug("start_stopButton");
        start_stopButton.addActionListener(e -> {
            startStop(null);
            log.debug("start_stopButton -> PAUSE = " + Configuration.PAUSE);
        });
        log.debug("adbReconnectCheckBox");
        adbReconnectCheckBox.addActionListener(e -> {
            if(adbReconnectCheckBox.isSelected()) Configuration.RECONNECT = 600; else Configuration.RECONNECT = -1;
            log.debug("adbReconnectCheckBox -> " + Configuration.RECONNECT);
        });
        log.debug("sellRunesCheckBox");
        sellRunesCheckBox.addActionListener(e -> {
            Configuration.SELL_ALL_RUNES = sellRunesCheckBox.isSelected();
            log.debug("sellRunesCheckBox -> " + Configuration.SELL_ALL_RUNES);
        });
        log.debug("viewOnlyCheckBox");
        viewOnlyCheckBox.addActionListener(e -> {
            Configuration.VIEW_ONLY_MODE = viewOnlyCheckBox.isSelected();
            log.debug("viewOnlyCheckBox -> " + Configuration.VIEW_ONLY_MODE);
        });
        buyEnergyCheckBox.addActionListener(e -> {
            Configuration.BUY_ENERGY_AND_GO = buyEnergyCheckBox.isSelected();
            log.debug("buyEnergyCheckBox -> " + Configuration.BUY_ENERGY_AND_GO);
        });
        log.debug("adbUsbButton");
        adbUsbButton.addActionListener(e -> {
            AdbController.runCommand(AdbController.adb_usb);
            log.debug("adbUsbButton -> click");
        });
        log.debug("start_stopButton");
        adbTcpip5555Button.addActionListener(e -> {
            AdbController.runCommand(AdbController.tcpip);
            log.debug("adbTcpip5555Button -> click");
        });
        log.debug("adbConnectIPButton");
        adbConnectIPButton.addActionListener(e -> {
            AdbController.runCommand(AdbController.adb_connect_ip+ipTextField.getText());
            log.debug("adbConnectIPButton -> click" + AdbController.adb_connect_ip+ipTextField.getText());
        });
        log.debug("adbDevicesButton");
        adbDevicesButton.addActionListener(e -> {
            AdbController.runCommand(AdbController.adb_devices);
            log.debug("adbDevicesButton -> click");
        });
        log.debug("adbDisconnectButton");
        adbDisconnectButton.addActionListener(e -> {
            AdbController.runCommand(AdbController.adb_disconnect);
            log.debug("adbDisconnectButton -> click");
        });
        log.debug("other");
        tlog.addMouseListener(new MouseAdapter() {
        });
        report.addActionListener(e -> {
            String msg = JOptionPane.showInputDialog(null, "Пожалуйста, опишите какие действия привели к ошибке и как она проявляется.");
            Utils.reportAdmin(msg);
        });
    }

    private static void reLocateWindow(JFrame guiFrame){
        if(Configuration.WINDOW_X_COORDINATE >= 0 & Configuration.WINDOW_Y_COORDINATE >= 0) {
            guiFrame.setLocation(Configuration.WINDOW_X_COORDINATE, Configuration.WINDOW_Y_COORDINATE);
        }
    }

    void setStatus(String text){
        status.setText(text);
    }

    private static void statusChecker(MForm form){
        Thread statusConnThread = new Thread(() -> {
            while (true) {
                    AdbController.runCommand("adb devices", form);
                    log.debug("run command 'adb devices' in run Thread");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        statusConnThread.setDaemon(true);
        statusConnThread.start();
    }

    public static void main(String[] args) {
        try{
            log.info("*** ЗАПУСК ПРОГРАММЫ ***");
            Configuration.loadParams();
            log.debug("NetworkAdapter.initPath=>");
            NetworkAdapter.initPath();
//            if(NetworkAdapter.checkNewVersion() && Configuration.UPDATE){
//                JOptionPane.showMessageDialog(null,
//                        "Ваша версия устарела. Необходимо обновление!");
//                Updater up = new Updater(NetworkAdapter.updUrl);
//                Updater.update();
//                System.exit(0);
//            }
            log.debug("Frame.setup=>");
            JFrame frame = new JFrame(Configuration.wndName);
            MForm form = new MForm();
            frame.setContentPane(form.mainPanel);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

            log.debug("Frame-elements.init=>");
            form.start_stopButton.setText(Configuration.PAUSE?"СТАРТ":"СТОП");
            form.adbReconnectCheckBox.setSelected(Configuration.RECONNECT > 0);
            form.buyEnergyCheckBox.setSelected(Configuration.BUY_ENERGY_AND_GO);
            form.sellRunesCheckBox.setSelected(Configuration.SELL_ALL_RUNES);
            form.viewOnlyCheckBox.setSelected(Configuration.VIEW_ONLY_MODE);
            form.waitEnergyTextField.setText(String.valueOf(Configuration.SLEEP_TIMEOUT_WITHOUT_ENERGY));
            form.ipTextField.setText(String.valueOf(Configuration.DEVICE_IP_ADDR));

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    if(NetworkAdapter.checkNewVersion() && Configuration.UPDATE){
                        new Updater(NetworkAdapter.updUrl);
                        Updater.update();
                        System.exit(0);
                    }
                    System.exit(0);
                }
            });


            frame.pack();
            frame.setVisible(true);
            reLocateWindow(frame);

            toLog("*** Начало работы программы");
            AdbController.tryAdb();
            statusChecker(form);
            NetworkAdapter.sendStat("", "", "");
            //Main Function
            String startDateTime;
            String endDateTime;
            while (true) {
                try {
                    startDateTime = Utils.getDatetimeNowInPythonFormat();
                    mainFunction();
                    endDateTime = Utils.getDatetimeNowInPythonFormat();
                    NetworkAdapter.sendStat(startDateTime, endDateTime, NetworkAdapter.callback);
                } catch (ServerException e) {
                    log.error(e);
                    decrementMaxErrCount();
                }
                if(!Configuration.PAUSE || !(AdbController.adbClients.size() == 0))
                    mainSleep();
                else
                    Thread.sleep(500);
            }
        } catch (Exception e) {
            log.error("Ошибка основной функции", e);
            Utils.reportAdmin("Ошибка основной функции" + e.getMessage());
        }
    }

    private static boolean isUpdate(String[] args) {
        boolean update = false;

        // process options
        int argi = 0;
        while (argi < args.length) {
            String arg = args[argi];
            if (!arg.startsWith("-"))
                break;
            for (int i=1; i<arg.length(); i++) {
                char c = arg.charAt(i);
                switch (c) {
                    case 'u' : update = true; break;
                    default : return false;
                }
            }
            argi++;
        }
        return update;
    }


    private static void decrementMaxErrCount(){
        Configuration.maxErrCount--;
        if(Configuration.maxErrCount <= 0){
            Utils.reportAdmin("Нестабильная работа.");
            JOptionPane.showMessageDialog(null, "Работа программы не стабильна.\n" +
                    "Сформируйте и отправьте разработчику отчет об ошибке.\n" +
                    "Отчет ('debug.zip') формируется в директории с логами (по-умолчанию: '"+Configuration.logsPath +"') после нажатия на кнопку 'report bug' .\n" +
                    "Работа программы приостановлена.");
            startStop(true);
            Configuration.maxErrCount = 5;
        }
    }

    private static void mainSleep() throws InterruptedException {
        toLog(String.format("*** Задержка отправки секунд: %s", Configuration.MAIN_TIMEOUT));
        Thread.sleep(Configuration.MAIN_TIMEOUT * 1000);
    }



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

    private final static int tapTimeout  = 500;
    private static boolean supportFlag = false;

    private static boolean getScreenshotViaADB() {
        toLog("*** Ожидание подключения к устройству");
        try{
            AdbController.runCmd("adb wait-for-device");
            AdbController.runCmd("adb shell screencap -p /data/local/tmp/tmp.png");
            AdbController.runCmd("adb pull /data/local/tmp/tmp.png screenshot.jpg");
            AdbController.runCmd("adb shell rm /data/local/tmp/tmp.png");
            toLog("*** Изображение получено");
            return true;
        }catch(Exception e){
            log.info("***ERROR***", e);
            return false;
        }
    }

    private static void changeImageSize(){
        int tmp = Image.getHeight();
        Image.setHeight(Image.getWidth());
        Image.setWidth(tmp);
    }

    private static void mainFunction() throws Exception, ServerException {

        //Image.delete();
        //AdbController.Image();
        while(Configuration.PAUSE){
            Thread.sleep(500);
        }


        while(!getScreenshotViaADB()) {
            toLog("ERR Не удалось получить изображение по ADB.");
            Thread.sleep(1000);
        }
        if(!Image.getReadFromFile()) return;
        Image.createCopy(origScrName);
//        if(Image.getWidth() <= Image.getHeight() && (Configuration.SCREEN_ROTATE == 0 || Configuration.SCREEN_ROTATE == 180)) {
//            JOptionPane.showMessageDialog(null, "Ваше устройство неправильно формирует изображение.\n" +
//                    "" + Image.getWidth() + " : " + Image.getHeight() +
//                    "C текущими настройками продолжение, скорее всего, невозможно.\n" +
//                    "Рекомендуется выставить в настройках SCREEN_ROTATE, равное 90 или 270.\n" +
//                    "Работа приостановлена!");
//            reportAdmin("rotated image");
//        }

        if(Image.getWidth() < Image.getHeight()) {
            changeImageSize();
        }

        if(Configuration.SCREEN_ROTATE != 0) {
            Image.rotate(Configuration.SCREEN_ROTATE);
            Image.createCopy(rotatedScrName);
        }

        Image.resizeImage(299, 299);
        Image.createCopy(resizedScrName);

        toLog("*** Отправка изображения на сервер");
        String srvMsg = NetworkAdapter.sendImage(Image.getScrName());
        toLog("*** Получен ответ от сервера: " + srvMsg);
        Image.imageNameSuffix = srvMsg;

        if(Configuration.VIEW_ONLY_MODE) return;

        // Добавление результата в список.
        Utils.addResultToList(srvMsg);
        if(Utils.isSameResultsLastNTimes(15) && srvMsg.equalsIgnoreCase(STAGE3)) {
            Actions.clickAutoBtn();
            toLog("--Click Autoplay button");
            Utils.resultList.clear();
        }
        if(Utils.isSameResultsLastNTimes(30))
            Utils.reportAdmin("Too many Same Results in Line");

        switch(srvMsg){
            case unauth: JOptionPane.showMessageDialog(null, "Вам необходимо запросить кодовое слово '"+Configuration.KEY+"' через форму на сайте программы."); startStop(true); break;
            case noMsg: throw new ServerException("Данные от сервера не получены.");
            case errMsg: throw new ServerException("Сервер сообщил об ошибке.");
            case START:         Actions.clickStartBtn(); break;
            case BOOT:          bootReaction(); break;
            case STAGE3:        bootReaction(); break;
            case BOSS:          bossReaction(); break;
            case LOSE:          loseOrVictoryReaction(); break;
            case VICTORY:       loseOrVictoryReaction(); break;
            case REVIVE:        reviveReaction(); break;
            case VICTORY2:      victory2Reaction(); break;
            case NORUNE:        noRuneReaction(); break;
            case RUNE5:         runeReaction(); break;
            case RUNE6:         runeReaction(); break;
            case RUNE:          runeReaction(); break;
            case REPLAY:        replayReaction(); break;
            case SELL:          sellReaction();  break;
            case NOENERGY:      energyReaction(); break;
            case BUYENERGY:     energyReaction(); break;
            case CLICKENERGY:   clickEnergyReaction(); break;
            case BUYENERGYOK:   buyEnergyOkReaction(); break;
            case ENERGYFULL:    energyFullReaction(); break;
        }
    }

    private static void bootReaction() {
        int timeout = Configuration.LONG_STAGE_TIMEOUT;
        toLog(String.format("*** Прохождение - ожидание секунд: %s", timeout));
        IntStream.range(0, timeout).forEachOrdered(n -> {
            if(!Configuration.PAUSE)
            {
                if(n%5==0)
                    toLog(String.format("*** Прохождение -  ожидание секунд: [%03d/%03d]", n, timeout));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    toLog("ERR Ошибка ожидания - bootReaction");
                    log.error("ERR Ошибка ожидания", e);
                }
            }
        });
    }

    private static void bossReaction()  {
        int timeout = Configuration.BOSS_TIMEOUT;
        toLog(String.format("*** Босс - ожидание секунд: %s", timeout));
        IntStream.range(0, timeout).forEachOrdered(n -> {
            if(!Configuration.PAUSE)
            {
                if(n%5==0)
                    toLog(String.format("*** Босс - ожидание секунд: [%03d/%03d]", n, timeout));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    toLog("ERR Ошибка ожидания - bossReaction");
                    log.error("Ошибка ожидания", e);
                }
            }
        });
    }

    private static void reviveReaction() {
        toLog("Воскрешения за кристаллы - НЕ будет.");
        toLog("Нажатие кнопки 'НЕТ'.");
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
        IntStream.range(0, 4).forEachOrdered(n -> do_adb_tap(50, 75+n*5));
    }

    private static void runeReaction() {
        if(Configuration.SELL_ALL_RUNES){
            toLog("Продажа всех рун ВКЛ");
            do_adb_tap(37, 78);
        } else {
            toLog("Продажа всех рун НЕ вкл");
            do_adb_tap(63, 78);
            do_adb_tap(63, 67);
        }
    }

    private static void replayReaction() {
        do_adb_tap(45, 50);
    }

    private static void sellReaction() {
        if(Configuration.SELL_ALL_RUNES)
            do_adb_tap(39, 60);
        else
            do_adb_tap(61, 60);
    }

    private static void energyFullReaction() {
        do_adb_tap(83, 17);
    }

    private static void buyEnergyOkReaction() {
        do_adb_tap(50, 60);
    }

    private static void clickEnergyReaction() {
        if(supportFlag) do_adb_tap(30, 50);
        else do_adb_tap(83, 17);
        supportFlag = !supportFlag;
    }

    private static void energyReaction() {
        if(Configuration.BUY_ENERGY_AND_GO)
            do_adb_tap(40, 60);
        else {
            do_adb_tap(60, 60);
            Configuration.SLEEP_TIMEOUT_WITHOUT_ENERGY = Integer.valueOf(wEnTF.getText());
            int timeout = Configuration.SLEEP_TIMEOUT_WITHOUT_ENERGY * 60;
            toLog(String.format("*** Энергия закончилась - ожидание секунд: %s", timeout));
            IntStream.range(0, timeout).forEachOrdered(n -> {
                if (!Configuration.PAUSE) {
                    form.setStatus("Ожидание энергии: " + (((timeout-n)/3600)%60)+":"+(((timeout-n)/60)%60)+":"+((timeout-n)%60));
                    if (n % 5 == 0)
                        log.info(String.format("*** Энергия закончилась - ожидание секунд: [%05d/%05d]", n, timeout));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        toLog("ERR Ошибка ожидания - energyReaction");
                        log.error("Ошибка ожидания", e);
                    }
                }
            });
        }
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
        toLog("*** Ожидание подключения к устройству");
        try{
            AdbController.runCmd("adb wait-for-device");
            String tap_x = ""+((Image.getWidth() * x / 100) + Configuration.SCREEN_MOVE_X);
            String tap_y = ""+((Image.getHeight() * y / 100) + Configuration.SCREEN_MOVE_Y);
            String shellCmdText = "adb shell input touchscreen tap " + tap_x + ' ' + tap_y + "";
            AdbController.runCmd(shellCmdText);
            toLog("*** Клик выполнен о координатам: ["+ tap_x + "," + tap_y + "]");
            if(Configuration.DEBUG)
                Image.drawPointOfTap(origScrName, Integer.parseInt(tap_x), Integer.parseInt(tap_y));
        }catch(Exception e){
            toLog("\n***ERR - do_adb_tap");
            log.error("ERR - do_adb_tap", e);
        }
    }

    private static void do_adb_tap90(int x, int y) {
        toLog("*** Ожидание подключения к устройству");
        try{
            AdbController.runCmd("adb wait-for-device");
            String tap_y = ""+(Image.getWidth() * x / 100);
            String tap_x = ""+(Image.getHeight() * (100-y) / 100);
            String shellCmdText = "adb shell input touchscreen tap " + tap_x + ' ' + tap_y + "";
            AdbController.runCmd(shellCmdText);
            toLog("*** Клик выполнен о координатам: ["+ tap_x + "," + tap_y + "]");
            if(Configuration.DEBUG)
                Image.drawPointOfTap(rotatedScrName, Integer.parseInt(tap_x), Integer.parseInt(tap_y));
        }catch(Exception e){
            toLog("\n***ERR - do_adb_tap");
            log.error("ERR - do_adb_tap", e);
        }
    }

    private static void do_adb_tap180(int x, int y) {
        toLog("*** Ожидание подключения к устройству");
        try{
            AdbController.runCmd("adb wait-for-device");
            String tap_x = ""+(Image.getWidth() * (100-x) / 100);
            String tap_y = ""+(Image.getHeight() * (100-y) / 100);
            String shellCmdText = "adb shell input touchscreen tap " + tap_x + ' ' + tap_y + "";
            AdbController.runCmd(shellCmdText);
            toLog("*** Клик выполнен о координатам: ["+ tap_x + "," + tap_y + "]");
            if(Configuration.DEBUG)
                Image.drawPointOfTap(rotatedScrName, Integer.parseInt(tap_x), Integer.parseInt(tap_y));
        }catch(Exception e){
            toLog("\n***ERR - do_adb_tap");
            log.error("ERR - do_adb_tap", e);
        }
    }

    private static void do_adb_tap270(int x, int y) {
        toLog("*** Ожидание подключения к устройству");
        try{
            AdbController.runCmd("adb wait-for-device");
            String tap_y = ""+(Image.getWidth() * (100-x) / 100);
            String tap_x = ""+(Image.getHeight() * y / 100);
            String shellCmdText = "adb shell input touchscreen tap " + tap_x + ' ' + tap_y + "";
            AdbController.runCmd(shellCmdText);
            toLog("*** Клик выполнен о координатам: ["+ tap_x + "," + tap_y + "]");
            if(Configuration.DEBUG)
                Image.drawPointOfTap(rotatedScrName, Integer.parseInt(tap_x), Integer.parseInt(tap_y));
        }catch(Exception e){
            toLog("\n***ERR - do_adb_tap");
            log.error("ERR - do_adb_tap", e);
        }
    }

    private static void toLog(String text) {
        stlog.append("\n"+text);
        stlog.setCaretPosition(stlog.getDocument().getLength());
        log.info(text);
    }

    static class ServerException extends Throwable {
        public ServerException(String s) {
            log.error(s);
        }
    }
}
