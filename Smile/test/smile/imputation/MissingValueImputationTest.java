/******************************************************************************
 *                   Confidential Proprietary                                 *
 *         (c) Copyright Haifeng Li 2011, All Rights Reserved                 *
 ******************************************************************************/

package smile.imputation;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import smile.data.AttributeDataset;
import smile.data.parser.ArffParser;
import smile.data.parser.DelimitedTextParser;
import smile.math.Math;

/**
 *
 * @author Haifeng Li
 */
public class MissingValueImputationTest {

    ArffParser arffParser = new ArffParser();
    DelimitedTextParser csvParser = new DelimitedTextParser();
    AttributeDataset movement;
    AttributeDataset control;
    AttributeDataset segment;
    AttributeDataset iris;
    AttributeDataset soybean;

    public MissingValueImputationTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        try {
            arffParser.setResponseIndex(4);
            iris = arffParser.parse(this.getClass().getResourceAsStream("/smile/data/weka/iris.arff"));

            arffParser.setResponseIndex(35);
            soybean = arffParser.parse(this.getClass().getResourceAsStream("/smile/data/weka/soybean.arff"));

            arffParser.setResponseIndex(19);
            segment = arffParser.parse(this.getClass().getResourceAsStream("/smile/data/weka/segment-challenge.arff"));

            csvParser.setDelimiter(",");
            movement = csvParser.parse("Movement", this.getClass().getResourceAsStream("/smile/data/uci/movement_libras.data"));

            csvParser.setDelimiter(" +");
            control = csvParser.parse("Control", this.getClass().getResourceAsStream("/smile/data/uci/synthetic_control.data"));
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    @After
    public void tearDown() {
    }

    private double impute(AttributeDataset dataset, MissingValueImputation imputation, double rate) throws Exception {

        int n = 0;
        double[][] data = dataset.toArray(new double[dataset.size()][]);
        double[][] dat = new double[data.length][data[0].length];
        for (int i = 0; i < dat.length; i++) {
            for (int j = 0; j < dat[i].length; j++) {
                if (Math.random() < rate) {
                    n++;
                    dat[i][j] = Double.NaN;
                } else {
                    dat[i][j] = data[i][j];                    
                }
            }
        }

        imputation.impute(dat);

        double rmse = 0.0;
        for (int i = 0; i < dat.length; i++) {
            for (int j = 0; j < dat[i].length; j++) {
                double d = (data[i][j] - dat[i][j]);
                rmse += d * d;
            }
        }

        rmse = Math.sqrt(rmse / n);
        return rmse;
    }

    void impute(AttributeDataset data) throws Exception {
        int p = data.attributes().length;        
        System.out.println("----------- " + data.getName() + " ----------------");
        System.out.println("----------- " + data.size() + " x " + p + " ----------------");
        System.out.println("MeanImputation");
        MissingValueImputation instance = new AverageImputation();
        System.out.println("RMSE of 1% missing values = " + impute(data, instance, 0.01));
        System.out.println("RMSE of 5% missing values = " + impute(data, instance, 0.05));
        System.out.println("RMSE of 10% missing values = " + impute(data, instance, 0.10));
        System.out.println("RMSE of 15% missing values = " + impute(data, instance, 0.15));
        System.out.println("RMSE of 20% missing values = " + impute(data, instance, 0.20));
        System.out.println("RMSE of 25% missing values = " + impute(data, instance, 0.25));

        System.out.println("KMeansImputation");
        instance = new KMeansImputation(10, 5);
        System.out.println("RMSE of 1% missing values = " + impute(data, instance, 0.01));
        System.out.println("RMSE of 5% missing values = " + impute(data, instance, 0.05));
        System.out.println("RMSE of 10% missing values = " + impute(data, instance, 0.10));
        System.out.println("RMSE of 15% missing values = " + impute(data, instance, 0.15));
        System.out.println("RMSE of 20% missing values = " + impute(data, instance, 0.20));
        System.out.println("RMSE of 25% missing values = " + impute(data, instance, 0.25));

        System.out.println("KNNImputation");
        instance = new KNNImputation(10);
        System.out.println("RMSE of 1% missing values = " + impute(data, instance, 0.01));
        System.out.println("RMSE of 5% missing values = " + impute(data, instance, 0.05));
        System.out.println("RMSE of 10% missing values = " + impute(data, instance, 0.10));
        System.out.println("RMSE of 15% missing values = " + impute(data, instance, 0.15));
        System.out.println("RMSE of 20% missing values = " + impute(data, instance, 0.20));
        System.out.println("RMSE of 25% missing values = " + impute(data, instance, 0.25));

        if (p > 15) {
            System.out.println("SVDImputation");
            instance = new SVDImputation(p / 5);
            System.out.println("RMSE of 1% missing values = " + impute(data, instance, 0.01));
            System.out.println("RMSE of 5% missing values = " + impute(data, instance, 0.05));
            System.out.println("RMSE of 10% missing values = " + impute(data, instance, 0.10));
            // Matrix will be rank deficient.
            // System.out.println("RMSE of 15% missing values = " + impute(data, instance, 0.15));
            // System.out.println("RMSE of 20% missing values = " + impute(data, instance, 0.20));
            // System.out.println("RMSE of 25% missing values = " + impute(data, instance, 0.25));
        }

        if (p > 15) {
            System.out.println("LLSImputation");
            instance = new LLSImputation(10);
            System.out.println("RMSE of 1% missing values = " + impute(data, instance, 0.01));
            System.out.println("RMSE of 5% missing values = " + impute(data, instance, 0.05));
            System.out.println("RMSE of 10% missing values = " + impute(data, instance, 0.10));
            System.out.println("RMSE of 15% missing values = " + impute(data, instance, 0.15));
            System.out.println("RMSE of 20% missing values = " + impute(data, instance, 0.20));
            System.out.println("RMSE of 25% missing values = " + impute(data, instance, 0.25));
        }
    }

    /**
     * Test of impute method.
     */
    @Test
    public void testImpute() throws Exception {
        impute(segment);
        impute(movement);
        impute(control);
    }
}