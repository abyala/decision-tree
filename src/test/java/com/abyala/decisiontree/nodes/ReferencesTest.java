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
import org.junit.Test;

import static org.junit.Assert.fail;

import static org.mockito.Mockito.*;

/**
 * User: abyala
 * Date: 7/2/12
 */
public class ReferencesTest extends AbstractNodeTest {
    @Test
    public void testNoReferences() throws DecisionTreeParserException {
        final ReferenceResolver resolver = mock(ReferenceResolver.class);
        final References references = new References();
        references.resolveAll(resolver);
        verify(resolver, never()).resolveReference(anyString(), anyString());
    }

    @Test
    public void testSingleReference() throws DecisionTreeParserException {
        final ReferenceResolver resolver = mock(ReferenceResolver.class);
        when(resolver.resolveReference("b", "a")).thenReturn(true);
        final References references = new References();
        references.addMapping("b", "a");

        references.resolveAll(resolver);
        verify(resolver, atMost(1)).resolveReference("b", "a");
    }

    @Test
    public void testMissingReference() throws DecisionTreeParserException {
        final ReferenceResolver resolver = mock(ReferenceResolver.class);
        when(resolver.resolveReference("b", "a")).thenReturn(false);
        final References references = new References();
        references.addMapping("b", "a");

        try {
            references.resolveAll(resolver);
            fail("Should fail when the reference resolver cannot handle an input");
        } catch (DecisionTreeParserException e) {
            verify(resolver, atMost(1)).resolveReference("b", "a");
        }
    }

    @Test
    public void testNestedReference() throws DecisionTreeParserException {
        final ReferenceResolver resolver = mock(ReferenceResolver.class);

        when(resolver.resolveReference("b", "a")).thenReturn(true);
        when(resolver.resolveReference("c", "b")).thenReturn(false).thenReturn(true);
        final References references = new References();
        references.addMapping("b", "a");
        references.addMapping("c", "b");

        references.resolveAll(resolver);
        verify(resolver, atMost(1)).resolveReference("b", "a");
        verify(resolver, atMost(2)).resolveReference("c", "b");
    }

    @Test
    public void testNestedReference_backwards() throws DecisionTreeParserException {
        // Same as the test above, but add the mappings backwards to verify the order doesn't matter
        final ReferenceResolver resolver = mock(ReferenceResolver.class);

        when(resolver.resolveReference("b", "c")).thenReturn(false).thenReturn(true);
        when(resolver.resolveReference("c", "a")).thenReturn(true);
        final References references = new References();
        references.addMapping("c", "a");
        references.addMapping("b", "c");

        references.resolveAll(resolver);
        verify(resolver, atMost(1)).resolveReference("c", "a");
        verify(resolver, atMost(2)).resolveReference("b", "c");
    }
}
