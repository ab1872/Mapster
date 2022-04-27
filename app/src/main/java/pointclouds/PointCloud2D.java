package pointclouds;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import mapdisplayutil.PathList;

public class PointCloud2D
{
    public double[] meshGeneration()
    {
        double[] answer = new double[];
        // Get max bounds.
        // Recurse on max bounds.
        // Fuse end points.
        return answer;
    }
    private void meshGenRecurse(double[] xCoord, double[] yCoord,
                                double[] sX, double[] sY, double[] eX, double[] eY)
    {
        double xMin = xCoord[0]; double xMax = xCoord[0];
        double yMin = yCoord[0]; double yMax = yCoord[0];
        double startingX; double endingX;
        for(int i=1;i<xCoord.length;++i)
        {
            if(xCoord[i]>xMax)
            {
                xMax = xCoord[i];
            }
        }
        for(int i=1;i<yCoord.length;++i)
        {
            if(yCoord[i]>yMax)
            {
                yMax = yCoord[i];
            }
        }
        for(int i=1;i<yCoord.length;++i)
        {
            if(yCoord[i]<yMin)
            {
                yMin = yCoord[i];
            }
        }
        for(int i=1;i<xCoord.length;++i)
        {
            if(xCoord[i]<xMin)
            {
                xMin = xCoord[i];
            }
        }
        double[] box = {xMin,yMin,xMax,yMax};
        startingX = box[0];
        endingX = box[2];
        double initialEndPtDistance = Math.hypot(startingX,endingX);
        if((startingX-endingX)<(0.01*initialEndPtDistance))
        {
            return;
        }
        LinearRegression regression = new LinearRegression(xCoord, yCoord);

        if(regression.R2()<0.7)
        {

        }

        // Divide in 4
        // Linear regression
        // Score > threshold?
        // Make Segment
        /*  Subdivide the area in question in 4
            For each area:
            Perform least squares regression on the points
            If the coefficient < threshold then make a segment. */

    }
    // Apache simple linear regression Least Squares Method.
    // remake point cloud (extrapolate: Y/N)

    private void fuseEndpoints()
    {
        // If any two endpoints are close together, fuse them together.
        // Segments into a LinePath.
    }

    // Match with floor: YES/NO, offset in real pixels.




    /**
     *  The {@code LinearRegression} class performs a simple linear regression
     *  on an set of <em>n</em> data points (<em>y<sub>i</sub></em>, <em>x<sub>i</sub></em>).
     *  That is, it fits a straight line <em>y</em> = &alpha; + &beta; <em>x</em>,
     *  (where <em>y</em> is the response variable, <em>x</em> is the predictor variable,
     *  &alpha; is the <em>y-intercept</em>, and &beta; is the <em>slope</em>)
     *  that minimizes the sum of squared residuals of the linear regression model.
     *  It also computes associated statistics, including the coefficient of
     *  determination <em>R</em><sup>2</sup> and the standard deviation of the
     *  estimates for the slope and <em>y</em>-intercept.
     *
     *  @author Robert Sedgewick
     *  @author Kevin Wayne
     */
    public class LinearRegression {
        private final double intercept, slope;
        private final double r2;
        private final double svar0, svar1;

        /**
         * Performs a linear regression on the data points {@code (y[i], x[i])}.
         *
         * @param  x the values of the predictor variable
         * @param  y the corresponding values of the response variable
         * @throws IllegalArgumentException if the lengths of the two arrays are not equal
         */
        public LinearRegression(double[] x, double[] y) {
            if (x.length != y.length) {
                throw new IllegalArgumentException("array lengths are not equal");
            }
            int n = x.length;

            // first pass
            double sumx = 0.0, sumy = 0.0, sumx2 = 0.0;
            for (int i = 0; i < n; i++) {
                sumx  += x[i];
                sumx2 += x[i]*x[i];
                sumy  += y[i];
            }
            double xbar = sumx / n;
            double ybar = sumy / n;

            // second pass: compute summary statistics
            double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
            for (int i = 0; i < n; i++) {
                xxbar += (x[i] - xbar) * (x[i] - xbar);
                yybar += (y[i] - ybar) * (y[i] - ybar);
                xybar += (x[i] - xbar) * (y[i] - ybar);
            }
            slope  = xybar / xxbar;
            intercept = ybar - slope * xbar;

            // more statistical analysis
            double rss = 0.0;      // residual sum of squares
            double ssr = 0.0;      // regression sum of squares
            for (int i = 0; i < n; i++) {
                double fit = slope*x[i] + intercept;
                rss += (fit - y[i]) * (fit - y[i]);
                ssr += (fit - ybar) * (fit - ybar);
            }

            int degreesOfFreedom = n-2;
            r2    = ssr / yybar;
            double svar  = rss / degreesOfFreedom;
            svar1 = svar / xxbar;
            svar0 = svar/n + xbar*xbar*svar1;
        }

        /**
         * Returns the <em>y</em>-intercept &alpha; of the best of the best-fit line <em>y</em> = &alpha; + &beta; <em>x</em>.
         *
         * @return the <em>y</em>-intercept &alpha; of the best-fit line <em>y = &alpha; + &beta; x</em>
         */
        public double intercept() {
            return intercept;
        }

        /**
         * Returns the slope &beta; of the best of the best-fit line <em>y</em> = &alpha; + &beta; <em>x</em>.
         *
         * @return the slope &beta; of the best-fit line <em>y</em> = &alpha; + &beta; <em>x</em>
         */
        public double slope() {
            return slope;
        }

        /**
         * Returns the coefficient of determination <em>R</em><sup>2</sup>.
         *
         * @return the coefficient of determination <em>R</em><sup>2</sup>,
         *         which is a real number between 0 and 1
         */
        public double R2() {
            return r2;
        }

        /**
         * Returns the standard error of the estimate for the intercept.
         *
         * @return the standard error of the estimate for the intercept
         */
        public double interceptStdErr() {
            return Math.sqrt(svar0);
        }

        /**
         * Returns the standard error of the estimate for the slope.
         *
         * @return the standard error of the estimate for the slope
         */
        public double slopeStdErr() {
            return Math.sqrt(svar1);
        }

        /**
         * Returns the expected response {@code y} given the value of the predictor
         * variable {@code x}.
         *
         * @param  x the value of the predictor variable
         * @return the expected response {@code y} given the value of the predictor
         *         variable {@code x}
         */
        public double predict(double x) {
            return slope*x + intercept;
        }

        /**
         * Returns a string representation of the simple linear regression model.
         *
         * @return a string representation of the simple linear regression model,
         *         including the best-fit line and the coefficient of determination
         *         <em>R</em><sup>2</sup>
         */
        public String toString() {
            StringBuilder s = new StringBuilder();
            s.append(String.format("%.2f n + %.2f", slope(), intercept()));
            s.append("  (R^2 = " + String.format("%.3f", R2()) + ")");
            return s.toString();
        }

    }
}