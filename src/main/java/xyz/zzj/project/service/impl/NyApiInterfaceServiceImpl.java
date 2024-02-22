package xyz.zzj.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import xyz.zzj.project.common.ErrorCode;
import xyz.zzj.project.exception.BusinessException;
import xyz.zzj.project.model.entity.NyApiInterface;
import xyz.zzj.project.model.enums.PostGenderEnum;
import xyz.zzj.project.model.enums.PostReviewStatusEnum;
import xyz.zzj.project.service.NyApiInterfaceService;
import xyz.zzj.project.mapper.NyApiInterfaceMapper;
import org.springframework.stereotype.Service;

/**
* @author zeng
* @description 针对表【ny_api_interface(接口信息)】的数据库操作Service实现
* @createDate 2024-02-22 21:30:06
*/
@Service
public class NyApiInterfaceServiceImpl extends ServiceImpl<NyApiInterfaceMapper, NyApiInterface>
    implements NyApiInterfaceService{

    @Override
    public void validNyApiInterface(NyApiInterface nyApiInterface, boolean add) {
        if (nyApiInterface == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
    }
}




