package org.example.utils;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

/**
 * Utility class for creating smooth animations and transitions
 */
public class AnimationUtils {
    
    private static final Duration DEFAULT_DURATION = Duration.millis(300);
    private static final Duration FAST_DURATION = Duration.millis(200);
    private static final Duration SLOW_DURATION = Duration.millis(500);
    
    /**
     * Fade in animation
     */
    public static FadeTransition fadeIn(Node node, Duration duration) {
        FadeTransition fade = new FadeTransition(duration, node);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        return fade;
    }
    
    public static FadeTransition fadeIn(Node node) {
        return fadeIn(node, DEFAULT_DURATION);
    }
    
    /**
     * Fade out animation
     */
    public static FadeTransition fadeOut(Node node, Duration duration) {
        FadeTransition fade = new FadeTransition(duration, node);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        return fade;
    }
    
    public static FadeTransition fadeOut(Node node) {
        return fadeOut(node, DEFAULT_DURATION);
    }
    
    /**
     * Slide in from left
     */
    public static TranslateTransition slideInLeft(Node node, Duration duration) {
        TranslateTransition slide = new TranslateTransition(duration, node);
        slide.setFromX(-node.getBoundsInParent().getWidth());
        slide.setToX(0);
        slide.setInterpolator(Interpolator.EASE_OUT);
        return slide;
    }
    
    public static TranslateTransition slideInLeft(Node node) {
        return slideInLeft(node, DEFAULT_DURATION);
    }
    
    /**
     * Slide in from right
     */
    public static TranslateTransition slideInRight(Node node, Duration duration) {
        TranslateTransition slide = new TranslateTransition(duration, node);
        slide.setFromX(node.getBoundsInParent().getWidth());
        slide.setToX(0);
        slide.setInterpolator(Interpolator.EASE_OUT);
        return slide;
    }
    
    public static TranslateTransition slideInRight(Node node) {
        return slideInRight(node, DEFAULT_DURATION);
    }
    
    /**
     * Slide up animation
     */
    public static TranslateTransition slideUp(Node node, Duration duration) {
        TranslateTransition slide = new TranslateTransition(duration, node);
        slide.setFromY(50);
        slide.setToY(0);
        slide.setInterpolator(Interpolator.EASE_OUT);
        return slide;
    }
    
    public static TranslateTransition slideUp(Node node) {
        return slideUp(node, DEFAULT_DURATION);
    }
    
    /**
     * Scale animation (zoom in/out)
     */
    public static ScaleTransition scaleIn(Node node, Duration duration) {
        ScaleTransition scale = new ScaleTransition(duration, node);
        scale.setFromX(0.8);
        scale.setFromY(0.8);
        scale.setToX(1.0);
        scale.setToY(1.0);
        scale.setInterpolator(Interpolator.EASE_OUT);
        return scale;
    }
    
    public static ScaleTransition scaleIn(Node node) {
        return scaleIn(node, DEFAULT_DURATION);
    }
    
    /**
     * Hover scale effect
     */
    public static void addHoverScaleEffect(Node node, double scale) {
        Scale scaleTransform = new Scale(1.0, 1.0);
        node.getTransforms().add(scaleTransform);
        
        node.setOnMouseEntered(e -> {
            ScaleTransition scaleUp = new ScaleTransition(FAST_DURATION, node);
            scaleUp.setToX(scale);
            scaleUp.setToY(scale);
            scaleUp.setInterpolator(Interpolator.EASE_OUT);
            scaleUp.play();
        });
        
        node.setOnMouseExited(e -> {
            ScaleTransition scaleDown = new ScaleTransition(FAST_DURATION, node);
            scaleDown.setToX(1.0);
            scaleDown.setToY(1.0);
            scaleDown.setInterpolator(Interpolator.EASE_OUT);
            scaleDown.play();
        });
    }
    
    public static void addHoverScaleEffect(Node node) {
        addHoverScaleEffect(node, 1.05);
    }
    
    /**
     * Rotation animation
     */
    public static RotateTransition rotate(Node node, double angle, Duration duration) {
        RotateTransition rotate = new RotateTransition(duration, node);
        rotate.setFromAngle(0);
        rotate.setToAngle(angle);
        rotate.setInterpolator(Interpolator.EASE_OUT);
        return rotate;
    }
    
    /**
     * Hover rotation effect
     */
    public static void addHoverRotateEffect(Node node, double angle) {
        node.setOnMouseEntered(e -> {
            RotateTransition rotateIn = rotate(node, angle, FAST_DURATION);
            rotateIn.play();
        });
        
        node.setOnMouseExited(e -> {
            RotateTransition rotateOut = rotate(node, 0, FAST_DURATION);
            rotateOut.play();
        });
    }
    
    /**
     * Pulse animation (for notifications or attention)
     */
    public static Timeline pulse(Node node, int cycles) {
        Timeline pulse = new Timeline();
        pulse.setCycleCount(cycles);
        pulse.setAutoReverse(true);
        
        KeyFrame keyFrame = new KeyFrame(Duration.millis(200),
            new KeyValue(node.scaleXProperty(), 1.1),
            new KeyValue(node.scaleYProperty(), 1.1)
        );
        pulse.getKeyFrames().add(keyFrame);
        
        return pulse;
    }
    
    /**
     * Blur effect animation
     */
    public static Timeline blurIn(Node node, double radius, Duration duration) {
        GaussianBlur blur = new GaussianBlur(0);
        node.setEffect(blur);
        
        Timeline blurTimeline = new Timeline();
        blurTimeline.getKeyFrames().add(
            new KeyFrame(duration, new KeyValue(blur.radiusProperty(), radius))
        );
        
        return blurTimeline;
    }
    
    public static Timeline blurOut(Node node, Duration duration) {
        GaussianBlur blur = (GaussianBlur) node.getEffect();
        if (blur == null) return new Timeline();
        
        Timeline blurTimeline = new Timeline();
        blurTimeline.getKeyFrames().add(
            new KeyFrame(duration, new KeyValue(blur.radiusProperty(), 0))
        );
        
        blurTimeline.setOnFinished(e -> node.setEffect(null));
        
        return blurTimeline;
    }
    
    /**
     * Combined entrance animation (fade + slide + scale)
     */
    public static ParallelTransition entranceAnimation(Node node) {
        FadeTransition fade = fadeIn(node, SLOW_DURATION);
        TranslateTransition slide = slideUp(node, SLOW_DURATION);
        ScaleTransition scale = scaleIn(node, SLOW_DURATION);
        
        return new ParallelTransition(fade, slide, scale);
    }
    
    /**
     * Staggered animation for multiple nodes
     */
    public static SequentialTransition staggeredEntrance(Node... nodes) {
        SequentialTransition sequence = new SequentialTransition();
        
        for (int i = 0; i < nodes.length; i++) {
            ParallelTransition entrance = entranceAnimation(nodes[i]);
            if (i > 0) {
                // Add small delay between animations
                PauseTransition pause = new PauseTransition(Duration.millis(100));
                sequence.getChildren().addAll(pause, entrance);
            } else {
                sequence.getChildren().add(entrance);
            }
        }
        
        return sequence;
    }
}
