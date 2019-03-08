package com.taotao.service.impl;

import com.taotao.common.pojo.PictureResult;
import com.taotao.common.utils.FastDFSClient;
import com.taotao.service.PictureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PictureServiceImpl implements PictureService {

    @Value("${IMAGE_SERVER_BASE_URL}")
    private String IMAGE_SERVER_BASE_URL;


    /**
     * 上传图片
     * @param picFile
     * @return
     */
    public PictureResult uploadPic(MultipartFile picFile) {

        System.out.println(IMAGE_SERVER_BASE_URL);

        PictureResult result = new PictureResult();

        //判断图片是否为空
        if (picFile.isEmpty()){
            result.setError(1);
            result.setMessage("图片为空");
            return result;
        }

        //上传图片服务器
        try {
            String originalFileName = picFile.getOriginalFilename();
            //获取图片扩展名,不含(.)
            String extName = originalFileName.substring(originalFileName.lastIndexOf(".")+1);

            FastDFSClient client = new FastDFSClient("classpath:client.conf");
            String url = client.uploadFile(picFile.getBytes(),extName);

            //拼接图片服务器的ip地址
            url = IMAGE_SERVER_BASE_URL + url;

            System.out.println(url);
            //把url响应给客户端
            result.setError(0);
            result.setUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
            result.setError(1);
            result.setMessage("图片上传失败！");
        }
        return result;
    }
}
