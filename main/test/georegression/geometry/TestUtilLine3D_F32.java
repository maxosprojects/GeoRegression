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

package georegression.geometry;

import georegression.misc.GrlConstants;
import georegression.struct.line.LineParametric3D_F32;
import georegression.struct.line.LineSegment3D_F32;
import georegression.struct.point.Point3D_F32;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestUtilLine3D_F32 {

	@Test
	public void convert_ls_lp() {
		LineSegment3D_F32 ls = new LineSegment3D_F32(1,2,3,6,8,10);

		LineParametric3D_F32 lp = UtilLine3D_F32.convert(ls,null);

		assertEquals(lp.p.x,1, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(lp.p.y,2, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(lp.p.z,3, GrlConstants.FLOAT_TEST_TOL);

		assertEquals(lp.slope.x,5, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(lp.slope.y,6, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(lp.slope.z,7, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void computeT() {
		LineParametric3D_F32 line = new LineParametric3D_F32(1,2,3,-4,1.5f,0.23f);

		float t0 = -3.4f;
		float t1 = 1.2f;

		Point3D_F32 p0 = line.getPointOnLine(t0);
		Point3D_F32 p1 = line.getPointOnLine(t1);

		assertEquals(t0,UtilLine3D_F32.computeT(line,p0),GrlConstants.FLOAT_TEST_TOL);
		assertEquals(t1,UtilLine3D_F32.computeT(line,p1),GrlConstants.FLOAT_TEST_TOL);
	}
}
