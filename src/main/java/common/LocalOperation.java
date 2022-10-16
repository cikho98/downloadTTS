package common;

import java.io.*;

public class LocalOperation {
    public static final int cache = 10 * 1024;
    private String filePath;

    public LocalOperation(String filePath) {
        this.filePath = filePath;
    }

    public void createDir() {
        File dir = new File(filePath);
        if (dir.exists()) {
            dir.mkdirs();
        }
    }

    public void createFile() {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeData(File file, InputStream is) {
        if (file.exists()) {
            try(FileOutputStream fos = new FileOutputStream(file);) {
                byte[] data = new byte[cache];
                int length;
                while ((length = is.read(data)) != -1) {
                    fos.write(data, 0, length);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
