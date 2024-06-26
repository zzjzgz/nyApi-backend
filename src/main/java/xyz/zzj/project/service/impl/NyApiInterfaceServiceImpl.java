package xyz.zzj.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import xyz.zzj.common.model.entity.NyApiInterface;
import xyz.zzj.project.common.ErrorCode;
import xyz.zzj.project.exception.BusinessException;
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
         Long id = nyApiInterface.getId();
         String name = nyApiInterface.getName();
         Long createUserId = nyApiInterface.getUserId();
         String description = nyApiInterface.getDescription();
         String url = nyApiInterface.getUrl();
         String method = nyApiInterface.getMethod();
         String requestHeader = nyApiInterface.getRequestHeader();
         String responseHeader = nyApiInterface.getResponseHeader();
         String requestParams = nyApiInterface.getRequestParams();
         if (add){
             //当有参数为空时，返回异常
             if (StringUtils.isAnyBlank(name,description,url,method,requestHeader,responseHeader,requestParams) || ObjectUtils.anyNull(id,createUserId)){
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
             }
             if (StringUtils.isNotBlank(name) && name.length() > 50){
                 throw new BusinessException(ErrorCode.PARAMS_ERROR,"名称过长");
             }
         }

    }
}




