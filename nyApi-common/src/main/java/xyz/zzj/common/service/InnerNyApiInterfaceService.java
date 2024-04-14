package xyz.zzj.common.service;


import com.baomidou.mybatisplus.extension.service.IService;
import xyz.zzj.common.model.entity.NyApiInterface;

/**
* @author zeng
* @description 针对表【ny_api_interface(接口信息)】的数据库操作Service
* @createDate 2024-02-22 21:30:06
*/
public interface InnerNyApiInterfaceService {


    //2. 从数据库中查询模拟接口是否存在，以及请求方法是否匹配。请求参数---（请求路径，请求方法）返回接口信息
    NyApiInterface getInterfaceInfo(String requestPath, String requestMethod);
}
