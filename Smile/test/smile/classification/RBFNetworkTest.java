/******************************************************************************
 *                   Confidential Proprietary                                 *
 *         (c) Copyright Haifeng Li 2011, All Rights Reserved                 *
 ******************************************************************************/

package smile.classification;

import smile.math.distance.EuclideanDistance;
import smile.math.rbf.RadialBasisFunction;
import smile.data.NominalAttribute;
import smile.data.parser.DelimitedTextParser;
import smile.data.AttributeDataset;
import smile.data.parser.ArffParser;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import smile.math.Math;
import smile.math.rbf.GaussianRadialBasis;
import smile.util.SmileUtils;
import smile.validation.LOOCV;
import static org.junit.Assert.*;

/**
 *
 * @author Haifeng Li
 */
@SuppressWarnings("unused")
public class RBFNetworkTest {

    public RBFNetworkTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of learn method, of class RBFNetwork.
     */
    @Test
    public void testLearn() {
        System.out.println("learn");
        ArffParser arffParser = new ArffParser();
        arffParser.setResponseIndex(4);
        try {
            AttributeDataset iris = arffParser.parse(this.getClass().getResourceAsStream("/smile/data/weka/iris.arff"));
            double[][] x = iris.toArray(new double[iris.size()][]);
            int[] y = iris.toArray(new int[iris.size()]);

            int n = x.length;
            LOOCV loocv = new LOOCV(n);
            int error = 0;
            for (int i = 0; i < n; i++) {
                double[][] trainx = Math.slice(x, loocv.train[i]);
                int[] trainy = Math.slice(y, loocv.train[i]);

                double[][] centers = new double[10][];
                RadialBasisFunction[] basis = SmileUtils.learnGaussianRadialBasis(trainx, centers, 5.0);
                RBFNetwork<double[]> rbf = new RBFNetwork<double[]>(trainx, trainy, new EuclideanDistance(), basis, centers);

                if (y[loocv.test[i]] != rbf.predict(x[loocv.test[i]]))
                    error++;
            }

            System.out.println("RBF network error = " + error);
            assertTrue(error <= 6);
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    /**
     * Test of learn method, of class RBFNetwork.
     */
    @Test
    public void testSegment() {
        System.out.println("Segment");
        ArffParser parser = new ArffParser();
        parser.setResponseIndex(19);
        try {
            AttributeDataset train = parser.parse(this.getClass().getResourceAsStream("/smile/data/weka/segment-challenge.arff"));
            AttributeDataset test = parser.parse(this.getClass().getResourceAsStream("/smile/data/weka/segment-test.arff"));

            double[][] x = train.toArray(new double[0][]);
            int[] y = train.toArray(new int[0]);
            double[][] testx = test.toArray(new double[0][]);
            int[] testy = test.toArray(new int[0]);
            
            double[][] centers = new double[100][];
            RadialBasisFunction[] basis = SmileUtils.learnGaussianRadialBasis(x, centers, 5.0);
            RBFNetwork<double[]> rbf = new RBFNetwork<double[]>(x, y, new EuclideanDistance(), basis, centers);
            
            int error = 0;
            for (int i = 0; i < testx.length; i++) {
                if (rbf.predict(testx[i]) != testy[i]) {
                    error++;
                }
            }

            System.out.format("Segment error rate = %.2f%%\n", 100.0 * error / testx.length);
            assertTrue(error <= 210);
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    /**
     * Test of learn method, of class RBFNetwork.
     */
    @Test
    public void testUSPS() {
        System.out.println("USPS");
        DelimitedTextParser parser = new DelimitedTextParser();
        parser.setResponseIndex(new NominalAttribute("class"), 0);
        try {
            AttributeDataset train = parser.parse("USPS Train", this.getClass().getResourceAsStream("/smile/data/usps/zip.train"));
            AttributeDataset test = parser.parse("USPS Test", this.getClass().getResourceAsStream("/smile/data/usps/zip.test"));

            double[][] x = train.toArray(new double[train.size()][]);
            int[] y = train.toArray(new int[train.size()]);
            double[][] testx = test.toArray(new double[test.size()][]);
            int[] testy = test.toArray(new int[test.size()]);
            
            double[][] centers = new double[200][];
            RadialBasisFunction basis = SmileUtils.learnGaussianRadialBasis(x, centers);
            RBFNetwork<double[]> rbf = new RBFNetwork<double[]>(x, y, new EuclideanDistance(), new GaussianRadialBasis(8.0), centers);
                
            int error = 0;
            for (int i = 0; i < testx.length; i++) {
                if (rbf.predict(testx[i]) != testy[i]) {
                    error++;
                }
            }

            System.out.format("USPS error rate = %.2f%%\n", 100.0 * error / testx.length);
            assertTrue(error <= 150);
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
}