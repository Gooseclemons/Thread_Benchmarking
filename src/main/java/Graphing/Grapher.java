package Graphing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Grapher {

    String jsonPath;
    JsonNode jsonFile;

    Grapher() throws IOException {
        jsonPath = new File("").getAbsolutePath();
        jsonPath = jsonPath.concat(File.separator + "jmh-result.json");
        jsonFile = new ObjectMapper().readTree(new File(jsonPath));
    }

    public static void main(String[] args) throws IOException {
        Grapher grapher = new Grapher();
        grapher.createGraph();
    }

    void createGraph() throws IOException {
        int iterations = getIterationCount();
        ArrayList<ArrayList<Double>> values = getRawData();

        XYSeries customSeries = new XYSeries("Custom Series");
        XYSeries platformSeries = new XYSeries("Platform Series");

        for (int i = 1; i <= iterations; i++) {
            customSeries.add(i, values.get(0).get(i - 1));
            platformSeries.add(i, values.get(1).get(i - 1));
        }

//        XYSeriesCollection customDataset = new XYSeriesCollection(customSeries);
//        XYSeriesCollection platformDataset = new XYSeriesCollection(platformSeries);

        XYSeriesCollection combinedDataset = new XYSeriesCollection();
        combinedDataset.addSeries(customSeries);
        combinedDataset.addSeries(platformSeries);

        JFreeChart combinedLineChart = ChartFactory.createXYLineChart("Runtime of custom vs platform RWL implementations", "Iteration", "Performance (ms/op)", combinedDataset);

        ChartUtils.saveChartAsPNG(new File("chart.png"), combinedLineChart, 800, 600);
    }

    /**
     * Iterations stays the same for every benchmark test
     */
    int getIterationCount() {
        return jsonFile.get(0).get("measurementIterations").asInt();
    }

    /**
     * Return the benchmark data for the two methods tested in the benchmark
     */
    ArrayList<ArrayList<Double>> getRawData() {
        ArrayList<ArrayList<Double>> returnData = new ArrayList<>();
        for (JsonNode benchmark : jsonFile) {
            JsonNode rawData = benchmark.get("primaryMetric").get("rawData").get(0);
            ArrayList<Double> data = new ArrayList<>();
            for (JsonNode measurement : rawData) {
                data.add(measurement.asDouble());
            }
            returnData.add(data);
        }
        return returnData;
    }

}
