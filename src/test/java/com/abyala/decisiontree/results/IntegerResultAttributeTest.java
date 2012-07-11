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

import com.abyala.decisiontree.exceptions.DecisionTreeParserException;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * User: abyala
 * Date: 7/3/12
 */
public class IntegerResultAttributeTest {
    @Test
    public void testSimpleBoolean() throws NoSuchMethodException, DecisionTreeParserException {
        IntegerResultAttribute.Builder builder = new IntegerResultAttribute.Builder("value", DummyIntegerResult.class.getMethod("setThisField", Integer.class));
        final IntegerResultAttribute attribute = builder.build();

        attribute.validateValue(String.valueOf(Integer.MAX_VALUE));
        attribute.validateValue(Integer.toString(Integer.MIN_VALUE));
        attribute.validateValue("0");

        assertThat((Integer) attribute.transformValueForSetter("1"), equalTo(1));
        assertThat((Integer) attribute.transformValueForSetter("-500"), equalTo(-500));
        assertValueFailsValidation(attribute, "Value is not an integer", "test");
        assertValueFailsValidation(attribute, "Value is not an integer", null);
    }

    @Test
    public void testBooleanWithDefault() throws NoSuchMethodException, DecisionTreeParserException {
        IntegerResultAttribute.Builder builder = new IntegerResultAttribute.Builder("value", DummyIntegerResult.class.getMethod("setThisField", Integer.class));
        builder.setDefaultValue(-1);
        final IntegerResultAttribute attribute = builder.build();

        attribute.validateValue(String.valueOf(Integer.MAX_VALUE));
        attribute.validateValue(Integer.toString(Integer.MIN_VALUE));
        attribute.validateValue("0");
        assertThat((Integer) attribute.transformValueForSetter("1"), equalTo(1));
        assertThat((Integer) attribute.transformValueForSetter("-500"), equalTo(-500));
        assertThat((Integer) attribute.transformValueForSetter(null), equalTo(-1));
    }

    private void assertValueFailsValidation(final ResultAttribute attribute, final String errorMessage, final String value) {
        try {
            attribute.validateValue(value);
            fail(errorMessage);
        } catch (DecisionTreeParserException e) {
            // Good
        }
    }

    private static class DummyIntegerResult {
        private Integer someField;

        public void setThisField(final Integer val) {
            someField = val;
        }
    }
}
