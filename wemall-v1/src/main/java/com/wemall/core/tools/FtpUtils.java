package com.wemall.core.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FTP工具类
 *
 * @author wangjinbin@163.com
 */
public class FtpUtils {
    final static Logger log = LoggerFactory.getLogger(FtpUtils.class);

    /**
     * Description: 向FTP服务器上传文件
     *
     * @Version1.0
     * @param url
     *            FTP服务器hostname
     * @param port
     *            FTP服务器端口
     * @param username
     *            FTP登录账号
     * @param password
     *            FTP登录密码
     * @param path
     *            FTP服务器保存目录
     * @param filename
     *            上传到FTP服务器上的文件名
     * @param input
     *            输入流
     * @return 成功返回true，否则返回false
     */
    public static boolean uploadFile(String url,// FTP服务器hostname
                                     int port,// FTP服务器端口
                                     String username, // FTP登录账号
                                     String password, // FTP登录密码
                                     String path, // FTP服务器保存目录
                                     String filename, // 上传到FTP服务器上的文件名
                                     InputStream input // 输入流
                                   ){
        boolean success = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(url, port);// 连接FTP服务器
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftp.login(username, password);// 登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)){
                ftp.disconnect();
                return success;
            }
            ftp.changeWorkingDirectory(path);
            ftp.storeFile(filename, input);

            input.close();
            ftp.logout();
            success = true;
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()){
                try {
                    ftp.disconnect();
                } catch (IOException ioe){
                }
            }
        }

        return success;
    }

    /**
     * Description: 从FTP服务器下载文件
     *
     * @Version1.0
     * @param url
     *            FTP服务器hostname
     * @param port
     *            FTP服务器端口
     * @param username
     *            FTP登录账号
     * @param password
     *            FTP登录密码
     * @param remotePath
     *            FTP服务器上的相对路径
     * @param fileName
     *            要下载的文件名
     * @param localPath
     *            下载后保存到本地的路径
     * @return
     */
    public static List<String> downFile(String url, // FTP服务器hostname
                                        int port,// FTP服务器端口
                                        String username, // FTP登录账号
                                        String password, // FTP登录密码
                                        String remotePath,// FTP服务器上的相对路径
                                        String fileName,// 要下载的文件名
                                        String match_flag,// 文件名匹配模式 0：模糊匹配 1：精确匹配
                                        String localPath// 下载后保存到本地的路径
                                      ){
        List<String> lst_files = new ArrayList<String>();

        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(url, port);
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftp.login(username, password);// 登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)){
                ftp.disconnect();
                log.error("FTP服务器连接失败！");

                return lst_files;
            }
            ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
            FTPFile[] fs = ftp.listFiles();
            for (FTPFile ff : fs){
                if ("0".equals(match_flag) && ff.getName().indexOf(fileName) >= 0){ // 文件名模糊匹配
                    File localFile = new File(localPath + "/" + ff.getName());
                    OutputStream is = new FileOutputStream(localFile);
                    ftp.retrieveFile(ff.getName(), is);
                    is.close();

                    lst_files.add(ff.getName());
                }else if (ff.getName().equals(fileName)){ // 文件名精确匹配
                    File localFile = new File(localPath + "/" + ff.getName());
                    OutputStream is = new FileOutputStream(localFile);
                    ftp.retrieveFile(ff.getName(), is);
                    is.close();

                    lst_files.add(ff.getName());
                }
            }

            ftp.logout();
        } catch (IOException e){
            log.error("FTP服务器连接异常！" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()){
                try {
                    ftp.disconnect();
                } catch (IOException ioe){
                }
            }
        }

        return lst_files;
    }
}
