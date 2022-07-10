package com.lxw.service;

import com.lxw.entity.UploadFileList;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2022-07-01
 */
public interface IUploadFileListService extends IService<UploadFileList> {
    /**
     * 文件上传后获取文件路径
     * @param file
     * @return
     */
    String getUploadFileUrl(MultipartFile file);
}
