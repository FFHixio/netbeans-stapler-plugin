/*
 * The MIT License
 *
 * Copyright 2012 Jesse Glick.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.kohsuke.stapler.netbeans.plugin;

import java.net.URL;
import org.junit.Test;
import org.jvnet.localizer.LocaleProvider;
import org.netbeans.modules.java.hints.test.api.HintTest;
import org.openide.filesystems.FileUtil;

public class MessagesHintTest {

    @Test public void simpleString() throws Exception {
        HintTest.create().classpath(cp())
                .input("package test;\n"
                + "public class Test {\n"
                + "    void m() {\n"
                + "        String s = \"hello\";\n"
                + "    }\n"
                + "}\n")
                .input("test/Messages.properties", "", false)
                .run(MessagesHint.class)
                .findWarning("3:19-3:26:hint:" + Bundle.ERR_MessagesHint())
                .applyFix()
                .assertOutput("package test;\n"
                + "public class Test {\n"
                + "    void m() {\n"
                + "        String s = Messages.Test_hello();\n"
                + "    }\n"
                + "}\n")
                .assertVerbatimOutput("test/Messages.properties", "Test.hello=hello\n");
    }

    // XXX unfriendly chars in text converted to some sort of reasonable key
    // XXX existing key with similar name means uniquify
    // XXX compound string with message formats
    // XXX "'" in string must be escaped for use with MessageFormat
    // XXX no LocaleProvider in CP
    // XXX no Messages.properties initially
    // XXX adds to existing Messages.properties with formatting intact
    // XXX other stuff being added, not strings

    private URL cp() {
        URL cp = LocaleProvider.class.getProtectionDomain().getCodeSource().getLocation();
        return cp.toString().endsWith("/") ? cp : FileUtil.getArchiveRoot(cp);
    }

}
