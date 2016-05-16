
package com.hykj.gamecenter.data;

import java.io.Serializable;

public class GroupInfo implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public int groupId; // 分组ID
    public int groupClass; // 分组类别
    public int groupType; // 分组类型
    public int orderType; // 排序类型
    public int orderNo; // 排序号（该值不一定连续）
    public String recommWrod; // 推荐语
    public String groupName; // 分组名称（办公/社交...休闲/策略...网游/单机...专题...启动推荐/首页推荐...）
    public String groupDesc; // 分组描述，分类时代表推荐词，专题时代表专题说明
    public String groupPicUrl; // 分组图片URL
    public String startTime; // 开始时间
    public String endTime; // 结束时间

    @Override
    public String toString() {
        return "GroupInfo [groupId=" + groupId + ", groupClass=" + groupClass + ", groupType="
                + groupType + ", orderType=" + orderType + ", orderNo=" + orderNo + ", recommWrod="
                + recommWrod + ", groupName=" + groupName + ", groupDesc=" + groupDesc
                + ", groupPicUrl=" + groupPicUrl + ", startTime=" + startTime + ", endTime="
                + endTime + "]";
    }

}
