package sw_client;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ibm.icu.text.Transliterator;
import java.io.*;
import java.util.Properties;

/**
 * Created by Starforge on 17.10.2017.
 */
public class Configuration {
    private static final Logger log = LogManager.getLogger("com.adbmanager.log4j2");
    public static String CYRILLIC_TO_LATIN = "Cyrillic-Latin";

    // Properties (user configurable)
    static int             RECONNECT = 600;
    static String           ADB_MODE = "USB";
    static String     DEVICE_IP_ADDR = "192.168.1.1";
    static String             SERVER = "http://217.71.231.9:30001";
    static String         ALT_SERVER = "http://217.71.231.9:30002";
    static Boolean    USE_ALT_SERVER = true;
    static Boolean            UPDATE = true;
    static Boolean             DEBUG = true;
    static int   WINDOW_X_COORDINATE = 0;
    static int   WINDOW_Y_COORDINATE = 0;
    static int         SCREEN_ROTATE = 0;
    static int         SCREEN_MOVE_X = 0;
    static int         SCREEN_MOVE_Y = 0;
    static int            MARGIN_TOP = 0;
    static int          MARGIN_RIGHT = 0;
    static int         MARGIN_BOTTOM = 0;
    static int           MARGIN_LEFT = 0;
    static int    LONG_STAGE_TIMEOUT = 15;
    static int          BOSS_TIMEOUT = 10;
    static Boolean    SELL_ALL_RUNES = false;
    static Boolean    VIEW_ONLY_MODE = false;
    static Boolean BUY_ENERGY_AND_GO = false;
    static int SLEEP_TIMEOUT_WITHOUT_ENERGY = 120;
    static String                KEY = getUserName();
    static int          MAIN_TIMEOUT = 1;

    static Properties prop = new Properties();
    static final String PATH_CONFFILE = System.getProperty("user.dir") + "\\swhlp.properties";

    // Configuration (not user configurable)
    static final int         VERSION = 310;
    static String           USERNAME = "";
    static String           USERADDR = "";
    static boolean             PAUSE = false;
    static int           maxErrCount = 5;
    static String            wndName = "SWSA HELPER v" + Configuration.VERSION;

    static String           logsPath = "logs";

    private static boolean bErrConfig = false;

    // Utils.java
    static final int maxResultListSize = 50;

    private static boolean containsCyrillic(String text){
        for(int i = 0; i < text.length(); i++) {
            if(Character.UnicodeBlock.of(text.charAt(i)).equals(Character.UnicodeBlock.CYRILLIC))
                return true;
        }
        return false;
    }

    private static String getUserName(){
        String username = System.getProperty("user.name");
        if(containsCyrillic(username)) {
            Transliterator toLatinTrans = Transliterator.getInstance(CYRILLIC_TO_LATIN);
            username = toLatinTrans.transliterate(username);
        }
        return username;
    }

    private static String getProperty(String key, Object default_value) throws IOException {
        if(prop.getProperty(key) == null) bErrConfig = true;
        return prop.getProperty(key, String.valueOf(default_value));
    }

