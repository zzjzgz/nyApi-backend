package xyz.zzj.project.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import xyz.zzj.project.common.ErrorCode;
import xyz.zzj.project.exception.BusinessException;
import xyz.zzj.project.model.entity.NyApiUserInterface;
import xyz.zzj.project.service.NyApiUserInterfaceService;
import xyz.zzj.project.mapper.NyApiUserInterfaceMapper;
import org.springframework.stereotype.Service;

/**
* @author zengz
* @description 针对表【ny_api_user_interface(用户接口关系表)】的数据库操作Service实现
* @createDate 2024-03-24 16:12:44
*/
@Service
public class NyApiUserInterfaceServiceImpl extends ServiceImpl<NyApiUserInterfaceMapper, NyApiUserInterface> implements NyApiUserInterfaceService{
    @Override
    public void validNyApiUserInterface(NyApiUserInterface nyApiUserInterface, boolean add) {



        if (nyApiUserInterface == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = nyApiUserInterface.getId();
        Long userId = nyApiUserInterface.getUserId();
        Long interfaceId = nyApiUserInterface.getInterfaceId();
        Integer leftNum = nyApiUserInterface.getLeftNum();


        if (add){
            //当有参数为空时，返回异常
            if (ObjectUtils.anyNull(id,userId,interfaceId)){
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            if (leftNum < 0){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"剩余次数不能小于0");
            }
        }

    }
}




