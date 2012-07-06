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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * User: abyala
 * Date: 7/3/12
 */
public class BooleanResultAttributeTest {
    @Test
    public void testSimpleBoolean() throws NoSuchMethodException, DecisionTreeParserException {
        BooleanResultAttribute.Builder builder = new BooleanResultAttribute.Builder("fieldA", DummyBooleanResult.class.getMethod("setFieldA", boolean.class));
        final BooleanResultAttribute attribute = builder.build();

        attribute.validateValue("true");
        attribute.validateValue("false");
        assertValueFailsValidation(attribute, "No capital boolean values", "TRUE");
        assertValueFailsValidation(attribute, "No capital boolean values", "FALSE");
        assertValueFailsValidation(attribute, "No capital boolean values", "True");
        assertValueFailsValidation(attribute, "No int boolean values", "1");
        assertValueFailsValidation(attribute, "No String boolean values", "foo");
        assertValueFailsValidation(attribute, "No blank values", "");
        assertValueFailsValidation(attribute, "No null values - this would be a problem with the Runtime anyway", null);

        assertThat((Boolean) attribute.transformValueForSetter("true"), equalTo(Boolean.TRUE));
        assertThat((Boolean) attribute.transformValueForSetter("false"), equalTo(Boolean.FALSE));
    }

    @Test
    public void testBooleanWithDefault() throws NoSuchMethodException, DecisionTreeParserException {
        BooleanResultAttribute.Builder builder = new BooleanResultAttribute.Builder("fieldA", DummyBooleanResult.class.getMethod("setFieldA", boolean.class));
        builder.setDefaultValue(true);
        final BooleanResultAttribute attribute = builder.build();

        attribute.validateValue("true");
        attribute.validateValue("false");
        assertThat((Boolean) attribute.transformValueForSetter("true"), equalTo(Boolean.TRUE));
        assertThat((Boolean) attribute.transformValueForSetter("false"), equalTo(Boolean.FALSE));
        assertThat((Boolean) attribute.transformValueForSetter(null), equalTo(Boolean.TRUE));
    }

    private void assertValueFailsValidation(final ResultAttribute attribute, final String errorMessage, final String value) {
        try {
            attribute.validateValue(value);
            fail(errorMessage);
        } catch (DecisionTreeParserException e) {
            // Good
        }
    }

    private static class DummyBooleanResult {
        private boolean fieldA;
        private boolean fieldB;

        public boolean isFieldA() {
            return fieldA;
        }

        public void setFieldA(final boolean fieldA) {
            this.fieldA = fieldA;
        }

        public boolean isFieldB() {
            return fieldB;
        }

        public void thisDoesNotLookLikeANormalBooleanSetter(final boolean fieldB) {
            this.fieldB = fieldB;
        }
    }
}
