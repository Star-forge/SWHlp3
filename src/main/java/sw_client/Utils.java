package sw_client;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Utils {
    private static final Logger log = LogManager.getLogger("com.adbmanager.log4j2");

    static List<String> resultList = new ArrayList<>();

    static void addResultToList(String srvMsg){
        resultList.add(srvMsg);
        if(resultList.size() > Configuration.maxResultListSize)
            resultList.remove(0);
    }

    static boolean isSameResultsLastNTimes(int N){
        if(resultList.size() < N)
            return false;

        List<String> list4check = new ArrayList<>(resultList.subList(resultList.size() - N, resultList.size()));
        while(list4check.contains(resultList.get(resultList.size() - 1)))
            list4check.remove(resultList.get(resultList.size() - 1));
        return list4check.size() == 0;
    }

    static void genZip(String sourcePath, String targetPath) {
        byte[] buffer = new byte[1024];

        try{
            FileOutputStream fos = new FileOutputStream(targetPath);
            ZipOutputStream zos = new ZipOutputStream(fos);

            List<File> files = (List<File>) FileUtils.listFiles(new File(sourcePath), null, true);
            for (File file : files) {
                String filepath = file.getAbsolutePath();
                String filename = file.getName();
                if(filename.contains(".zip")) continue;
                ZipEntry ze = new ZipEntry(filename);
                zos.putNextEntry(ze);
                FileInputStream in = new FileInputStream(filepath);
                int len;
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                in.close();
                zos.closeEntry();
                log.debug("Отладочный файл {} добавлен", filename);
            }
            //remember close it
            zos.close();
            log.debug("Отладочная информация запакована");

        }catch(IOException ex){
            log.error("Не удалось создать архив с отладкой", ex);
        }
    }

    static void reportAdmin(String msg){
        String targetPath = Configuration.logsPath + "\\debug.zip";
        Utils.genZip(Configuration.logsPath, targetPath);
        File zip = new File(targetPath);
        if(zip.exists()) {
            if (NetworkAdapter.reportBug(msg, zip)) {
                JOptionPane.showMessageDialog(null, "Cообщение об ошибке отправлено!");
            } else
                JOptionPane.showMessageDialog(null, "Невозможно отправить сообщение об ошибке.\n" +
                        "Вы можете самостоятельно переслать файл debug.zip, который лежит в папке '"+Configuration.logsPath +"'.");
        } else JOptionPane.showMessageDialog(null, "Проблемы при создании отчета об ошибке.\n" +
                "Пожалуйста, заархивируйте содержимое папки '" + Configuration.logsPath + "' и отправьте архив разработчику.");
    }

    // "2017-12-28 07:05:35.016424"
    static String getDatetimeNowInPythonFormat(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS000");
        Date date = new Date();
        return dateFormat.format(date);
    }

}
