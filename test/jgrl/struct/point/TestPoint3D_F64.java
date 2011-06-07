/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Java Geometric Regression Library (JGRL).
 *
 * JGRL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * JGRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with JGRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package jgrl.struct.point;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestPoint3D_F64 extends GenericGeoTupleTests3D_F64<Point3D_F64> {

    public TestPoint3D_F64() {
        super(new Point3D_F64());
    }

    @Test
    public void generic() {
        checkAll();
    }

    @Test
    public void plus_pt() {
        Point3D_F64 a = new Point3D_F64(1,2,3);
        Point3D_F64 b = new Point3D_F64(1,2,3);

        Point3D_F64 c = a.plus(b);

        assertEquals(2,c.getX(),1e-8);
        assertEquals(4,c.getY(),1e-8);
        assertEquals(6,c.getZ(),1e-8);
    }

    @Test
    public void plus_v() {
        Point3D_F64 a = new Point3D_F64(1,2,3);
        Vector3D_F64 b = new Vector3D_F64(1,2,3);

        Point3D_F64 c = a.plus(b);

        assertEquals(2,c.getX(),1e-8);
        assertEquals(4,c.getY(),1e-8);
        assertEquals(6,c.getZ(),1e-8);
    }
}
