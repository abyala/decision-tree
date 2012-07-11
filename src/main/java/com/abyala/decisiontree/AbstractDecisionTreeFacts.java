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

import com.abyala.decisiontree.exceptions.InvalidFactDataypeException;
import com.abyala.decisiontree.exceptions.InvalidFactException;

/**
 * User: abyala
 * Date: 6/28/12
 */
public abstract class AbstractDecisionTreeFacts implements DecisionTreeFacts {
    @Override
    public String getString(final String key) throws InvalidFactException {
        final Object value = get(key);

        try {
            return (String) value;
        } catch (ClassCastException e) {
            throw new InvalidFactDataypeException(key, value, String.class, value.getClass());
        }
    }

    @Override
    public Integer getInt(final String key) throws InvalidFactException {
        final Object value = get(key);

        try {
            return (Integer) value;
        } catch (ClassCastException e) {
            throw new InvalidFactDataypeException(key, value, Integer.class, value.getClass());
        }
    }

    @Override
    public Boolean getBoolean(final String key) throws InvalidFactException {
        final Object value = get(key);

        try {
            return (Boolean) value;
        } catch (ClassCastException e) {
            throw new InvalidFactDataypeException(key, value, Boolean.class, value.getClass());
        }
    }
}
