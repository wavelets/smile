/******************************************************************************
 *                   Confidential Proprietary                                 *
 *         (c) Copyright Haifeng Li 2011, All Rights Reserved                 *
 ******************************************************************************/

package smile.math.distance;

import java.util.Iterator;
import smile.math.SparseArray;

/**
 * Chebyshev distance (or Tchebychev distance), or L<sub>&infin;</sub> metric
 * is a metric defined on a vector space where the distance between two vectors
 * is the greatest of their differences along any coordinate dimension.
 * 
 * @author Haifeng Li
 */
public class SparseChebyshevDistance implements Metric<SparseArray> {

    /**
     * Constructor.
     */
    public SparseChebyshevDistance() {
    }

    @Override
    public String toString() {
        return "Chebyshev distance";
    }

    @Override
    public double d(SparseArray x, SparseArray y) {
        if (x.isEmpty()) {
            throw new IllegalArgumentException("List x is empty.");
        }

        if (y.isEmpty()) {
            throw new IllegalArgumentException("List y is empty.");
        }

        Iterator<SparseArray.Entry> iterX = x.iterator();
        Iterator<SparseArray.Entry> iterY = y.iterator();

        SparseArray.Entry a = iterX.hasNext() ? iterX.next() : null;
        SparseArray.Entry b = iterY.hasNext() ? iterY.next() : null;

        double dist = 0.0;

        while (a != null && b != null) {
            if (a.i < b.i) {
                double d = Math.abs(a.x);
                if (dist < d)
                    dist = d;

                a = iterX.hasNext() ? iterX.next() : null;
            } else if (a.i > b.i) {
                double d = Math.abs(b.x);
                if (dist < d)
                    dist = d;

                b = iterY.hasNext() ? iterY.next() : null;
            } else {
                double d = Math.abs(a.x - b.x);
                if (dist < d)
                    dist = d;

                a = iterX.hasNext() ? iterX.next() : null;
                b = iterY.hasNext() ? iterY.next() : null;
            }
        }

        while (a != null) {
            double d = Math.abs(a.x);
            if (dist < d)
                dist = d;

            a = iterX.hasNext() ? iterX.next() : null;
        }

        while (b != null) {
            double d = Math.abs(b.x);
            if (dist < d)
                dist = d;

            b = iterY.hasNext() ? iterY.next() : null;
        }

        return Math.sqrt(dist);
    }
}
