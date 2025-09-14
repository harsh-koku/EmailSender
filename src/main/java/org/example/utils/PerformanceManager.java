package org.example.utils;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Performance manager for optimizing JavaFX application performance
 */
public class PerformanceManager {
    
    private static PerformanceManager instance;
    private final ConcurrentHashMap<Node, AnimationTimer> activeAnimations = new ConcurrentHashMap<>();
    private final List<Runnable> deferredTasks = new ArrayList<>();
    private long lastFrameTime = 0;
    private int frameCount = 0;
    private double averageFPS = 60.0;
    
    // Performance settings
    private boolean enableGpuAcceleration = true;
    private boolean enableAnimationThrottling = true;
    private boolean enableMemoryOptimizations = true;
    private int maxConcurrentAnimations = 10;
    
    private PerformanceManager() {
        setupPerformanceMonitoring();
    }
    
    public static PerformanceManager getInstance() {
        if (instance == null) {
            instance = new PerformanceManager();
        }
        return instance;
    }
    
    /**
     * Setup performance monitoring
     */
    private void setupPerformanceMonitoring() {
        AnimationTimer perfTimer = new AnimationTimer() {
            @Override
            public void handle(long currentTime) {
                if (lastFrameTime == 0) {
                    lastFrameTime = currentTime;
                    return;
                }
                
                frameCount++;
                
                // Update FPS every second
                if (currentTime - lastFrameTime >= 1_000_000_000L) {
                    averageFPS = frameCount;
                    frameCount = 0;
                    lastFrameTime = currentTime;
                    
                    // Auto-adjust performance settings based on FPS
                    adjustPerformanceSettings();
                    
                    // Process deferred tasks during idle time
                    processDeferredTasks();
                }
            }
        };
        perfTimer.start();
    }
    
    /**
     * Auto-adjust performance settings based on current FPS
     */
    private void adjustPerformanceSettings() {
        if (averageFPS < 30) {
            // Performance is poor, enable aggressive optimizations
            enableAnimationThrottling = true;
            maxConcurrentAnimations = 5;
            cleanupUnusedResources();
        } else if (averageFPS > 50) {
            // Performance is good, can be more lenient
            maxConcurrentAnimations = 15;
        }
    }
    
    /**
     * Register an animation for monitoring
     */
    public void registerAnimation(Node node, AnimationTimer animation) {
        if (activeAnimations.size() >= maxConcurrentAnimations && enableAnimationThrottling) {
            // Defer animation if too many are running
            deferredTasks.add(() -> {
                activeAnimations.put(node, animation);
                animation.start();
            });
        } else {
            activeAnimations.put(node, animation);
            animation.start();
        }
    }
    
    /**
     * Unregister an animation
     */
    public void unregisterAnimation(Node node) {
        AnimationTimer animation = activeAnimations.remove(node);
        if (animation != null) {
            animation.stop();
        }
    }
    
    /**
     * Process deferred tasks during idle time
     */
    private void processDeferredTasks() {
        if (!deferredTasks.isEmpty() && activeAnimations.size() < maxConcurrentAnimations) {
            int tasksToProcess = Math.min(3, deferredTasks.size());
            for (int i = 0; i < tasksToProcess; i++) {
                Runnable task = deferredTasks.remove(0);
                task.run();
            }
        }
    }
    
    /**
     * Optimize node for better performance
     */
    public void optimizeNode(Node node) {
        if (enableGpuAcceleration) {
            // Enable GPU acceleration hints
            node.getStyleClass().add("gpu-accelerated");
            node.setCache(true);
            node.setCacheHint(javafx.scene.CacheHint.SPEED);
        }
        
        if (enableMemoryOptimizations) {
            // Set up automatic cleanup for nodes that go out of view
            node.visibleProperty().addListener((obs, oldVisible, newVisible) -> {
                if (!newVisible) {
                    // Clean up resources when node is not visible
                    cleanupNodeResources(node);
                }
            });
        }
    }
    
    /**
     * Clean up resources for a specific node
     */
    private void cleanupNodeResources(Node node) {
        // Stop any animations for this node
        unregisterAnimation(node);
        
        // Clear cache if enabled
        if (node.isCache()) {
            node.setCache(false);
            node.setCache(true);
        }
    }
    
    /**
     * Clean up unused resources
     */
    private void cleanupUnusedResources() {
        // Remove inactive animations
        activeAnimations.entrySet().removeIf(entry -> {
            Node node = entry.getKey();
            return !node.isVisible() || node.getParent() == null;
        });
        
        // Suggest garbage collection if memory is low
        if (isMemoryLow()) {
            System.gc();
        }
    }
    
    /**
     * Check if system memory is low
     */
    private boolean isMemoryLow() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        double memoryUsagePercent = (double) usedMemory / maxMemory;
        return memoryUsagePercent > 0.8; // 80% threshold
    }
    
    /**
     * Throttle animation based on current performance
     */
    public boolean shouldThrottleAnimation() {
        return enableAnimationThrottling && 
               (activeAnimations.size() >= maxConcurrentAnimations || averageFPS < 40);
    }
    
    /**
     * Get current FPS for debugging
     */
    public double getCurrentFPS() {
        return averageFPS;
    }
    
    /**
     * Get performance statistics
     */
    public String getPerformanceStats() {
        return String.format(
            "FPS: %.1f | Active Animations: %d | Deferred Tasks: %d | Memory Usage: %.1f%%",
            averageFPS,
            activeAnimations.size(),
            deferredTasks.size(),
            getMemoryUsagePercent()
        );
    }
    
    private double getMemoryUsagePercent() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        return (double) usedMemory / maxMemory * 100;
    }
    
    // Getters and setters for performance settings
    public boolean isGpuAccelerationEnabled() {
        return enableGpuAcceleration;
    }
    
    public void setGpuAccelerationEnabled(boolean enabled) {
        this.enableGpuAcceleration = enabled;
    }
    
    public boolean isAnimationThrottlingEnabled() {
        return enableAnimationThrottling;
    }
    
    public void setAnimationThrottlingEnabled(boolean enabled) {
        this.enableAnimationThrottling = enabled;
    }
    
    public boolean isMemoryOptimizationsEnabled() {
        return enableMemoryOptimizations;
    }
    
    public void setMemoryOptimizationsEnabled(boolean enabled) {
        this.enableMemoryOptimizations = enabled;
    }
    
    public int getMaxConcurrentAnimations() {
        return maxConcurrentAnimations;
    }
    
    public void setMaxConcurrentAnimations(int max) {
        this.maxConcurrentAnimations = Math.max(1, max);
    }
    
    /**
     * Shutdown and cleanup all resources
     */
    public void shutdown() {
        activeAnimations.values().forEach(AnimationTimer::stop);
        activeAnimations.clear();
        deferredTasks.clear();
    }
}
