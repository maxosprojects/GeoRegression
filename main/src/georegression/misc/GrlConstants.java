/*
 * Copyright (C) 2011-2016, Peter Abeles. All Rights Reserved.
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

package georegression.misc;


import org.ejml.UtilEjml;

/**
 * Constants used throughout the library.
 *
 * @author Peter Abeles
 */
public class GrlConstants {

	public static float F_PI = (float)Math.PI;
	public static float F_PI2 = (float)(2.0*Math.PI);
	public static float F_PId2 = (float)(Math.PI/2.0);

	public static double PI = Math.PI;
	public static double PI2 = 2.0*Math.PI;
	public static double PId2 = Math.PI/2.0;

	// identifies the library version
	public static String VERSION = "0.12";

	// standard tolerances used in unit tests
	public static float FLOAT_TEST_TOL = 1e-4f;
	public static double DOUBLE_TEST_TOL = 1e-8;
	public static double DOUBLE_TEST_TOL_SQRT = Math.sqrt(DOUBLE_TEST_TOL);
	public static float FLOAT_TEST_TOL_SQRT = (float)Math.sqrt(FLOAT_TEST_TOL);

	// tolerances for convergence
	public static float FCONV_TOL_A = 1e-5f;
	public static double DCONV_TOL_A = 1e-10;
	public static float FCONV_TOL_B = 1e-6f;
	public static double DCONV_TOL_B = 1e-12;

	public static float F_EPS = (float)Math.pow(2,-21);
	public static double EPS = UtilEjml.EPS;

}
