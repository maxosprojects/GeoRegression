/*
 * Copyright (C) 2011-2015, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package georegression.fitting.ellipse;

import georegression.geometry.UtilEllipse_F64;
import georegression.misc.GrlConstants;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.EllipseQuadratic_F64;
import georegression.struct.shapes.EllipseRotated_F64;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestFitEllipseWeightedAlgebraic {

	Random rand = new Random(234);

	@Test
	public void checkCircle() {
		checkEllipse(0, 0, 3, 3, 0);
	}

	/**
	 * Give it points which perfectly describe an ellipse.   This isn't actually an easy case.  See comments
	 * in random section.
	 */
	@Test
	public void checkEllipse() {
		checkEllipse(0,0,3,1.5,0);
		checkEllipse(1,2,3,1.5,0);
		checkEllipse(1,2,3,1.5,0.25);
	}

	public void checkEllipse( double x0 , double y0, double a, double b, double phi ) {
		EllipseRotated_F64 rotated = new EllipseRotated_F64(x0,y0,a,b,phi);

		List<Point2D_F64> points = new ArrayList<Point2D_F64>();
		double weights[] = new double[21];
		for( int i = 0; i < 20; i++ ) {
			double theta = 2.0*(double)Math.PI*i/20;
			points.add(UtilEllipse_F64.computePoint(theta, rotated, null));
			weights[i] = 0.3;
//			System.out.println(points.get(i).x+" "+points.get(i).y);
		}

		// throw in a point that's way off but has no weight and should be ignored
		points.add( new Point2D_F64(20,34));
		weights[20] = 0;

		EllipseQuadratic_F64 expected = UtilEllipse_F64.convert(rotated,null);

		FitEllipseWeightedAlgebraic alg = new FitEllipseWeightedAlgebraic();
		assertTrue(alg.process(points,weights));

		EllipseQuadratic_F64 found = alg.getEllipse();

		normalize(expected);
		normalize(found);

		assertEquals(expected.a,found.a, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(expected.b,found.b, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(expected.c,found.c, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(expected.d,found.d, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(expected.e,found.e, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(expected.f,found.f, GrlConstants.DOUBLE_TEST_TOL);
	}

	/**
	 * A bad point is given and see if the estimated error gets worse as it's weight is increased
	 */
	@Test
	public void checkErrorIncreasedWithWeight() {
		checkEllipse(0,0,3,1.5,0);
		checkEllipse(1,2,3,1.5,0);
		checkEllipse(1,2,3,1.5,0.25);
	}
	public void checkErrorIncreasedWithWeight( double x0 , double y0, double a, double b, double phi ) {
		EllipseRotated_F64 rotated = new EllipseRotated_F64(x0,y0,a,b,phi);

		double errorWeight[] = new double[]{0,0.1,0.5,2.0};
		double error[] = new double[4];

		for (int i = 0; i < error.length; i++) {
			List<Point2D_F64> points = new ArrayList<Point2D_F64>();
			double weights[] = new double[21];
			for( int j = 0; j < 20; j++ ) {
				double theta = 2.0*(double)Math.PI*j/20;
				points.add(UtilEllipse_F64.computePoint(theta, rotated, null));
				weights[j] = 1.0;
			}

			// throw in a point that's way off but has no weight and should be ignored
			points.add( new Point2D_F64(20,34));
			weights[20] = errorWeight[i];

			EllipseQuadratic_F64 expected = UtilEllipse_F64.convert(rotated,null);

			FitEllipseWeightedAlgebraic alg = new FitEllipseWeightedAlgebraic();
			assertTrue(alg.process(points,weights));

			EllipseQuadratic_F64 found = alg.getEllipse();

			normalize(expected);
			normalize(found);

			error[i] = Math.abs(expected.a - found.a);
			error[i] += Math.abs(expected.b - found.b);
			error[i] += Math.abs(expected.c - found.c);
			error[i] += Math.abs(expected.d - found.d);
			error[i] += Math.abs(expected.e - found.e);
			error[i] += Math.abs(expected.f - found.f);
		}


		assertTrue( error[1] > error[0]);
		assertTrue( error[2] > error[1]);
		assertTrue( error[3] > error[2]);
	}

	/**
	 * Randomly generate points and see if it produces a valid ellipse
	 *
	 * The paper mentions that the case of perfect data is actually numerically unstable.  The random test
	 * below has been commented out since even the original algorithm run in octave can't pass that test.
	 */
	@Test
	public void checkRandom() {
//		for( int i = 0; i < 100; i++ ) {
//			System.out.println("i = "+i);
//			double x0 = (rand.nextDouble()-0.5)*2;
//			double y0 = (rand.nextDouble()-0.5)*2;
//			double b = rand.nextDouble();
//			double a = b+rand.nextDouble()*2+0.1;
//			double theta = (rand.nextDouble()-0.5)*Math.PI;
//
//			checkEllipse(x0,y0,a,b,theta);
//		}
	}

	private void normalize( EllipseQuadratic_F64 ellipse )  {
		ellipse.a /= ellipse.f;
		ellipse.b /= ellipse.f;
		ellipse.c /= ellipse.f;
		ellipse.d /= ellipse.f;
		ellipse.e /= ellipse.f;
		ellipse.f /= ellipse.f;
	}

}
