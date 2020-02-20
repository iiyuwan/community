package com.juice.community.provider;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.BucketAuthorization;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileBucketLocalAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import cn.ucloud.ufile.http.OnProgressListener;
import com.juice.community.exception.CustomException;
import com.juice.community.exception.ECustomErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.swing.undo.CannotUndoException;
import java.io.File;
import java.io.InputStream;
import java.util.UUID;

@Service
public class UCloudProvider {
    @Value("${ucloud.ufile.public-key}")
    private String publicKey;
    @Value("${ucloud.ufile.private-key}")
    private String privateKey;
    @Value("${ucloud.ufile.bucket-name}")
    private String bucketName;
    @Value("${ucloud.ufile.region}")
    private String region;
    @Value("${ucloud.ufile.proxySuffix}")
    private String proxySuffix;
    @Value("${ucloud.ufile.expires-duration}")
    private Integer expiresDuration;
    public String upload(InputStream inputStream,String mimeType,String fileName){
        ObjectAuthorization objectAuthorization = new UfileObjectLocalAuthorization(publicKey, privateKey);
        ObjectConfig config = new ObjectConfig(region, proxySuffix);
        String generateFileName;
        String[] filePaths=fileName.split("\\.");
        if(filePaths.length>1){
            generateFileName= UUID.randomUUID().toString()+"."+filePaths[filePaths.length-1];
        }else {
            throw  new CustomException(ECustomErrorCode.FILE_UPLOAD_FAIL);
        }

        try {

            PutObjectResultBean response = UfileClient.object(objectAuthorization, config)
                    .putObject(inputStream, mimeType)
                    .nameAs(generateFileName)
                    .toBucket(bucketName)
                    /**
                     * 是否上传校验MD5, Default = true
                     */
                    //  .withVerifyMd5(false)
                    /**
                     * 指定progress callback的间隔, Default = 每秒回调
                     */
                    //  .withProgressConfig(ProgressConfig.callbackWithPercent(10))
                    /**
                     * 配置进度监听
                     */
                    .setOnProgressListener((bytesWritten, contentLength) -> {

                    })
                    .execute();

                    if(response!=null&&response.getRetCode()==0){
                        String url=UfileClient.object(objectAuthorization,config)
                                .getDownloadUrlFromPrivateBucket(generateFileName,bucketName,expiresDuration)
                                .createUrl();
                        return url;
                    }else {
                        throw  new CustomException(ECustomErrorCode.FILE_UPLOAD_FAIL);
                    }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
