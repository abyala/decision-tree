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

package com.abyala.decisiontree.nodes;

import com.abyala.decisiontree.SimpleDecisionTreeFacts;
import com.abyala.decisiontree.SimpleStringResult;
import com.abyala.decisiontree.exceptions.DecisionTreeParserException;
import com.abyala.decisiontree.exceptions.InvalidFactDataypeException;
import com.abyala.decisiontree.exceptions.InvalidFactException;
import com.abyala.decisiontree.exceptions.MissingFactException;
import com.abyala.decisiontree.types.InputType;
import com.abyala.decisiontree.types.IntegerInputType;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * User: abyala
 * Date: 6/28/12
 */
public class IntegerNodeTest extends AbstractNodeTest {
    private IntegerInputType ALL_VALUES_TYPE;
    private IntegerInputType ONLY_NEGATIVE_TYPE;
    private IntegerInputType NON_NEGATIVE_TYPE;
    private IntegerInputType ONLY_THREE_TYPE;
    private IntegerInputType TEN_TO_TWENTY_TYPE;

    @Before
    public void setUp() throws Exception {
        ONLY_NEGATIVE_TYPE = createIntegerNodeType("range", null, -1);
        NON_NEGATIVE_TYPE = createIntegerNodeType("range", 0, null);
        ONLY_THREE_TYPE = createIntegerNodeType("range", 3, 3);
        TEN_TO_TWENTY_TYPE = createIntegerNodeType("range", 10, 20);
        ALL_VALUES_TYPE = createIntegerNodeType("range", null, null);
    }

    @Test
    public void testOnlyNegatives_lowerBoundTest() throws DecisionTreeParserException {
        IntegerNode.Builder builder = new IntegerNode.Builder(ONLY_NEGATIVE_TYPE);
        builder.addResultMapping("-100", simpleAttribute("1"));
        IntegerNode node = builder.build();
        try {
            node.validate();
            fail("Should fail because node doesn't include negatives below -100");
        } catch (DecisionTreeParserException e) {
            assertThat(e.getMessage(), equalTo("Invalid configuration: Node \"range\" is missing minimum mapping of \"unbounded\""));
        }

        // Add in another value, but not all the way negative
        builder.addResultMapping("-5000", simpleAttribute("2"));
        node = builder.build();
        try {
            node.validate();
            fail("Should fail because node doesn't include negatives below -5000");
        } catch (DecisionTreeParserException e) {
            assertThat(e.getMessage(), equalTo("Invalid configuration: Node \"range\" is missing minimum mapping of \"unbounded\""));
        }

        // Add in the final value to go all the way negative
        builder.addResultMapping("unbounded", simpleAttribute("3"));
        node = builder.build();
        node.validate();
    }

    @Test
    public void testOnlyNegatives_upperBoundTest() throws DecisionTreeParserException {
        IntegerNode.Builder builder = new IntegerNode.Builder(ONLY_NEGATIVE_TYPE);
        builder.addResultMapping("unbounded", simpleAttribute("1"));
        IntegerNode node = builder.build();
        node.validate();

        // Now add a mapping that's too high, and let it fail
        builder.addResultMapping("0", simpleAttribute("2"));
        node = builder.build();
        try {
            node.validate();
            fail("Should fail because node doesn't allow values above -1");
        } catch (DecisionTreeParserException e) {
            assertThat(e.getMessage(), equalTo("Invalid configuration: Node \"range\" cannot match value 0 because it falls above the max value allowed"));
        }
    }

    @Test
    public void testNonNegatives_lowerBoundTest() throws DecisionTreeParserException {
        IntegerNode.Builder builder = new IntegerNode.Builder(NON_NEGATIVE_TYPE);
        builder.addResultMapping("5", simpleAttribute("1"));
        IntegerNode node = builder.build();

        try {
            node.validate();
            fail("Should fail because node doesn't include values from 0 to 4");
        } catch (DecisionTreeParserException e) {
            assertThat(e.getMessage(), equalTo("Invalid configuration: Node \"range\" is missing minimum mapping of \"0\""));
        }

        // Add in the missing value
        builder.addResultMapping("0000", simpleAttribute("2"));
        node = builder.build();
        node.validate();

        // Add in a value that goes beyond the minimum mapping
        builder.addResultMapping("-1", simpleAttribute("3"));
        node = builder.build();
        try {
            node.validate();
            fail("Should fail because node goes below minimum allowed value of 0");
        } catch (DecisionTreeParserException e) {
            assertThat(e.getMessage(), equalTo("Invalid configuration: Node \"range\" cannot match value -1 because it falls below the min value allowed"));
        }
    }

