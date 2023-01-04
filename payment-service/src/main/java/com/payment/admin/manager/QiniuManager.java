package com.payment.admin.manager;

import com.google.gson.Gson;
import com.payment.admin.utils.FileUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * Created by M. on 2021/3/30.
 */
@Component
public class QiniuManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(QiniuManager.class);

    private UploadManager uploadManager;
    private Auth auth;

    public QiniuManager() {
        // 指向华南数据中心
        Configuration cfg = new Configuration(Zone.huanan());
        //...其他参数参考类注释
        uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        auth = Auth.create(accessKey, secretKey);
    }

    /**
     * 构建成可访问的文件地址
     *
     * @param fileName
     * @return
     */
    public String mergeUrl(String fileName) {
        return QINIU_URL_PREFIX + fileName;
    }

    /**
     * 构建成可访问的文件地址，并加上瘦身的参数
     *
     * @param fileName
     * @return
     */
    public String mergeUrlAndSlim(String fileName) {
        return QINIU_URL_PREFIX + fileName + "?imageslim";
    }

    /**
     * 下载网络上的文件，并上传到七牛
     *
     * @param url
     * @return
     * @throws Exception
     */
    public String uploadByUrl(String url) throws Exception {
        return uploadByUrl(url, null);
    }

    /**
     * 下载网络上的文件，并上传到七牛
     *
     * @param url      网络URL
     * @param fileName 指定保存的文件名
     * @return
     * @throws Exception
     */
    public String uploadByUrl(String url, String fileName) throws Exception {
        InputStream is = FileUtils.downloadFileInputStream(url);
        return uploadByStream(is, fileName);
    }

    /**
     * 上传到七牛对象存储，以文件内容的hash值作为文件名
     *
     * @param is
     * @return 返回上传后的文件名
     * @throws Exception
     */
    public String uploadByStream(InputStream is) throws Exception {
        return uploadByStream(is, null);
    }

    /**
     * 上传到七牛对象存储，指定上传后的文件名
     *
     * @param is       数据流
     * @param fileName 上传后的文件名，默认不指定的情况下，以文件内容的hash值作为文件名
     * @return 返回上传后的文件名
     * @throws Exception
     */
    public String uploadByStream(InputStream is, String fileName) throws Exception {
        try {
            // 获取上传的凭证
            String upToken = auth.uploadToken(bucket);
            Response response = uploadManager.put(is, fileName, upToken, null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return putRet.key;
        } catch (QiniuException ex) {
            LOGGER.error("uploadByStream error, response is: " + ex.response, ex);
        }
        return null;
    }

    /**
     * 获取本地的文件，上传到七牛
     *
     * @param localPath
     * @return
     */
    public String uploadByLocal(String localPath) {
        return uploadByLocal(localPath, null);
    }

    /**
     * 获取本地的文件，上传到七牛
     *
     * @param localPath 本地文件名
     * @param fileName  保存到七牛的文件名
     * @return 返回上传后的文件名
     */
    public String uploadByLocal(String localPath, String fileName) {
        try {
            // 获取上传的凭证
            String upToken = auth.uploadToken(bucket);
            Response response = uploadManager.put(localPath, fileName, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return putRet.key;
        } catch (QiniuException ex) {
            LOGGER.error("uploadByStream error, response is: " + ex.response, ex);
        }

        return null;
    }
}
