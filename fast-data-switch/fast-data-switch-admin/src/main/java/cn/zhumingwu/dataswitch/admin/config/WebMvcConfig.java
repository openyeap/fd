package cn.zhumingwu.dataswitch.admin.config;


import freemarker.template.TemplateModelException;
import cn.zhumingwu.dataswitch.core.util.I18nUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * web mvc config
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class WebMvcConfig implements WebFilter {
    @Autowired
    private freemarker.template.Configuration configuration;

    public static Mono<ServerWebExchange> getRequest() {
        return Mono.subscriberContext()
                .map(ctx -> ctx.get(ServerWebExchange.class));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        try {
            configuration.setSharedVariable("i18n", I18nUtil.getInstance(""));
        } catch (TemplateModelException e) {
            e.printStackTrace();
        }
        return chain.filter(exchange)
                .subscriberContext(ctx -> ctx.put(ServerWebExchange.class, exchange));

    }
}
