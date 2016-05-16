
package com.hykj.gamecenter.logic.entry;

/** 权限控制实体类 */
public class AuthorityInfo {

    public String name;
    public String flag;
    public String desp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    @Override
    public String toString() {
        return "AuthorityInfo [name=" + name + ", flag=" + flag + ", desp=" + desp + "]";
    }

}
