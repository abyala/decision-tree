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
 * Date: 6/28/12
 */
public class InvalidFactException extends Exception {
    private final String fieldName;
    private final Object fieldValue;

    public InvalidFactException(final String fieldName, final Object fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public InvalidFactException(final String fieldName, final Object fieldValue, final String message) {
        super(message);
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public InvalidFactException(final String fieldName, final Object fieldValue, final String message, final Throwable cause) {
        super(message, cause);
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public InvalidFactException(final String fieldName, final Object fieldValue, final Throwable cause) {
        super(cause);
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}
