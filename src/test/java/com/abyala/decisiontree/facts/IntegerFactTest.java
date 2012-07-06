/*
 * Copyright (c) 2012 Andrew Byala
 *
 * This file is part of DecisionTree.
 *
 * DecisionTree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DecisionTree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DecisionTree.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.abyala.decisiontree.facts;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * User: abyala
 * Date: 6/27/12
 */
public class IntegerFactTest {
    @Test
    public void testPositiveNumber() {
        final IntegerFact fact = new IntegerFact("name", 5);
        assertThat(fact.getKey(), equalTo("name"));
        assertThat(fact.getValue(), instanceOf(Integer.class));
        assertThat((Integer) fact.getValue(), is(5));
        assertThat(fact.toString(), is("IntegerFact{key='name', value=5}"));
    }

    @Test
    public void testNegativeNumber() {
        final IntegerFact fact = new IntegerFact("negativeName", -3);
        assertThat(fact.getKey(), equalTo("negativeName"));
        assertThat(fact.getValue(), instanceOf(Integer.class));
        assertThat((Integer) fact.getValue(), is(-3));
        assertThat(fact.toString(), is("IntegerFact{key='negativeName', value=-3}"));
    }
}
