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
public class StringFactTest {
    @Test
    public void testSimpleValue() {
        final StringFact fact = new StringFact("name", "foo");
        assertThat(fact.getKey(), equalTo("name"));
        assertThat(fact.getValue(), instanceOf(String.class));
        assertThat((String) fact.getValue(), is("foo"));
        assertThat(fact.toString(), is("StringFact{key='name', value='foo'}"));
    }

    @Test
    public void testEmptyString() {
        final StringFact fact = new StringFact("anotherName", "");
        assertThat(fact.getKey(), equalTo("anotherName"));
        assertThat(fact.getValue(), instanceOf(String.class));
        assertThat((String) fact.getValue(), is(""));
        assertThat(fact.toString(), is("StringFact{key='anotherName', value=''}"));
    }

    @Test
    public void testNull() {
        final StringFact fact = new StringFact("aThirdName", null);
        assertThat(fact.getKey(), equalTo("aThirdName"));
        assertThat(fact.getValue(), nullValue());
        assertThat(fact.toString(), is("StringFact{key='aThirdName', value=null}"));
    }

    @Test
    public void testStringCalledNull() {
        final StringFact fact = new StringFact("aThirdName", "null");
        assertThat(fact.getKey(), equalTo("aThirdName"));
        assertThat(fact.getValue(), instanceOf(String.class));
        assertThat((String) fact.getValue(), equalTo("null"));
        assertThat(fact.toString(), is("StringFact{key='aThirdName', value='null'}"));
    }
}
