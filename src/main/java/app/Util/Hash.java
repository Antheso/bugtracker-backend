package app.Util;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class Hash {

    private String value;

    public Hash(String value) {
        this.value = value;
    }

    public String getHash()  {
        return Hashing.sha256()
                .hashString(value, StandardCharsets.UTF_8)
                .toString();
    }
}
