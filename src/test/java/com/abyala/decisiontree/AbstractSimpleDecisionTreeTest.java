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

package com.abyala.decisiontree;

import com.abyala.decisiontree.exceptions.InvalidFactException;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * User: abyala
 * Date: 6/20/12
 */
public abstract class AbstractSimpleDecisionTreeTest extends AbstractDecisionTreeTest {

    @Test
    public void testSetup() {
        assertThat(tree, not(nullValue()));
    }
/*

    @Test
    public void testCell1() {
        final DecisionTreeFacts facts = createFacts("paypal", 500);
        assertThat(tree.evaluate(facts), is("Cell 1"));
    }

    @Test
    public void testCell2() {
        final DecisionTreeFacts facts = createFacts("paypal", 10002);
        assertThat(tree.evaluate(facts), is("Cell 2"));
    }

    @Test
    public void testCell3_cheap() {
        final DecisionTreeFacts facts = createFacts("other", 500);
        assertThat(tree.evaluate(facts), is("Cell 3"));
    }

    @Test
    public void testCell3_expensive() {
        final DecisionTreeFacts facts = createFacts("other", 50000);
        assertThat(tree.evaluate(facts), is("Cell 3"));
    }

*/
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOrigin() throws InvalidFactException {
        final AbstractDecisionTreeFacts facts = createFacts("fnord", 1000);
        tree.evaluate(facts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPaypalAmount() throws InvalidFactException {
        final AbstractDecisionTreeFacts facts = createFacts("paypal", -1);
        tree.evaluate(facts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOtherAmount() throws InvalidFactException {
        final AbstractDecisionTreeFacts facts = createFacts("other", -1);
        tree.evaluate(facts);
    }

    private static AbstractDecisionTreeFacts createFacts(final String origin, final int amount) {
        final SimpleDecisionTreeFacts facts = new SimpleDecisionTreeFacts();
        facts.put("origin", origin);
        facts.put("amount", amount);

        return facts;
    }

}
