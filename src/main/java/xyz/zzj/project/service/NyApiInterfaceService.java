package xyz.zzj.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.zzj.common.model.entity.NyApiInterface;

/**
* @author zeng
* @description 针对表【ny_api_interface(接口信息)】的数据库操作Service
* @createDate 2024-02-22 21:30:06
*/
public interface NyApiInterfaceService extends IService<NyApiInterface> {

    /**
     * 校验
     *
     * @param nyApiInterface
     * @param add 是否为创建校验
     */
    void validNyApiInterface(NyApiInterface nyApiInterface, boolean add);
}