    static void loadParams() throws Exception {
        log.info("*** ЧТЕНИЕ ФАЙЛА КОНФИГУРАЦИИ ***");
        log.debug("Read: " + PATH_CONFFILE);
        InputStream input = null;
        try {
            USERNAME = getUserName();
            USERADDR = NetworkAdapter.getExternalIP();

            input = new FileInputStream(PATH_CONFFILE);
            prop.load(input);

            // get the property value and print it out
            RECONNECT                   = Integer.valueOf(getProperty("RECONNECT", RECONNECT));
            DEVICE_IP_ADDR              = getProperty("DEVICE_IP_ADDR", DEVICE_IP_ADDR);
            ADB_MODE                    = getProperty("ADB_MODE", ADB_MODE);
            SERVER                      = getProperty("SERVER", SERVER);
            ALT_SERVER                  = getProperty("ALT_SERVER", ALT_SERVER);
            USE_ALT_SERVER              = Boolean.valueOf(getProperty("USE_ALT_SERVER", USE_ALT_SERVER));
            UPDATE                      = Boolean.valueOf(getProperty("UPDATE", UPDATE));
            DEBUG                       = Boolean.valueOf(getProperty("DEBUG", DEBUG));
            WINDOW_X_COORDINATE         = Integer.valueOf(getProperty("WINDOW_X_COORDINATE", WINDOW_X_COORDINATE));
            WINDOW_Y_COORDINATE         = Integer.valueOf(getProperty("WINDOW_Y_COORDINATE", WINDOW_Y_COORDINATE));
            SCREEN_ROTATE               = Integer.valueOf(getProperty("SCREEN_ROTATE", SCREEN_ROTATE));
            SCREEN_MOVE_X               = Integer.valueOf(getProperty("SCREEN_MOVE_X", SCREEN_MOVE_X));
            SCREEN_MOVE_Y               = Integer.valueOf(getProperty("SCREEN_MOVE_Y", SCREEN_MOVE_Y));
            MARGIN_TOP                  = Integer.valueOf(getProperty("MARGIN_TOP", MARGIN_TOP));
            MARGIN_RIGHT                = Integer.valueOf(getProperty("MARGIN_RIGHT", MARGIN_RIGHT));
            MARGIN_BOTTOM               = Integer.valueOf(getProperty("MARGIN_BOTTOM", MARGIN_BOTTOM));
            MARGIN_LEFT                 = Integer.valueOf(getProperty("MARGIN_LEFT", MARGIN_LEFT));
            LONG_STAGE_TIMEOUT          = Integer.valueOf(getProperty("LONG_STAGE_TIMEOUT", LONG_STAGE_TIMEOUT));
            BOSS_TIMEOUT                = Integer.valueOf(getProperty("BOSS_TIMEOUT", BOSS_TIMEOUT));
            SELL_ALL_RUNES              = Boolean.valueOf(getProperty("SELL_ALL_RUNES", SELL_ALL_RUNES));
            VIEW_ONLY_MODE              = Boolean.valueOf(getProperty("VIEW_ONLY_MODE", VIEW_ONLY_MODE));
            BUY_ENERGY_AND_GO           = Boolean.valueOf(getProperty("BUY_ENERGY_AND_GO", BUY_ENERGY_AND_GO));
            SLEEP_TIMEOUT_WITHOUT_ENERGY = Integer.valueOf(getProperty("SLEEP_TIMEOUT_WITHOUT_ENERGY", SLEEP_TIMEOUT_WITHOUT_ENERGY));
            KEY                         = getProperty("KEY", getUserName());
            MAIN_TIMEOUT                = Integer.valueOf(getProperty("MAIN_TIMEOUT", MAIN_TIMEOUT));
            if(bErrConfig) {
                writeConfig();
            }
            printConf();
        } catch (IOException ex) {
            log.debug("Ошибка чтения файла конфигурации.", ex);
            writeConfig();
            //saveAllParams();
            loadParams();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    log.debug("Ошибка при закрытии потока чтения файла конфигурации.", e);
                }
            }
        }
    }

    static void printConf(){
        int strFillerLen = 30;
        log.info(StringUtils.rightPad("VERSION", strFillerLen, '.') + VERSION);
        log.info(StringUtils.rightPad("SERVER", strFillerLen, '.') + SERVER);
        log.info(StringUtils.rightPad("ALT_SERVER", strFillerLen, '.') + ALT_SERVER);
        log.info(StringUtils.rightPad("USE_ALT_SERVER", strFillerLen, '.') + USE_ALT_SERVER);
        log.info(StringUtils.rightPad("RECONNECT", strFillerLen, '.') + RECONNECT);
        log.info(StringUtils.rightPad("ADB_MODE", strFillerLen, '.') + ADB_MODE);
        log.info(StringUtils.rightPad("DEVICE_IP_ADDR", strFillerLen, '.') + DEVICE_IP_ADDR);
        log.info(StringUtils.rightPad("UPDATE", strFillerLen, '.') + UPDATE);
        log.info(StringUtils.rightPad("DEBUG", strFillerLen, '.') + DEBUG);
        log.info(StringUtils.rightPad("WINDOW_X_COORDINATE", strFillerLen, '.') + WINDOW_X_COORDINATE);
        log.info(StringUtils.rightPad("WINDOW_Y_COORDINATE", strFillerLen, '.') + WINDOW_Y_COORDINATE);
        log.info(StringUtils.rightPad("SCREEN_ROTATE", strFillerLen, '.') + SCREEN_ROTATE);
        log.info(StringUtils.rightPad("SCREEN_MOVE_X", strFillerLen, '.') + SCREEN_MOVE_X);
        log.info(StringUtils.rightPad("SCREEN_MOVE_Y", strFillerLen, '.') + SCREEN_MOVE_Y);
        log.info(StringUtils.rightPad("MARGIN_TOP", strFillerLen, '.') + MARGIN_TOP);
        log.info(StringUtils.rightPad("MARGIN_RIGHT", strFillerLen, '.') + MARGIN_RIGHT);
        log.info(StringUtils.rightPad("MARGIN_BOTTOM", strFillerLen, '.') + MARGIN_BOTTOM);
        log.info(StringUtils.rightPad("MARGIN_LEFT", strFillerLen, '.') + MARGIN_LEFT);
        log.info(StringUtils.rightPad("LONG_STAGE_TIMEOUT", strFillerLen, '.') + LONG_STAGE_TIMEOUT);
        log.info(StringUtils.rightPad("BOSS_TIMEOUT", strFillerLen, '.') + BOSS_TIMEOUT);
        log.info(StringUtils.rightPad("SELL_ALL_RUNES", strFillerLen, '.') + SELL_ALL_RUNES);
        log.info(StringUtils.rightPad("VIEW_ONLY_MODE", strFillerLen, '.') + VIEW_ONLY_MODE);
        log.info(StringUtils.rightPad("BUY_ENERGY_AND_GO", strFillerLen, '.') + BUY_ENERGY_AND_GO);
        log.info(StringUtils.rightPad("SLEEP_TIMEOUT_WITHOUT_ENERGY", strFillerLen, '.') + SLEEP_TIMEOUT_WITHOUT_ENERGY);
        log.info(StringUtils.rightPad("KEY", strFillerLen, '.') + KEY);
        log.info(StringUtils.rightPad("MAIN_TIMEOUT", strFillerLen, '.') + MAIN_TIMEOUT);
        log.info(StringUtils.rightPad("", strFillerLen+5, '-'));
        log.info(StringUtils.rightPad("USERNAME", strFillerLen, '.') + USERNAME);
        log.info(StringUtils.rightPad("USERADDR", strFillerLen, '.') + USERADDR);
    }

    static boolean writeConfig(){
        OutputStream out = null;
        boolean ret;
        try {
            out = new FileOutputStream(PATH_CONFFILE);

            String config = "#Файл настроек summoners war helper" +
                    "\n#***ПОДКЛЮЧЕНИЕ К СЕРВЕРУ***" +
                    "\n#Адрес основного сервера:" +
                    "\nSERVER=" + String.valueOf(SERVER) +
                    "\n#Адрес резервного сервера (beta-server):" +
                    "\nALT_SERVER=" + String.valueOf(ALT_SERVER) +
                    "\n#Подключаться к резервному серверу по-умолчанию:" +
                    "\nUSE_ALT_SERVER=" + String.valueOf(USE_ALT_SERVER) +
                    "\n" +
                    "\n#***ПОДКЛЮЧЕНИЕ К УСТРОЙСТВУ***" +
                    "\n#Переподключаться по ADB автоматически (таймаут в секундах/\"-1\" - для отключения):" +
                    "\nRECONNECT=" +   String.valueOf(RECONNECT) +
                    "\n#IP-адрес устройства (необходим для подключения по TCPIP):" +
                    "\nDEVICE_IP_ADDR=" +   String.valueOf(DEVICE_IP_ADDR) +
                    "\n#Режим подключения (USB или TCPIP):" +
                    "\nADB_MODE=" +   String.valueOf(ADB_MODE) +
                    "\n" +
                    "\n#***ГЛОБАЛЬНЫЕ НАСТРОЙКИ ПРОГРАММЫ***" +
                    "\n#Обновлять клиент (при завершении работы):" +
                    "\nUPDATE=" + String.valueOf(UPDATE) +
                    "\n#Сохранение отладочной информации:" +
                    "\nDEBUG=" + String.valueOf(DEBUG) +
                    "\n#Перемещать окно на координату Х при запуске:" +
                    "\nWINDOW_X_COORDINATE=" + String.valueOf(WINDOW_X_COORDINATE) +
                    "\n#Перемещать окно на координату Y при запуске:" +
                    "\nWINDOW_Y_COORDINATE=" + String.valueOf(WINDOW_Y_COORDINATE) +
                    "\n#Угол поворота экрана (необходим, если изображения приходят с устройства повернутые на 90, 180 или 270 градусов):" +
                    "\nSCREEN_ROTATE=" + String.valueOf(SCREEN_ROTATE) +
                    "\n#Смещение скрина на координату Х:" +
                    "\nSCREEN_MOVE_X=" + String.valueOf(SCREEN_MOVE_X) +
                    "\n#Смещение скрина на координату Y:" +
                    "\nSCREEN_MOVE_Y=" + String.valueOf(SCREEN_MOVE_Y) +
                    "\n#Смещение верхней границы экрана:" +
                    "\nMARGIN_TOP=" + String.valueOf(MARGIN_TOP) +
                    "\n#Смещение правой границы экрана:" +
                    "\nMARGIN_RIGHT=" + String.valueOf(MARGIN_RIGHT) +
                    "\n#Смещение нижней границы экрана:" +
                    "\nMARGIN_BOTTOM=" + String.valueOf(MARGIN_BOTTOM) +
                    "\n#Смещение левой границы экрана:" +
                    "\nMARGIN_LEFT=" + String.valueOf(MARGIN_LEFT) +
                    "\n#Ваш идентификатор для доступа:" +
                    "\nKEY=" + String.valueOf(KEY) +
                    "\n" +
                    "\n#***ИГРОВЫЕ НАСТРОЙКИ ПРОГРАММЫ***" +
                    "\n#Таймаут проверки состояния, в сек (рекомендуется от 1 до 5):" +
                    "\nMAIN_TIMEOUT=" + String.valueOf(MAIN_TIMEOUT) +
                    "\n#Таймаут ожидания прохождения от загрузки локации до босса, в сек:" +
                    "\nLONG_STAGE_TIMEOUT=" + String.valueOf(LONG_STAGE_TIMEOUT) +
                    "\n#Таймаут ожидания убийства босса, в сек:" +
                    "\nBOSS_TIMEOUT=" + String.valueOf(BOSS_TIMEOUT) +
                    "\n#Продавать руны:" +
                    "\nSELL_ALL_RUNES=" + String.valueOf(SELL_ALL_RUNES) +
                    "\n#Только просмотр (бот не кликает, однако следит за игрой):" +
                    "\nVIEW_ONLY_MODE=" + String.valueOf(VIEW_ONLY_MODE) +
                    "\n#Покупать энергию, если она закончилась:" +
                    "\nBUY_ENERGY_AND_GO=" + String.valueOf(BUY_ENERGY_AND_GO) +
                    "\n#Таймаут ожидания накопления энергии (затем продолжение), в мин:" +
                    "\nSLEEP_TIMEOUT_WITHOUT_ENERGY=" + String.valueOf(SLEEP_TIMEOUT_WITHOUT_ENERGY) + "\n";
            out.write(config.getBytes());
            out.flush();
            ret = true;
        } catch (IOException io) {
            log.debug("Ошибка записи файла конфигурации.", io);
            ret = false;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.debug("Ошибка при закрытии потока записи файла конфигурации.", e);
                    ret = false;
                }
            }
        }
        return ret;
    }

    private static void saveParam(String key, String value){
        OutputStream out = null;
        try {
            out = new FileOutputStream(PATH_CONFFILE);
            prop.setProperty(key, value);
            prop.store(out, "#");
        } catch (IOException io) {
            log.debug("Ошибка записи файла конфигурации.", io);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.debug("Ошибка при закрытии потока записи файла конфигурации.", e);
                }
            }
        }
    }


}
