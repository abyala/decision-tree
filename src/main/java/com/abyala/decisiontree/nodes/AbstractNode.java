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
import com.abyala.decisiontree.exceptions.NoMappingDeclaredException;
import com.abyala.decisiontree.types.InputType;

import java.util.Collection;

/**
 * User: abyala
 * Date: 6/18/12
 */
public abstract class AbstractNode implements Node {
    private final InputType type;

    protected AbstractNode(final InputType type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return getType().getName();
    }

    public InputType getType() {
        return type;
    }

    @Override
    public Object evaluate(final DecisionTreeFacts facts) throws InvalidFactException {
        final Object result = doEvaluate(facts);

        if (result == null) {
            throw new NoMappingDeclaredException(getName(), facts.get(getName()));
        } else if (result instanceof ResultNode) {
            return ((ResultNode) result).createResult();
        } else if (result instanceof Node) {
            return ((Node) result).evaluate(facts);
        } else {
            throw new IllegalStateException("Unexpected element value found in node " + getName() + " with facts " + facts + " - " + result);
        }
    }

    @Override
    public void validate() throws DecisionTreeParserException {
        validateShallow();
        for (Node child : getChildNodes()) {
            child.validate();
        }
    }

    protected abstract Object doEvaluate(final DecisionTreeFacts facts) throws InvalidFactException;
    protected abstract void validateShallow() throws DecisionTreeParserException;
    protected abstract Collection<Node> getChildNodes();

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("AbstractNode");
        sb.append("{type=").append(type);
        sb.append('}');
        return sb.toString();
    }
}
