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

import com.abyala.decisiontree.DecisionTreeFacts;
import com.abyala.decisiontree.exceptions.DecisionTreeParserException;
import com.abyala.decisiontree.exceptions.InvalidFactException;
import com.abyala.decisiontree.exceptions.MissingFactException;
import com.abyala.decisiontree.types.InputType;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User: abyala
 * Date: 6/18/12
 */
public class BooleanNode extends AbstractNode {
    private final Object trueValue;
    private final Object falseValue;

    private BooleanNode(final InputType type, final Object trueValue, final Object falseValue) {
        super(type);
        this.trueValue = trueValue;
        this.falseValue = falseValue;
    }

    @Override
    public Object doEvaluate(final DecisionTreeFacts facts) throws InvalidFactException {
        final Boolean key = facts.getBoolean(getName());
        if (key == null) {
            throw new MissingFactException(getName());
        }

        return (key ? trueValue : falseValue);
    }

    @Override
    protected void validateShallow() throws DecisionTreeParserException {
        if (trueValue == null) {
            throw new DecisionTreeParserException("Invalid configuration: Node \"" + getName() + "\" must have a mapping for value \"true\"");
        } else if (falseValue == null) {
            throw new DecisionTreeParserException("Invalid configuration: Node \"" + getName() + "\" must have a mapping for value \"false\"");
        }
    }

    @Override
    protected Collection<Node> getChildNodes() {
        final Collection<Node> nodes = new ArrayList<Node>(2);
        if (trueValue instanceof Node) {
            nodes.add((Node) trueValue);
        }
        if (falseValue instanceof Node) {
            nodes.add((Node) falseValue);
        }
        return nodes;
    }

    public static class Builder implements NodeBuilder {
        private final InputType type;
        private Object trueValue;
        private Object falseValue;

        public Builder(final InputType type) {
            this.type = type;
        }

        @Override
        public NodeBuilder addResultMapping(final String key, final ResultNode result) throws DecisionTreeParserException {
            return addMapping(key, result);
        }

        @Override
        public NodeBuilder addNodeMapping(final String key, final Node node) throws DecisionTreeParserException {
            return addMapping(key, node);
        }

        @Override
        public NodeBuilder addReferenceMapping(final String key, final String reference) throws DecisionTreeParserException {
            throw new DecisionTreeParserException("Boolean input types do not support references.");
        }

        private NodeBuilder addMapping(final String key, final Object value) throws DecisionTreeParserException {
            if ("true".equals(key)) {
                if (trueValue == null) {
                    trueValue = value;
                } else {
                    throw new DecisionTreeParserException("Invalid configuration: Node \"" + type.getName() + "\" may not have multiple mappings for the same value \"true\"");
                }
            } else if ("false".equals(key)) {
                if (falseValue == null) {
                    falseValue = value;
                } else {
                    throw new DecisionTreeParserException("Invalid configuration: Node \"" + type.getName() + "\" may not have multiple mappings for the same value \"false\"");
                }
            } else {
                throw new DecisionTreeParserException("Invalid configuration: Node \"" + type.getName() + "\" may only have mappings for \"true\" and \"false\", and not \"" + key + "\"");
            }

            return this;
        }

        @Override
        public Node build() throws DecisionTreeParserException {
            return new BooleanNode(type, trueValue, falseValue);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("BooleanNode");
        sb.append("{true=").append(trueValue);
        sb.append(", false=").append(falseValue);
        sb.append('}');
        return sb.toString();
    }
}
