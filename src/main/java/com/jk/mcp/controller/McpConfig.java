package com.jk.mcp.controller;

import com.jk.mcp.service.ShoppingCart;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

import java.util.List;

@Configurable
public class McpConfig {

    @Bean
    public List<ToolCallback> tools(ShoppingCart shoppingCart) {
        return List.of(ToolCallbacks.from(shoppingCart));
    }
}
