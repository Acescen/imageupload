package com.imageupload.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * @Author: jurui
 * @Email: 1067492158@qq.com
 * @Description
 * @Date: Created in 2020-12-26 20:57
 */
public class QiniuUpload {

    //设置好账号的ACCESS_KEY和SECRET_KEY
    private static String ACCESS_KEY = VariableName.accessKey;
    private static String SECRET_KEY = VariableName.secretKey;

    //要上传的空间
    private static String bucketname = VariableName.bucket;

    //密钥配置
    private static Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    private static Configuration cfg = new Configuration(Zone.autoZone());
    //创建上传对象
    private static UploadManager uploadManager = new UploadManager(cfg);

    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public static String getUpToken() {
        return auth.uploadToken(bucketname);
    }

    /**
     *   文件上传工具类
     * @param file MultipartFile文件
     * @return
     * @throws Exception
     */
    public static String updateFile(MultipartFile file) throws Exception {
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        try {
            InputStream inputStream = file.getInputStream();
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[600]; //buff用于存放循环读取的临时数据
            int rc = 0;
            while ((rc = inputStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }

            byte[] uploadBytes = swapStream.toByteArray();
            try {
                Response response = uploadManager.put(uploadBytes, null, getUpToken());
                //解析上传成功的结果
                DefaultPutRet putRet;
                putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                return VariableName.domain + putRet.key;

            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                }
            }
        } catch (UnsupportedEncodingException ex) {
        }
        return null;
    }
}

