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

import com.abyala.decisiontree.facts.BooleanFact;
import com.abyala.decisiontree.facts.Fact;
import com.abyala.decisiontree.facts.IntegerFact;
import com.abyala.decisiontree.facts.StringFact;

import java.util.HashMap;
import java.util.Map;

/**
 * User: abyala
 * Date: 6/18/12
 */
public class SimpleDecisionTreeFacts extends AbstractDecisionTreeFacts {
    private final Map<String, Fact> facts = new HashMap<String, Fact>();

    public void put(final String key, final String value) {
        facts.put(key, new StringFact(key, value));
    }

    public void put(final String key, final int value) {
        facts.put(key, new IntegerFact(key, value));
    }

    public void put(final String key, final boolean value) {
        facts.put(key, new BooleanFact(key, value));
    }

    public Object get(final String key) {
        final Fact fact = facts.get(key);
        return fact == null ? null : fact.getValue();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SimpleDecisionTreeFacts");
        sb.append("{facts=").append(facts);
        sb.append('}');
        return sb.toString();
    }

}
