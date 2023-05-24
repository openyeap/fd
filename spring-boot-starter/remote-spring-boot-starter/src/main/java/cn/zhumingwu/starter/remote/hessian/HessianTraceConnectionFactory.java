package cn.zhumingwu.starter.remote.hessian;

import cn.zhumingwu.web.utils.TraceUtils;
import com.caucho.hessian.client.HessianConnection;
import com.caucho.hessian.client.HessianURLConnectionFactory;
import lombok.var;

import java.io.IOException;
import java.net.URL;

public class HessianTraceConnectionFactory extends HessianURLConnectionFactory {

    public HessianTraceConnectionFactory() {
    }

    @Override
    public HessianConnection open(URL url) throws IOException {
//        if (url.getProtocol().startsWith("lbs")) {
//            super.open(new URL(url.getProtocol()));
//        }
        var connection = super.open(url);
        connection.addHeader(TraceUtils.TRACE_ID, TraceUtils.getTraceId());
        return connection;
    }
}
