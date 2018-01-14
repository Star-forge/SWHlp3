package sw_client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Dimension;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Created by Starforge on 04.01.2018.
 */
public class Updater extends JFrame{
    private static final Logger log = LogManager.getLogger("com.adbmanager.log4j2");
    private static final String PATH_JARFILE = System.getProperty("user.dir") + "\\";
    private static JProgressBar sProgress;

    public Updater(String url){
        this.setPreferredSize(new Dimension(300, 80));
        this.setSize(new Dimension(300, 80));
        this.setTitle("Обновление SW Helper");
        JProgressBar progress = new JProgressBar(0, 100);
        progress.setValue(0);
        progress.setStringPainted(true);
        this.add(progress);
        sProgress = progress;
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.requestFocus(true);
    }

    private static String getJarPath(){
        String jarPath = Updater.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath().substring(1);
        log.debug("Путь к jar: " + jarPath);
        return jarPath;
    }

    private static void downloadLatestVersion(String newVersionFileName){
        URL url;
        try {
            url = new URL(NetworkAdapter.updUrl);
            log.debug("try to open connection by url: " + url);
            HttpURLConnection hConnection = (HttpURLConnection) url
                    .openConnection();
            HttpURLConnection.setFollowRedirects(true);
            log.debug("connection resp code: " + hConnection.getResponseCode());
            if (HttpURLConnection.HTTP_OK == hConnection.getResponseCode()) {
                InputStream in = hConnection.getInputStream();
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(newVersionFileName));
                int filesize = hConnection.getContentLength();
                sProgress.setMaximum(filesize);
                byte[] buffer = new byte[4096];
                int numRead;
                long numWritten = 0;
                while ((numRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, numRead);
                    numWritten += numRead;
                    System.out.println((double)numWritten/(double)filesize);
                    sProgress.setValue((int) numWritten);
                }
                if(filesize!=numWritten)
                    log.error("Wrote "+numWritten+" bytes, should have been "+filesize);
                else
                    log.debug("Downloaded successfully.");
                out.close();
                in.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    static void copyFile(String _source, String _target) {
        Path source = Paths.get(_source);
        Path target = Paths.get(_target);
        log.debug("copy file s:%s, t:%s", source, target);
        CopyOption[] options = new CopyOption[] { REPLACE_EXISTING };
        try {
            Files.copy(source, target, options);
        } catch (IOException x) {
            log.error("Unable to copy: %s: %s%n", source, x);
        }
    }

    static void deleteFile(String _target) {
        Path target = Paths.get(PATH_JARFILE+_target);
        log.debug("delete file t:%s", target);
        try {
            Files.delete(target);
        } catch (IOException x) {
            log.error("Unable to delete: %s: %s%n", target, x);
        }
    }

    static void update(){
        String source =  getJarPath();
        String target =  source+"."+Configuration.VERSION+".bak";
        copyFile(source, target);
        downloadLatestVersion(source);
        try {
            Runtime.getRuntime().exec("java -jar "+source);
        } catch (IOException e) {
           log.error("Ошибка запуска обновленной программы %s%n", e);
        }
    }
}
