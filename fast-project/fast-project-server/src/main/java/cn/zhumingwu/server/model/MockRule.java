package cn.zhumingwu.server.model;

import lombok.Data;


@Data
public class MockRule {
    /*
     * 请求路径
     */
//    @NotBlank(message = "请求路径不能为空")
    private String requestPath = "";

    /*
     * mock接口地址
     */
    private String mockUrl = "";

    /*
     * 处理方式 0:默认 1：随机 2：轮询
     */
    private Integer handleType = 0;

    /*
     * 有效时间  单位：分；只有处理方式选择“轮询”有效
     */
    private Integer validTime = 0;

    /*
     * 数据索引  从0开始，只有“轮询”方式有效。
     */
    private Integer dataIndex = 0;

    /*
     * 直接数据
     */
    private String responseData = "";

    /*
     * 开启状态  0:停用 1:启用
     */
    private Integer status = 1;

    /*
     * 数据创建时间 格式：yyyy-MM-dd HH:mm:ss
     */
    private String createTime = "";

    /*
     * 数据修改时间 格式：yyyy-MM-dd HH:mm:ss
     */
    private String updateTime = "";
}
