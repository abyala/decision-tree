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

package com.abyala.decisiontree;

import com.abyala.decisiontree.exceptions.InvalidFactException;
import com.abyala.decisiontree.nodes.Node;
import com.abyala.decisiontree.types.InputType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * User: abyala
 * Date: 6/14/12
 */
public class DecisionTree {
    private final Map<String, InputType> inputTypes;
    private final Node rootNode;

    public DecisionTree(final Node rootNode, final Collection<InputType> inputs) {
        this.rootNode = rootNode;
        inputTypes = new HashMap<String, InputType>();
        for (InputType input : inputs) {
            inputTypes.put(input.getName(), input);
        }
    }

    public Object evaluate(DecisionTreeFacts facts) throws InvalidFactException {
        return rootNode.evaluate(facts);
    }
}
