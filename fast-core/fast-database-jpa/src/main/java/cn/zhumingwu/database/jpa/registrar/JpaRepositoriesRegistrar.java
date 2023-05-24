package cn.zhumingwu.database.jpa.registrar;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension;
import org.springframework.data.repository.config.AnnotationRepositoryConfigurationSource;
import org.springframework.data.repository.config.RepositoryConfigurationDelegate;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;
import org.springframework.data.repository.config.RepositoryConfigurationUtils;
import org.springframework.data.util.Streamable;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class JpaRepositoriesRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

    Environment environment;
    ResourceLoader resourceLoader;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry, BeanNameGenerator generator) {
        Assert.notNull(metadata, "AnnotationMetadata must not be null!");
        Assert.notNull(registry, "BeanDefinitionRegistry must not be null!");
        Assert.notNull(this.resourceLoader, "ResourceLoader must not be null!");
        if (metadata.getAnnotationAttributes(this.getAnnotation().getName()) != null) {
            CustomAnnotationRepositoryConfigurationSource configurationSource = new CustomAnnotationRepositoryConfigurationSource(metadata, this.getAnnotation(), this.resourceLoader, this.environment, registry, generator);
            RepositoryConfigurationExtension extension = this.getExtension();
            RepositoryConfigurationUtils.exposeRegistration(extension, registry, configurationSource);
            RepositoryConfigurationDelegate delegate = new RepositoryConfigurationDelegate(configurationSource, this.resourceLoader, this.environment);
            delegate.registerRepositoriesIn(registry, extension);
        }
    }

    protected Class<? extends Annotation> getAnnotation() {
        return ScanJpaRepositories.class;
    }

    protected RepositoryConfigurationExtension getExtension() {
        return new JpaRepositoryConfigExtension();
    }

    class CustomAnnotationRepositoryConfigurationSource extends AnnotationRepositoryConfigurationSource {

        private final Environment environment;

        public CustomAnnotationRepositoryConfigurationSource(AnnotationMetadata metadata,
                                                             Class<? extends Annotation> annotation,
                                                             ResourceLoader resourceLoader,
                                                             Environment environment,
                                                             BeanDefinitionRegistry registry,
                                                             BeanNameGenerator generator
        ) {
            super(metadata, annotation, resourceLoader, environment, registry, generator);

            this.environment = environment;
        }


        @Override
        public Streamable<String> getBasePackages() {
            Streamable<String> rawPackages = super.getBasePackages();
            return Streamable.of(() -> rawPackages.stream().flatMap(raw -> parsePackagesSpel(raw).stream()));
        }

        private List<String> parsePackagesSpel(@Nullable String rawPackage) {
            Objects.requireNonNull(rawPackage, "Package specification cannot be null");

            String packages = this.environment.resolvePlaceholders(rawPackage);

            return Arrays.stream(packages.split(","))
                    .map(String::trim)
                    .filter(StringUtils::hasText)
                    .collect(Collectors.toList());
        }
    }
}
