package com.jk.mcp.service;

import com.jk.mcp.entity.Cart;
import com.jk.mcp.repository.CartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCart implements McpService {

  private static final Logger logger = LoggerFactory.getLogger(ShoppingCart.class);

  private final CartRepository cartRepository;

  public ShoppingCart(CartRepository cartRepository) {
    this.cartRepository = cartRepository;
  }

  @Override
  public String generate(String prompt) {
    logger.debug("Generating shopping cart service response");
    return "Shopping cart service is ready.";
  }

  @Tool(
          name = "addItem",
          description = "Add an item to the shopping cart. If the item already exists, increase its quantity. Specify item name and quantity."
  )
  public String addItem(String name, int quantity) {

    if (name == null || name.trim().isEmpty()) {
      logger.warn("Cannot add shopping cart item: item name is empty");
      return "Error: Item name cannot be empty.";
    }

    if (quantity <= 0) {
      logger.warn("Cannot add shopping cart item: quantity must be greater than 0");
      return "Error: Quantity must be greater than 0.";
    }

    String productName = name.trim();

    Cart existingItem = cartRepository.findByProductIgnoreCase(productName);

    if (existingItem == null) {

      Cart cart = new Cart();
      cart.setProduct(productName);
      cart.setQuantity(quantity);

      cartRepository.save(cart);
      logger.info("Added {} of '{}' to the shopping cart", quantity, productName);

      return "Added " + quantity + " of '" + productName + "' to the shopping cart.";

    } else {

      int newQuantity = existingItem.getQuantity() + quantity;
      existingItem.setQuantity(newQuantity);

      cartRepository.save(existingItem);
      logger.info("Increased '{}' quantity by {} to {}", productName, quantity, newQuantity);

      return "Updated '" + productName + "' quantity to " + newQuantity + ".";
    }
  }

  @Tool(
          name = "getItems",
          description = "Get all items currently in the shopping cart with their quantities."
  )
  public List<Cart> getItems() {

    List<Cart> items = cartRepository.findAll();
    logger.debug("Retrieved {} shopping cart items", items.size());
    return items;
  }

  @Tool(
          name = "updateItem",
          description = "Set the quantity of an existing shopping cart item to the specified quantity. This replaces the current quantity; it does not add to it."
  )
  public String updateItem(String name, int quantity) {

    if (name == null || name.trim().isEmpty()) {
      logger.warn("Cannot update shopping cart item: item name is empty");
      return "Error: Item name cannot be empty.";
    }

    if (quantity <= 0) {
      logger.warn("Cannot update shopping cart item: quantity must be greater than 0");
      return "Error: Quantity must be greater than 0.";
    }

    String productName = name.trim();
    Cart item = cartRepository.findByProductIgnoreCase(productName);

    if (item == null) {
      logger.warn("Cannot update shopping cart item '{}': item not found", productName);
      return "Error: Item '" + productName + "' not found in the shopping cart.";
    }

    item.setQuantity(quantity);
    cartRepository.save(item);
    logger.info("Updated '{}' quantity to {}", productName, quantity);

    return "Updated '" + productName + "' quantity to " + quantity + ".";
  }

  @Tool(
          name = "renameItem",
          description = "Rename an existing shopping cart item. Specify its current name and the new name."
  )
  public String renameItem(String name, String newName) {

    if (name == null || name.trim().isEmpty()) {
      logger.warn("Cannot rename shopping cart item: item name is empty");
      return "Error: Item name cannot be empty.";
    }

    if (newName == null || newName.trim().isEmpty()) {
      logger.warn("Cannot rename shopping cart item: new item name is empty");
      return "Error: New item name cannot be empty.";
    }

    String productName = name.trim();
    String newProductName = newName.trim();
    Cart item = cartRepository.findByProductIgnoreCase(productName);

    if (item == null) {
      logger.warn("Cannot rename shopping cart item '{}': item not found", productName);
      return "Error: Item '" + productName + "' not found in the shopping cart.";
    }

    Cart existingItem = cartRepository.findByProductIgnoreCase(newProductName);
    if (existingItem != null && !existingItem.getId().equals(item.getId())) {
      logger.warn("Cannot rename '{}' to '{}': another item already has that name", productName, newProductName);
      return "Error: An item named '" + newProductName + "' already exists in the shopping cart.";
    }

    item.setProduct(newProductName);
    cartRepository.save(item);
    logger.info("Renamed shopping cart item '{}' to '{}'", productName, newProductName);

    return "Renamed '" + productName + "' to '" + newProductName + "'.";
  }

  @Tool(
          name = "removeItem",
          description = "Remove a specified quantity of an item from the shopping cart. If the quantity equals the current quantity, the item is completely removed."
  )
  public String removeItem(String name, int quantity) {

    if (name == null || name.trim().isEmpty()) {
      logger.warn("Cannot remove shopping cart item: item name is empty");
      return "Error: Item name cannot be empty.";
    }

    if (quantity <= 0) {
      logger.warn("Cannot remove shopping cart item: quantity must be greater than 0");
      return "Error: Quantity must be greater than 0.";
    }

    String productName = name.trim();

    Cart item = cartRepository.findByProductIgnoreCase(productName);

    if (item == null) {
      logger.warn("Cannot remove shopping cart item '{}': item not found", productName);
      return "Error: Item '" + productName + "' not found in the shopping cart.";
    }

    int currentQuantity = item.getQuantity();

    if (quantity > currentQuantity) {
      logger.warn("Cannot remove {} of '{}': current quantity is {}", quantity, productName, currentQuantity);
      return "Error: Cannot remove " + quantity
              + " of '" + productName
              + "'. Current quantity is " + currentQuantity + ".";
    }

    if (quantity == currentQuantity) {

      cartRepository.delete(item);
      logger.info("Removed '{}' completely from the shopping cart", productName);

      return "Removed '" + productName + "' completely from the shopping cart.";

    } else {

      int remainingQuantity = currentQuantity - quantity;

      item.setQuantity(remainingQuantity);

      cartRepository.save(item);
      logger.info("Removed {} of '{}'; remaining quantity is {}", quantity, productName, remainingQuantity);

      return "Removed " + quantity
              + " of '" + productName
              + "'. Remaining quantity: "
              + remainingQuantity + ".";
    }
  }
}