package cn.zhumingwu.database.mybatis.interceptor;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import cn.zhumingwu.core.util.NamingUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Properties;

@Slf4j
@Intercepts({
//        @Signature(type = ParameterHandler.class, method = "setParameters", args = {PreparedStatement.class}),
//        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class}),
//        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
//        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
})

public class JoinTableInterceptor implements Interceptor, ApplicationContextAware {

    private ApplicationContext applicationContext;
//
//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        Object result = invocation.proceed();
//        if (JoinTableFlag.getFlag()) {
//            Method method = invocation.getMethod(); //代理方法
//            Class<?> returnType = method.getReturnType();
//            if (returnType.isAssignableFrom(List.class)) {
//                Object[] args = invocation.getArgs(); //方法参数
//                MappedStatement mappedStatement = (MappedStatement) args[0];
//                String id = mappedStatement.getId();
//                String resource = id.substring(0, id.lastIndexOf("."));
//                Class<?> type = (Class<?>) ((ParameterizedType) (Class.forName(resource).getGenericInterfaces()[0])).getActualTypeArguments()[0];
//                Field[] fields = type.getDeclaredFields();
//                List<?> list = (List<?>) result;
//                for (Field field : fields) {
//                    JoinTable joinTable = field.getAnnotation(JoinTable.class);
//                    if (joinTable != null) {
//                        field.setAccessible(true);
//                        WriteMapper<?, ?> baseMapper = (WriteMapper<?, ?>) applicationContext.getBean(joinTable.mappedClass());
//                        list.forEach(row -> {
//                            try {
//                                //TODO 加查询条件
//                                Wrapper wrapper = Wrapper.build();
//                                Field f = row.getClass().getField("");
//                                Object obj = f.get(row);
//                                wrapper.eq(f.getName(), obj);
////                                field.set(row, baseMapper.selectList(wrapper));
//                            } catch (IllegalAccessException | NoSuchFieldException e) {
//                                log.error("Exception", e);
//                            }
//                        });
//                    }
//                }
//            }
//        }
//        return result;
//    }


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        Executor executor = (Executor) invocation.getTarget();
        MappedStatement statement = (MappedStatement) args[0];
        Object parameter = args[1];
        RowBounds rowBounds = (RowBounds) args[2];
        boolean isUpdate = args.length == 2;

        ResultHandler resultHandler = (ResultHandler) args[3];
        BoundSql boundSql;
        if (args.length == 4) {
            boundSql = statement.getBoundSql(parameter);
        } else {
            boundSql = (BoundSql) args[5];
        }
        var sql = boundSql.getSql();
        NamingUtils.formatLog(log,"getArgs");
        System.out.println(sql);
        NamingUtils.formatLog(log,"getMethod");
        System.out.println(invocation.getMethod());
        NamingUtils.formatLog(log,"getTarget");
        System.out.println(invocation.getTarget());


        var MappedStatement = (MappedStatement) invocation.getArgs()[0];
        System.out.println(MappedStatement);

        System.out.println(MappedStatement.getBoundSql(null));
        ;

        var obj = invocation.proceed();
        return obj;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
