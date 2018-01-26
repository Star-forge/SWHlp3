package sw_client;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Starforge on 28.10.2017.
 */
public class AdbController {
    private static final Logger log = LogManager.getLogger("com.adbmanager.log4j2");

    public static final Integer UNCONNECTED = 0;
    public static final Integer CONNECTED = 1;
    public static final Integer ONLINE = 2;
    public static final Integer OFFLINE = 3;
    public static final Integer UNKNOWN = 4;

    public static final Integer USB = 0;
    public static final Integer WIFI = 1;

    public static final String adb_usb = "adb usb";
    public static final String tcpip = "adb tcpip 5555";
    public static final String adb_connect_ip = "adb connect ";
    public static final String adb_disconnect = "adb disconnect";
    public static final String adb_devices = "adb devices";

    public static List<AdbClient> adbClients = new ArrayList<>();

    static void tryAdb(){
        try {
            AdbController.runCommand(AdbController.adb_devices);
        }catch(Exception e){
            log.debug("Не найден adb", e);
            saveAdb();
        }
    }

    static void saveAdb(){
        NetworkAdapter.downloadFile(NetworkAdapter.staticUrl + "adb.exe", "adb.exe");
        NetworkAdapter.downloadFile(NetworkAdapter.staticUrl + "AdbWinApi.dll", "AdbWinApi.dll");
        NetworkAdapter.downloadFile(NetworkAdapter.staticUrl + "AdbWinUsbApi.dll", "AdbWinUsbApi.dll");
    }

    public static class AdbClient{
        public String clientName;
        public Integer connectionType;
        public Integer status;

        public AdbClient(String clientName){
            this.clientName = clientName;
            this.connectionType = AdbController.UNKNOWN;
            this.status = AdbController.UNCONNECTED;
        }

        public AdbClient(String clientName, Integer connectionType, Integer status){
            this.clientName = clientName;
            this.connectionType = connectionType;
            this.status = status;
        }
    }

    public void disconnectAll(){
        //this.runCommand(adb_disconnect);
    }


    public static void getDeviceList(){
        getResultOfCommand(adb_devices);
        log.debug("run command 'adb devices' in run Thread");
    }

    public static void getResultOfCommand(String cmd){
        Process p;
        try {
            String line, text = "";

            p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            while ((line = reader.readLine())!= null) {
                log.debug(String.format("command line text is '%s'", line));
                if(!(line.equalsIgnoreCase("List of devices attached") | line.isEmpty())) {
                    String[] data = line.split("\\t");
                    Integer connectionType = AdbController.USB;
                    Integer status = AdbController.UNCONNECTED;
                    if(data[1].contains("device"))
                        status = AdbController.CONNECTED;
                    if(data[0].contains(":"))
                        connectionType = AdbController.WIFI;
                    adbClients.add(new AdbClient(data[0], connectionType, status));
                }

                if(line.startsWith("unable to connect")){
                    log.error(line);
                }
                if(line.length() > 0) {
                    text += line;
                    log.debug(String.format("status text is '%s'", line));
                }
            }
        } catch (Exception e) {
            log.error("Ошибка в процессе выполнения команды пользователя:", e);
        }
    }

    public static void runCmd(String cmd){
        Process p;
        try {
            String line;
            p = Runtime.getRuntime().exec(cmd);
            log.info("wait-" + cmd);
            p.waitFor();
            //log.info("-wait");
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            while ((line = reader.readLine()) != null) {
                log.debug(String.format("command line text is '%s'", line));
                if (line.startsWith("unable to connect")) {
                    log.error(line);
                }
            }
        } catch (IOException io){
            saveAdb();
        } catch (Exception e) {
            log.error("Ошибка в процессе выполнения команды пользователя:", e);
        }
    }

    public static void runCommand(String cmd){
        new Thread(new Runnable() {
            @Override public void run() {
                Process p;
                try {
                    String line, text = "";

                    p = Runtime.getRuntime().exec(cmd);
                    p.waitFor();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

                    while ((line = reader.readLine())!= null) {
                        log.debug(String.format("command line text is '%s'", line));
                        line = line.replaceAll("List of devices attached","");
                        line = line.replaceAll("\tdevice","");
                        if(line.startsWith("unable to connect")){
//                                    Alert alert = new Alert(Alert.AlertType.ERROR);
//                                    // alert.setTitle("unable to connect");
//                                    alert.setHeaderText("Ошибка соединения");
//                                    alert.setContentText(line);
//                                    alert.showAndWait().ifPresent(rs -> {
//                                        if (rs == ButtonType.OK) {
//                                            System.out.println("Pressed OK.");
//                                        }
//                                    });
                            log.error(line);
                        }
                        if(line.length() > 0) {
                            text += line;
                            log.debug(String.format("status text is '%s'", line));
                        }
                    }
                    if(text.length() == 0) text = "Не подключено или статус не определен.";
                } catch (Exception e) {

                    log.error("Ошибка в процессе выполнения команды пользователя:", e);
                }
            }
        }).start();
    }

    public static void runCommand(String cmd, MForm frame){
        new Thread(new Runnable() {
            @Override public void run() {
                Process p;
                try {
                    String line, text = "";

                    p = Runtime.getRuntime().exec(cmd);
                    p.waitFor();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

                    while ((line = reader.readLine())!= null) {
                        log.debug(String.format("command line text is '%s'", line));
                        line = line.replaceAll("List of devices attached","");
                        line = line.replaceAll("\tdevice","");
                        if(line.startsWith("unable to connect")){
//                                    Alert alert = new Alert(Alert.AlertType.ERROR);
//                                    // alert.setTitle("unable to connect");
//                                    alert.setHeaderText("Ошибка соединения");
//                                    alert.setContentText(line);
//                                    alert.showAndWait().ifPresent(rs -> {
//                                        if (rs == ButtonType.OK) {
//                                            System.out.println("Pressed OK.");
//                                        }
//                                    });
                            log.error(line);
                        }
                        if(line.length() > 0) {
                            text += line;
                            log.debug(String.format("status text is '%s'", line));
                        }
                    }
                    if(text.length() == 0) text = "Не подключено или статус не определен.";
                    frame.setStatus(text);

                } catch(IOException ie){
                    log.debug("Не найден adb", ie);
                    saveAdb();
                } catch (Exception e) {
                    frame.setStatus("Статус: " + e.toString());
                    log.error("Ошибка в процессе выполнения команды пользователя:", e);
                }
            }
        }).start();
    }
}
