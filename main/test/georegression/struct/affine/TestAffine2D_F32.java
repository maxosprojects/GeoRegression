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

package georegression.struct.affine;

import georegression.struct.GenericInvertibleTransformTests_F32;
import georegression.struct.InvertibleTransform;
import georegression.struct.point.Point2D_F32;
import georegression.transform.affine.AffinePointOps_F32;

import java.util.Random;

/**
 * @author Peter Abeles
 */
public class TestAffine2D_F32 extends GenericInvertibleTransformTests_F32<Point2D_F32> {

	Random rand = new Random();

	@Override
	public Point2D_F32 createRandomPoint() {
		return new Point2D_F32( (float) (float)rand.nextGaussian() * 3,
				(float) (float)rand.nextGaussian() * 3 );
	}

	@Override
	public Affine2D_F32 createRandomTransform() {

		float a11 = (float) (float)rand.nextGaussian() * 3.0f;
		float a12 = (float) (float)rand.nextGaussian() * 3.0f;
		float a21 = (float) (float)rand.nextGaussian() * 3.0f;
		float a22 = (float) (float)rand.nextGaussian() * 3.0f;
		float tx = (float) (float)rand.nextGaussian() * 3.0f;
		float ty = (float) (float)rand.nextGaussian() * 3.0f;

		return new Affine2D_F32( a11, a12, a21, a22, tx, ty );
	}

	@Override
	public Point2D_F32 apply( InvertibleTransform se, Point2D_F32 point, Point2D_F32 result ) {
		return AffinePointOps_F32.transform((Affine2D_F32) se, point, result);
	}
}
