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

import com.abyala.decisiontree.exceptions.DecisionTreeParserException;
import com.abyala.decisiontree.exceptions.InvalidFactException;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * User: abyala
 * Date: 6/20/12
 */
public class IntegerRangeTest extends AbstractDecisionTreeTest {
    @Override
    protected String getFileLocation() {
        return "src" + File.separator + "test" + File.separator + "resources" + File.separator + "testIntegerRanges.xml";
    }

    @Test
    public void testSetup() {
        assertThat(tree, not(nullValue()));
    }

    @Test
    public void testNegative() throws InvalidFactException {
        final DecisionTreeFacts facts = createFacts(-1);
        validateResult(facts, "negativeNumber");
    }

    @Test
    public void testZero() throws InvalidFactException {
        final DecisionTreeFacts facts = createFacts(0);
        validateResult(facts, "zero");
    }

    @Test
    public void testOne() throws InvalidFactException {
        final DecisionTreeFacts facts = createFacts(1);
        validateResult(facts, "positiveNumber");
    }

    @Test
    public void testBeforeNextMin() throws InvalidFactException {
        final DecisionTreeFacts facts = createFacts(999);
        validateResult(facts, "positiveNumber");
    }

    @Test
    public void testNextMin() throws InvalidFactException {
        final DecisionTreeFacts facts = createFacts(1000);
        validateResult(facts, "largePositiveNumber");
    }

    @Test
    public void testAboveNextMin() throws InvalidFactException {
        final DecisionTreeFacts facts = createFacts(1001);
        validateResult(facts, "largePositiveNumber");
    }

    @Test
    public void testTreeMissingLowerBound() {
        try {
            parseDecisionTreeAtFileLocation("src" + File.separator + "test" + File.separator + "resources" + File.separator + "testMissingLowerBoundIntRange.xml");
            fail("Should have rejected tree with a missing lower bound");
        } catch (DecisionTreeParserException e) {
            assertThat(e.getMessage(), equalTo("Invalid configuration: Node \"range\" is missing minimum mapping of \"unbounded\""));
        }
    }

    private DecisionTreeFacts createFacts(final int value) {
        final SimpleDecisionTreeFacts facts = new SimpleDecisionTreeFacts();
        facts.put("range", value);
        return facts;
    }

    private void validateResult(final DecisionTreeFacts facts, final String expected) throws InvalidFactException {
        final Object result = tree.evaluate(facts);
        assertThat(result, instanceOf(SimpleStringResult.class));
        assertThat(((SimpleStringResult) result).getValue(), equalTo(expected));
    }
}
