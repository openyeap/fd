package ltd.fdsa.database.repository;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.core.util.NamingUtils;
import ltd.fdsa.database.entity.Status;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

@Slf4j
public class DAO {

    public static <T> List<T> getObjectList(DataSource ds, String psql, Class<T> tClass) {
        List<T> ls = new ArrayList<>();
        final Map<String, Method> methods = new HashMap<>();
        for (var method : tClass.getMethods()) {
            if (!method.getName().startsWith("set")) {
                continue;
            }
            if (methods.containsKey(method.getName())) {
                continue;
            }
            Type[] paraType = method.getGenericParameterTypes();
            if (paraType.length != 1) {
                continue;
            }
            methods.put(method.getName(), method);

        }
        try (var conn = ds.getConnection();
             var pst = conn.prepareStatement(psql);
             var rs = pst.executeQuery();) {

            //取得ResultSet的列名
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            int columnsCount = resultSetMetaData.getColumnCount();
            String[] columnNames = new String[columnsCount];
            for (int i = 0; i < columnsCount; i++) {

                columnNames[i] = resultSetMetaData.getColumnLabel(i + 1);
            }
            while (rs.next()) {
                T bean = (T) tClass.newInstance();
                //反射, 从ResultSet绑定到JavaBean
                for (int i = 0; i < columnNames.length; i++) {
                    //取得Set方法
                    String name = NamingUtils.underlineToCamel("set_" + columnNames[i]);
                    Method method = methods.get(name);
                    if (method == null) {
                        continue;
                    }
                    var paraType = Arrays.stream(method.getGenericParameterTypes()).findFirst().get().toString();
                    Object value = null;
                    if (paraType.indexOf("long") >= 0 || paraType.indexOf("bigint") >= 0)
                        value = rs.getLong(columnNames[i]);
                    else if (paraType.indexOf("Integer") >= 0)
                        value = rs.getInt(columnNames[i]);
                    else if (paraType.indexOf("String") >= 0)
                        value = rs.getString(columnNames[i]);
                    else if (paraType.indexOf("Double") >= 0)
                        value = rs.getDouble(columnNames[i]);
                    else if (paraType.indexOf("Float") >= 0)
                        value = rs.getFloat(columnNames[i]);
                    else if (paraType.indexOf("java.sql.Date") >= 0)
                        value = rs.getDate(columnNames[i]);
                    else if (paraType.indexOf("java.util.Date") >= 0)
                        value = new java.util.Date(rs.getTimestamp(columnNames[i]).getTime());
                    else if (paraType.indexOf("Time") >= 0)
                        value = rs.getDate(columnNames[i]);
                    else if (paraType.indexOf("Timestamp") >= 0)
                        value = rs.getTimestamp(columnNames[i]);
                    else if (paraType.indexOf("BigDecimal") >= 0)
                        value = rs.getBigDecimal(columnNames[i]);
                    else if (paraType.indexOf("boolean") >= 0)
                        value = rs.getBoolean(columnNames[i]);
                    else if (paraType.indexOf("Short") >= 0)
                        value = rs.getShort(columnNames[i]);
                    else if (paraType.indexOf("Byte") >= 0)
                        value = rs.getByte(columnNames[i]);
                    else if (paraType.contains(Status.class.getCanonicalName())) {
                        var status = rs.getInt(columnNames[i]);
                        for (var v : Status.values()) {
                            if (v.ordinal() == status) {
                                value = v;
                                break;
                            }
                        }

                    } else {
                        log.info("{}", paraType);
                        value = (Status) rs.getObject(columnNames[i]);
                    }
                    method.invoke(bean, value);
                }
                ls.add(bean);
            }


        } catch (SQLException e) {
            log.error("", e);
        } catch (Exception e) {
            log.error("", e);
        } finally {
        }
        return ls;
    }
}
