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

package com.abyala.decisiontree.results;

import com.abyala.decisiontree.*;
import com.abyala.decisiontree.exceptions.InvalidFactException;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * User: abyala
 * Date: 6/20/12
 */
public class DefaultResultTest extends AbstractDecisionTreeTest {
    @Override
    protected String getFileLocation() {
        return "src" + File.separator + "test" + File.separator + "resources" + File.separator + "testDefaultResultValue.xml";
    }

    @Test
    public void testSetup() {
        assertThat(tree, not(nullValue()));
    }

    @Test
    public void testAllDefaults() throws InvalidFactException {
        final DecisionTreeFacts facts = createFacts("allDefaults");
        validateResult(facts, "foo", true, "someMemo", -1);
    }

    @Test
    public void testTwoDefined() throws InvalidFactException {
        final DecisionTreeFacts facts = createFacts("twoDefined");
        validateResult(facts, "bar", false, "someMemo", -1);
    }

    @Test
    public void testAllDefined() throws InvalidFactException {
        final DecisionTreeFacts facts = createFacts("allDefined");
        validateResult(facts, "foo", true, "howdy", -1);
    }

    @Test
    public void testDefaultInputType() throws InvalidFactException {
        final DecisionTreeFacts facts = createFacts("I will use the default input, thank you very much");
        validateResult(facts, "bar", false, "someMemo", -1);
    }

    private DecisionTreeFacts createFacts(final String input) {
        final SimpleDecisionTreeFacts facts = new SimpleDecisionTreeFacts();
        facts.put("fooInput", input);
        return facts;
    }

    private void validateResult(final DecisionTreeFacts facts, final String expectedString, final Boolean expectedBool, final String expectedMemo, final int expectedNum)
            throws InvalidFactException {
        final Object result = tree.evaluate(facts);
        final DefaultTestResult dtr = (DefaultTestResult) result;
        assertThat(dtr.getString(), equalTo(expectedString));
        assertThat(dtr.getBool(), equalTo(expectedBool));
        assertThat(dtr.getMemo(), equalTo(expectedMemo));
    }
}
