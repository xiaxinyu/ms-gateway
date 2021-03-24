package com.sailfish.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.google.common.collect.Lists;
import com.sailfish.gateway.handler.JsonExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.Strings;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.config.PropertiesRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.result.view.ViewResolver;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author XIAXINYU3
 * @date 2021.3.24
 */
@Slf4j
@Configuration
public class SentinelConfig {
    List<ViewResolver> viewResolvers;

    ServerCodecConfigurer serverCodecConfigurer;

    private static final int intervalSec = 1;

    @Autowired
    DiscoveryClientRouteDefinitionLocator discoveryClientRouteDefinitionLocator;

    @Autowired
    PropertiesRouteDefinitionLocator propertiesRouteDefinitionLocator;


    public SentinelConfig(ObjectProvider<List<ViewResolver>> viewResolversProvider,
                          ServerCodecConfigurer serverCodecConfigurer) {
        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    /**
     * 配置SentinelGatewayBlockExceptionHandler，限流后异常处理
     *
     * @return
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public JsonExceptionHandler sentinelGatewayBlockExceptionHandler() {
        JsonExceptionHandler jsonExceptionHandler = new JsonExceptionHandler();
        jsonExceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
        jsonExceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
        return jsonExceptionHandler;
    }

    /**
     * 配置SentinelGatewayFilter
     *
     * @return
     */
    @Bean
    @Order(-1)
    public GlobalFilter sentinelGatewayFilter() {
        return new SentinelGatewayFilter();
    }

    @PostConstruct
    public void doInit() {
        initSteamSentinelRules();
    }

    private void initSteamSentinelRules() {
        Set<GatewayFlowRule> flowRules = new HashSet<>();
        List<String> steamAllServiceNames = getSteamAllServiceNames();
        steamAllServiceNames.forEach(t -> {
            log.info("限流配置：serviceName={}", t);
            GatewayFlowRule gatewayFlowRule = new GatewayFlowRule();
            // 服务
            gatewayFlowRule.setResource(t);
            // QPS
            gatewayFlowRule.setCount(1);
            // 每秒
            gatewayFlowRule.setIntervalSec(intervalSec);
            flowRules.add(gatewayFlowRule);
        });
        GatewayRuleManager.loadRules(flowRules);
    }

    private List<String> getSteamAllServiceNames() {
        Flux<RouteDefinition> ymlRouteDefinitionFlux = propertiesRouteDefinitionLocator.getRouteDefinitions();
        Flux<RouteDefinition> routeDefinitionFlux = discoveryClientRouteDefinitionLocator.getRouteDefinitions();
        Iterable<RouteDefinition> ymlRoutes = ymlRouteDefinitionFlux.toIterable();
        Iterable<RouteDefinition> discoveryRoutes = routeDefinitionFlux.toIterable();

        ArrayList<String> serviceNames = Lists.newArrayList();
        for (RouteDefinition route : ymlRoutes) {
            String serviceName = getServiceName(route.getId());
            serviceNames.add(serviceName);
        }

        for (RouteDefinition route : discoveryRoutes) {
            String serviceName = getServiceName(route.getId());
            serviceNames.add(serviceName);
        }
        return serviceNames;
    }

    /**
     * 获取路由名称ID
     *
     * @param routeId 路由ID
     * @return
     */
    private String getServiceName(String routeId) {
        if (StringUtils.isEmpty(routeId) || !routeId.contains("_")) {
            return routeId;
        }
        return Strings.toLowerCase(Arrays.asList(StringUtils.split(routeId, "_")).get(1));
    }
}
