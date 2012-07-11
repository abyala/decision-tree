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

package com.abyala.decisiontree.types;

import com.abyala.decisiontree.nodes.NodeBuilder;
import com.abyala.decisiontree.nodes.StringNode;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * User: abyala
 * Date: 6/14/12
 */
public class StringInputType implements InputType {
    private final String name;
    private final Set<String> enumValues;
    private final String defaultValue;

    private StringInputType(final String name, final Set<String> values, final String defaultValue) {
        this.name = name;
        this.enumValues = Collections.unmodifiableSet(new HashSet<String>(values));
        this.defaultValue = defaultValue;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public NodeBuilder createNodeBuilder() {
        return new StringNode.Builder(this);
    }

    public Set<String> getEnumValues() {
        return enumValues;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public static class Builder implements InputTypeBuilder {
        private final Set<String> enumValues = new HashSet<String>();
        private final String name;
        private String defaultValue;

        public Builder(final String name) {
            this.name = name;
        }

        public Builder addEnumValue(final String value, final boolean isDefault) {
            enumValues.add(value);
            if (isDefault) {
                this.defaultValue = value;
            }
            return this;
        }

        public StringInputType build() {
            return new StringInputType(name, enumValues, defaultValue);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("StringInputType");
        sb.append("{name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
