package com.i2dsp.maintenance.config.Enum;

/**
 * description 保养类型周期范围
 * date: 2021-07-01 11:21
 *
 * @author 林隆星
 */
public enum ScopeEnum {
    YEAR("y"),
    MONTH("m"),
    WEEK("w"),
    DAY("d");

    private String scope;

    /**
     * 私自构造器
     * @param scope
     */
    ScopeEnum(String scope) {
        this.scope = scope;
    }

    /**
     * 根据周期单位的名称，返回类型的枚举实例。
     *
     * @param scope 周期单位
     */
    public static ScopeEnum fromScope(String scope) {
        for (ScopeEnum scopeEnum : ScopeEnum.values()) {
            if (scopeEnum.getScope().equals(scope)) {
                return scopeEnum;
            }
        }
        return null;
    }

    /**
     * 检验是否为为有效的周期单位
     *
     * @param scope 周期单位
     */
    public static Boolean isScopeEnum(String scope) {
        for (ScopeEnum scopeEnum : ScopeEnum.values()) {
            if (scopeEnum.getScope().equals(scope)) {
                return true;
            }
        }
        return false;
    }

    private String getScope() {
        return this.scope;
    }

}
