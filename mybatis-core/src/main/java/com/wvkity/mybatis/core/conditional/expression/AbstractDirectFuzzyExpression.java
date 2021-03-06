package com.wvkity.mybatis.core.conditional.expression;

import com.wvkity.mybatis.core.constant.Match;
import com.wvkity.mybatis.core.mapping.sql.utils.ScriptUtil;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings({"serial"})
public abstract class AbstractDirectFuzzyExpression extends DirectExpressionWrapper {

    /**
     * 匹配模式
     */
    @Getter
    @Setter
    protected Match match = Match.ANYWHERE;

    /**
     * 转义字符
     */
    @Getter
    @Setter
    protected Character escape;

    @Override
    public String getSegment() {
        StringBuilder builder = new StringBuilder(60);
        Match realMatch = this.match == null ? Match.ANYWHERE : this.match;
        builder.append(ScriptUtil.convertConditionArg(getAlias(), this.column,
                this.symbol, this.logic, defaultPlaceholder(
                        realMatch.getSegment(this.value != null ? String.valueOf(this.value) : ""))));
        if (escape != null) {
            builder.append(" ESCAPE ").append("'").append(escape).append("'");
        }
        return builder.toString();
    }
}
