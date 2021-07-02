package ltd.fdsa.switcher.core.pipeline.impl;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.core.util.ClassLoaderSwapper;
import ltd.fdsa.switcher.core.PluginType;

import ltd.fdsa.switcher.core.pipeline.Pipeline;
import ltd.fdsa.switcher.core.config.Configuration;
import ltd.fdsa.switcher.core.constant.Constants;
import ltd.fdsa.switcher.core.container.FastDataSwitcher;
import ltd.fdsa.web.view.Result;
import org.assertj.core.util.Strings;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


@ToString(exclude = {"config"})
@Slf4j
public abstract class AbstractPipeline implements Pipeline {

    protected Configuration config;
    protected String name;
    protected PluginType type;
    protected String description;

    protected final AtomicBoolean running = new AtomicBoolean(false);
    protected final List<Pipeline> nextSteps = new LinkedList<>();

    @Override
    public Result<String> init(Configuration configuration)  {
        this.config = configuration;
        this.name = this.config.getString(Constants.JOB_NAME, Constants.JOB_NAME_DEFAULT);
        this.type = PluginType.valueOf(this.config.getString(Constants.PIPELINE_TYPE, Constants.PIPELINE_TYPE_DEFAULT));
        this.description = this.config.getString(Constants.JOB_DESCRIPTION, "");

        var configurations = this.config.getConfigurations(Constants.PIPELINE_NODES);
        if (configurations != null && configurations.length > 0) {
            getNextSteps(configurations);
        }
        return Result.success();
    }

    private void getNextSteps(Configuration[] configurations) {
        for (var configuration : configurations) {
            var className = configuration.getString(Constants.PIPELINE_CLASS_NAME);
            if (Strings.isNullOrEmpty(className)) {
                continue;
            }
            try (ClassLoaderSwapper classLoaderSwapper = ClassLoaderSwapper.createClassLoaderSwapper()) {
                classLoaderSwapper.newClassLoader(FastDataSwitcher.getPluginManager().getClassLoader(className));
                var pipeline = FastDataSwitcher.getPluginManager().getInstance(className, Pipeline.class);
                if (pipeline == null) {
                    continue;
                }
                configuration.set(Constants.JOB_NAME, this.name + "." + configuration.getString(Constants.JOB_NAME, Constants.JOB_NAME_DEFAULT));
                pipeline.init(configuration);
                this.nextSteps.add(pipeline);
                classLoaderSwapper.restoreClassLoader();
            }
        }
    }

    @Override
    public Map<String, String> scheme() {
        return Collections.emptyMap();
    }


    @Override
    public void start() {
        if (this.running.compareAndSet(false, true)) {
            for (var pipeline : this.nextSteps) {
                pipeline.start();
            }
        }
    }

    @Override
    public void collect(Map<String, Object>... records) {
        if (!this.isRunning()) {
            return;
        }
        for (var item : this.nextSteps) {
            item.collect(records);
        }
    }

    @Override
    public void stop() {
        if (this.running.compareAndSet(true, false)) {
            for (var pipeline : this.nextSteps) {
                pipeline.start();
            }
        }
    }


    @Override
    public boolean isRunning() {
        return this.running.get();
    }

    public String getName() {
        return this.name;
    }

    public PluginType getType() {
        return this.type;
    }

    public String getDescription() {
        return this.description;
    }
}
