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

import com.abyala.decisiontree.exceptions.DecisionTreeParserException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * User: abyala
 * Date: 7/2/12
 */
public class References {
    private final Map<String, String> references = new HashMap<String, String>();

    public void addMapping(final String key, final String reference) {
        references.put(key, reference);
    }

    public void resolveAll(final ReferenceResolver builder) throws DecisionTreeParserException {
        final Map<String, String> unresolved = new HashMap<String, String>(references);

        for (int unresolvedLastTime = -1; unresolvedLastTime != unresolved.size(); ) {
            unresolvedLastTime = unresolved.size();

            for (Iterator<Map.Entry<String, String>> iterator = unresolved.entrySet().iterator(); iterator.hasNext();) {
                final Map.Entry<String, String> entry = iterator.next();
                final String key = entry.getKey();
                final String value = entry.getValue();
                if (builder.resolveReference(key, value)) {
                    iterator.remove();
                }
            }
        }

        if (!unresolved.isEmpty()) {
            throw new DecisionTreeParserException("Unmatched references found: " + collectionToString(unresolved.values()));
        }
    }

    private String collectionToString(final Collection<String> values) {
        final StringBuilder result = new StringBuilder();
        for (String value : values) {
            if (result.length() > 0) {
                result.append(",");
            }
            result.append(value);
        }
        return result.toString();
    }
}
