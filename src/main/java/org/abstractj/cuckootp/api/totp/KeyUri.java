/**
 * Copyright 2012 Bruno Oliveira, and individual contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.abstractj.cuckootp.api.totp;

public class KeyUri {

    private static String domain = "abstractj.org";

    public static String format(String name, String secret) {
        String format = "otpauth://totp/%s@%s?secret=%s";

        return String.format(format, name, domain, secret);
    }
}
