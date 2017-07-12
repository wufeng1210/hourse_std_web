package com.hourse.web.mapper;

import com.hourse.web.model.ImageInfo;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wufeng on 2017/4/11.
 */
@Repository
public interface ImageInfoMapper {
    @Select("select * from image_info where hourseId = #{hourseId}")
    List<ImageInfo> getImageInfo(ImageInfo imageInfo);

    /**
     * 增加房屋图片信息，返回房屋图片id
     * @param imageInfo
     * @return
     */
    @InsertProvider(method = "insert", type = ImageProvider.class)
    int insert(ImageInfo imageInfo);

    @DeleteProvider(method= "delete", type= ImageProvider.class)
    public int delete(String hourseIds);
}
