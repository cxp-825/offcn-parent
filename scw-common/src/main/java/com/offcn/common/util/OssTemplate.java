package com.offcn.common.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OssTemplate {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    //String => url
    public String upload(InputStream inputStream, String fileName) {
        //文件的路径  根据时间日期进行区分
        //一天一个目录
        //格式：pic/1027/fileName
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String folderName = format.format(new Date());
        //将fileName进行非重复操作
        fileName = UUID.randomUUID().toString().replace("-","") + "_" + fileName;
        //上传
        OSS ossClient = new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);
        ossClient.putObject(bucketName,"pic/" + folderName + "/" + fileName,inputStream);
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ossClient.shutdown();
        //获取最后访问资源的路径

        return "http://" + bucketName + "." + endpoint + "/pic/" + folderName + "/" + fileName;
    }
}
