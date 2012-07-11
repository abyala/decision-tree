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
import com.abyala.decisiontree.exceptions.InvalidFactException;
import com.abyala.decisiontree.results.ResultAttribute;
import com.abyala.decisiontree.results.ResultSpec;
import com.abyala.decisiontree.results.TextResultAttribute;
import com.abyala.decisiontree.types.BooleanInputType;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * User: abyala
 * Date: 6/29/12
 */
public class BooleanNodeTest extends AbstractNodeTest {

    private Node node;

    @Before
    public void setUp() throws Exception {
        final Method setter = SimpleStringResult.class.getMethod("setValue", new Class[]{String.class});
        final ResultAttribute attribute = new TextResultAttribute.Builder("value", setter).build();
        SIMPLE_SPEC = new ResultSpec.Builder(SimpleStringResult.class)
                .addAttribute(attribute)
                .build();
        node = new IntegerNode.Builder(createIntegerNodeType("range", null, null))
                .addResultMapping("unbounded", simpleAttribute("3"))
                .build();
    }

    @Test
    public void testMissingFalse() throws DecisionTreeParserException {
        runFirstThenSecondTest(simpleAttribute("1"), simpleAttribute("2"), true);
        runFirstThenSecondTest(simpleAttribute("1"), node, true);
        runFirstThenSecondTest(node, simpleAttribute("2"), true);
        runFirstThenSecondTest(node, node, true);
    }

    @Test
    public void testMissingTrue() throws DecisionTreeParserException {
        runFirstThenSecondTest(simpleAttribute("1"), simpleAttribute("2"), false);
        runFirstThenSecondTest(simpleAttribute("1"), node, false);
        runFirstThenSecondTest(node, simpleAttribute("2"), false);
        runFirstThenSecondTest(node, node, false);
    }

    @Test
    public void testEvaluation() throws DecisionTreeParserException, InvalidFactException {
        Node anotherNode = new IntegerNode.Builder(createIntegerNodeType("range", null, null))
                .addResultMapping("unbounded", simpleAttribute("4"))
                .build();

        runEvaluationTest(simpleAttribute("1"), simpleAttribute("2"), "1", "2");
        runEvaluationTest(simpleAttribute("1"), node, "1", "3");
        runEvaluationTest(node, simpleAttribute("2"), "3", "2");
        runEvaluationTest(node, anotherNode, "3", "4");
    }

    private void runEvaluationTest(final Object trueObject, final Object falseObject, final String trueResult, final String falseResult) throws DecisionTreeParserException, InvalidFactException {
        final BooleanNode.Builder builder = new BooleanNode.Builder(new BooleanInputType.Builder("b").build());
        doAddMapping(builder, trueObject, "true");
        doAddMapping(builder, falseObject, "false");

        final Node node = builder.build();
        node.validate();

        final SimpleDecisionTreeFacts facts = new SimpleDecisionTreeFacts();
        facts.put("b", true);
        facts.put("range", 5);
        assertThat(((SimpleStringResult) node.evaluate(facts)).getValue(), equalTo(trueResult));

        facts.put("b", false);
        assertThat(((SimpleStringResult) node.evaluate(facts)).getValue(), equalTo(falseResult));
    }


    public void runFirstThenSecondTest(final Object firstObject, final Object secondObject, boolean firstSetTrue) throws DecisionTreeParserException {
        final String firstMapping = firstSetTrue ? "true" : "false";
        final String secondMapping = firstSetTrue ? "false" : "true";

        final BooleanNode.Builder builder = new BooleanNode.Builder(new BooleanInputType.Builder("b").build());
        doAddMapping(builder, firstObject, firstMapping);

        try {
            builder.build().validate();
            fail("Should reject without a mapping of " + secondMapping);
        } catch (DecisionTreeParserException e) {
            assertThat(e.getMessage(), equalTo("Invalid configuration: Node \"b\" must have a mapping for value \"" + secondMapping + "\""));
        }

        doAddMapping(builder, secondObject, secondMapping);
        builder.build().validate();
    }

    private void doAddMapping(final BooleanNode.Builder builder, final Object object, final String name) throws DecisionTreeParserException {
        if (object instanceof ResultNode) {
            builder.addResultMapping(name, (ResultNode) object);
        } else if (object instanceof Node) {
            builder.addNodeMapping(name, (Node) object);
        } else {
            throw new RuntimeException("Invalid test case: mapping object is of unknown type " + (object == null ? "null" : object.getClass().getName()));
        }
    }
}
