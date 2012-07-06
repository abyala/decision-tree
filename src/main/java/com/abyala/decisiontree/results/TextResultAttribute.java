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

import java.lang.reflect.Method;

/**
 * User: abyala
 * Date: 6/26/12
 */
public class TextResultAttribute extends AbstractResultAttribute {
    private final String defaultValue;

    private TextResultAttribute(final String name, final Method setter, final String defaultValue) {
        super(name, setter);
        this.defaultValue = defaultValue;
    }

    @Override
    public void validateValue(final String value) throws DecisionTreeParserException {
        // All values are fine
    }

    @Override
    public Object transformValueForSetter(final String rawValue) {
        return rawValue == null ? defaultValue : rawValue;
    }

    public static class Builder {
        private final String name;
        private final Method method;
        private String defaultValue;

        public Builder(final String name, final Method method) {
            this.name = name;
            this.method = method;
        }

        public Builder setDefaultValue(final String defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public TextResultAttribute build() {
            return new TextResultAttribute(name, method, defaultValue);
        }
    }
}
