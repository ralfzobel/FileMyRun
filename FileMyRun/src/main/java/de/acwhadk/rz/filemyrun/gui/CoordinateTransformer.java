package de.acwhadk.rz.filemyrun.gui;

import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.DefaultCoordinateOperationFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import com.vividsolutions.jts.geom.Coordinate;

public class CoordinateTransformer {

    private MathTransform transform;

	public CoordinateTransformer(int sourceSrid, int targetSrid)
            throws FactoryException {
        CoordinateReferenceSystem sourceCRS = CRS.decode("epsg:4326");
        CoordinateReferenceSystem targetCRS = CRS.decode("epsg:25832");

        DefaultCoordinateOperationFactory trFactory = new DefaultCoordinateOperationFactory();
        CoordinateOperation operation = trFactory.createOperation(sourceCRS, targetCRS);
        transform = operation.getMathTransform();
    }

    public Coordinate[] transform(Coordinate... input) throws TransformException {
        if (input == null) {
            return new Coordinate[0];
        }
        if (transform == null) {
            return new Coordinate[0];
        }
        Coordinate[] output = new Coordinate[input.length];
        double[] sourceCoor = new double[2];
        double[] targetCoor = new double[2];
        for (int i = 0; i < input.length; ++i) {
            sourceCoor[0] = input[i].x;
            sourceCoor[1] = input[i].y;

            // This is the transformation
            transform.transform(sourceCoor, 0, targetCoor, 0, 1);

            // Set output
            output[i] = new Coordinate(targetCoor[0], targetCoor[1]);
        }
        return output;
    }
}
