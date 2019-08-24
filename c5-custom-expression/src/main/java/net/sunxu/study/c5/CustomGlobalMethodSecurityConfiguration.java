package net.sunxu.study.c5;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * 修改 {@link GlobalMethodSecurityConfiguration} 中的配置,
 * 主要是使用自定义的 MethodSecurityExpressionHandler, 里面还有一些对它的设置, 不过这里没有写.
 */
@Configuration
// GlobalMethodSecurityConfiguraton 需要这个注解中的配置值, 如果可以自己实现GlobalMethodSecurityConfiguration 中的所有bean 则也可以不需要这个注解.
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomGlobalMethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    /**
     * 创建expressionHandler, 这个在基类的对象是defaultMethodExpressionHandler, 它还有一些其他的配置,
     * 不过因为这里演示用不着, 所以就不用了.
     *
     * @return
     */
    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return new CustomMethodSecurityExpressionHandler();
    }
}
