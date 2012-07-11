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

import com.abyala.decisiontree.nodes.IntegerNode;
import com.abyala.decisiontree.nodes.NodeBuilder;

/**
 * User: abyala
 * Date: 6/14/12
 */
public class IntegerInputType implements InputType {
    private final String name;
    private final int maxValue;
    private final int minValue;

    private IntegerInputType(final String name, final int minValue, final int maxValue) {
        this.name = name;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public int getMinValue() {
        return minValue;
    }

    @Override
    public NodeBuilder createNodeBuilder() {
        return new IntegerNode.Builder(this);
    }

    public static class Builder implements InputTypeBuilder {
        private final String name;
        private int minValue = Integer.MIN_VALUE;
        private int maxValue = Integer.MAX_VALUE;

        public Builder(final String name) {
            this.name = name;
        }

        public IntegerInputType build() {
            return new IntegerInputType(name, minValue, maxValue);
        }

        public Builder setMinValue(final int value) {
            this.minValue = value;
            return this;
        }

        public void setMaxValue(final int value) {
            this.maxValue = value;
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("IntegerInputType");
        sb.append("{name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
