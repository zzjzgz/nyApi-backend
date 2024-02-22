package xyz.zzj.project.model.dto.nyapiinterface;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @TableName product
 */
@Data
public class NyApiInterfaceAddRequest implements Serializable {

    /**
     * 接口名称
     */
    private String name;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}