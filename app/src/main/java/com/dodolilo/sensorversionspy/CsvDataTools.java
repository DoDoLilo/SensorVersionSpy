package com.dodolilo.sensorversionspy;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class CsvDataTools {
    public enum FileSaveType {
        CSV(".csv"), TXT(".txt");

        private final String fileType;

        FileSaveType(String fileType) {
            this.fileType = fileType;
        }

        public String getFileType() {
            return fileType;
        }
    }

    /**
     * 将多个传感器数据数组转为可供csv文件存储的字符串格式.
     * 注意每个数值之间都需要插入','进行间隔，结尾要用'\n'结束.
     *
     * @param accValues
     * @param gyroValues
     * @param magValues
     * @param quatValues
     * @return
     */
    public static String convertSensorValuesToCsvFormat(
            float[] accValues,
            float[] gyroValues,
            float[] magValues,
            float[] quatValues
    ) {
        StringBuilder csvLine = new StringBuilder();

        csvLine.append(System.currentTimeMillis());
        for (float data : accValues) {
            csvLine.append(',');
            csvLine.append(data);
        }
        for (float data : gyroValues) {
            csvLine.append(',');
            csvLine.append(data);
        }
        for (float data : magValues) {
            csvLine.append(',');
            csvLine.append(data);
        }
        for (float data : quatValues) {
            csvLine.append(',');
            csvLine.append(data);
        }
        csvLine.append('\n');

        return csvLine.toString();
    }

    /**
     *
     * @param point
     * @return
     */
    public static String convertPointToCsvFormat(float[] point) {
        StringBuilder csvLine = new StringBuilder();
        csvLine.append(System.currentTimeMillis());
        csvLine.append(',');
        csvLine.append(point[0]);
        csvLine.append(',');
        csvLine.append(point[1]);
        csvLine.append('\n');
        return csvLine.toString();
    }


    /**
     * 向Android<b>应用外部存储空间的cache文件夹</b>中保存<b>csv文件</b>.
     * 涉及UI Handler，若不是在主线程中使用，需要主动创建Looper.
     * @param fileName 文件名，应该以".csv"结尾，不需要外部给外存路径
     * @param dataStr  写入csv文件的数据
     * @param context     上下文
     * @return true 如果保存文件成功
     */
    public static boolean saveCsvToExternalStorage(String fileName, FileSaveType fileType, String dataStr, Context context) {
        //检查文件名是否以“.csv"结尾
        if (!fileName.endsWith(fileType.getFileType())) {
            fileName = fileName.concat(fileType.getFileType());
        }

        //检查外存是否可写
        if (!isExternalStorageWritable()) {
            MessageBuilder.showMessageWithOK(context, "Write Error", "External Storage Not Writable!");
            return false;
        }

        //写文件，选择外部存储cache文件夹
        File externalCacheFile = new File(context.getExternalCacheDir(), fileName);
        try (FileOutputStream fos = new FileOutputStream(externalCacheFile)) {
            fos.write(dataStr.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            MessageBuilder.showMessageWithOK(context, "Write Error", e.getMessage());
            return false;
        }

        MessageBuilder.showMessageWithOK(context, "Save Succeed to", externalCacheFile.getName());
        return true;
    }

    /**
     * 从Android<b>应用外部存储空间的files文件夹</b>中读取<b>csv文件</b>.
     * NOTE:读取一个文件时，不仅要检查存储空间是否可读，还需要检查该文件是否存在.
     * 涉及UI Handler，若不是在主线程中使用，需要主动创建Looper.
     * @param csvFileName 文件名，应该以".csv"结尾，不需要外部给外存路径
     * @param context     上下文
     * @return null 如果外部存储空间不可读or读失败；否则返回读取到的StringBuilder内容
     */
    public static String readCsvFromExternalStorage(String csvFileName, Context context) {
        //检查文件名是否以“.csv"结尾
        if (!csvFileName.endsWith(".csv")) {
            csvFileName = csvFileName.concat(".csv");
        }

        //检查外存是否可读
        if (!isExternalStorageReadable()) {
            MessageBuilder.showMessageWithOK(context, "Read Error", "External Storage Not Readable!");
            return null;
        }

        File externalFilesFile = new File(context.getExternalFilesDir(null), csvFileName);
        //检查文件是否存在，且文件大小 > 0 byte
        if (externalFilesFile.exists() && externalFilesFile.length() > 0L) {
            StringBuilder readDataStrBuilder = new StringBuilder();
            try (InputStream is = new FileInputStream(externalFilesFile)) {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line = br.readLine();
                while (line != null) {
                    readDataStrBuilder.append(line).append('\n');
                    line = br.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
                MessageBuilder.showMessageWithOK(context, "Read Error", e.getMessage());
                return null;
            }
            return readDataStrBuilder.toString();
        }

        //文件不存在或为空文件
        MessageBuilder.showMessageWithOK(context, "Read Error", externalFilesFile.getAbsolutePath().concat("不存在or为空文件"));
        return null;
    }

    /**
     * 检查手机<b>应用外部存储空间</b>是否可写.
     *
     * @return true 外部存储空间可写
     */
    private static boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 检查手机<b>应用外部存储空间</b>是否可读.
     *
     * @return true 外部存储空间可读
     */
    private static boolean isExternalStorageReadable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ||
                Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
    }


    /**
     * 向Android手机<b>应用内部存储空间</b>中写csv文件.
     *
     * @param csvFileName 文件名，应该以".csv"结尾，不需要外部给内存路径
     * @param csvData     写入csv文件的数据
     * @param context     上下文
     * @return true 如果存储成功
     */
    public static boolean saveCsvToInternalStorage(String csvFileName, StringBuilder csvData, Context context) {


        return true;
    }

    /**
     * 从Android手机<b>应用内部存储空间</b>中读出指定文件名的csv文件.
     *
     * @param csvFileName 文件名，应该以".csv"结尾，不需要外部给内存路径
     * @param context     上下文
     * @return 读取到的文件内容StringBuilder，方便外部再做操作
     */
    public static StringBuilder readCsvFromInternalStorage(String csvFileName, Context context) {
        // TODO: 2022/7/12 在安装时需要以信息流的形式访问文件，请将文件保存在项目的 /res/raw 目录中，
        //  该函数不适合在应用安装后手动往应用空间中增加文件！所以不适合读取需要人工设定的“打点文件.csv”!

        return null;
    }

    /**
     * 将读出来的打点文件转为Map<坐标名，坐标>
     *
     * @param pointsStr 应该是通过{@link CsvDataTools#readCsvFromExternalStorage(String, Context)}
     *                  从【打点文件】中读出的字符串"name:x,y\n" * N，此时每行由'\n'作为分隔符
     * @return Map<坐标名String ， 坐标float [ ]>
     */
    public static Map<String, float[]> changePointsCsvToMap(String pointsStr) {
        if (pointsStr == null) {
            return new HashMap<>();
        }
        //NOTE: 无法确定Map的初始大小
        Map<String, float[]> pointMap = new HashMap<>();
        String[] lines = pointsStr.split("\n");

        //从每一行中读出坐标名与坐标
        for (String line : lines) {
            if (line.length() == 0) {
                continue;
            }
            //line = "name:x,y"
            String[] nameAndPoint = line.split(":");
            String[] xyStrArr = nameAndPoint[1].split(",");
            if (xyStrArr.length == 2) {
                pointMap.put(nameAndPoint[0], new float[]{Float.parseFloat(xyStrArr[0]), Float.parseFloat(xyStrArr[1])});
            }
        }

        return pointMap;
    }
}
