package com.hourse.web.service.impl;

import com.hourse.web.mapper.ImageInfoMapper;
import com.hourse.web.model.ImageInfo;
import com.hourse.web.service.IImageInfoService;
import com.hourse.web.util.ImageBase64Util;
import com.hourse.web.util.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.File;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by wufeng on 2017/4/11.
 */
@Service
@Transactional
public class ImageInfoServiceImpl implements IImageInfoService {

    private static Logger logger = LoggerFactory.getLogger(ImageInfoServiceImpl.class);

    @Resource
    private ImageInfoMapper imageInfoMapper;

    public List<ImageInfo> getImageInfo(ImageInfo imageInfo) {
        return imageInfoMapper.getImageInfo(imageInfo);
    }


    public String insertImageInfo(String imageBases, ImageInfo imageInfo) {
        String path = "";
        try {
            String[] imageBase = imageBases.split(",");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            for(int i=0; i<imageBase.length; i++){
                Date date= new Date();
                String firstPath = df.format(new Date());
                long time = date.getTime();
                File tmpDir = new File(PropertiesUtils.get("homedir.uploader.cdn") + File.separator + firstPath);
                if (!tmpDir.exists())
                    tmpDir.mkdirs();
                path = PropertiesUtils.get("homedir.uploader.cdn") + File.separator + firstPath +File.separator+ time + ".jpg";
                ImageBase64Util.GenerateImage(URLDecoder.decode(imageBase[i], "UTF-8"), path);
                imageInfo.setImagePath(path);
                imageInfo.setImageType("0");
                imageInfo.setImageUrl(PropertiesUtils.get("server.staticfile.cdn")+ File.separator + firstPath +File.separator+ time + ".jpg");
                imageInfoMapper.insert(imageInfo);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("添加图片信息失败",e);
        }
        return path;
    }

    public int delete(String hourseIds) {
        return  imageInfoMapper.delete(hourseIds);
    }
}
