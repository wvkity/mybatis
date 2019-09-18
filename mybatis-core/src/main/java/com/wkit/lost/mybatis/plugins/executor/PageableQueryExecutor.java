package com.wkit.lost.mybatis.plugins.executor;

import com.wkit.lost.mybatis.plugins.cache.Cache;
import com.wkit.lost.mybatis.plugins.cache.CacheFactory;
import com.wkit.lost.mybatis.plugins.dbs.dialect.Dialect;
import com.wkit.lost.mybatis.plugins.dbs.dialect.PageableDialect;
import com.wkit.lost.mybatis.utils.MappedStatementUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Log4j2
public class PageableQueryExecutor extends AbstractQueryExecutor {

    /**
     * 查询总记录{@link MappedStatement}对象缓存
     */
    protected Cache<String, MappedStatement> recordMsCache = null;

    @Override
    protected Mode getTarget() {
        return Mode.PAGEABLE;
    }

    @Override
    public boolean filter( Argument arg ) {
        return true;
    }

    @Override
    protected Object doIntercept( Executor executor, MappedStatement statement, Object parameter, RowBounds rowBounds,
                                  ResultHandler resultHandler, CacheKey cacheKey, BoundSql boundSql ) throws Exception {
        List result;
        try {
            // 检查分页方言是否存在
            validateDialectExists();
            // 检查是否需要拦截
            PageableDialect dialect = ( PageableDialect ) this.factory;
            if ( dialect.filter( statement, parameter, rowBounds ) ) {
                // 检查是否需要查询总记录数
                if ( dialect.beforeOfQueryRecord( statement, parameter, rowBounds ) ) {
                    // 查询总记录数
                    long records = executeQueryRecord( executor, statement, parameter, rowBounds, boundSql, resultHandler );
                    // 处理总记录数
                    if ( !dialect.afterOfQueryRecord( records, parameter, rowBounds ) ) {
                        // 记录数为0则返回空集合
                        return dialect.executePagingOnAfter( new ArrayList<>(), parameter, rowBounds );
                    }
                }
                // 执行自定义分页查询
                result = Executors.executeQueryPageableOfCustom( dialect, executor, statement, parameter, rowBounds,
                        resultHandler, boundSql, cacheKey );
            } else {
                // 执行默认查询
                result = executor.query( statement, parameter, rowBounds, resultHandler, cacheKey, boundSql );
            }
            // 分页结果集处理
            return dialect.executePagingOnAfter( result, parameter, rowBounds );
        } catch ( Exception e ) {
            log.warn( "Pageable query plug-in failed to execute: `{}` -- {}", statement.getId(), e );
            throw e;
        } finally {
            Optional.ofNullable( factory ).ifPresent( Dialect::completed );
        }
    }

    /**
     * 执行总记录查询
     * @param executor      执行器
     * @param statement     {@link MappedStatement}对象
     * @param parameter     接口参数
     * @param rowBounds     分页参数
     * @param boundSql      绑定SQL
     * @param resultHandler 返回值处理对象
     * @return 总记录数
     * @throws SQLException 异常信息
     */
    private Long executeQueryRecord( Executor executor, MappedStatement statement, Object parameter, RowBounds rowBounds, BoundSql boundSql, ResultHandler resultHandler ) throws SQLException {
        String msId = statement.getId() + Dialect.PAGEABLE_RECORD_SUFFIX;
        // 检查是否存在总记录查询(MappedStatement对象)
        MappedStatement recordMs = MappedStatementUtil.getExistsMappedStatement( statement.getConfiguration(), msId );
        if ( recordMs != null ) {
            // 执行查询
            return Executors.executeQueryRecordOfExists( executor, recordMs, parameter, boundSql, resultHandler );
        } else {
            // 从缓存中读取查询记录MappedStatement对象
            recordMs = recordMsCache.get( msId );
            if ( recordMs == null ) {
                // 创建
                recordMs = MappedStatementUtil.newQueryRecordMappedStatement( statement, msId );
                recordMsCache.put( msId, recordMs );
            }
            return Executors.executeQueryRecordOfCustom( this.factory, executor, recordMs, parameter, boundSql, rowBounds, resultHandler );
        }
    }

    @Override
    protected String getDefaultDialect() {
        return "com.wkit.lost.mybatis.plugins.dbs.decorator.PageableDecorator";
    }

    @Override
    public void setProperties( Properties properties ) {
        super.setProperties( properties );
        this.recordMsCache = CacheFactory.createCache( properties.getProperty( "recordMsCache" ), "recordMs", properties );
    }
}
