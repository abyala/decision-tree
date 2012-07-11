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
import com.abyala.decisiontree.types.StringInputType;

import java.util.*;

/**
 * User: abyala
 * Date: 6/18/12
 */
public class StringNode extends AbstractNode {
    private final Map<String, Object> children;

    private StringNode(final InputType type, final Map<String, Node> nodes, final Map<String, ResultNode> results) {
        super(type);
        children = new HashMap<String, Object>() ;
        children.putAll(nodes);
        children.putAll(results);
    }

    @Override
    public Object doEvaluate(final DecisionTreeFacts facts) throws InvalidFactException {
        final String key = facts.getString(getName());
        if (key == null) {
            throw new MissingFactException(getName());
        }

        if (children.containsKey(key)) {
            return children.get(key);
        } else {
            final String defaultValue = ((StringInputType) getType()).getDefaultValue();
            if (defaultValue != null && children.containsKey(defaultValue)) {
                return children.get(defaultValue);
            }
        }

        return null;
    }

    @Override
    protected void validateShallow() throws DecisionTreeParserException {
        final StringInputType type = (StringInputType) getType();
        final Set<String> requiredValues = new HashSet<String>(type.getEnumValues());
        final Set<String> valuesToInspect = new HashSet<String>(children.keySet());

        for (String mappedNode : valuesToInspect) {
            if (!requiredValues.remove(mappedNode)) {
                throw new DecisionTreeParserException("Invalid configuration: Node \"" + getName() + "\" is not defined with an enumerated value \"" + mappedNode + "\"");
            }
        }
        if (!requiredValues.isEmpty()) {
            throw new DecisionTreeParserException("Invalid configuration: Node \"" + getName() + "\" is missing entries for values " + requiredValues);
        }
    }

    @Override
    protected Collection<Node> getChildNodes() {
        final Collection<Node> nodes = new ArrayList<Node>();
        for (Object o : children.values()) {
            if (o instanceof Node) {
                nodes.add((Node) o);
            }
        }
        return nodes;
    }

    public static class Builder implements NodeBuilder, ReferenceResolver {
        private final InputType type;
        private final References references = new References();
        private final Map<String, Node> nodes = new HashMap<String, Node>();
        private final Map<String, ResultNode> results = new HashMap<String, ResultNode>();

        public Builder(final InputType type) {
            this.type = type;
        }

        @Override
        public NodeBuilder addResultMapping(final String key, final ResultNode result) throws DecisionTreeParserException {
            results.put(key, result);
            return this;
        }

        @Override
        public NodeBuilder addNodeMapping(final String key, final Node node) throws DecisionTreeParserException {
            nodes.put(key, node);
            return this;
        }

        @Override
        public NodeBuilder addReferenceMapping(final String key, final String reference) throws DecisionTreeParserException {
            references.addMapping(key, reference);
            return this;
        }

        @Override
        public boolean resolveReference(final String key, final String value) throws DecisionTreeParserException {
            if (nodes.containsKey(value)) {
                addNodeMapping(key, nodes.get(value));
                return true;
            } else if (results.containsKey(value)) {
                addResultMapping(key, results.get(value));
                return true;
            } else {
                return false;
            }
        }

        @Override
        public Node build() throws DecisionTreeParserException {
            references.resolveAll(this);
            return new StringNode(type, nodes, results);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("StringNode");
        sb.append("{children=").append(children);
        sb.append('}');
        return sb.toString();
    }
}
