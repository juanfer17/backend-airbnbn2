package com.backendparkingflypass.general.utils;

import java.io.*;

public class UtilsFiles {
    private static UtilsFiles utilsFiles;

    public static UtilsFiles getInstance() {
        if(utilsFiles == null) {
            utilsFiles = new UtilsFiles();
        }
        return utilsFiles;
    }
    public String copyFile(String fileName, String folderName) throws IOException {
        File directory = new File(folderName);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        directory.setWritable(true);
        // copia la direccion
        String filePath = directory.getCanonicalPath() + "/" + fileName;
        // nuevo archivo en esa dirección
        File temp = new File(filePath);
        InputStream is = this.getClass().getResourceAsStream("/" + fileName);
        FileOutputStream targetFile = new FileOutputStream(temp);
        FileWriter fw = new FileWriter(temp);
        byte[] buffer = new byte[512 * 1024];
        int nbReader;
        while ((nbReader = is.read(buffer)) != -1)
            targetFile.write(buffer, 0, nbReader);
        fw.close();
        targetFile.close();
        is.close();
        return temp.getAbsolutePath();
    }
}
