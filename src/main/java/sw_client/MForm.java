package sw_client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

    static void startStop(Boolean stop){
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
        tlog.addMouseListener(new MouseAdapter() {
        });
        report.addActionListener(e -> {
            String msg = JOptionPane.showInputDialog(null, "Пожалуйста, опишите какие действия привели к ошибке и как она проявляется.");
            Utils.reportAdmin(Utils.transliterateStringIfNeeded(msg));
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
                    Core.mainFunction();
                    endDateTime = Utils.getDatetimeNowInPythonFormat();
                    NetworkAdapter.sendStat(startDateTime, endDateTime, NetworkAdapter.callback);
                } catch (Core.ServerException e) {
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
            Utils.reportAdmin(Utils.transliterateStringIfNeeded("Ошибка основной функции") + e.getMessage());
        }
    }

    private static void decrementMaxErrCount(){
        Configuration.maxErrCount--;
        if(Configuration.maxErrCount <= 0){
            Utils.reportAdmin(Utils.transliterateStringIfNeeded("Нестабильная работа."));
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

    static void toLog(String text) {
        stlog.append("\n"+text);
        stlog.setCaretPosition(stlog.getDocument().getLength());
        log.info(text);
    }
}