    @Test
    public void testOnlyThreeType_tooLow() throws DecisionTreeParserException {
        IntegerNode.Builder builder = new IntegerNode.Builder(ONLY_THREE_TYPE);
        builder.addResultMapping("2", simpleAttribute("1"));

        try {
            builder.build().validate();
            fail("Should have failed because mapping of 2 is below minimum of 3");
        } catch (DecisionTreeParserException e) {
            assertThat(e.getMessage(), equalTo("Invalid configuration: Node \"range\" cannot match value 2 because it falls below the min value allowed"));
        }
    }

    @Test
    public void testOnlyThreeType_tooHigh() throws DecisionTreeParserException {
        IntegerNode.Builder builder = new IntegerNode.Builder(ONLY_THREE_TYPE);
        builder.addResultMapping("4", simpleAttribute("1"));

        try {
            builder.build().validate();
            fail("Should have failed because mapping of 4 is below maximum of 3");
        } catch (DecisionTreeParserException e) {
            assertThat(e.getMessage(), equalTo("Invalid configuration: Node \"range\" cannot match value 4 because it falls above the max value allowed"));
        }
    }

    @Test
    public void testOnlyThreeType_onlyPossibleValue() throws DecisionTreeParserException {
        IntegerNode.Builder builder = new IntegerNode.Builder(ONLY_THREE_TYPE);
        builder.addResultMapping("3", simpleAttribute("1"));

        builder.build().validate();
    }

    @Test
    public void testTenToTwentyType() throws DecisionTreeParserException {
        IntegerNode.Builder builder = new IntegerNode.Builder(TEN_TO_TWENTY_TYPE);
        builder.addResultMapping("10", simpleAttribute("1"));
        builder.build().validate();

        builder.addResultMapping("19", simpleAttribute("2"));
        builder.build().validate();

        builder.addResultMapping("20", simpleAttribute("3"));
        builder.build().validate();

        // Now go too far
        builder.addResultMapping("21", simpleAttribute("4"));
        try {
            builder.build().validate();
        } catch (DecisionTreeParserException e) {
            assertThat(e.getMessage(), equalTo("Invalid configuration: Node \"range\" cannot match value 21 because it falls above the max value allowed"));
        }
    }

    @Test
    public void testAllValuesType() throws DecisionTreeParserException {
        IntegerNode.Builder builder = new IntegerNode.Builder(ALL_VALUES_TYPE);
        builder.addResultMapping("100", simpleAttribute("1"));
        try {
            builder.build().validate();
        } catch (DecisionTreeParserException e) {
            assertThat(e.getMessage(), equalTo("Invalid configuration: Node \"range\" is missing minimum mapping of \"unbounded\""));
        }

        builder.addResultMapping("unbounded", simpleAttribute("2"));
        builder.build().validate();
    }

    @Test
    public void testMultipleMappings() throws DecisionTreeParserException {
        IntegerNode.Builder builder = new IntegerNode.Builder(ALL_VALUES_TYPE);
        builder.addResultMapping("100", simpleAttribute("1"));
        try {
            builder.addResultMapping("100", simpleAttribute("2"));
            fail("Should not allow mapping the same value twice to a single input");
        } catch (DecisionTreeParserException e) {
            assertThat(e.getMessage(), equalTo("Invalid configuration: Node \"range\" may not have multiple mappings for the same value 100"));
        }
    }

    @Test
    public void testWithNestedInvalidNodes() throws DecisionTreeParserException {
        IntegerNode.Builder childBuilder = new IntegerNode.Builder(ALL_VALUES_TYPE);
        childBuilder.addResultMapping("0", simpleAttribute("1"));

        IntegerNode.Builder builder = new IntegerNode.Builder(ONLY_THREE_TYPE);
        builder.addNodeMapping("3", childBuilder.build());

        try {
            builder.build().validate();
            fail("Parent should not succeed if the child fails validation");
        } catch (DecisionTreeParserException e) {
            assertThat(e.getMessage(), equalTo("Invalid configuration: Node \"range\" is missing minimum mapping of \"unbounded\""));
        }

        // Complete the child node.  The parent should still fail, since a Node already built is immutable
        childBuilder.addResultMapping("unbounded", simpleAttribute("2"));
        try {
            builder.build().validate();
            fail("Parent should not succeed if the child fails validation");
        } catch (DecisionTreeParserException e) {
            assertThat(e.getMessage(), equalTo("Invalid configuration: Node \"range\" is missing minimum mapping of \"unbounded\""));
        }

        // Rebuild the parent node. It should succeed now.
        builder = new IntegerNode.Builder(ONLY_THREE_TYPE);
        builder.addNodeMapping("3", childBuilder.build());
        builder.build().validate();
    }

