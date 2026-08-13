package com.jk.mcp.controller;

import com.jk.mcp.service.ChatService;
import com.jk.mcp.service.McpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class McpController {

    private final McpService mcpService;
    private final ChatService chatService;

    @Autowired
    public McpController(McpService mcpService, ChatService chatService) {
        this.mcpService = mcpService;
        this.chatService = chatService;
    }

    @PostMapping("/mcp/generate")
    public String generate(@RequestParam("prompt") String prompt) {
        return mcpService.generate(prompt);
    }

    @PostMapping({"/chat", "/api/chat"})
    public Map<String, String> chat(@RequestBody Map<String, String> request) {
        String prompt = request.getOrDefault("message", "").trim();
        if (prompt.isEmpty()) {
            return Map.of("reply", "Please provide a message to start the shopping chat.");
        }
        return Map.of("reply", chatService.chat(prompt));
    }
}
