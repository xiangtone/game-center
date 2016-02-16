package com.reportforms.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import com.aliyun.openservices.ClientConfiguration;
import com.aliyun.openservices.ClientException;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.OSSException;
import com.aliyun.openservices.oss.model.CannedAccessControlList;
import com.aliyun.openservices.oss.model.GetObjectRequest;
import com.aliyun.openservices.oss.model.OSSObjectSummary;
import com.aliyun.openservices.oss.model.ObjectListing;
import com.aliyun.openservices.oss.model.ObjectMetadata;

/**
 * 该示例代码展示了如果在OSS中创建和删除一个Bucket，以及如何上传和下载一个文件。
 * 
 * 该示例代码的执行过程是：
 * 1. 检查指定的Bucket是否存在，如果不存在则创建它；
 * 2. 上传一个文件到OSS；
 * 3. 下载这个文件到本地；
 * 4. 清理测试资源：删除Bucket及其中的所有Objects。
 * 
 * 尝试运行这段示例代码时需要注意：
 * 1. 为了展示在删除Bucket时除了需要删除其中的Objects,
 *    示例代码最后为删除掉指定的Bucket，因为不要使用您的已经有资源的Bucket进行测试！
 * 2. 请使用您的API授权密钥填充ACCESS_ID和ACCESS_KEY常量；
 * 3. 需要准确上传用的测试文件，并修改常量uploadFilePath为测试文件的路径；
 *    修改常量downloadFilePath为下载文件的路径。
 * 4. 该程序仅为示例代码，仅供参考，并不能保证足够健壮。
 *
 */
public class OSSObjectSample {

    private static final String ACCESS_ID = "1fpumj6d3egnzg1gj5y4lq8i";
    private static final String ACCESS_KEY = "qi3586g7KqZt2BcKkiTXviE0At4=";
    private static final String OSS_ENDPOINT = "http://oss.aliyuncs.com";

    /**
     * @param args
     */
    public static void main(String[] args)throws Exception {
        String bucketName = "vaac";
        String key = "app/copy.txt";

        String uploadFilePath = "d:/temp/upload/copy.txt";
       // String downloadFilePath = "d:/temp/photo1.jpg";

        // 可以使用ClientConfiguration对象设置代理服务器、最大重试次数等参数。
        ClientConfiguration config = new ClientConfiguration();
     
        OSSClient client = new OSSClient(OSS_ENDPOINT, ACCESS_ID, ACCESS_KEY, config);
        
        OSSClient clients = new OSSClient(OSS_ENDPOINT, ACCESS_ID, ACCESS_KEY);
        ensureBucket(client, bucketName);

        try {
            setBucketPublicReadable(client, bucketName);

            System.out.println("正在上传...");
            uploadFile(client, bucketName, key, uploadFilePath);

//            System.out.println("正在下载...");
//            downloadFile(client, bucketName, key, downloadFilePath);
        } finally {
//            deleteBucket(client, bucketName);
        }
    }

    // 如果Bucket不存在，则创建它。
    private static void ensureBucket(OSSClient client, String bucketName)
            throws OSSException, ClientException{

        if (client.isBucketExist(bucketName)){
            return;
        }

        //创建bucket
        client.createBucket(bucketName);
    }

    // 删除一个Bucket和其中的Objects 
    private static void deleteBucket(OSSClient client, String bucketName)
            throws OSSException, ClientException {

        ObjectListing ObjectListing = client.listObjects(bucketName);
        List<OSSObjectSummary> listDeletes = ObjectListing
                .getObjectSummaries();
        for (int i = 0; i < listDeletes.size(); i++) {
            String objectName = listDeletes.get(i).getKey();
            // 如果不为空，先删除bucket下的文件
            client.deleteObject(bucketName, objectName);
        }
        client.deleteBucket(bucketName);
    }

    // 把Bucket设置为所有人可读
    private static void setBucketPublicReadable(OSSClient client, String bucketName)
            throws OSSException, ClientException {
        //创建bucket
        client.createBucket(bucketName);

        //设置bucket的访问权限，public-read-write权限
        client.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
    }

    // 上传文件
    private static void uploadFile(OSSClient client, String bucketName, String key, String filename)
            throws OSSException, ClientException, FileNotFoundException {
        File file = new File(filename);

        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(file.length());
        // 可以在metadata中标记文件类型
        objectMeta.setContentType("image/jpeg");

        InputStream input = new FileInputStream(file);
        client.putObject(bucketName, key, input, objectMeta);
        
    }

    // 下载文件
    private static void downloadFile(OSSClient client, String bucketName, String key, String filename)
            throws OSSException, ClientException {
        client.getObject(new GetObjectRequest(bucketName, key),
                new File(filename));
    }
}
