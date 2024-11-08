package org.ardeu.librarymanagementsystem.ui.viewcontrollers.revenue;

import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.ardeu.librarymanagementsystem.domain.controllers.LoanController;
import org.ardeu.librarymanagementsystem.domain.entities.loan.Loan;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

/**
 * RevenueViewController is responsible for managing and displaying revenue-related analytics in the application.
 */
public class RevenueViewController {

    private final LoanController loanController;
    private final ObservableMap<UUID, Loan> loans;
    private final double[] monthlyRevenueFromCurrentYear;
    private final Map<Integer, Double> yearlyRevenue;

    @FXML
    public Label allTimeRevenueLabel;

    @FXML
    public Label last30DaysRevenueLabel;

    @FXML
    public Label pastYearRevenueLabel;

    @FXML
    public GridPane otherRevenueAnalyticsGrid;

    @FXML
    public Label revenuePerMonthLabel;

    /**
     * Constructs a RevenueViewController and initializes the necessary data structures.
     */
    public RevenueViewController() {
        this.loanController = new LoanController();
        this.loans = this.loanController.getAllLoans().getData();
        this.monthlyRevenueFromCurrentYear = new double[12];
        this.yearlyRevenue = new TreeMap<>();
    }

    /**
     * Initializes the controller and sets up the revenue data.
     */
    @FXML
    public void initialize() {
        setUpRevenueData();
        this.loans.addListener((MapChangeListener<UUID, Loan>) _ -> setUpRevenueData());
    }

    /**
     * Sets up the revenue data by updating the labels and charts.
     */
    private void setUpRevenueData() {
        setUpRevenueLabels();
        setUpRevenuePerMonth();
        setUpYearlyRevenue();
    }

    /**
     * Sets up the yearly revenue data and updates the yearly revenue bar chart.
     */
    private void setUpYearlyRevenue() {
        this.yearlyRevenue.clear();
        this.yearlyRevenue.putAll(this.loanController.getRevenuePerYear().getData());
        otherRevenueAnalyticsGrid.getChildren().removeIf(node -> {
            Integer columnIndex = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);
            return columnIndex != null && rowIndex != null && columnIndex == 0 && rowIndex == 1;
        });
        otherRevenueAnalyticsGrid.add(setUpYearlyRevenueBarChart(), 0, 1);
    }

    /**
     * Creates and returns a bar chart displaying the yearly revenue.
     *
     * @return a VBox containing the yearly revenue bar chart
     */
    private Node setUpYearlyRevenueBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Year");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Revenue");

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (Map.Entry<Integer, Double> entry : this.yearlyRevenue.entrySet()) {
            if (entry.getKey() >= LocalDate.now().getYear() - 4) {
                xAxis.getCategories().add(entry.getKey().toString());
                series.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
            }
        }

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.getData().add(series);
        barChart.setLegendVisible(false);

        VBox vBox = new VBox(barChart);
        VBox.setVgrow(vBox, Priority.ALWAYS);
        GridPane.setValignment(vBox, VPos.TOP);

        return vBox;
    }

    /**
     * Sets up the monthly revenue data and updates the monthly revenue grid.
     */
    private void setUpRevenuePerMonth() {
        List<Loan> loans  = this.loanController.getLoansFrom(LocalDate.now().getYear()).getData();
        Arrays.fill(this.monthlyRevenueFromCurrentYear, 0);
        for (Loan loan : loans) {
            int month = loan.getLoanDate().getMonthValue();
            this.monthlyRevenueFromCurrentYear[month - 1] += loan.getPrice();
        }
        otherRevenueAnalyticsGrid.getChildren().removeIf(node -> {
            Integer columnIndex = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);
            return columnIndex != null && rowIndex != null && columnIndex == 1 && rowIndex == 1;
        });
        otherRevenueAnalyticsGrid.add(setUpRevenuePerMonthGrid(), 1, 1);
    }

    /**
     * Creates and returns a grid displaying the monthly revenue.
     *
     * @return a VBox containing the monthly revenue grid
     */
    private Node setUpRevenuePerMonthGrid() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);

        for (int i = 0; i < 12; i++) {
            Label monthLabel = new Label(
                    LocalDate.of(LocalDate.now().getYear(), i + 1, 1)
                            .getMonth()
                            .getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ro-RO")).toUpperCase());
            Label revenueLabel = new Label(String.format("%.2f RON", this.monthlyRevenueFromCurrentYear[i]));

            gridPane.add(monthLabel, 0, i);
            gridPane.add(revenueLabel, 1, i);

            GridPane.setValignment(monthLabel, VPos.TOP);
            GridPane.setValignment(revenueLabel, VPos.TOP);
        }

        VBox vBox = new VBox(gridPane);
        VBox.setVgrow(vBox, Priority.ALWAYS);
        GridPane.setValignment(vBox, VPos.TOP);
        gridPane.setAlignment(Pos.CENTER);
        return vBox;
    }

    /**
     * Sets up the revenue labels with the latest revenue data.
     */
    private void setUpRevenueLabels() {
        this.revenuePerMonthLabel.setText("Revenue per month for the year " + LocalDate.now().getYear());
        this.allTimeRevenueLabel.setText(String.format("%.2f RON", this.loanController.getAllTimeRevenue().getData()));
        this.last30DaysRevenueLabel.setText(String.format("%.2f RON", this.loanController.getRevenueForLast30Days().getData()));
        this.pastYearRevenueLabel.setText(String.format("%.2f RON", this.loanController.getRevenueForPastYear().getData()));
    }
}
