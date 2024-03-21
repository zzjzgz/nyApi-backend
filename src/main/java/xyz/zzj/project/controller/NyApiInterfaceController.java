package xyz.zzj.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import xyz.zzj.nyapiclientsdk.client.NyApiClient;
import xyz.zzj.project.annotation.AuthCheck;
import xyz.zzj.project.common.*;
import xyz.zzj.project.constant.CommonConstant;
import xyz.zzj.project.exception.BusinessException;
import xyz.zzj.project.model.dto.nyapiinterface.NyApiInterfaceAddRequest;
import xyz.zzj.project.model.dto.nyapiinterface.NyApiInterfaceInvokeRequest;
import xyz.zzj.project.model.dto.nyapiinterface.NyApiInterfaceQueryRequest;
import xyz.zzj.project.model.dto.nyapiinterface.NyApiInterfaceUpdateRequest;
import xyz.zzj.project.model.entity.NyApiInterface;
import xyz.zzj.project.model.entity.User;
import xyz.zzj.project.model.enums.InterfaceStatusEnum;
import xyz.zzj.project.service.NyApiInterfaceService;
import xyz.zzj.project.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * api接口
 *
 * @author zeng
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class NyApiInterfaceController {

    @Resource
    private NyApiInterfaceService nyApiInterfaceService;

    @Resource
    private UserService userService;

    @Resource
    private NyApiClient nyApiClient;

    // region 增删改查

    /**
     * 创建
     *
     * @param nyApiInterfaceAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addNyApiInterface(@RequestBody NyApiInterfaceAddRequest nyApiInterfaceAddRequest, HttpServletRequest request) {
        if (nyApiInterfaceAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        NyApiInterface nyApiInterface = new NyApiInterface();
        BeanUtils.copyProperties(nyApiInterfaceAddRequest, nyApiInterface);
        // 校验
        User loginUser = userService.getLoginUser(request);
        nyApiInterface.setUserId(loginUser.getId());
        boolean result = nyApiInterfaceService.save(nyApiInterface);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newNyApiInterfaceId = nyApiInterface.getId();
        nyApiInterfaceService.validNyApiInterface(nyApiInterface, true);
        return ResultUtils.success(newNyApiInterfaceId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteNyApiInterface(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        NyApiInterface oldNyApiInterface = nyApiInterfaceService.getById(id);
        if (oldNyApiInterface == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR

            );
        }
        // 仅本人或管理员可删除
        if (!oldNyApiInterface.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = nyApiInterfaceService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param nyApiInterfaceUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateNyApiInterface(@RequestBody NyApiInterfaceUpdateRequest nyApiInterfaceUpdateRequest,
                                            HttpServletRequest request) {
        if (nyApiInterfaceUpdateRequest == null || nyApiInterfaceUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        NyApiInterface nyApiInterface = new NyApiInterface();
        BeanUtils.copyProperties(nyApiInterfaceUpdateRequest, nyApiInterface);
        // 参数校验
        nyApiInterfaceService.validNyApiInterface(nyApiInterface, false);
        User user = userService.getLoginUser(request);
        long id = nyApiInterfaceUpdateRequest.getId();
        // 判断是否存在
        NyApiInterface oldNyApiInterface = nyApiInterfaceService.getById(id);
        if (oldNyApiInterface == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldNyApiInterface.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = nyApiInterfaceService.updateById(nyApiInterface);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<NyApiInterface> getNyApiInterfaceById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        NyApiInterface nyApiInterface = nyApiInterfaceService.getById(id);
        return ResultUtils.success(nyApiInterface);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param nyApiInterfaceQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<NyApiInterface>> listNyApiInterface(NyApiInterfaceQueryRequest nyApiInterfaceQueryRequest) {
        NyApiInterface nyApiInterfaceQuery = new NyApiInterface();
        if (nyApiInterfaceQueryRequest != null) {
            BeanUtils.copyProperties(nyApiInterfaceQueryRequest, nyApiInterfaceQuery);
        }
        QueryWrapper<NyApiInterface> queryWrapper = new QueryWrapper<>(nyApiInterfaceQuery);
        List<NyApiInterface> nyApiInterfaceList = nyApiInterfaceService.list(queryWrapper);
        return ResultUtils.success(nyApiInterfaceList);
    }

    /**
     * 分页获取列表
     *
     * @param nyApiInterfaceQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<NyApiInterface>> listNyApiInterfaceByPage(NyApiInterfaceQueryRequest nyApiInterfaceQueryRequest, HttpServletRequest request) {
        if (nyApiInterfaceQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        NyApiInterface nyApiInterfaceQuery = new NyApiInterface();
        BeanUtils.copyProperties(nyApiInterfaceQueryRequest, nyApiInterfaceQuery);
        long current = nyApiInterfaceQueryRequest.getCurrent();
        long size = nyApiInterfaceQueryRequest.getPageSize();
        String sortField = nyApiInterfaceQueryRequest.getSortField();
        String sortOrder = nyApiInterfaceQueryRequest.getSortOrder();
        String content = nyApiInterfaceQuery.getDescription();
        // description 需支持模糊搜索
        nyApiInterfaceQuery.setDescription(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<NyApiInterface> queryWrapper = new QueryWrapper<>(nyApiInterfaceQuery);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<NyApiInterface> nyApiInterfacePage = nyApiInterfaceService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(nyApiInterfacePage);
    }

    /**
     * 发布接口
     *
     * @param idRequest
     * @param request
     * @return
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> onlineNyApiInterface(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        long id = getId(idRequest);
        // 仅本人或管理员可修改接口状态
        NyApiInterface nyApiInterface = new NyApiInterface();
        nyApiInterface.setUserId(id);
        nyApiInterface.setStatus(InterfaceStatusEnum.ONLINE.getValue());
        boolean result = nyApiInterfaceService.updateById(nyApiInterface);
        return ResultUtils.success(result);
    }

    /**
     * 在线调用
     *
     * @param invokeRequest
     * @param request
     * @return
     */
    @PostMapping("/invoke")
    public BaseResponse<Object> offlineNyApiInvokeInterface(@RequestBody NyApiInterfaceInvokeRequest invokeRequest, HttpServletRequest request) {
        if (invokeRequest == null || invokeRequest.getId() == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = invokeRequest.getId();
        String userRequestParams = invokeRequest.getUserRequestParams();
        //判断接口是否存在
        NyApiInterface apiInterface = nyApiInterfaceService.getById(id);
        if (apiInterface == null || userRequestParams == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //判断接口是否开启
        if (apiInterface.getStatus() != InterfaceStatusEnum.ONLINE.getValue()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        //重新生成一个nyapi的客户端，因为客户端内置了管理员的令牌
        NyApiClient teamNyApiClient = new NyApiClient(accessKey,secretKey);
        //解析传来的参数
        Gson gson = new Gson();
        xyz.zzj.nyapiclientsdk.model.User paramsUser = gson.fromJson(userRequestParams, xyz.zzj.nyapiclientsdk.model.User.class);
        String userNameByPost = teamNyApiClient.getUserNameByPost(paramsUser);
        return ResultUtils.success(userNameByPost);
    }
    /**
     * 下线接口
     *
     * @param idRequest
     * @param request
     * @return
     */
    @PostMapping("/offline")
    public BaseResponse<Boolean> offlineNyApiInterface(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        long id = getId(idRequest);
        // 仅本人或管理员可修改接口状态
        NyApiInterface nyApiInterface = new NyApiInterface();
        nyApiInterface.setUserId(id);
        nyApiInterface.setStatus(InterfaceStatusEnum.OFFLINE.getValue());
        boolean result = nyApiInterfaceService.updateById(nyApiInterface);
        return ResultUtils.success(result);
    }

    /**
     * 发布和下线的公共方法
     * @param idRequest
     * @return
     */
    private long getId(IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = idRequest.getId();
        // 判断是否存在
        NyApiInterface oldNyApiInterface = nyApiInterfaceService.getById(id);
        if (oldNyApiInterface == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        //判断接口是否可以调用
        xyz.zzj.nyapiclientsdk.model.User user = new xyz.zzj.nyapiclientsdk.model.User();
        user.setUsername("zzj");
        String userNameByPost = nyApiClient.getUserNameByPost(user);
        if (StringUtils.isBlank(userNameByPost)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口验证失败");
        }
        return id;
    }

}
