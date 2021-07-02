package ltd.fdsa.starter.jdbc.collection;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class LazyResultSetCollection<E> implements Collection<E> {
    private final ResultSet rs;
    private final RowMapper<E> mapper;
    private final Class<E> clazz;

    public LazyResultSetCollection(ResultSet resultSet, RowMapper<E> mapper, Class<E> clazz) {
        this.rs = resultSet;
        this.mapper = mapper;
        this.clazz = clazz;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return true;
    }

    @Override
    public Iterator<E> iterator() {
        return new ResultSetIterator<>(rs, clazz);
    }

    @Override
    public Object[] toArray() {
        List<E> results = new ArrayList<>();

        this.stream().forEach(results::add);

        return results.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        List<E> results = new ArrayList<>();

        this.stream().forEach(results::add);

        return results.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return true;
    }

    @Override
    public void clear() {
    }

    private class ResultSetIterator<C> implements Iterator<C> {
        private final ResultSet rs;
        private final Class<C> clazz;
        private C next;

        public ResultSetIterator(ResultSet resultSet, Class<C> clazz) {
            rs = resultSet;
            this.clazz = clazz;
        }

        @Override
        public boolean hasNext() {
            if (next == null) {
                boolean hasNext = false;
                try {
                    if (hasNext = rs.next()) {
                        next = (C)mapper.mapRow(rs, 0);
                    } else {
                        return false;
                    }

                } catch (Exception e) {
                }

                return hasNext;
            } else {

                return true;
            }
        }

        @Override
        public C next() {
            if (next == null) {
                try {
                    rs.next();
                    return (C)mapper.mapRow(rs, 0);
                } catch (Exception e) {
                    return null;
                }
            } else {
                C returnItem = next;
                next = null;
                return returnItem;
            }
        }
    }
}
