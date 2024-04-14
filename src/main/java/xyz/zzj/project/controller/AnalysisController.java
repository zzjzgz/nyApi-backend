package xyz.zzj.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zzj.common.model.entity.NyApiInterface;
import xyz.zzj.common.model.entity.NyApiUserInterface;
import xyz.zzj.project.annotation.AuthCheck;
import xyz.zzj.project.common.BaseResponse;
import xyz.zzj.project.common.ErrorCode;
import xyz.zzj.project.common.ResultUtils;
import xyz.zzj.project.exception.BusinessException;
import xyz.zzj.project.mapper.NyApiUserInterfaceMapper;
import xyz.zzj.project.model.vo.InterfaceInfoVo;
import xyz.zzj.project.service.NyApiInterfaceService;
import xyz.zzj.project.service.NyApiUserInterfaceService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @BelongsPackage: xyz.zzj.project.controller
 * @ClassName: AnalysisController
 * @Author: zengz
 * @CreateTime: 2024/4/14 16:20
 * @Description: 分析接口
 * @Version: 1.0
 */

@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {

    @Resource
    private NyApiUserInterfaceMapper nyApiUserInterfaceMapper;

    @Resource
    private NyApiInterfaceService nyApiInterfaceService;

    /**
     * 接口分析
     * 仅管理员可访问
     * @return 接口的调用次数排名前 5 的接口信息
     */
    @GetMapping("/top/interface/invoke")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<List<InterfaceInfoVo>> listTopInvokeInterfaceInfo() {
        List<NyApiUserInterface> nyApiUserInterfaceList = nyApiUserInterfaceMapper.listTopInvokeInterfaceInfo(5);
        //根据接口id进行分组，接口id作为key
        Map<Long, List<NyApiUserInterface>> interfaceIdMap = nyApiUserInterfaceList.stream()
                .collect(Collectors.groupingBy(NyApiUserInterface::getInterfaceId));
        LambdaQueryWrapper<NyApiInterface> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(NyApiInterface::getId,interfaceIdMap.keySet());
        List<NyApiInterface> list = nyApiInterfaceService.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        List<InterfaceInfoVo> interfaceInfoVoList = list.stream().map(nyApiInterface -> {
            InterfaceInfoVo interfaceInfoVo = new InterfaceInfoVo();
            //将获取到的接口信息转化为 vo 对象
            BeanUtils.copyProperties(nyApiInterface, interfaceInfoVo);
            //获取通过接口id分组后得到的total值，并把他写入 vo的total属性 里面
            int totalNum = interfaceIdMap.get(nyApiInterface.getId()).get(0).getTotalNum();
            interfaceInfoVo.setTotalNum(totalNum);
            return interfaceInfoVo;
        }).collect(Collectors.toList());
        return ResultUtils.success(interfaceInfoVoList);
    }
}
