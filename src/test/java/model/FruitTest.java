/******************************************************************************
 * This work is applicable to the conditions of the MIT License,              *
 * which can be found in the LICENSE file, or at                              *
 * https://github.com/philippwinter/pacman/blob/master/LICENSE                *
 *                                                                            *
 * Copyright (c) 2013 Philipp Winter, Jonas Heidecke & Niklas Kaddatz         *
 ******************************************************************************/

package model;

import controller.MainController;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

/**
 * FruitTest
 */
public class FruitTest {

    private Fruit instance;
    private Position pos;

    @Before
    public void setUp() {
        MainController.reset();

        pos = Map.getInstance().getPositionContainer().get(1, 5);

        instance = new Fruit(pos,1);
    }

    @Test
    public void testGetState() {
        assertNotNull(instance.getState());
    }

    @Test
    public void testChangeState() {
        instance.changeState(StaticTarget.State.EATEN);
        assertEquals(StaticTarget.State.EATEN, instance.getState());
    }

    @Test
    public void testGetScore() {
        assertEquals(100, instance.getScore());
    }

    @Test
    public void testEquals() {
        Fruit c = new Fruit(Map.getInstance().getPositionContainer().get(0, 0),1);
        Fruit c1 = instance;
        assertThat(c, is(not(c1)));
        assertThat(instance, is(c1));
    }

}