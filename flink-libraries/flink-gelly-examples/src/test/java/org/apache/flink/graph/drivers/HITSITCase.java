/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.graph.drivers;

import org.apache.flink.client.program.ProgramParametrizationException;

import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Tests for {@link HITS}.
 */
@RunWith(Parameterized.class)
public class HITSITCase
extends DriverBaseITCase {

	public HITSITCase(String idType, TestExecutionMode mode) {
		super(idType, mode);
	}

	private String[] parameters(int scale, String output) {
		return new String[] {
			"--algorithm", "HITS",
			"--input", "RMatGraph", "--scale", Integer.toString(scale), "--type", idType, "--simplify", "directed",
			"--output", output};
	}

	@Test
	public void testLongDescription() throws Exception {
		String expected = regexSubstring(new HITS().getLongDescription());

		expectedOutputFromException(
			new String[]{"--algorithm", "HITS"},
			expected,
			ProgramParametrizationException.class);
	}

	@Test
	public void testPrintWithSmallRMatGraph() throws Exception {
		// skip 'char' since it is not printed as a number
		Assume.assumeFalse(idType.equals("char") || idType.equals("nativeChar"));

		expectedCount(parameters(8, "print"), 233);
	}

	@Test
	public void testPrintWithLargeRMatGraph() throws Exception {
		// skip 'char' since it is not printed as a number
		Assume.assumeFalse(idType.equals("char") || idType.equals("nativeChar"));

		// skip 'byte' which cannot store vertex IDs for scale > 8
		Assume.assumeFalse(idType.equals("byte") || idType.equals("nativeByte"));

		expectedCount(parameters(12, "print"), 3349);
	}
}
