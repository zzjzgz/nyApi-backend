package xyz.zzj.project.service;

import xyz.zzj.project.model.entity.NyApiUserInterface;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author zengz
* @description 针对表【ny_api_user_interface(用户接口关系表)】的数据库操作Service
* @createDate 2024-03-24 16:12:44
*/
public interface NyApiUserInterfaceService extends IService<NyApiUserInterface> {

    void validNyApiUserInterface(NyApiUserInterface nyApiUserInterface, boolean add);
}
