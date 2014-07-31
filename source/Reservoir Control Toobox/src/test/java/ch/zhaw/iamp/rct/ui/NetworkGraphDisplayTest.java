/*
 * Copyright (c) 2014, Zurich University of Applied Sciences, ZHAW
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package ch.zhaw.iamp.rct.ui;

import ch.zhaw.iamp.rct.graph.NetworkGraph;
import static org.easymock.EasyMock.*;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class NetworkGraphDisplayTest {

    private NetworkGraphDisplay display;
    private NetworkGraph graph;
    private GrammarWindow window;

    @Before
    public void setUp() {
        display = new NetworkGraphDisplay();
        graph = createMock(NetworkGraph.class);
        window = createMock(GrammarWindow.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetGrammarWindowOnNull() {
        display.setGrammarWindow(null);
    }

    @Test
    public void testSetGrammarWindow() {
        display.setGrammarWindow(window);
        assertSame(window, display.grammarWindow);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNetworkGraphOnNull() {
        display.setNetworkGraph(null);
    }

    @Test
    public void testSetNetworkGraph() {
        display.setNetworkGraph(graph);
        assertSame(graph, display.networkGraph);
    }

}
