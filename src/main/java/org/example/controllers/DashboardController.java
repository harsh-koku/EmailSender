package org.example.controllers;

import org.example.views.DashboardView;
import org.example.utils.AnimationUtils;

/**
 * Controller for the dashboard view
 */
public class DashboardController {
    
    private final DashboardView view;
    private Runnable navigateToCompose;
    
    public DashboardController(DashboardView view) {
        this.view = view;
        initialize();
    }
    
    public DashboardController(DashboardView view, Runnable navigateToCompose) {
        this.view = view;
        this.navigateToCompose = navigateToCompose;
        initialize();
    }
    
    private void initialize() {
        // Setup event handlers for dashboard buttons
        setupEventHandlers();
    }
    
    private void setupEventHandlers() {
        // New Email button handler
        view.getNewEmailBtn().setOnAction(e -> {
            // Add click animation
            AnimationUtils.addClickEffect(view.getNewEmailBtn()).play();
            
            // Navigate to compose if callback is available
            if (navigateToCompose != null) {
                navigateToCompose.run();
            }
        });
        
        // Refresh button handler
        view.getRefreshBtn().setOnAction(e -> {
            AnimationUtils.addClickEffect(view.getRefreshBtn()).play();
            refreshDashboardData();
        });
    }
    
    private void refreshDashboardData() {
        // Implement dashboard data refresh logic here
        // For now, just show a visual feedback
        // This could update metrics, reload charts, etc.
    }
    
    public DashboardView getView() {
        return view;
    }
    
    // Future methods for dashboard functionality:
    // - refreshData()
    // - updateMetrics()
    // - loadChartData()
    // - handleQuickActions()
}
