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

package com.abyala.decisiontree.exceptions;

/**
 * User: abyala
 * Date: 6/30/12
 */
public class InvalidFactDataypeException extends InvalidFactException {
    private final Class expectedClass;
    private final Class actualClass;

    public InvalidFactDataypeException(final String fieldName, final Object fieldValue, final Class expectedClass, final Class actualClass) {
        super(fieldName, fieldValue, "Invalid fact for \"" + fieldName + "\" - expected type " + expectedClass + " but got type " + actualClass + " and value \"" + fieldValue + "\"");
        this.expectedClass = expectedClass;
        this.actualClass = actualClass;
    }

    public Class getExpectedClass() {
        return expectedClass;
    }

    public Class getActualClass() {
        return actualClass;
    }
}
