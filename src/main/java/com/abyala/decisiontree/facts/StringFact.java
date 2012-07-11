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

package com.abyala.decisiontree.facts;

/**
 * User: abyala
 * Date: 6/18/12
 */
public class StringFact implements Fact {
    private final String key;
    private final String value;

    public StringFact(final String key, final String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("StringFact");
        sb.append("{key=");
        if (key == null) {
            sb.append("null");
        } else {
            sb.append('\'').append(key).append('\'');
        }
        sb.append(", value=");
        if (value == null) {
            sb.append("null");
        } else {
            sb.append("'").append(value).append("'");
        }
        sb.append('}');
        return sb.toString();
    }
}
