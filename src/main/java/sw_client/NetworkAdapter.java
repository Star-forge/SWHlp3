package sw_client;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static io.restassured.RestAssured.*;

/**
 * Created by Starforge on 31.10.2017.
 */
class NetworkAdapter {
    private static final Logger log = LogManager.getLogger("com.adbmanager.log4j2");

    private static String srvUrl = "";
    private static String apiUrl = srvUrl + "/api/UploadFile4Recognition";
    private static String apiUrl2 = srvUrl + "/classify";
    private static String reportUrl = srvUrl + "/report";
    private static String chkverUrl = srvUrl + "/chkver";
    private static String statUrl = srvUrl + "/stat";
    static String staticUrl = srvUrl + "/static/";
    static String updUrl = srvUrl + "/static/SWHlp.jar";
    static String callback = "";

    static void initPath(){
        if(Configuration.USE_ALT_SERVER)
            srvUrl = Configuration.ALT_SERVER;
        else
            srvUrl = Configuration.SERVER;
        apiUrl = srvUrl + "/api/UploadFile4Recognition";
        apiUrl2 = srvUrl + "/classify";
        reportUrl = srvUrl + "/report";
        chkverUrl = srvUrl + "/chkver";
        statUrl = srvUrl + "/stat";
        staticUrl = srvUrl + "/static/";
        updUrl = srvUrl + "/static/SWHlp.jar";
    }

    static Boolean checkNewVersion(){
        try {
            log.debug("check version ["+chkverUrl+"]");
            Response res = get(chkverUrl);
            String json = res.asString();
            JsonPath jp = new JsonPath(json);
            String version = jp.get("version");
            log.info(String.format("Ваша версия: %s. Последняя версия: %s.", Configuration.VERSION, version));
            return (Configuration.VERSION < Integer.parseInt(version));
        }catch (Exception e) {
            log.error("Не удалось проверить наличие новой версии", e);
        }
        return false;
    }

    static boolean reportBug(String msg, File file){
        try {
            log.debug("report ["+reportUrl+"]");

            Response res = given().
                formParam("user_name", Configuration.USERNAME).
                formParam("user_addr", Configuration.USERADDR).
                formParam("key", Configuration.KEY).
                formParam("msg", msg).
                formParam("filename", "debug.zip").
                multiPart("file", file, "application/octet-stream").
                post(reportUrl);

            return res.getStatusCode() == 200;
        }catch (Exception e) {
            //log.error("Не удалось отправить отчёт." +e.getMessage());
            return false;
        }
    }


    static String getExternalIP(){
        try {
            Response res = get("https://api.ipify.org");
            return res.asString();
        }catch (Exception e) {
            log.error("Не удалось получить внешний адрес клиента", e);
            return "UNKNOWN";
        }
    }

    static void sendStat(String startdate, String enddate, String callback_id) {
        try {
            Response res = given().
                    formParam("user_name", Configuration.USERNAME).
                    formParam("client_startdate", startdate).
                    formParam("client_enddate", enddate).
                    formParam("client_version", Configuration.VERSION).
                    formParam("callback_id", callback_id).
                    post(statUrl);
            if(res.getStatusCode() == 200){
                log.debug("Отправка статистики - успешно.");
            } else {
                log.debug("Ответ сервера на отправку статистики: "+ res.getStatusCode());
            }
        }catch (Exception e) {
            log.error("ERR Ошибка отправки статистики на сервер");
        }
    }

    static String sendImage(String imageName) throws MForm.ServerException {
        try {
//            final byte[] bytes = IOUtils.toByteArray(new FileInputStream(imageName));
            log.debug("send image to srv ["+apiUrl2+"] via POST msg");
            Response res = given().
                    formParam("user_name", Configuration.USERNAME).
                    formParam("user_addr", Configuration.USERADDR).
                    formParam("key", Configuration.KEY).
                    formParam("filename", "sw_screenshot").
                    multiPart("file", new File(imageName), "image/jpg").
                    //multiPart("file", "image.jpg", bytes, "image/jpg").
                    post(apiUrl2);

//            final byte[] bytes = IOUtils.toByteArray(new FileInputStream(imageName));

            if(res.getStatusCode() == 200){
                JsonPath jp = new JsonPath(res.asString());
                callback = jp.get("callback_id");
                String message = jp.get("Message");
                log.info(String.format("***ПОЛУЧЕН ОТВЕТ СЕРВЕРА НА ИЗОБРАЖЕНИЕ: [callback='%s'][message='%s'] ***", callback, message));
                return message;
            } else {
                log.debug("Ответ сервера: "+ res.getStatusCode());
                return "";
            }
        }catch (Exception e) {
            log.error("Не удалось отправить изображение." +e.getMessage());
            throw new MForm.ServerException("Не удалось отправить изображение." +e.getMessage());
        }
    }

    static void downloadFile(String sourceurl, String dest){
        URL url;
        try {
            url = new URL(sourceurl);
            HttpURLConnection hConnection = (HttpURLConnection) url
                    .openConnection();
            HttpURLConnection.setFollowRedirects(true);
            if (HttpURLConnection.HTTP_OK == hConnection.getResponseCode()) {
                InputStream in = hConnection.getInputStream();
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(dest));
                int filesize = hConnection.getContentLength();
                byte[] buffer = new byte[4096];
                int numRead;
                long numWritten = 0;
                while ((numRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, numRead);
                    numWritten += numRead;
                    System.out.println((double)numWritten/(double)filesize);
                }
                if(filesize!=numWritten)
                    System.out.println("Wrote "+numWritten+" bytes, should have been "+filesize);
                else
                    System.out.println("Downloaded successfully.");
                out.close();
                in.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
