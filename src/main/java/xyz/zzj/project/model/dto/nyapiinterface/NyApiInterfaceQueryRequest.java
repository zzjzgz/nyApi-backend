package xyz.zzj.project.model.dto.nyapiinterface;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import xyz.zzj.project.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询请求
 *
 * @author zeng
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NyApiInterfaceQueryRequest extends PageRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 创建人id
     */
    private Long createUserId;

    /**
     * 描述
     */
    private String description;

    /**
     * 请求地址
     */
    private String requestUrl;

    /**
     * 请求类型
     */
    private String massage;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 请求体
     */
    private String responseHeader;

    /**
     * 接口状态，0-关闭，1-打开
     */
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}