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
public class BooleanFactTest {
    @Test
    public void testTrue() {
        final BooleanFact fact = new BooleanFact("name", true);
        assertThat(fact.getKey(), equalTo("name"));
        assertThat(fact.getValue(), instanceOf(Boolean.class));
        assertThat((Boolean) fact.getValue(), is(true));
        assertThat(fact.toString(), is("BooleanFact{key='name', value=true}"));
    }

    @Test
    public void testFalse() {
        final BooleanFact fact = new BooleanFact("anotherName", false);
        assertThat(fact.getKey(), equalTo("anotherName"));
        assertThat(fact.getValue(), instanceOf(Boolean.class));
        assertThat((Boolean) fact.getValue(), is(false));
        assertThat(fact.toString(), is("BooleanFact{key='anotherName', value=false}"));
    }
}
