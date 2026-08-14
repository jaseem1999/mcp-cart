package com.jk.mcp.service;

import com.jk.mcp.entity.Cart;
import com.jk.mcp.repository.CartRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCart implements McpService {

  private final CartRepository cartRepository;

  public ShoppingCart(CartRepository cartRepository) {
    this.cartRepository = cartRepository;
  }

  @Override
  public String generate(String prompt) {
    return "Shopping cart service is ready.";
  }

  @Tool(
          name = "addItem",
          description = "Add an item to the shopping cart. If the item already exists, increase its quantity. Specify item name and quantity."
  )
  public String addItem(String name, int quantity) {

    if (name == null || name.trim().isEmpty()) {
      return "Error: Item name cannot be empty.";
    }

    if (quantity <= 0) {
      return "Error: Quantity must be greater than 0.";
    }

    String productName = name.trim();

    Cart existingItem = cartRepository.findByProductIgnoreCase(productName);

    if (existingItem == null) {

      Cart cart = new Cart();
      cart.setProduct(productName);
      cart.setQuantity(quantity);

      cartRepository.save(cart);

      return "Added " + quantity + " of '" + productName + "' to the shopping cart.";

    } else {

      int newQuantity = existingItem.getQuantity() + quantity;
      existingItem.setQuantity(newQuantity);

      cartRepository.save(existingItem);

      return "Updated '" + productName + "' quantity to " + newQuantity + ".";
    }
  }

  @Tool(
          name = "getItems",
          description = "Get all items currently in the shopping cart with their quantities."
  )
  public List<Cart> getItems() {

    return cartRepository.findAll();
  }

  @Tool(
          name = "removeItem",
          description = "Remove a specified quantity of an item from the shopping cart. If the quantity equals the current quantity, the item is completely removed."
  )
  public String removeItem(String name, int quantity) {

    if (name == null || name.trim().isEmpty()) {
      return "Error: Item name cannot be empty.";
    }

    if (quantity <= 0) {
      return "Error: Quantity must be greater than 0.";
    }

    String productName = name.trim();

    Cart item = cartRepository.findByProductIgnoreCase(productName);

    if (item == null) {
      return "Error: Item '" + productName + "' not found in the shopping cart.";
    }

    int currentQuantity = item.getQuantity();

    if (quantity > currentQuantity) {
      return "Error: Cannot remove " + quantity
              + " of '" + productName
              + "'. Current quantity is " + currentQuantity + ".";
    }

    if (quantity == currentQuantity) {

      cartRepository.delete(item);

      return "Removed '" + productName + "' completely from the shopping cart.";

    } else {

      int remainingQuantity = currentQuantity - quantity;

      item.setQuantity(remainingQuantity);

      cartRepository.save(item);

      return "Removed " + quantity
              + " of '" + productName
              + "'. Remaining quantity: "
              + remainingQuantity + ".";
    }
  }
}