    @Test
    public void testWithMixedContent() throws DecisionTreeParserException {
        IntegerNode.Builder childBuilder = new IntegerNode.Builder(ALL_VALUES_TYPE);
        childBuilder.addResultMapping("unbounded", simpleAttribute("1"));

        IntegerNode.Builder builder = new IntegerNode.Builder(ALL_VALUES_TYPE);
        builder.addNodeMapping("0", childBuilder.build());
        builder.addResultMapping("unbounded", simpleAttribute("2"));

        builder.build().validate();
    }

    @Test
    public void testEvaluate() throws DecisionTreeParserException, InvalidFactException {
        IntegerNode.Builder builder = new IntegerNode.Builder(ALL_VALUES_TYPE);
        builder.addResultMapping("unbounded", simpleAttribute("negative"));
        builder.addResultMapping("0", simpleAttribute("zero"));
        builder.addResultMapping("1", simpleAttribute("positive"));
        final IntegerNode node = builder.build();

        assertThat(((SimpleStringResult) node.evaluate(singleIntegerFact("range", Integer.MIN_VALUE))).getValue(), equalTo("negative"));
        assertThat(((SimpleStringResult) node.evaluate(singleIntegerFact("range", -1))).getValue(), equalTo("negative"));
        assertThat(((SimpleStringResult) node.evaluate(singleIntegerFact("range", 0))).getValue(), equalTo("zero"));
        assertThat(((SimpleStringResult) node.evaluate(singleIntegerFact("range", 1))).getValue(), equalTo("positive"));
        assertThat(((SimpleStringResult) node.evaluate(singleIntegerFact("range", Integer.MAX_VALUE))).getValue(), equalTo("positive"));

        try {
            node.evaluate(new SimpleDecisionTreeFacts());
            fail("Should have rejected with missing \"range\" fact");
        } catch (MissingFactException e) {
            assertThat(e.getFieldName(), equalTo("range"));
            assertThat(e.getFieldValue(), nullValue());
        }

        try {
            node.evaluate(singleStringFact("range", "foo"));
            fail("Should have rejected with invalid field type \"range\"");
        } catch (InvalidFactDataypeException e) {
            assertThat(e.getExpectedClass().getName(), equalTo(Integer.class.getName()));
            assertThat(e.getActualClass().getName(), equalTo(String.class.getName()));
            assertThat(e.getFieldName(), equalTo("range"));
            assertThat((String) e.getFieldValue(), equalTo("foo"));
        }
    }

    @Test
    public void testEvaluate_withChildren() throws DecisionTreeParserException, InvalidFactException {
        final IntegerNode.Builder childBuilder = new IntegerNode.Builder(ALL_VALUES_TYPE);
        childBuilder.addResultMapping("unbounded", simpleAttribute("child-negative"));
        childBuilder.addResultMapping("0", simpleAttribute("child-non-negative"));

        final InputType parentType = createIntegerNodeType("topLevel", 0, 1);
        final IntegerNode.Builder parentBuilder = new IntegerNode.Builder(parentType);
        parentBuilder.addResultMapping("0", simpleAttribute("parent-zero"));
        parentBuilder.addNodeMapping("1", childBuilder.build());

        final Node node = parentBuilder.build();
        node.validate();

        assertTwoIntegerFacts(node, 0, -100, "parent-zero");
        assertTwoIntegerFacts(node, 0, 0, "parent-zero");
        assertTwoIntegerFacts(node, 0, 100, "parent-zero");
        assertTwoIntegerFacts(node, 1, -100, "child-negative");
        assertTwoIntegerFacts(node, 1, 0, "child-non-negative");
        assertTwoIntegerFacts(node, 1, 100, "child-non-negative");
    }

    private void assertTwoIntegerFacts(final Node node, final Integer topLevelValue, final Integer childValue, final String expected) throws InvalidFactException {
        final SimpleDecisionTreeFacts facts = new SimpleDecisionTreeFacts();
        facts.put("topLevel", topLevelValue);
        facts.put("range", childValue);

        final SimpleStringResult result = (SimpleStringResult) node.evaluate(facts);
        final String actual = result.getValue();
        assertThat(actual, equalTo(expected));
    }
}
