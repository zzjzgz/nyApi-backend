package xyz.zzj.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import xyz.zzj.common.model.entity.NyApiUserInterface;


/**
* @author zengz
* @description 针对表【ny_api_user_interface(用户接口关系表)】的数据库操作Service
* @createDate 2024-03-24 16:12:44
*/
public interface NyApiUserInterfaceService extends IService<NyApiUserInterface> {

    void validNyApiUserInterface(NyApiUserInterface nyApiUserInterface, boolean add);




    boolean invokeCount(long interfaceInfoId,long userId);


}
