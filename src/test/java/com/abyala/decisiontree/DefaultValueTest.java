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

import java.io.File;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * User: abyala
 * Date: 6/20/12
 */
public class DefaultValueTest extends AbstractDecisionTreeTest {
    @Override
    protected String getFileLocation() {
        return "src" + File.separator + "test" + File.separator + "resources" + File.separator + "testDefaultInputValue.xml";
    }

    @Test
    public void testSetup() {
        assertThat(tree, not(nullValue()));
    }

    @Test
    public void testValueBeforeDefault() throws InvalidFactException {
        final DecisionTreeFacts facts = createFacts("a");
        validateResult(facts, "LetterA");
    }

    @Test
    public void testValueThatIsDefault() throws InvalidFactException {
        final DecisionTreeFacts facts = createFacts("b");
        validateResult(facts, "LetterB");
    }

    @Test
    public void testValueAfterDefault() throws InvalidFactException {
        final DecisionTreeFacts facts = createFacts("c");
        validateResult(facts, "LetterC");
    }

    @Test
    public void testValueUnknownValueAsDefault() throws InvalidFactException {
        final DecisionTreeFacts facts = createFacts("d");
        validateResult(facts, "LetterB");
    }

    private DecisionTreeFacts createFacts(final String letter) {
        final SimpleDecisionTreeFacts facts = new SimpleDecisionTreeFacts();
        facts.put("letter", letter);
        return facts;
    }

    private void validateResult(final DecisionTreeFacts facts, final String expected) throws InvalidFactException {
        final Object result = tree.evaluate(facts);
        assertThat(result, instanceOf(SimpleStringResult.class));
        assertThat(((SimpleStringResult) result).getValue(), equalTo(expected));
    }
}
