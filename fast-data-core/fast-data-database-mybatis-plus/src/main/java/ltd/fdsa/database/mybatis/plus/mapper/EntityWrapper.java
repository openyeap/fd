package ltd.fdsa.database.mybatis.plus.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;

public class EntityWrapper<T> extends Wrapper<T> {

    @Override
    public T getEntity() {
        return null;
    }

    @Override
    public MergeSegments getExpression() {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public String getSqlSegment() {
        return null;
    }
}
