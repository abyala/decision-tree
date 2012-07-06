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

import com.abyala.decisiontree.SimpleStringResult;
import com.abyala.decisiontree.SimpleDecisionTreeFacts;
import com.abyala.decisiontree.DecisionTreeFacts;
import com.abyala.decisiontree.exceptions.DecisionTreeParserException;
import com.abyala.decisiontree.results.ResultAttribute;
import com.abyala.decisiontree.results.ResultSpec;
import com.abyala.decisiontree.results.TextResultAttribute;
import com.abyala.decisiontree.types.IntegerInputType;

import java.lang.reflect.Method;

/**
 * User: abyala
 * Date: 6/29/12
 */
public abstract class AbstractNodeTest {

    protected ResultSpec SIMPLE_SPEC;

    protected AbstractNodeTest() {
        try {
            final Method setter = SimpleStringResult.class.getMethod("setValue", new Class[]{String.class});
            final ResultAttribute attribute = new TextResultAttribute.Builder("value", setter).build();
            this.SIMPLE_SPEC = new ResultSpec.Builder(SimpleStringResult.class)
                    .addAttribute(attribute)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected static IntegerInputType createIntegerNodeType(final String fieldName, final Integer min, final Integer max) {
        final IntegerInputType.Builder builder = new IntegerInputType.Builder(fieldName);
        builder.setMinValue(min == null ? Integer.MIN_VALUE : min);
        builder.setMaxValue(max == null ? Integer.MAX_VALUE : max);
        return builder.build();
    }

    protected ResultNode simpleAttribute(final String value) throws DecisionTreeParserException {
        return new ResultNode.Builder(SIMPLE_SPEC).addAttribute("value", value).build();
    }

    protected DecisionTreeFacts singleIntegerFact(final String fieldName, final Integer value) {
        final SimpleDecisionTreeFacts facts = new SimpleDecisionTreeFacts();
        facts.put(fieldName, value);
        return facts;
    }

    protected DecisionTreeFacts singleStringFact(final String fieldName, final String value) {
        final SimpleDecisionTreeFacts facts = new SimpleDecisionTreeFacts();
        facts.put(fieldName, value);
        return facts;
    }
}
