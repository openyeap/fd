package cn.zhumingwu.dataswitch.core.container;

import com.google.common.base.Strings;
import lombok.var;
import cn.zhumingwu.dataswitch.core.pipeline.Pipeline;
import cn.zhumingwu.base.config.Configuration;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PipelineProxyFactory {
    final PluginManager pluginManager;

    public PipelineProxyFactory(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
        // this.pluginManager.scan("./plugins");
    }


    public Pipeline getProxy(Configuration config, Object pipeline) {
        if (pipeline == null) {
            throw new IllegalArgumentException();
        }
        var interceptor = new PipelineInterceptor(config, pipeline);
        return ProxyFactory.getProxy(Pipeline.class, interceptor);
    }

    class PipelineInterceptor implements MethodInterceptor {
        private final Configuration context;
        private final Object pipeline;
        private final String name;

        private final String description;
        protected final AtomicBoolean running = new AtomicBoolean(false);
        protected final List<Pipeline> nextSteps = new LinkedList<>();

        public PipelineInterceptor(Configuration config, Object pipeline) {
            this.context = config;
            this.pipeline = pipeline;
            this.name = this.context.getString("name");
            this.description = this.context.getString("description");
            var configurations = this.context.getConfigurations("pipelines");
            if (configurations != null && configurations.length > 0) {
                loadPipelines(configurations);
            }
        }


        @Override
        public Object invoke(MethodInvocation methodInvocation) throws Throwable {
            var method = methodInvocation.getMethod();
            var arguments = methodInvocation.getArguments();
            switch (method.getName()) {
                case "context":
                    return this.context;
                case "start":
                    if (this.running.compareAndSet(false, true)) {
                        for (var pipeline : this.nextSteps) {
                            pipeline.start();
                        }
                        return method.invoke(this.pipeline, arguments);
                    }
                    return null;
                case "stop":
                    if (this.running.compareAndSet(true, false)) {
                        method.invoke(this.pipeline, arguments);
                        for (var pipeline : this.nextSteps) {
                            pipeline.stop();
                        }
                    }
                    return null;
                case "isRunning":
                    return this.running.get();
                case "nextSteps":
                    return this.nextSteps;
                default:
                    return method.invoke(this.pipeline, arguments);
            }

        }


        private void loadPipelines(Configuration[] configurations) {
            for (var configuration : configurations) {
                var className = configuration.get("class");
                if (Strings.isNullOrEmpty(className)) {
                    continue;
                }
                try (ClassLoaderSwapper classLoaderSwapper = ClassLoaderSwapper.createClassLoaderSwapper()) {
                    classLoaderSwapper.newClassLoader(pluginManager.getClassLoader(className));
                    var pipeline = getProxy(configuration, pluginManager.getInstance(className, Pipeline.class));
                    if (pipeline == null) {
                        continue;
                    }
                    configuration.set("name", this.name + "." + configuration.getString("name"));
                    pipeline.init();
                    this.nextSteps.add(pipeline);
                    classLoaderSwapper.restoreClassLoader();
                }
            }
        }
    }
}
