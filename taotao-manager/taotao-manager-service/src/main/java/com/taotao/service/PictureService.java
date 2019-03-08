package com.taotao.service;

import com.taotao.common.pojo.PictureResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片上传service
 */
public interface PictureService {

    PictureResult uploadPic(MultipartFile picFile) throws Exception;
}
