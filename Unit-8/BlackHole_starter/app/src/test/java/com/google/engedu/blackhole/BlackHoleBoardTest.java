/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.blackhole;

import android.util.Pair;

import org.junit.Test;
import static org.junit.Assert.*;

public class BlackHoleBoardTest {
    @Test
    public void testCoordsToIndex() {
        BlackHoleBoard b = new BlackHoleBoard();
        assertEquals(0, b.coordsToIndex(0, 0));
        assertEquals(1, b.coordsToIndex(0, 1));
        assertEquals(2, b.coordsToIndex(1, 1));
        assertEquals(3, b.coordsToIndex(0, 2));
        assertEquals(4, b.coordsToIndex(1, 2));
        assertEquals(5, b.coordsToIndex(2, 2));
    }

    @Test
    public void testIndexToCoords() {
        BlackHoleBoard b = new BlackHoleBoard();
        Coordinates coords = b.indexToCoords(0);
        assertEquals(0, coords.x);
        assertEquals(0, coords.y);
        coords = b.indexToCoords(1);
        assertEquals(0, coords.x);
        assertEquals(1, coords.y);
        for (int i = 0; i < b.BOARD_SIZE; i++) {
            coords = b.indexToCoords(i);
           assertEquals(i, b.coordsToIndex(coords.x, coords.y));
        }
    }

    @Test
    public void testGetScore() {
        BlackHoleBoard board1 = new BlackHoleBoard();
        board1.tiles= new BlackHoleTile[]{new BlackHoleTile(0, 1),
                                new BlackHoleTile(0, 2), new BlackHoleTile(0, 3),
                            new BlackHoleTile(0, 4), new BlackHoleTile(0, 5), new BlackHoleTile(0, 6),
                        new BlackHoleTile(0, 7)/**/, new BlackHoleTile(0, 8), new BlackHoleTile(0, 9), new BlackHoleTile(0, 10),
                    null                ,new BlackHoleTile(1, 1)/**/, new BlackHoleTile(1, 2), new BlackHoleTile(1, 3), new BlackHoleTile(1, 4),
                new BlackHoleTile(1, 5)/**/, new BlackHoleTile(1, 6)/**/, new BlackHoleTile(1, 7), new BlackHoleTile(1, 8), new BlackHoleTile(1, 9), new BlackHoleTile(1, 10),};

            assertEquals(5,board1.getScore());


        BlackHoleBoard board2 = new BlackHoleBoard();
        board1.tiles= new BlackHoleTile[]{null,
                                new BlackHoleTile(0, 1)/**/, new BlackHoleTile(1, 1)/**/,
                            new BlackHoleTile(0, 2), new BlackHoleTile(0, 3), new BlackHoleTile(0, 4),
                        new BlackHoleTile(1, 2), new BlackHoleTile(1, 3), new BlackHoleTile(1, 4), new BlackHoleTile(1, 5),
                    new BlackHoleTile(0, 5), new BlackHoleTile(0, 6), new BlackHoleTile(0, 7), new BlackHoleTile(0, 8), new BlackHoleTile(0, 9),
                new BlackHoleTile(0, 10), new BlackHoleTile(1, 6), new BlackHoleTile(1, 7), new BlackHoleTile(1, 8), new BlackHoleTile(1, 9), new BlackHoleTile(1, 10)};

            assertEquals(0,board2.getScore());
    }

}
