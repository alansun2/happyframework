package com.alan344happyframework.util;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 文件处理工具类
 */
@Slf4j
public class FileUtils {

    private static final Charset CHARSET = Charset.forName("GB18030");

    private static final CSVParser CSVPARSER = new CSVParser();

    /**
     * zip 解压
     *
     * @param zipFilePath zip文件路径
     * @param destDir     解压路径
     * @return fileNames
     */
    public static List<String> unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if (!dir.exists()) {
            boolean mkdirs = dir.mkdirs();
            if (!mkdirs) {
                throw new RuntimeException("创建文件夹失败");
            }
        }
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        List<String> fileNames = new ArrayList<>();
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis, CHARSET);
            ZipEntry ze = zis.getNextEntry();
            String fileName;
            File newFile;
            while (ze != null) {
                fileName = ze.getName();
                newFile = new File(destDir + File.separator + fileName);
                //create directories for sub directories in zip
                boolean mkdirs = new File(newFile.getParent()).mkdirs();
                if (!mkdirs) {
                    throw new RuntimeException("创建文件夹失败");
                }
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
                fileNames.add(fileName);
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileNames;
    }

    /**
     * Gzip解压
     */
    public static boolean unGzip(String srcPath, String desPath) {

        File file = new File(srcPath);
        if (!file.exists()) {
            log.info(srcPath + "文件不存在");
            return false;
        }
        InputStream in;
        try {
            in = new GZIPInputStream(new FileInputStream(srcPath));

            org.apache.commons.io.FileUtils.copyInputStreamToFile(in, new File(desPath));
        } catch (FileNotFoundException e) {
            log.error("File not found. " + srcPath, e);
            return false;
        } catch (IOException e) {
            log.error("unGzip fail", e);
            return false;
        }
        return true;
    }

    /**
     * 文件拷贝
     *
     * @param in
     * @param desPath
     */
    public static void streamHandler(InputStream in, String desPath) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {

            File file = new File(desPath);
            if (file.exists()) {
                return;
            }
            File file1 = new File(desPath.substring(0, desPath.lastIndexOf("/")));
            if (!file1.exists()) {
                boolean mkdirs = file1.mkdirs();
            }
            bis = new BufferedInputStream(in);
            OutputStream out = new FileOutputStream(file, true);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[1024];
            int length = 0;
            while ((length = bis.read(buff)) != -1) {
                bos.write(buff, 0, length);
            }
        } catch (IOException e) {
            log.error("从inputStream获取数据失败", e);
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                log.error("从inputStream获取数据失败", e);
            }
        }
    }

    /**
     * 获取文件流
     *
     * @param path 文件路径
     * @return content
     */
    public static String readFile(String path) {
        StringBuilder key = new StringBuilder();
        File file = new File(path);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String tempString;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                key.append(tempString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return key.toString();
    }

    /**
     * 获取csvreader
     *
     * @param desPath
     * @param charSet
     * @param skipLine
     * @param cSVParser
     * @return
     */
    public static CSVReader getCSVReader(String desPath, String charSet, int skipLine, CSVParser cSVParser) {
        try {
            CSVReaderBuilder csvReaderBuilder = new CSVReaderBuilder(new InputStreamReader(new FileInputStream(new File(desPath)), charSet)).withCSVParser(cSVParser).withSkipLines(skipLine);
            return csvReaderBuilder.build();
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            log.error("get csvreader fail", e);
        }
        return null;
    }

    /**
     * 获取csvreader 默认，
     *
     * @param desPath
     * @param charSet
     * @param skipLine
     * @return
     */
    public static CSVReader getCSVReader(String desPath, String charSet, int skipLine) {
        return getCSVReader(desPath, charSet, skipLine, CSVPARSER);
    }

    /**
     * openOutputStream
     *
     * @param file   file
     * @param append append
     * @return FileOutputStream
     * @throws IOException e
     */
    public static FileOutputStream openOutputStream(final File file, final boolean append) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (!file.canWrite()) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            final File parent = file.getParentFile();
            if (parent != null) {
                if (!parent.mkdirs() && !parent.isDirectory()) {
                    throw new IOException("Directory '" + parent + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file, append);
    }

    /**
     * 获取图片的后缀
     *
     * @param imageInputStream imageInputStream
     * @return 图片后缀
     * @throws IOException e
     */
    public static Map<Integer, Object> getBufferedImageFormat(ImageInputStream imageInputStream) throws IOException {
        Map<Integer, Object> map = new HashMap<>();
        ImageIO.setUseCache(false);
        Iterator<ImageReader> it = ImageIO.getImageReaders(imageInputStream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = 0;
        String suffix = null;
        while (it.hasNext()) {

            ImageReader imageReader = it.next();
            // 设置解码器的输入流
            imageReader.setInput(imageInputStream, true, true);

            // 图像文件格式后缀
            suffix = imageReader.getFormatName().trim().toLowerCase();
            //校验宽和高是否符合要求
            if (!"bmp、jpg、jpeg、png、gif".contains(suffix)) {
                throw new RuntimeException("不支持的图片格式");
            }
            // 解码成功返回BufferedImage对象
            // 0即为对第0张图像解码(gif格式会有多张图像),前面获取宽度高度的方法中的参数0也是同样的意思
            BufferedImage read = imageReader.read(i, imageReader.getDefaultReadParam());
            ImageIO.write(read, suffix, baos);
            i++;
        }

        map.put(1, baos);
        if (i == 1) {
            map.put(2, suffix);
            return map;
        } else if (i > 1) {
            map.put(2, "gif");
            return map;
        }
        return null;
    }

    /**
     * 文本压缩
     *
     * @param str 待压缩文本
     */
    public static String compress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        return out.toString(StandardCharsets.ISO_8859_1.toString());
    }

    /**
     * 文本解压缩
     */
    public static String uncompress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(str
                .getBytes(StandardCharsets.ISO_8859_1.toString()));
        GZIPInputStream gunZip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = gunZip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        // toString()使用平台默认编码，也可以显式的指定如toString(&quot;GBK&quot;)
        return out.toString();
    }

    public static void main(String[] wer) throws IOException {
        String str = "eJxlz0FPgzAUwPE7n4JwnZECax0mO8DinJOpC8wZL6RCKdWslLbOMeN3F3GJTTz-f**9vE-Ltm0nS9JzXBTNO9e57gRx7EvbAc7ZXxSClTnWeSDLf5EcBJMkx5UmcogehNAHwDSsJFyzip3EscacKswNocq3fDjzu2LczyPvAvomYXSIq6v17GZBKGq9ZZrMXBmK5aGdq01Ru8-3k614yl5o9nrsVgG8rTajiMVr3UVb8BE-4qrJaE3c9Pouprv9Igj0XEGePITtKBEyRNF0apzUbEdOP3kITQIAxkbdE6lYwwfgg574Pfh53PqyvgGYjV4o";
        int s = str.length();
        String compress = compress(str);
        String uncompress = uncompress(compress);
        System.out.println(str.equals(uncompress));
    }

    private static final List<String> FORMATS = Arrays.asList("jpg", "png", "bmp", "jpeg");

    /**
     * 检查文件格式
     *
     * @param fileName 文件名
     */
    public static String checkFormat(String fileName, List<String> formats) {
        String[] split = fileName.split("\\.");
        if (split.length == 1) {
            throw new RuntimeException("文件必须带格式");
        }
        String format = split[split.length - 1];
        boolean flag = false;
        for (String s : formats) {
            if (format.equalsIgnoreCase(s)) {
                flag = true;
                break;
            }
        }

        if (!flag) {
            throw new RuntimeException("不支持的文件格式");
        }
        return format;
    }
}
