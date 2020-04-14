package ltd.fdsa.starter.logger.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import ltd.fdsa.starter.logger.interceptor.LogInterceptor;

@Configuration
@ConditionalOnProperty("log.interceptor")
public class LogAutoConfig implements WebMvcConfigurer {
	
	@Value("log.interceptor.addPathPatterns")
	private String[] addPathPatterns;
	@Value("log.interceptor.excludePathPatterns")
	private String[] excludePathPatterns;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		/**
		 * 自定义拦截器，添加拦截路径和排除拦截路径 addPathPatterns():添加需要拦截的路径
		 * excludePathPatterns():添加不需要拦截的路径 在括号中还可以使用集合的形式，如注释部分代码所示
		 */

		InterceptorRegistration reg = registry.addInterceptor(new LogInterceptor());

		for (String item : this.addPathPatterns) {
			reg.addPathPatterns(item);
		}

		for (String item : this.excludePathPatterns) {
			reg.excludePathPatterns(item);
		}

	}
}
