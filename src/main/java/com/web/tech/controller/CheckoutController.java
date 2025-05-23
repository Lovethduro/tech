
package com.web.tech.controller;

import com.web.tech.model.Cart;
import com.web.tech.model.Orders;
import com.web.tech.model.User;
import com.web.tech.service.CartService;
import com.web.tech.service.OrderService;
import com.web.tech.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CheckoutController {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthenticated access to checkout");
            return "redirect:/login";
        }

        String email = authentication.getName().toLowerCase();
        User user = userService.findByEmail(email);
        if (user == null) {
            logger.warn("User not found for email: {}", email);
            return "redirect:/login";
        }

        Cart cart = cartService.getOrCreateCart(user.getId());
        model.addAttribute("cart", cart);
        model.addAttribute("cartCount", cartService.getCartCount(user.getId()));
        model.addAttribute("shippingAddress", user.getAddress() != null ? user.getAddress() : "");
        return "admin/user/checkout";
    }

    @PostMapping("/checkout")
    public String processCheckout(
            @RequestParam("shippingAddress") String shippingAddress,
            @RequestParam("paymentMethod") String paymentMethod,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        logger.debug("Processing checkout for user: {}", authentication.getName());

        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthenticated checkout attempt");
            redirectAttributes.addFlashAttribute("errorMessage", "Please log in to checkout");
            return "redirect:/login";
        }

        String email = authentication.getName().toLowerCase();
        User user = userService.findByEmail(email);
        if (user == null) {
            logger.warn("User not found for email: {}", email);
            redirectAttributes.addFlashAttribute("errorMessage", "User not found");
            return "redirect:/login";
        }

        Cart cart = cartService.getOrCreateCart(user.getId());
        if (cart == null || cart.getItems().isEmpty()) {
            logger.warn("Empty cart for user: {}", user.getId());
            redirectAttributes.addFlashAttribute("errorMessage", "Your cart is empty");
            return "redirect:admin/user/cart";
        }

        try {
            if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
                logger.error("Invalid shipping address for user: {}", user.getId());
                redirectAttributes.addFlashAttribute("errorMessage", "Please enter a valid shipping address");
                return "redirect:admin/user/checkout";
            }

            if (shippingAddress.length() < 10) {
                logger.error("Shipping address too short for user: {}", user.getId());
                redirectAttributes.addFlashAttribute("errorMessage", "Shipping address must be at least 10 characters");
                return "redirect:admin/user/checkout";
            }

            Orders order = orderService.createOrder(user.getId(), cart, shippingAddress, paymentMethod);
            cartService.clearCart(user.getId());
            logger.info("Order placed successfully for user: {}", user.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Order placed successfully!");
            return "redirect:admin/user/order-confirmation";
        } catch (IllegalArgumentException e) {
            logger.error("Checkout error for user: {}: {}", user.getId(), e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:admin/user/checkout";
        } catch (Exception e) {
            logger.error("Unexpected error during checkout for user: {}: {}", user.getId(), e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to place order: " + e.getMessage());
            return "redirect:admin/user/checkout";
        }
    }
}
