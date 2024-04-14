package xyz.zzj.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import xyz.zzj.common.model.entity.NyApiInterface;
import xyz.zzj.common.service.InnerNyApiInterfaceService;
import xyz.zzj.project.common.ErrorCode;
import xyz.zzj.project.exception.BusinessException;
import xyz.zzj.project.mapper.NyApiInterfaceMapper;

import javax.annotation.Resource;

/**
 * @BelongsPackage: xyz.zzj.project.service.impl
 * @ClassName: InnerNyApiInterfaceServiceImpl
 * @Author: zengz
 * @CreateTime: 2024/4/9 21:32
 * @Description:  判断接口是否存在
 * @Version: 1.0
 */
@DubboService
public class InnerNyApiInterfaceServiceImpl implements InnerNyApiInterfaceService {



    @Resource
    private NyApiInterfaceMapper nyApiInterfaceMapper;

    /**
     * 根据请求路径、请求方法、请求参数获取接口信息
     * @param url 请求路径
     * @param method 请求方法
     * @return 接口信息
     */
    @Override
    public NyApiInterface getInterfaceInfo(String url, String method) {
        // 校验参数，不为空
        if (StringUtils.isAnyBlank(url, method)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数不能为空");
        }
        LambdaQueryWrapper<NyApiInterface> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(NyApiInterface::getUrl,url).eq(NyApiInterface::getMethod,method);
        return nyApiInterfaceMapper.selectOne(queryWrapper);

    }
}
