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
public class BooleanResultAttribute extends AbstractResultAttribute {
    private final boolean defaultValue;

    private BooleanResultAttribute(final String name, final Method setter, final boolean defaultValue) {
        super(name, setter);
        this.defaultValue = defaultValue;
    }

    @Override
    public void validateValue(final String value) throws DecisionTreeParserException {
        if (!("true".equals(value) || "false".equals(value))) {
            throw new DecisionTreeParserException("Invalid value \"" + value + "\" for boolean result attribute \"" + getName() + "\"");
        }
    }

    @Override
    public Object transformValueForSetter(final String rawValue) {
        return (rawValue == null ? defaultValue : Boolean.valueOf(rawValue));
    }

    public static class Builder {
        private final String name;
        private final Method setter;
        private boolean defaultValue = false;

        public Builder(final String name, final Method setter) {
            this.name = name;
            this.setter = setter;
        }

        public Builder setDefaultValue(final boolean defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public BooleanResultAttribute build() {
            return new BooleanResultAttribute(name, setter, defaultValue);
        }
    }
}
