package com.wkit.lost.mybatis.core.mapping.sql;

import com.wkit.lost.mybatis.core.mapping.sql.utils.ScriptUtil;
import com.wkit.lost.mybatis.utils.Constants;

/**
 * 逻辑删除SQL构建器
 * @author wvkity
 */
public abstract class AbstractLogicDeleteProvider extends AbstractProvider {

    protected static final String CRITERIA_HAS_CONDITION_SEGMENT = Constants.PARAM_CRITERIA + " != null and "
            + Constants.PARAM_CRITERIA + ".hasCondition";
    protected static final String CRITERIA_WHERE_SEGMENT = ScriptUtil.unSafeJoint( Constants.PARAM_CRITERIA,
            Constants.CHAR_DOT, "whereSegment" );
}
