package com.jk.mcp;

import com.jk.mcp.service.ShoppingCart;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class McpApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpApplication.class, args);
    }

    @Bean
    public List<ToolCallback> tools(ShoppingCart shoppingCart) {
        return List.of(ToolCallbacks.from(shoppingCart));
    }

    @Bean
    public ChatClient chatClient(
            OllamaChatModel chatModel,
            List<ToolCallback> toolCallbacks) {
        System.out.printf("Hi");
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
