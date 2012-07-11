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
import com.abyala.decisiontree.exceptions.NoMappingDeclaredException;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * User: abyala
 * Date: 6/20/12
 */
public class ReferenceTest extends AbstractDecisionTreeTest {

    @Override
    protected String getFileLocation() {
        return "src" + File.separator + "test" + File.separator + "resources" + File.separator + "testReferences.xml";
    }

    @Test
    public void testSetup() {
        assertThat(tree, not(nullValue()));
    }

    @Test
    public void testDirectLink() throws InvalidFactException {
        final DecisionTreeFacts facts = createFact("a", null);
        validateResult(facts, "LetterA");
    }

    @Test
    public void testIndirectLink() throws InvalidFactException {
        final DecisionTreeFacts facts = createFact("a_ref", null);
        validateResult(facts, "LetterA");
    }

    @Test
    public void testAllNestedValues() throws InvalidFactException {
        validateResult(createFact("b", 0), "LetterB-0");
        validateResult(createFact("b", 5), "LetterB-5");
        validateResult(createFact("b", 10), "LetterB-0");
        validateResult(createFact("b_ref", 0), "LetterB-0");
        validateResult(createFact("b_ref", 5), "LetterB-5");
        validateResult(createFact("b_ref", 10), "LetterB-0");
    }

    @Test(expected = NoMappingDeclaredException.class)
    public void testInvalidTopLevelRef() throws InvalidFactException {
        tree.evaluate(createFact("invalid", null));
    }

    @Test(expected = NoMappingDeclaredException.class)
    public void testInvalidLowerLevelRef() throws InvalidFactException {
        tree.evaluate(createFact("b", -1));
    }

    private void validateResult(final DecisionTreeFacts facts, final String expected) throws InvalidFactException {
        final SimpleStringResult result = (SimpleStringResult) tree.evaluate(facts);
        assertThat(result.getValue(), equalTo(expected));
    }

    private DecisionTreeFacts createFact(final String letter, final Integer range) {
        final SimpleDecisionTreeFacts facts = new SimpleDecisionTreeFacts();
        facts.put("letter", letter);
        if (range != null) {
            facts.put("range", range);
        }
        return facts;
    }
}
