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
import com.abyala.decisiontree.types.IntegerInputType;

import java.util.*;

import static java.util.AbstractMap.SimpleEntry;

/**
 * User: abyala
 * Date: 6/18/12
 */
public class IntegerNode extends AbstractNode {
    private final List<Map.Entry<Integer, Object>> children;

    private IntegerNode(final InputType type, final Map<Integer, Object> children) {
        super(type);

        this.children = new ArrayList<Map.Entry<Integer, Object>>();
        for (Map.Entry<Integer, Object> entry : children.entrySet()) {
            this.children.add(new SimpleEntry<Integer, Object>(entry));
        }
        Collections.sort(this.children, new Comparator<Map.Entry<Integer, Object>>() {
            @Override
            public int compare(final Map.Entry<Integer, Object> o1, final Map.Entry<Integer, Object> o2) {
                if (o1.getKey() == Integer.MIN_VALUE) return 1;
                if (o2.getKey() == Integer.MIN_VALUE) return -1;
                return o2.getKey() - o1.getKey();
            }
        });
    }

    @Override
    public Object doEvaluate(final DecisionTreeFacts facts) throws InvalidFactException {
        final Integer key = facts.getInt(getName());
        if (key == null) {
            throw new MissingFactException(getName());
        }

        for (Map.Entry<Integer, Object> entry : children) {
            if (key >= entry.getKey()) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void validateShallow() throws DecisionTreeParserException {
        final IntegerInputType type = (IntegerInputType) getType();
        final List valuesToInspect = new ArrayList(children);

        final int minValue = type.getMinValue();
        final int maxValue = type.getMaxValue();
        boolean includedMinValue = false;
        for (Object o : valuesToInspect) {
            final Map.Entry<Integer, ?> entry = (Map.Entry<Integer, ?>) o;
            final Integer key = entry.getKey();
            if (key < minValue) {
                throw new DecisionTreeParserException("Invalid configuration: Node \"" + getName() + "\" cannot match value " + key + " because it falls below the min value allowed");
            } else if (key > maxValue) {
                throw new DecisionTreeParserException("Invalid configuration: Node \"" + getName() + "\" cannot match value " + key + " because it falls above the max value allowed");
            } else if (key == minValue) {
                includedMinValue = true;
            }
        }

        if (!includedMinValue) {
            throw new DecisionTreeParserException("Invalid configuration: Node \"" + getName() + "\" is missing minimum mapping of \"" + (minValue == Integer.MIN_VALUE ? "unbounded" : minValue) + "\"");
        }
    }

    @Override
    protected Collection<Node> getChildNodes() {
        final Collection<Node> result = new ArrayList<Node>();
        for (Map.Entry<Integer, Object> child : children) {
            if (child.getValue() instanceof Node) {
                result.add((Node) child.getValue());
            }
        }
        return result;
    }

    public static class Builder implements NodeBuilder, ReferenceResolver {
        private final InputType type;
        private final References references = new References();
        private final Map<Integer, Object> children = new HashMap<Integer, Object>();

        public Builder(final InputType type) {
            this.type = type;
        }

        public NodeBuilder addResultMapping(final String key, final ResultNode result) throws DecisionTreeParserException {
            return addChild(key, result);
        }

        public NodeBuilder addNodeMapping(final String key, final Node node) throws DecisionTreeParserException {
            return addChild(key, node);
        }

        @Override
        public NodeBuilder addReferenceMapping(final String key, final String reference) throws DecisionTreeParserException {
            references.addMapping(key, reference);
            return this;
        }

        private NodeBuilder addChild(final String key, final Object value) throws DecisionTreeParserException {
            final Integer intKey = parseIntValue(key);
            if (children.containsKey(intKey)) {
                throw new DecisionTreeParserException("Invalid configuration: Node \"" + type.getName() + "\" may not have multiple mappings for the same value " + key);
            }

            children.put(intKey, value);
            return this;
        }

        private Integer parseIntValue(final String key) {
            return "unbounded".equals(key) ? Integer.MIN_VALUE : Integer.parseInt(key);
        }

        @Override
        public boolean resolveReference(final String key, final String value) throws DecisionTreeParserException {
            final Integer referencesKey = parseIntValue(value);
            if (children.containsKey(referencesKey)) {
                addChild(key, children.get(referencesKey));
                return true;
            } else {
                return false;
            }
        }

        public IntegerNode build() throws DecisionTreeParserException {
            references.resolveAll(this);
            return new IntegerNode(type, children);
        }
    }
}
