package com.jk.mcp.controller;

import com.jk.mcp.service.ShoppingCart;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class McpConfig {

    @Bean
    public List<ToolCallback> tools(ShoppingCart shoppingCart) {
        return List.of(ToolCallbacks.from(shoppingCart));
    }

    @Bean
    public ChatClient chatClient(
            OllamaChatModel chatModel,
            List<ToolCallback> toolCallbacks) {

        return ChatClient.builder(chatModel)
                .defaultTools(toolCallbacks)
                .defaultSystem("""
                    You are a helpful shopping assistant.
                    Use the shopping tools when the user asks to
                    add, remove, or list items.
                    """)
                .build();
    }
}