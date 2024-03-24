package xyz.zzj.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import xyz.zzj.nyapiclientsdk.client.NyApiClient;
import xyz.zzj.project.annotation.AuthCheck;
import xyz.zzj.project.common.BaseResponse;
import xyz.zzj.project.common.DeleteRequest;
import xyz.zzj.project.common.ErrorCode;
import xyz.zzj.project.common.ResultUtils;
import xyz.zzj.project.constant.CommonConstant;
import xyz.zzj.project.constant.UserConstant;
import xyz.zzj.project.exception.BusinessException;
import xyz.zzj.project.model.dto.nyapiinterface.NyApiInterfaceAddRequest;
import xyz.zzj.project.model.dto.nyapiinterface.NyApiInterfaceUpdateRequest;
import xyz.zzj.project.model.dto.nyapiuserinterface.NyApiUserInterfaceQueryRequest;
import xyz.zzj.project.model.entity.NyApiUserInterface;
import xyz.zzj.project.model.entity.User;
import xyz.zzj.project.service.NyApiUserInterfaceService;
import xyz.zzj.project.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * api接口
 *
 * @author zeng
 */
@RestController
@RequestMapping("/UserInterfaceInfo")
@Slf4j
public class NyApiUserInterfaceController {

    @Resource
    private NyApiUserInterfaceService nyApiUserUserInterfaceService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建
     *
     * @param nyApiUserInterfaceAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addNyApiUserInterface(@RequestBody NyApiInterfaceAddRequest nyApiUserInterfaceAddRequest, HttpServletRequest request) {
        if (nyApiUserInterfaceAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        NyApiUserInterface nyApiUserInterface = new NyApiUserInterface();
        BeanUtils.copyProperties(nyApiUserInterfaceAddRequest, nyApiUserInterface);
        // 校验
        User loginUser = userService.getLoginUser(request);
        nyApiUserInterface.setUserId(loginUser.getId());
        boolean result = nyApiUserUserInterfaceService.save(nyApiUserInterface);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newNyApiUserInterfaceId = nyApiUserInterface.getId();
        nyApiUserUserInterfaceService.validNyApiUserInterface(nyApiUserInterface, true);
        return ResultUtils.success(newNyApiUserInterfaceId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteNyApiUserInterface(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        NyApiUserInterface oldNyApiUserInterface = nyApiUserUserInterfaceService.getById(id);
        if (oldNyApiUserInterface == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR

            );
        }
        // 仅本人或管理员可删除
        if (!oldNyApiUserInterface.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = nyApiUserUserInterfaceService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param nyApiUserInterfaceUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateNyApiUserInterface(@RequestBody NyApiInterfaceUpdateRequest nyApiUserInterfaceUpdateRequest,
                                            HttpServletRequest request) {
        if (nyApiUserInterfaceUpdateRequest == null || nyApiUserInterfaceUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        NyApiUserInterface nyApiUserInterface = new NyApiUserInterface();
        BeanUtils.copyProperties(nyApiUserInterfaceUpdateRequest, nyApiUserInterface);
        // 参数校验
        nyApiUserUserInterfaceService.validNyApiUserInterface(nyApiUserInterface, false);
        User user = userService.getLoginUser(request);
        long id = nyApiUserInterfaceUpdateRequest.getId();
        // 判断是否存在
        NyApiUserInterface oldNyApiUserInterface = nyApiUserUserInterfaceService.getById(id);
        if (oldNyApiUserInterface == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldNyApiUserInterface.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = nyApiUserUserInterfaceService.updateById(nyApiUserInterface);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<NyApiUserInterface> getNyApiUserInterfaceById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        NyApiUserInterface nyApiUserInterface = nyApiUserUserInterfaceService.getById(id);
        return ResultUtils.success(nyApiUserInterface);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param nyApiUserInterfaceQueryRequest
     * @return
     */

    @GetMapping("/list")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<List<NyApiUserInterface>> listNyApiUserInterface(NyApiUserInterfaceQueryRequest nyApiUserInterfaceQueryRequest) {
        NyApiUserInterface nyApiUserInterfaceQuery = new NyApiUserInterface();
        if (nyApiUserInterfaceQueryRequest != null) {
            BeanUtils.copyProperties(nyApiUserInterfaceQueryRequest, nyApiUserInterfaceQuery);
        }
        QueryWrapper<NyApiUserInterface> queryWrapper = new QueryWrapper<>(nyApiUserInterfaceQuery);
        List<NyApiUserInterface> nyApiUserInterfaceList = nyApiUserUserInterfaceService.list(queryWrapper);
        return ResultUtils.success(nyApiUserInterfaceList);
    }

    /**
     * 分页获取列表
     *
     * @param nyApiUserInterfaceQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Page<NyApiUserInterface>> listNyApiUserInterfaceByPage(NyApiUserInterfaceQueryRequest nyApiUserInterfaceQueryRequest, HttpServletRequest request) {
        if (nyApiUserInterfaceQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        NyApiUserInterface nyApiUserInterfaceQuery = new NyApiUserInterface();
        BeanUtils.copyProperties(nyApiUserInterfaceQueryRequest, nyApiUserInterfaceQuery);
        long current = nyApiUserInterfaceQueryRequest.getCurrent();
        long size = nyApiUserInterfaceQueryRequest.getPageSize();
        String sortField = nyApiUserInterfaceQueryRequest.getSortField();
        String sortOrder = nyApiUserInterfaceQueryRequest.getSortOrder();
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<NyApiUserInterface> queryWrapper = new QueryWrapper<>(nyApiUserInterfaceQuery);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<NyApiUserInterface> nyApiUserInterfacePage = nyApiUserUserInterfaceService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(nyApiUserInterfacePage);
    }

}
