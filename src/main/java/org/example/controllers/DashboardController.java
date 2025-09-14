package org.example.controllers;

import org.example.views.DashboardView;

/**
 * Controller for the dashboard view
 */
public class DashboardController {
    
    private final DashboardView view;
    
    public DashboardController(DashboardView view) {
        this.view = view;
        initialize();
    }
    
    private void initialize() {
        // Setup any dashboard-specific event handlers here
        // For now, the dashboard is mostly static with sample data
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
