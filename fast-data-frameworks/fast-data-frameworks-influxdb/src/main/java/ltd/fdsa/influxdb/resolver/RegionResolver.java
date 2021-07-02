package ltd.fdsa.influxdb.resolver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j

public class RegionResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        //判断参数是否有自定义注解
        return methodParameter.hasParameterAnnotation(RequestBody.class)
//                && (methodParameter.getParameterType() == PointQuery.class || methodParameter.getParameterType() == DeviceQuery.class)
                ;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();
//        if (methodParameter.getParameterType() == PointQuery.class) {
//            JSONObject object = JSON.parseObject(request.getInputStream(), JSONObject.class);
//            if (object == null) {
//                return null;
//            }
//            PointQuery query = object.toJavaObject(PointQuery.class);
//            if (query == null) {
//                return null;
//            }
//            var region = object.getJSONObject("region");
//            if (region == null) {
//                return query;
//            }
//            var type = (String) region.get("type");
//            switch (type) {
//                case "Box":
//                    query.setRegion(region.toJavaObject(Box.class));
//                    break;
//                case "Polygon":
//                    query.setRegion(region.toJavaObject(Polygon.class));
//                    break;
//                default:
//                    query.setRegion(region.toJavaObject(Circle.class));
//                    break;
//            }
//            return query;
//        }
//
//        JSONObject object = JSON.parseObject(request.getInputStream(), JSONObject.class);
//        if (object == null) {
//            return null;
//        }
//        DeviceQuery query = object.toJavaObject(DeviceQuery.class);
//        if (query == null) {
//            return null;
//        }
//        var region = object.getJSONObject("region");
//        if (region == null) {
//            return query;
//        }
//        var type = (String) region.get("type");
//        switch (type) {
//            case "Box":
//                query.setRegion(region.toJavaObject(Box.class));
//                break;
//            case "Polygon":
//                query.setRegion(region.toJavaObject(Polygon.class));
//                break;
//            default:
//                query.setRegion(region.toJavaObject(Circle.class));
//                break;
//        }
//        return query;
        return null;
    }
}
