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

package georegression.fitting.se;

import georegression.fitting.MotionTransformPoint;
import georegression.geometry.GeometryMath_F32;
import georegression.geometry.UtilPoint2D_F32;
import georegression.struct.point.Point2D_F32;
import georegression.struct.se.Se2_F32;
import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.DecompositionFactory;
import org.ejml.interfaces.decomposition.SingularValueDecomposition;
import org.ejml.ops.CommonOps;

import java.util.List;

/**
 * <p>
 * Finds the rigid body motion which minimizes the different between the two sets of associated points in 2D.  The
 * rotation is computed from the SVD of a cross correlation matrix.
 * </p>
 * <p>
 * The mean square error function that is minimized is:<br>
 * f(p) = (1/N) sum( i=1:N , ||x_i - R(theta)*p_i + T||<sup>2</sup> )<br>
 * where theta is the angle of rotation and T is the translation, x is the set of 'to' points
 * and p is the set of 'from' points.
 * </p>
 * <p>
 * Based upon the sketch of Arun et al. 1987 provided in: D.W. Eggert, A. Loruso, R.B. Fisher, "Estimating 3-D Rigid Body Transformation:
 * A Comparison of Four Major Algorithms" Machine Vision and Applications 1997
 * </p>
 *
 * @author Peter Abeles
 */
public class MotionSe2PointSVD_F32 implements MotionTransformPoint<Se2_F32, Point2D_F32> {

	Se2_F32 motion = new Se2_F32();

	Point2D_F32 meanFrom = new Point2D_F32();
	Point2D_F32 meanTo = new Point2D_F32();

	SingularValueDecomposition<DenseMatrix64F> svd = DecompositionFactory.svd(2,2,true,true,false);
	DenseMatrix64F Sigma = new DenseMatrix64F(2,2);
	DenseMatrix64F U = new DenseMatrix64F(2,2);
	DenseMatrix64F V = new DenseMatrix64F(2,2);
	DenseMatrix64F R = new DenseMatrix64F(2,2);

	@Override
	public Se2_F32 getTransformSrcToDst() {
		return motion;
	}

	@Override
	public boolean process( List<Point2D_F32> srcPts, List<Point2D_F32> dstPts) {
		if( srcPts.size() != dstPts.size() )
			throw new IllegalArgumentException( "There must be a 1 to 1 correspondence between the two sets of points" );

		// find the mean of both sets of points
		UtilPoint2D_F32.mean(srcPts, meanFrom );
		UtilPoint2D_F32.mean(dstPts, meanTo );

		final int N = srcPts.size();

		// compute the cross-covariance matrix Sigma of the two sets of points
		// Sigma = (1/N)*sum(i=1:N,[p*x^T]) + mu_p*mu_x^T
		// for performance reasons usage of matrices is postponed
		float s11 = 0, s12 = 0;
		float s21 = 0, s22 = 0;

		// mu*mu^2
		float m11 = meanFrom.x * meanTo.x, m12 = meanFrom.x * meanTo.y;
		float m21 = meanFrom.y * meanTo.x, m22 = meanFrom.y * meanTo.y;

		for( int i = 0; i < N; i++ ) {
			Point2D_F32 f = srcPts.get( i );
			Point2D_F32 t = dstPts.get( i );

			s11 += f.x * t.x;
			s12 += f.x * t.y;
			s21 += f.y * t.x;
			s22 += f.y * t.y;
		}

		s11 = s11 / N - m11;
		s12 = s12 / N - m12;
		s21 = s21 / N - m21;
		s22 = s22 / N - m22;

		Sigma.data[0] = s11;Sigma.data[1] = s12;
		Sigma.data[2] = s21;Sigma.data[3] = s22;
		// Compute the SVD of the cross correlation matrix
		// The rotation matrix is R = V*U^T

		if( !svd.decompose(Sigma) )
			return false;

		svd.getU(U,false);
		svd.getV(V, false);

		CommonOps.multTransB(V,U,R);

		// There are situations where R might not have a determinant of one and is instead
		// a reflection is returned
		/**/double det = CommonOps.det(R);
		if( det < 0 ) {
			for( int i = 0; i < 2; i++ )
				V.set( i, 1, -V.get( i, 1 ) );
			CommonOps.multTransB(V,U,R);
			det = CommonOps.det(R);
			if( det < 0 ) {
				throw new RuntimeException( "Crap" );
			}
		}

		// extract the yaw from the rotation matrix
		float yaw = (float)Math.atan2( R.get( 1, 0 ), R.get( 0, 0 ) );

		// save the results
		GeometryMath_F32.rotate( yaw, meanFrom, meanFrom );
		motion.getTranslation().x = meanTo.x - meanFrom.x;
		motion.getTranslation().y = meanTo.y - meanFrom.y;
		motion.setYaw( yaw );

		return true;
	}

	@Override
	public int getMinimumPoints() {
		return 3;
	}
}
