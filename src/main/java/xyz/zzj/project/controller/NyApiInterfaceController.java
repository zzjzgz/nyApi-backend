package xyz.zzj.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import xyz.zzj.project.annotation.AuthCheck;
import xyz.zzj.project.common.BaseResponse;
import xyz.zzj.project.common.DeleteRequest;
import xyz.zzj.project.common.ErrorCode;
import xyz.zzj.project.common.ResultUtils;
import xyz.zzj.project.constant.CommonConstant;
import xyz.zzj.project.exception.BusinessException;
import xyz.zzj.project.model.dto.nyapiinterface.NyApiInterfaceAddRequest;
import xyz.zzj.project.model.dto.nyapiinterface.NyApiInterfaceQueryRequest;
import xyz.zzj.project.model.dto.nyapiinterface.NyApiInterfaceUpdateRequest;
import xyz.zzj.project.model.entity.NyApiInterface;
import xyz.zzj.project.model.entity.User;
import xyz.zzj.project.service.NyApiInterfaceService;
import xyz.zzj.project.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 帖子接口
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
        nyApiInterfaceService.validNyApiInterface(nyApiInterface, true);
        User loginUser = userService.getLoginUser(request);
        nyApiInterface.setUserId(loginUser.getId());
        boolean result = nyApiInterfaceService.save(nyApiInterface);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newNyApiInterfaceId = nyApiInterface.getId();
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
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
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

    // endregion

}
