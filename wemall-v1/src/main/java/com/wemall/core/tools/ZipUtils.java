package com.wemall.core.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 压缩工具类
 *
 * @author wangjinbin@163.com
 */
public class ZipUtils {
    final static Logger log = LoggerFactory.getLogger(ZipUtils.class);

    /**
     * 压缩文件
     *
     * @param srcfileName
     *            原文件列表
     * @param zipfileName
     *            压缩后的文件名
     */
    public static void zipFiles(List<String> srcfileName, String zipfileName){
        byte[] buf = new byte[1024];
        try {
            File zip_file = new File(zipfileName);
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip_file));
            Iterator itr = srcfileName.iterator();
            while (itr.hasNext()){
                String src_file_name = (String) itr.next();
                File src_file = new File(src_file_name);
                FileInputStream in = new FileInputStream(src_file);
                out.putNextEntry(new ZipEntry(src_file.getName()));
                int len;
                while ((len = in.read(buf)) > 0){
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
            log.debug("压缩成功！");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 解压文件
     *
     * @param zipfileName
     *            压缩文件名
     * @param descDir
     *            目标路径
     */
    public static void unZipFiles(String zipfileName, String descDir){
        try {
            File zipfile = new File(zipfileName);
            ZipFile zf = new ZipFile(zipfile);
            for (Enumeration entries = zf.entries(); entries.hasMoreElements();){
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = zf.getInputStream(entry);
                OutputStream out = new FileOutputStream(descDir + zipEntryName);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0){
                    out.write(buf1, 0, len);
                }
                in.close();
                out.close();
                log.debug("压缩文件" + zipEntryName + "解压成功！");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}